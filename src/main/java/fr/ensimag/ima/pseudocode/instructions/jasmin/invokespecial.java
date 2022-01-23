package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.jasmin.SpecialScanner;

// TODO A FAIRE généraliser cette classe pas que pour les read
public class invokespecial extends UnaryInstruction {

    public invokespecial(SpecialScanner op) {
        super(op);
    }

}