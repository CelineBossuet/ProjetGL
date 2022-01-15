package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;

public class Selection extends AbstractLValue{
    private AbstractExpr object;
    private AbstractIdentifier field;

    public Selection(AbstractExpr object, AbstractIdentifier field){
        this.field=field;
        this.object=object;
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
    public DAddr codeGenAddr(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        object.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
