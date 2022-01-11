package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type left = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type right = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if(!left.isInt()){
            throw new UnsupportedOperationException("Modulo que entre des int, l'opérande de gauche est de type "+left);
        }
        if(!right.isInt()){
            throw new UnsupportedOperationException("Modulo que entre des int, l'opérande de droite est de type "+right);
        }
        setType(compiler.getEnvironmentType().INT);
        return getType();
        // throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg){
        //Génération de l'instruction pour un modulo dans le registre reg
        return new REM(val, reg);
    }

}
