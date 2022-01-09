package fr.ensimag.deca.tree;


import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("peut pas utiliser geneInstru() pour AbstractOpIneq");
    }
}
