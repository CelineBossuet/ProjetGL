package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SLT;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ifge;
import fr.ensimag.ima.pseudocode.instructions.jasmin.iflt;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction geneBranchInstru(boolean saut, Label l) {
        if (saut) {
            return new BLT(l);
            // cette instruction permet de brancher au label si c'est plus petit que zero
        } else {
            return new BGE(l);
            // cette instruction permet de brancher au label si la comparaison est plus
            // grande ou égale à zero
        }
    }

    @Override
    protected Instruction genSccInstruction(GPRegister result) {
        return new SLT(result);
        // Set if Less Than
        // met la valeur de la comparaison dans le registre si la condition est
        // respectée
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        if (jump)
            compiler.addInstruction(new iflt(l));
        else
            compiler.addInstruction(new ifge(l));
    }

}
