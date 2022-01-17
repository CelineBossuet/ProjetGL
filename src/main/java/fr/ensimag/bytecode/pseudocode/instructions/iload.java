package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.*;

public class iload extends UnaryInstructionToReg {

    public iload(GPRegister op) {
        super(op);
    }

}
