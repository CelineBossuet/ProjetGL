package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fmul;
import fr.ensimag.ima.pseudocode.instructions.jasmin.imul;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        // Génération de l'instruction pour une multiplication dans le registre reg
        return new MUL(val, reg);
    }

    @Override
    protected void codeGenArithJasmin(DecacCompiler compiler) {
        if (getType().isInt())
            compiler.addInstruction(new imul());
        else if (getType().isFloat())
            compiler.addInstruction(new fmul());
        else
            throw new DecacInternalError("Type " + getType() + " not supported.");
    }

}
