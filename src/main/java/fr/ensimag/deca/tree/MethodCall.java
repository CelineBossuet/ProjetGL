package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
    private  AbstractExpr implicitParam;
    private  AbstractIdentifier methodName;
    private  ListExpr param;

    public MethodCall(AbstractExpr implicitParameter, AbstractIdentifier methodName, ListExpr params) {
        this.implicitParam = implicitParameter;
        this.methodName = methodName;
        this.param = params;
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
        implicitParam.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
