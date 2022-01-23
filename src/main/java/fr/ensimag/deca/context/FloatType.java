package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class FloatType extends Type {

    public FloatType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isFloat() {
        return true;
    }

    @Override
    public DVal getDefaultValue() {
        return new ImmediateFloat(0.0f);
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isFloat();
        //throw new UnsupportedOperationException("not yet implemented");
    }
}
