package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import org.apache.log4j.Logger;

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

    private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);

    public static Logger getLOG() {
        return LOG;
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
        }else if ((left.isInt() && right.isInt()) || (left.isFloat() && right.isFloat())){
            setType(right);
            return right;
        }
        else{
            throw new ContextualError("Can't do aritmetical operation between types "+left+" and "+right
                    , this.getLocation());
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }
}
