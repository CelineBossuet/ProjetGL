package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class Return extends AbstractInst{
    private AbstractExpr returnValue;

    public Return(AbstractExpr returnValue){
        this.returnValue=returnValue;
    }
    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        //throw new UnsupportedOperationException("Not yet implemented");
        Type t = this.returnValue.verifyExpr(compiler, localEnv, currentClass);
        if(returnType.isVoid()){
            throw new ContextualError("Return statement forbidden in a void method", getLocation());
        }
        if (t.isInt() && returnType.isFloat()){
            //nothing to do
        }else if(!t.sameType(returnType)){
            throw new ContextualError("Bad return type", this.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        compiler.addInstruction(new LOAD(returnValue.codeGenReg(compiler), Register.getR(0)));
        compiler.addInstruction(new BRA(returnLabel));
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        returnValue.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnValue.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnValue.iter(f);

    }
}
