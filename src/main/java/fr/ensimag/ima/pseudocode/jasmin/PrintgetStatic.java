package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Operand;

public class PrintgetStatic extends Operand {

    public Type getType() {
        return type;
    }

    public PrintgetStatic(Type type) {
        super();
        this.type = type;
        //this.suffix = suffix;
    }

    private final Type type;
    //private final String suffix;

    @Override
    public String toString() {
        String type = "";
        if (getType().isFloat()) {
            throw new UnsupportedOperationException("Not yet implemented, can't print float");
        } else if (getType().isInt()) {
            throw new UnsupportedOperationException("Not yet implemented, can't print float");
        } else if (getType().isString()) {
            type = "java/lang/System/out Ljava/io/PrintStream;";
        } else {
            throw new DecacInternalError("Type not supported.");
        }
        return type;
    }

}