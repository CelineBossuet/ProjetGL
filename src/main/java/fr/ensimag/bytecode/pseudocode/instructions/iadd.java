package fr.ensimag.bytecode.pseudocode.instructions;


import fr.ensimag.bytecode.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.bytecode.pseudocode.DVal;
import fr.ensimag.bytecode.pseudocode.GPRegister;

public class iadd extends BinaryInstructionDValToReg {
    public iadd(DVal op1, GPRegister op2) {
        super(op1, op2);
    }
}