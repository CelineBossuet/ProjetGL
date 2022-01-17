package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;
import sun.jvm.hotspot.debugger.cdbg.Sym;

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
    protected void verifyClass(DecacCompiler compiler) throws ContextualError { // A FAIRE
        if (superClass.getDefinition()==null){
            this.superClass.setDefinition(compiler.OBJECT);
        }else{
            superClass.verifyTypeClass(compiler);
        }
        System.out.println(this.superClass.getClassDefinition());
        ClassType newClass = new ClassType(this.name.getName(), this.getLocation(), this.superClass.getClassDefinition());
        ClassDefinition definition = newClass.getDefinition();
        try {
            compiler.getEnvironmentType().declareClass(this.name.getName(), definition);
        }catch (Environment.DoubleDefException e) {
            if(alreadyUsed.contains(name.getName())) {
                throw new ContextualError("Classe déjà déclaré", this.getLocation());
            }else{
                this.alreadyUsed.add(name.getName());
            }
        }
        this.name.setDefinition(definition);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        ClassDefinition currentDef = (ClassDefinition) this.name.getDefinition();
        ClassDefinition superDef = (ClassDefinition) this.superClass.getDefinition();
        currentDef.setNumberOfFields(superDef.getNumberOfFields());
        currentDef.setNumberOfMethods(superDef.getNumberOfMethods());
        for(AbstractDeclField adf : this.field.getList()){
            adf.verifyMembers(compiler, superDef, currentDef);
        }
        for(AbstractDeclMethod adm : this.method.getList()){
            adm.verifyMembers(compiler, superDef, currentDef);
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
    protected void codeGenClass(DecacCompiler compiler) {
        ClassDefinition currentDef = name.getClassDefinition();
        ClassDefinition superDef =currentDef.getSuperClass();
        currentDef.setConstructorLabel(compiler.getLabelManager().newLabel("init."+name.getName()));
        //on a créé le Label pour l'initialisation de la classe

        VTable vTable;
        VTable superVTable;
        if(superDef==null){
            superVTable =null;
            compiler.getMemoryManager().createConstant(new NullOperand(), compiler.getCurrentBlock());
            //pas de VTable parent pour notre classe
        }
        else{
            LOG.info("on garde en mémoire le pointeur vers la VTable parent");
            superVTable=superDef.getvTable();
            DAddr tab = compiler.getMemoryManager().allocGB(1);
            DAddr AddrVTable = compiler.getMemoryManager().getCurrentGBOperand();
            compiler.addInstruction(new LEA(superVTable.getOperand(), Register.getR(0)));
            compiler.addInstruction(new STORE(Register.getR(0), AddrVTable));

        }
        vTable= new VTable(currentDef.getNumberOfMethods(), superVTable);
        //on peut donc créer notre VTable maintenant
        currentDef.setvTable(vTable); //on l'ajoute dans notre definition
        vTable.setOperand(compiler.getMemoryManager().getCurrentGBOperand());

        for (Map.Entry<Symbol, ExpDefinition> e : currentDef.getMembers().getEnvironment().entrySet()){
            if(e.getValue().isMethod()){
                MethodDefinition m=(MethodDefinition) e.getValue();
                Symbol name = e.getKey();
                vTable.set(m.getIndex(), new LabelOperand(m.getLabel()));
            }
        }
        vTable.codeGen(compiler);

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
