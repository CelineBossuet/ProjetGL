package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

public class ret extends UnaryInstructionToReg {

    public ret(GPRegister op1) {
        super(op1);
    }

}
