package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public abstract class AbstractDeclMethod extends Tree{

    protected abstract void verifyMembers(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition currentClass) throws ContextualError;

    public abstract void verifyBody(DecacCompiler compiler, ClassDefinition currentDef) throws ContextualError;

    protected abstract void codeGenMethodBody(DecacCompiler compiler);
}
