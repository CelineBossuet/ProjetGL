package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;

public abstract class AbstractDeclMethod extends Tree{

    protected abstract void verifyMembers(DecacCompiler compiler, Environment<ExpDefinition> env, ClassDefinition currentClass) throws ContextualError;

    public abstract void verifyBody(DecacCompiler compiler, ClassDefinition currentDef) throws ContextualError;

    protected abstract void codeGenMethodBody(DecacCompiler compiler);
}
