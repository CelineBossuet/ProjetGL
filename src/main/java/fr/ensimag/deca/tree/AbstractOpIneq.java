package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import org.apache.log4j.Logger;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    private static final Logger LOG = Logger.getLogger(AbstractOpIneq.class);

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("peut pas utiliser geneInstru() pour AbstractOpIneq");
    }

    protected void codeGenArithJasmin(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
