package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut) {
    if(saut){
        getLeftOperand().codeGenCond(compiler, l, saut);
        getRightOperand().codeGenCond(compiler, l, saut);
    }
    else{
        Label endOr= compiler.getLabelManager().newLabel("endOr"); //on créé notre label correspondant à la fin du Or
        getRightOperand().codeGenCond(compiler, l, saut);
        getLeftOperand().codeGenCond(compiler, endOr, !saut);
        compiler.addLabel(endOr); //on ajoute notre label au compilateur
    }
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
