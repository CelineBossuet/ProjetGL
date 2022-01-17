package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl13
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {
    private AbstractIdentifier name;
    private AbstractIdentifier superClass;
    private ListDeclField field;
    private ListDeclMethod method;
    private static VTable ObjectTable=new VTable(1, null);
    private HashSet<SymbolTable.Symbol> alreadyUsed= new HashSet<>();
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DeclClass.class);

    public static Logger getLOG() {
        return LOG;
    }


    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField field, ListDeclMethod method){
        this.name = name;
        this.superClass=superClass;
        this.field=field;
        this.method=method;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError { // A FAIR
        superClass.verifyTypeClass(compiler);
        ClassDefinition superc = compiler.getEnvironmentType().getClass(superClass.getName());
        ClassType newClass = new ClassType(this.name.getName(), this.getLocation(), superc);
        this.name.setType(newClass);
        ClassDefinition definition = newClass.getDefinition();
        this.name.setDefinition(definition);
        try {
            compiler.getEnvironmentType().declareClass(this.name.getName(), this.name.getClassDefinition());
        }catch (Environment.DoubleDefException e) {
            if(alreadyUsed.contains(name.getName())) {
                throw new ContextualError("Classe déjà déclaré", this.getLocation());
            }else{
                this.alreadyUsed.add(name.getName());
            }
        }
        this.name.verifyTypeClass(compiler);
        System.out.println("victoire");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        System.out.println(this.name.getClassDefinition());
        System.out.println(this.superClass);

        (this.name.getClassDefinition()).setNumberOfFields(superClass.getClassDefinition().getNumberOfFields());
        (this.name.getClassDefinition()).setNumberOfMethods(superClass.getClassDefinition().getNumberOfMethods());
        for(AbstractDeclField adf : this.field.getList()){
            adf.verifyMembers(compiler, this.name.getClassDefinition().getMembers(), this.name.getClassDefinition());
        }
        for(AbstractDeclMethod adm : this.method.getList()){
            adm.verifyMembers(compiler, this.name.getClassDefinition().getMembers(), this.name.getClassDefinition());
        }
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        ClassDefinition currentDef = (ClassDefinition) this.name.getDefinition();
        for(AbstractDeclField adf : this.field.getList()){
            adf.verifyBody(compiler, currentDef);
        }
        for(AbstractDeclMethod adm : this.method.getList()){
            adm.verifyBody(compiler, currentDef);
        }
    }

    @Override
    protected void codeGenClass(DecacCompiler compiler, int first) {
        System.out.println("------------------------");
        ClassDefinition currentDef = name.getClassDefinition();
        ClassDefinition superDef = this.superClass.getClassDefinition();
        System.out.println("SUperDef "+superDef);
        System.out.println(superDef.getvTable());
        if(first==0){
            System.out.println("On soccupe d'Object");
            //On s'occument aussi de la classe Object
            currentDef = compiler.OBJECT;
            superDef = currentDef.getSuperClass();
        }

        currentDef.setConstructorLabel(compiler.getLabelManager().newLabel("init."+name.getName()));
        //on a créé le Label pour l'initialisation de la classe
        VTable vTable;
        VTable superVTable;
        if(superDef==null){
            //pas de extends donc on considère Object comme superclass
            superVTable =null;
            compiler.getMemoryManager().createConstant(new NullOperand(), compiler.getCurrentBlock());

            //pas de VTable parent pour notre classe
        }
        else{
            LOG.info("on garde en mémoire le pointeur vers la VTable parent");
            System.out.println(superDef);
            System.out.println("super Table "+superDef.getvTable());
            superVTable=superDef.getvTable();
            DAddr tab = compiler.getMemoryManager().allocGB(1);
            DAddr AddrVTable = compiler.getMemoryManager().getCurrentGBOperand();
            compiler.addInstruction(new LEA(this.ObjectTable.getOperand(), Register.getR(0)));
            compiler.addInstruction(new STORE(Register.getR(0), AddrVTable));

        }
        System.out.println("nouvelle table de taille"+currentDef.getNumberOfMethods());
        vTable= new VTable(currentDef.getNumberOfMethods(), superVTable);
        System.out.println(vTable.getOperand());
        //on peut donc créer notre VTable maintenant

        vTable.setOperand(compiler.getMemoryManager().getCurrentGBOperand());
        currentDef.setvTable(vTable); //on l'ajoute dans notre definition

        System.out.println("ma table courante est "+currentDef.getvTable());
        for (Map.Entry<Symbol, ExpDefinition> e : currentDef.getMembers().getEnvironment().entrySet()){
            if(e.getValue().isMethod()){
                MethodDefinition m=(MethodDefinition) e.getValue();
                Symbol name = e.getKey();
                System.out.println(m+" de nom "+name);
                System.out.println(m.getIndex());
                vTable.set(m.getIndex(), new LabelOperand(m.getLabel()));
            }
        }
        vTable.codeGen(compiler);
        if(first==0){
            this.ObjectTable=vTable;}
        System.out.println("------------------------");

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void codeGenClassBody(DecacCompiler compiler) {
        ClassDefinition currentdef = name.getClassDefinition();
        compiler.addLabel(currentdef.getConstructorLabel());
        compiler.startBlock();

        GPRegister thisReg = Register.getR(1);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));

        boolean needsInit=false;
        LOG.info("initialisation des attributs par défaut");
        for(AbstractDeclField f : field.getList()){
            if(needsInit){
                f.codeFieldNeedsInit(compiler, thisReg);
            }else{
            needsInit = f.codeFieldNeedsInit(compiler, thisReg);}
        }

        if(needsInit){
            thisReg = compiler.allocate();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
            for(AbstractDeclField f : field.getList()){
                f.codeGenFieldBody(compiler, thisReg);
            }
            compiler.release(thisReg);
        }


        compiler.endBlock(false, true, 0, null);
        compiler.addInstruction(new RTS());
        for (AbstractDeclMethod m : method.getList()){
            m.codeGenMethodBody(compiler);
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //throw new UnsupportedOperationException("Not yet supported");
        this.superClass.prettyPrint(s, prefix, false);
        this.name.prettyPrint(s, prefix, false);
        this.field.prettyPrint(s, prefix, false);
        this.method.prettyPrint(s, prefix, true); //Seul à true car seul qui va être directement réutilisé

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        superClass.iter(f);
        field.iter(f);
        method.iter(f);
        //throw new UnsupportedOperationException("Not yet supported");
    }

}
