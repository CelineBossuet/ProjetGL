package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.jasmin.PrintInvoked;

// TODO A FAIRE généraliser cette classe pas que pour les print
public class invokevirtual extends UnaryInstruction {

    public invokevirtual(PrintInvoked op) { // todo Prendre pas que des printinvoked
        super(op);
    }

}