package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type left = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type right = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (left.isBoolean() && right.isBoolean()) {
            LOG.trace("les 2 expression sont bien des booleans");
            this.setType(left);
            return left;
        } else {
            throw new ContextualError(
                    "The two operands must be boolean for And/Or operation", this.getLocation());
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("Instruction non implémentable sur les binaires");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        return codeGenCondToReg(compiler);
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("AbsOpBool codeGenStack");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Redéfinition de la méthode car dépend du type d'opération booléenne et donc
     * est abstract
     */
    @Override
    protected abstract void codeGenCond(DecacCompiler compiler, Label l, boolean saut);

    protected void codeGenArithJasmin(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with booleans");
    }
}
