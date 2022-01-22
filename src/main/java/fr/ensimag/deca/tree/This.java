package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

public class This extends AbstractExpr {
    private boolean implicit;

    public This(boolean implicit) {
        super();
        this.implicit = implicit;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {
        if (currentClass == null){
            throw new ContextualError("Object is null", this.getLocation());
        }
        Type t = currentClass.getType();
        this.setType(t);
        return t;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        return new RegisterOffset(-2, Register.LB);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        //nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        //nothing to do
    }
}
