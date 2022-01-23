package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.ima.pseudocode.Operand;

public class Constant extends Operand {

    private final Object constant;

    public Constant(Object constant) {
        this.constant = constant;
    }

    public Object getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return constant.toString();
    }

}