package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
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
    private static VTable ObjectTable=new VTable(0, null);
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
        s.print("class ");
        s.print(this.name.getName().getName()+ " ");
        if (this.superClass != null && this.superClass.getName().getName() != "Object"){
            s.print(" extends ");

            s.print(this.superClass.getName().getName());
        }
        s.println(" { ");
        s.indent();
        this.field.decompile(s);
        s.println();
        this.method.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        LOG.info("Debut vérification de la classe");
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
                throw new ContextualError(this.name.getName().getName() + " already declared", this.getLocation());
            }else{
                this.alreadyUsed.add(name.getName());
            }
        }
        this.name.verifyTypeClass(compiler);
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        if(!this.name.getType().isClass()){
            throw new ContextualError("Type "+this.name.getName()+ " isn't a class", this.getLocation());
        }
        if(!this.superClass.getType().isClass()){
            throw new ContextualError("Type "+this.superClass.getName()+ " isn't a class", this.getLocation());
        }
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
        ClassDefinition currentDef = (ClassDefinition) this.name.getDefinition();
        for(AbstractDeclField adf : this.field.getList()){
            LOG.info("On verifie le Field "+adf);
            adf.verifyBody(compiler, currentDef);
        }
        for(AbstractDeclMethod adm : this.method.getList()){
            LOG.info("On verifie la methode "+adm);
            adm.verifyBody(compiler, currentDef);
        }
    }

    @Override
    protected void codeGenClass(DecacCompiler compiler, int first) {
        ClassDefinition currentDef = name.getClassDefinition();
        ClassDefinition superDef = this.superClass.getClassDefinition();
        if(first==0){
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
            superVTable=superDef.getvTable();
            DAddr tab = compiler.getMemoryManager().allocGB(1);
            DAddr AddrVTable = compiler.getMemoryManager().getCurrentGBOperand();
            compiler.addInstruction(new LEA(this.ObjectTable.getOperand(), Register.getR(0)));
            compiler.addInstruction(new STORE(Register.getR(0), AddrVTable));

        }
        vTable= new VTable(currentDef.getNumberOfMethods(), superVTable);

        //on peut donc créer notre VTable maintenant

        vTable.setOperand(compiler.getMemoryManager().getCurrentGBOperand());
        currentDef.setvTable(vTable); //on l'ajoute dans notre definition

        for (Map.Entry<Symbol, ExpDefinition> e : currentDef.getMembers().getEnvironment().entrySet()){
            if(e.getValue().isMethod()){

                MethodDefinition m=(MethodDefinition) e.getValue();
                Symbol name = e.getKey();
                vTable.set(m.getIndex(), new LabelOperand(m.getLabel()));
            }
        }
        vTable.codeGen(compiler);
        if(first==0){
            this.ObjectTable=vTable;
        }

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
        compiler.getMemoryManager().allocLB(1);
        compiler.addInstruction(new PUSH(thisReg));
        if(currentdef.getSuperClass().getConstructorLabel()!=null){
            compiler.getMemoryManager().allocBSR();
            compiler.addInstruction(new BSR(currentdef.getSuperClass().getConstructorLabel()),
                "chaine de constructeurs");
        }

        compiler.addInstruction(new SUBSP(1));

        if(needsInit){
            thisReg = compiler.allocate();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
            getLOG().info("On initie les fields de la classe");
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
