package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Operand;

public class PrintInvoked extends Operand {

    public Type getType() {
        return type;
    }

    public PrintInvoked(Type type, String suffix) {
        super();
        this.type = type;
        this.suffix = suffix;
    }

    private final Type type;
    private final String suffix;

    @Override
    public String toString() {
        String type = "";
        if (getType().isFloat()) {
            throw new UnsupportedOperationException("Not yet implemented, can't print float");
        } else if (getType().isInt()) {
            throw new UnsupportedOperationException("Not yet implemented, can't print float");
        } else if (getType().isString()) {
            type = "java/lang/String;";
        } else {
            throw new DecacInternalError("Type not supported.");
        }
        return "java/io/PrintStream/print" + suffix + "(L" + type + ")V";
    }

}
