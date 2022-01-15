package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

public class Return extends AbstractInst{
    private AbstractExpr returnValue;

    public Return(AbstractExpr returnValue){
        this.returnValue=returnValue;
    }
    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {

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
