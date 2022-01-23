package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ifeq;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ifne;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction geneBranchInstru(boolean saut, Label l) {
        if (saut) {
            return new BEQ(l);
            // Branch if Equals
        } else {
            return new BNE(l);
            // Branch if not Equals
        }
    }

    @Override
    protected Instruction genSccInstruction(GPRegister result) {
        return new SEQ(result);
        // Set on EQuals
        // Si le Zero flag testé est vrai l'opérand destinataire est set sinon il est
        // cleared
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        if (jump)
            compiler.addInstruction(new ifeq(l));
        else
            compiler.addInstruction(new ifne(l));
    }

}
