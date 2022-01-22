package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.ima.pseudocode.Operand;

public class PrintInvoked extends Operand {

    public PrintInvoked(String suffix) {
        super();
        this.suffix = suffix;
    }

    private final String suffix;

    @Override
    public String toString() {
        return "java/io/PrintStream/print" + suffix + "(Ljava/lang/String;)V";
    }

}
