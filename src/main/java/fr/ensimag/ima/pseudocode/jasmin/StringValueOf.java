package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Operand;

public class StringValueOf extends Operand {

    public Type getType() {
        return type;
    }

    public StringValueOf(Type type) {
        super();
        this.type = type;
    }

    private final Type type;

    @Override
    public String toString() {
        String type = "";
        if (getType().isFloat()) {
            type = "F";
        } else if (getType().isInt()) {
            type = "I";
        } else {
            throw new DecacInternalError("Type not supported.");
        }
        return "java/lang/String/valueOf(" + type + ")Ljava/lang/String;";
    }

}
