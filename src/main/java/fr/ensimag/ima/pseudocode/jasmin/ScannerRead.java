package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Operand;

public class ScannerRead extends Operand {

    public ScannerRead(Type type) {
        super();
        this.type = type;
    }

    private final Type type;

    @Override
    public String toString() {
        String method = "";
        if (type.isInt())
            method = "nextInt()I";
        else if (type.isFloat())
            method = "nextFloat()F";
        else
            throw new DecacInternalError("Type " + type + "not supported.");
        return "java/util/Scanner/" + method;
    }

}
