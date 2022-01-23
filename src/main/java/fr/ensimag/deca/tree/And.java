package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut) {
        if (saut) {
            getLOG().info("récuperer les operands de droite et gauche pour saut vrai");
            Label endLabel = compiler.getLabelManager().newLabel("endAnd"); //on créé un label qui correspond à la fin du And
            getLeftOperand().codeGenCond(compiler, endLabel, !saut);
            getRightOperand().codeGenCond(compiler,l, saut);

            compiler.addLabel(endLabel); //on ajoute le label dans notre compilateur
        } else {
            getLOG().info("récupérer des opérands de droites et gauche pour saut false");
            getLeftOperand().codeGenCond(compiler, l,saut);
            getRightOperand().codeGenCond(compiler, l,saut);
        }
    }


    @Override
    protected String getOperatorName() {
        return "&&";
    }



}
