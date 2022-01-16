package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.bytecode.pseudocode.DVal;
import fr.ensimag.bytecode.pseudocode.GPRegister;
import fr.ensimag.bytecode.pseudocode.ImmediateInteger;

public class aaload extends BinaryInstructionDValToReg {

    public aaload(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public aaload(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

}
