package fr.ensimag.ima.pseudocode.instructions.jasmin;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

public class gotoo extends UnaryInstruction {

    public gotoo(Label lbl) {
        super(lbl);
    }

    protected void display(PrintStream s) {
        if (isActive()) {
            s.print("goto"); // force goto utilisation
            super.displayOperands(s);
        }
    }

}
