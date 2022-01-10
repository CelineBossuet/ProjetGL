package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
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

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        // TODO verifier type int ou float
        GPRegister reg;
        if (!getOperand().NeedsRegister()) {
            reg = compiler.getRegisterManager().getCurrent();
            DVal val = getOperand().codeGenNoReg(compiler);
            compiler.addInstruction(new OPP(val, reg));
        } else {
            reg = getOperand().codeGenReg(compiler);
            compiler.addInstruction(new OPP(reg, reg));
        }
        return reg;
    }
}
