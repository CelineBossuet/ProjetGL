package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class New extends AbstractExpr{
    private AbstractIdentifier name;

    public New(AbstractIdentifier name){
        this.name=name;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
