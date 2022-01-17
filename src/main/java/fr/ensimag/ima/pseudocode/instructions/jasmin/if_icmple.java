package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

public class if_icmple extends BinaryInstructionDValToReg {

    public if_icmple(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

}
