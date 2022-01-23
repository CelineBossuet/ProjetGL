package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.ima.pseudocode.instructions.jasmin.irem;

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
    protected boolean canOverFlow(){
        return true;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type left = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type right = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!left.isInt()) {
            throw new ContextualError(
                    "Modulo should be between Int but left operand is: " + left, getLocation());
        }
        if (!right.isInt()) {
            throw new ContextualError(
                    "Modulo should be between Int but right operand is: " + right, getLocation());
        }
        setType(new IntType(compiler.getSymbolTable().create("int")));

        return getType();
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        // Génération de l'instruction pour un modulo dans le registre reg
        return new REM(val, reg);
    }

    @Override
    protected void codeGenArithJasmin(DecacCompiler compiler) {
        if (getType().isInt())
            compiler.addInstruction(new irem());
        else
            throw new DecacInternalError("Type " + getType() + " not supported.");
    }

}
