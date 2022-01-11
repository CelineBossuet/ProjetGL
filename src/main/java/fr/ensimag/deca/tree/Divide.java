package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        // Génération de l'instruction pour une division dans le registre reg

        if (getType().isInt()) {
            getLOG().info("Division entière");
            return new QUO(val, reg);
        } else if (getType().isFloat()) {
            getLOG().info("division pour des float");
            return new DIV(val, reg);
        } else {
            throw new DecacInternalError("Division interdite pour ce type");
        }
    }
}
