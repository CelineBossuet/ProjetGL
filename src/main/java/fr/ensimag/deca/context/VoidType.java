package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;

/**
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public class VoidType extends Type {

    public VoidType(SymbolTable.Symbol name) {
        super(name);
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public DVal getDefaultValue() {
        throw new DecacInternalError("Pas de valeur par défaut pour type Void");
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isVoid();
        //throw new UnsupportedOperationException("not yet implemented");
    }

}
