package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

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
        //throw new UnsupportedOperationException("not yet implemented");
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
        System.out.println("aie");
        return new FLOAT(reg, reg);
    }

}
