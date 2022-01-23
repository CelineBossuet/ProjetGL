package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fneg;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ineg;
import org.apache.log4j.Logger;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type ope = getOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!ope.isInt() && !ope.isFloat()) {
            throw new ContextualError("Unary Minus not possible for type " + ope, getLocation());
        }
        setType(ope);
        return getType();
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected Instruction geneInstru(GPRegister reg) {
        // generation de l'instruction pour le moins unaire donc on a besoin que de
        // notre registre reg
        return new OPP(reg, reg);
    }

    private static final Logger LOG = Logger.getLogger(UnaryMinus.class);

    @Override
    protected void geneInstruJasmin(DecacCompiler compiler) {
        if (getType().isInt())
            compiler.addInstruction(new ineg());
        else if (getType().isFloat())
            compiler.addInstruction(new fneg());
        else
            throw new DecacInternalError("Type " + getType() + " not supported.");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        // TODO verifier type int ou float
        GPRegister reg;
        if (!getOperand().NeedsRegister()) {
            LOG.info("l'opération a besoin de registres");
            reg = compiler.getRegisterManager().getCurrent();
            DVal val = getOperand().codeGenNoReg(compiler);
            compiler.addInstruction(new OPP(val, reg));
        } else {
            LOG.info("l'opération n'a pas besoin de registres");
            reg = getOperand().codeGenReg(compiler);
            compiler.addInstruction(new OPP(reg, reg));
        }
        return reg;
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("UnaryMinus codeGenStack");

        // compute expression
        getOperand().codeGenStack(compiler);

        // convert it
        if (getType().isInt())
            compiler.addInstruction(new ineg());
        else if (getType().isFloat())
            compiler.addInstruction(new fneg());
        else
            throw new DecacInternalError("Type " + getType() + " not supported.");
    }
}
