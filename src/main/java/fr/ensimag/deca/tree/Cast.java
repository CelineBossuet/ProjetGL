package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class Cast extends AbstractExpr{
    private AbstractIdentifier typeToCheck;
    private AbstractExpr expr;

    public Cast(AbstractIdentifier typeToCheck, AbstractExpr expr){
        this.expr=expr;
        this.typeToCheck=typeToCheck;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
