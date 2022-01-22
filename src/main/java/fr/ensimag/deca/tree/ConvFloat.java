package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.jasmin.i2f;
import fr.ensimag.ima.pseudocode.instructions.jasmin.iload;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl13
 * @date 01/01/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) {
        // throw new UnsupportedOperationException("not yet implemented");
        this.setType(new FloatType(compiler.getSymbolTable().create("float")));
        return this.getType();
    }

    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    @Override
    protected Instruction geneInstru(GPRegister reg) {
        // generation de l'instruction de la conversion d'un entier vers un float
        return new FLOAT(reg, reg);
    }

    @Override
    protected void geneInstruJasmin(DecacCompiler compiler) {
        compiler.addInstruction(new i2f());
    }

}
