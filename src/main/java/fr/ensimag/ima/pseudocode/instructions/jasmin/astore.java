package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

public class astore extends UnaryInstructionToReg {

    public astore(GPRegister op) {
        super(op);
    }

}
