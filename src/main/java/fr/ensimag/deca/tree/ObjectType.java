package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.IMAProgram;

public class ObjectType extends ClassType {

    public ObjectType(SymbolTable.Symbol className, ObjectDefinition definition, IMAProgram imaProgram) {
        super(className);
        this.definition = new ObjectDefinition(this, definition, imaProgram);
    }
}
