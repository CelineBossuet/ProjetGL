package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;

/**
 * Type defined by a class.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class ClassType extends Type {

    protected ClassDefinition definition;

    public ClassDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    @Override
    public DVal getDefaultValue() {
        return new NullOperand();
    }

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     */
    protected ClassType(Symbol className) {
        super(className);
    }

    @Override
    public boolean sameType(Type otherType) {
        return otherType.isClass();
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {//A faire dans la partie classe
        //throw new UnsupportedOperationException("not yet implemented");
        ClassType t = this.getDefinition().getSuperClass().getType();
        while(t!=null){
            if (t.sameType(potentialSuperClass)){
                return true;
            }else{
                t = t.getDefinition().getSuperClass().getType();
            }
        }
        return false;
    }
}
