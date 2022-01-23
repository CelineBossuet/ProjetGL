package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.jasmin.StringValueOf;

// TODO A FAIRE généraliser cette classe pas que pour les valueOf
public class invokestatic extends UnaryInstruction {

    public invokestatic(StringValueOf op) { // todo Prendre pas que des StringValueOf
        super(op);
    }

}