package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl13
 * @date 01/01/2022
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    protected int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(compiler.getEnvironmentType().INT);

        return compiler.getEnvironmentType().INT;
        //throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    protected boolean NeedsRegister(){
        return false;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler){
        return new ImmediateInteger(value);
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        compiler.addInstruction(new LOAD(new ImmediateInteger(getValue()), reg));
        return reg;
    }
}
