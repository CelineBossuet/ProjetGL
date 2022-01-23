package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PEA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.jasmin.istore;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type left = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr right = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, left);
        setRightOperand(right);

        return left;
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("mothode non implémentable pour Assign");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        getLOG().trace("Assign codeGenReg");
        AbstractLValue left = getLeftOperand();
        AbstractExpr right = getRightOperand();
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        GPRegister rightReg;
        DAddr val = left.codeGenAddr(compiler);
        if (compiler.getRegisterManager().getMax() - compiler.getRegisterManager().getCurrentv() + 1 <= 0) {
            rightReg = right.codeGenReg(compiler);
            getLOG().info("pas de registre disponible il faut en allouer un");
            getLOG().debug("pas de registre disponible if faut en allouer un");
            compiler.getMemoryManager().allocLB(1); // on suppose une taille de 1 pour les variables
            compiler.addInstruction(new PEA(val));
            compiler.addInstruction(new POP(Register.getR(0)));
            getLOG().info("POP permet de restoré la valeur lvalue sauvée ");
            val = new RegisterOffset(0, Register.getR(0));

        } else {
            GPRegister alloc = compiler.allocate();
            rightReg = right.codeGenReg(compiler);
            compiler.release(alloc);
        }
        compiler.addInstruction(new STORE(rightReg, val));
        compiler.addInstruction(new LOAD(rightReg, reg));
        // permet de d'assigner la valeur pour la variable puis de la LOAD
        return reg;
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("Assign codeGenStack");
        AbstractLValue left = getLeftOperand();
        AbstractExpr right = getRightOperand();

        // compute right expression
        right.codeGenStack(compiler);

        // store result in left identifier
        compiler.addInstruction(new istore(((Identifier) left).getExpDefinition().getOperand()));
    }

    protected void codeGenArithJasmin(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with assignation");
    }
}
