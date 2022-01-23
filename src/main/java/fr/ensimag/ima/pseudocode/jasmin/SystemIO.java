package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.ima.pseudocode.Operand;

public class SystemIO extends Operand {

    private boolean in;

    public SystemIO(boolean in) {
        this.in = in;
    }

    @Override
    public String toString() {
        return "java/lang/System/" + (in ? "in" : "out");
    }

}