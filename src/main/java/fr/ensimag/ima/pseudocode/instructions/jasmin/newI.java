package fr.ensimag.ima.pseudocode.instructions.jasmin;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.jasmin.ScannerObject;

// TODO A FAIRE généraliser cette classe pas que pour les read
public class newI extends UnaryInstruction {

    public newI(ScannerObject op) { // todo Prendre pas que des printinvoked
        super(op);
    }

    protected void display(PrintStream s) {
        if (isActive()) {
            s.print("new"); // force goto utilisation
            super.displayOperands(s);
        }
    }

}