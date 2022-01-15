package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
    private  AbstractExpr implicitParameter;
    private  AbstractIdentifier methodName;
    private  ListExpr params;

    public MethodCall(AbstractExpr implicitParameter, AbstractIdentifier methodName, ListExpr params) {
        this.implicitParameter = implicitParameter;
        this.methodName = methodName;
        this.params = params;
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
