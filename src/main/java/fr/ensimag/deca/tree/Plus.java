package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADD;

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
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg){
        //Génération de l'instruction pour une addition dans le registre reg
        return new ADD(val, reg);
    }

    @Override
    protected void codeGenInstBytecode(DecacCompiler compiler, Label returnLabel, Label local) {

    }
}
