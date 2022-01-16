package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.*;
import fr.ensimag.bytecode.pseudocode.UnaryInstruction;

public class ret extends UnaryInstructionToReg {

    public ret(GPRegister op1) {
        super(op1);
    }

}
