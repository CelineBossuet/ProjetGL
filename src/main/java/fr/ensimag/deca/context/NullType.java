package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class NullType extends Type {

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isNull();
        //throw new UnsupportedOperationException("not yet implemented");
    }

    public NullType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public DVal getDefaultValue() {
        throw new DecacInternalError("pas de valeur par défaut pour le type null car on peut rien déclarer");
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

}
