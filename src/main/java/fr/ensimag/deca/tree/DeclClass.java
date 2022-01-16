package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;

import java.io.PrintStream;
import java.util.HashSet;

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
