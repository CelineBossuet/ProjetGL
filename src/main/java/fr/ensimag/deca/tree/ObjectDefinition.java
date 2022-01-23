package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.ima.pseudocode.IMAProgram;

public class ObjectDefinition extends ClassDefinition {
    IMAProgram constructor;

    public ObjectDefinition(ObjectType type, ObjectDefinition superClass, IMAProgram constructor) {
        super(type, Location.BUILTIN, superClass);
        this.constructor = constructor;
    }
}