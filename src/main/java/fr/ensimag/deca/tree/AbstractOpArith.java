package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type left = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type right = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if(left.isFloat() && right.isInt()){
            ConvFloat conversion = new ConvFloat(getRightOperand());
            conversion.verifyExpr(compiler, localEnv, currentClass);
            setRightOperand(conversion);
            this.setType(left);
            return left;
        }else if(left.isInt() && right.isFloat()){
            ConvFloat conversion = new ConvFloat(getLeftOperand());
            conversion.verifyExpr(compiler, localEnv, currentClass);
            setLeftOperand(conversion);
            this.setType(right);
            return right;
        }else{
            setType(right);
            return right;
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }
}
