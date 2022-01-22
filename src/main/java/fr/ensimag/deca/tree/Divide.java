package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fdiv;
import fr.ensimag.ima.pseudocode.instructions.jasmin.idiv;

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

    @Override
    protected void codeGenArithJasmin(DecacCompiler compiler) {
        if (getType().isInt())
            compiler.addInstruction(new idiv());
        else if (getType().isFloat())
            compiler.addInstruction(new fdiv());
        else
            throw new DecacInternalError("Type " + getType() + " not supported.");
    }
}
