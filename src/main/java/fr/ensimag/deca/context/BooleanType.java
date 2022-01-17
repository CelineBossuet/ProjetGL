package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class BooleanType extends Type {

    public BooleanType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public DVal getDefaultValue() {
        return new ImmediateInteger(0);
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isBoolean();
        //throw new UnsupportedOperationException("not yet implemented");
    }

}
