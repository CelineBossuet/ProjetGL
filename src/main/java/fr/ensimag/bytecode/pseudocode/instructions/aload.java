package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.bytecode.pseudocode.DVal;
import fr.ensimag.bytecode.pseudocode.GPRegister;
import fr.ensimag.bytecode.pseudocode.ImmediateInteger;

public class aload extends BinaryInstructionDValToReg {

    public aload(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public aload(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

}
