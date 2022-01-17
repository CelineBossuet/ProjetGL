package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.*;

public class invokevirtual extends UnaryInstructionToReg {

    public invokevirtual(GPRegister op) {
        super(op);
    }

}