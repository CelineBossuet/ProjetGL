package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction geneBranchInstru(boolean saut, Label l) {
        if (saut) {
            return new BGT(l);
            //cette instruction permet de brancher à l si la comparaison est plus grande que zéro
        } else {
            return new BLE(l);
            //cette instruction permet de brancher à l si la comparaison est plus petite ou égale à zéro
        }
    }

    @Override
    protected Instruction genSccInstruction(GPRegister result) {
        return new SGT(result);
        //Set if Greater Than
        //met la valeur de la comparaison dans le registre si la condition est respectée
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

}
