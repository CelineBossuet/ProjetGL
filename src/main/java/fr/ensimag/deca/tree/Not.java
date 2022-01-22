package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        Type ope = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (ope.isBoolean()){
            this.setType(ope);
            return ope;
        }else{
            throw new ContextualError("! is only for boolean but the operand is: " + ope, this.getLocation());
        }
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected Instruction geneInstru(GPRegister reg) {
        throw new DecacInternalError("No instruction for Not");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {

        return codeGenCondToReg(compiler);
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut){
        getLOG().info("le Not inverse la logique dans codeGenCond");

        getOperand().codeGenCond(compiler, l, !saut);
    }
}
