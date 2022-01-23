package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public abstract class Instruction {
    private boolean active = true;

    protected boolean isActive() {
        return active;
    }

    /**
     * Disable the instruction in generated code.
     */
    protected void disable() {
        active = false;
    }

    String getName() {
        return this.getClass().getSimpleName();
    }

    abstract void displayOperands(PrintStream s);

    protected void display(PrintStream s) {
        if (isActive()) {
            s.print(getName());
            displayOperands(s);
        }
    }
}
