package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.UnaryInstruction;
import fr.ensimag.bytecode.pseudocode.DVal;
import fr.ensimag.bytecode.pseudocode.GPRegister;
import fr.ensimag.bytecode.pseudocode.ImmediateInteger;
import fr.ensimag.bytecode.pseudocode.Operand;
import fr.ensimag.bytecode.pseudocode.UnaryInstruction;

public class ret extends UnaryInstruction {

    public ret(Operand op1) {
        super(op1);
    }


}
