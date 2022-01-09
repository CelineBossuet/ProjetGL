package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 * Operator "x >= y"
 * 
 * @author gl13
 * @date 01/01/2022
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction geneBranchInstru(boolean saut, Label l) {
        if (saut) {
            return new BGE(l);
            //cette instruction permet de brancher à l si la comparaison est plus grande ou égale à zero
        } else {
            return new BLT(l);
            //cette instruction permet de brancher à l si la comparaison est plus petite que zéro
        }
    }

    @Override
    protected Instruction genSccInstruction(GPRegister result) {
        return new SGE(result);
        //Set if Greater of Equals
        //met la valeur de la comparaison dans le registre si la condition est respectée
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
