package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.jasmin.bipush;
import fr.ensimag.ima.pseudocode.jasmin.Constant;

import java.io.PrintStream;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(new BooleanType(compiler.getSymbolTable().create("boolean")));
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    @Override
    protected boolean NeedsRegister() {
        return false;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        if (value) {
            return new ImmediateInteger(1);
        } else {
            return new ImmediateInteger(0);
        }
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("BooleanIdentifier codeGenStack");
        compiler.addInstruction(new bipush(new Constant(value ? 1 : 0)));
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut) {
        getLOG().trace("BooleanLit codeGenCond");
        if (saut == getValue()) {
            compiler.addInstruction(new BRA(l));
        } else {
            getLOG().info("pas besoin de jump le boolean litteral " + getValue());
        }
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        // nothing
    }
}
