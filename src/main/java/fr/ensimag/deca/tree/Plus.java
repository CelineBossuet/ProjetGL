package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fadd;
import fr.ensimag.ima.pseudocode.instructions.jasmin.iadd;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        // Génération de l'instruction pour une addition dans le registre reg
        return new ADD(val, reg);
    }

    @Override
    protected void codeGenArithJasmin(DecacCompiler compiler) {
        if (getType().isInt())
            compiler.addInstruction(new iadd());
        else if (getType().isFloat())
            compiler.addInstruction(new fadd());
        else
            throw new DecacInternalError("Type " + getType() + " not supported.");
    }
}
