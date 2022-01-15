package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

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
        Type cla = this.superClass.verifyType(compiler);
        if (!cla.isClass()){
            throw new ContextualError("Ce n'est pas une classe", this.getLocation());
        }
        ClassType newClass = new ClassType(this.name.getName(), this.getLocation(), this.superClass.getClassDefinition());
        try {
            compiler.getEnvironmentType().declareClass(this.name.getName(), newClass.getDefinition());
        }catch (Environment.DoubleDefException e) {
            throw new ContextualError("Classe déjà déclaré", this.getLocation());
        }
        this.name.setDefinition(newClass.getDefinition());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        ClassDefinition currentDef = this.name.getClassDefinition();
        ClassDefinition superDef = this.superClass.getClassDefinition();
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
        throw new UnsupportedOperationException("not yet implemented");
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
