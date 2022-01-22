package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fsub;
import fr.ensimag.ima.pseudocode.instructions.jasmin.isub;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        // Génération de l'instruction pour une soustraction dans le registre reg
        return new SUB(val, reg);
    }

    @Override
    protected void codeGenArithJasmin(DecacCompiler compiler) {
        if (getType().isInt())
            compiler.addInstruction(new isub());
        else if (getType().isFloat())
            compiler.addInstruction(new fsub());
        else
            throw new DecacInternalError("Type " + getType() + " non supporté.");
    }
}
