package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg){
        throw new DecacInternalError("Instruction non implémentable sur les binaires");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler){
        Label elseBranch = compiler.getLabelManager().newLabel("elseC2R");
        Label endBranch = compiler.getLabelManager().newLabel("endC2R");
        GPRegister r = compiler.getRegisterManager().getCurrent();

        compiler.addInstruction(new CMP(0, r));

        compiler.addInstruction(new BNE(elseBranch));


        compiler.addInstruction(new BRA(endBranch));
        compiler.addLabel(elseBranch);

        compiler.addInstruction(new LOAD(1, r));
        compiler.addLabel(endBranch);
        return r;
    }
}
