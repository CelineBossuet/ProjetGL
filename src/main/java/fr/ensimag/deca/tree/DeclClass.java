package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.VTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LEA;
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
            System.out.println("oh");
            this.superClass.setDefinition(compiler.OBJECT);
            System.out.println(superClass.getClassDefinition());
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
        System.out.println(superClass.getDefinition());
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
        throw new UnsupportedOperationException("Not yet implemented");
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
        throw new UnsupportedOperationException("Not yet supported");
    }

}
