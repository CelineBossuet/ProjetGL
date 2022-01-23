package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SLE;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ifgt;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ifle;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction geneBranchInstru(boolean saut, Label l) {
        if (saut) {
            return new BLE(l);
            // cette instruction permet de brancher à l si la comparaison est plus petite ou
            // égale à zéro
        } else {
            return new BGT(l);
            // cette instruction permet de brancher à l si la comparaison est plus grande
            // que zéro
        }
    }

    @Override
    protected Instruction genSccInstruction(GPRegister result) {
        return new SLE(result);
        // Set if Lower or Equals
        // met la valeur de la comparaison dans le registre si la condition est
        // respectée
    }

    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        if (jump)
            compiler.addInstruction(new ifle(l));
        else
            compiler.addInstruction(new ifgt(l));
    }

}
