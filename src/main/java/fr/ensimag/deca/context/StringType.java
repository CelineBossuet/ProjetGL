package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class StringType extends Type {

    public StringType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public DVal getDefaultValue() {
        throw new DecacInternalError("Pas de valeur par défaut car on peut pas déclarer des types String");
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isString();
        //throw new UnsupportedOperationException("not yet implemented");
    }

}
