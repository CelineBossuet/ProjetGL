package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;

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
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg){
        //Génération de l'instruction pour une division dans le registre reg
        return new DIV(val, reg);
    }
}
