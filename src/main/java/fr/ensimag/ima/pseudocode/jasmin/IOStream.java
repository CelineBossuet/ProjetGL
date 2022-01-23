package fr.ensimag.ima.pseudocode.jasmin;

import fr.ensimag.ima.pseudocode.Operand;

public class IOStream extends Operand {

    private boolean in;

    public IOStream(boolean in) {
        this.in = in;
    }

    @Override
    public String toString() {
        return "Ljava/io/" + (in ? "InputStream;" : "PrintStream;");
    }

}
