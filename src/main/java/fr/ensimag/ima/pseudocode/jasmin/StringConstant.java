package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.ima.pseudocode.Operand;

public class StringConstant extends Operand {

    private final String constant;

    public StringConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return "\"" + constant + "\"";
    }

}