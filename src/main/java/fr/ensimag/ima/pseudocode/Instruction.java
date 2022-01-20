package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Ensimag
 * @date 01/01/2022
 */
public abstract class Instruction {
    String getName() {
        return this.getClass().getSimpleName();
    }
    abstract void displayOperands(PrintStream s);
    void display(PrintStream s) {
        if (active){
            s.print(getName());
            displayOperands(s);
        }
    }
    private boolean active=true;

    protected void disable(){
        active = false;
    }
}
