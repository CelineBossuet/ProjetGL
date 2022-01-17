package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

public class aaload extends BinaryInstructionDValToReg {

    public aaload(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public aaload(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

}
