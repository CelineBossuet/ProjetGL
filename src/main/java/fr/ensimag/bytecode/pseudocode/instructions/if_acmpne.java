package fr.ensimag.bytecode.pseudocode.instructions;

import fr.ensimag.bytecode.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.bytecode.pseudocode.DVal;
import fr.ensimag.bytecode.pseudocode.GPRegister;
import fr.ensimag.bytecode.pseudocode.Operand;

public class if_acmpne extends BinaryInstructionDValToReg {

    public if_acmpne(DVal op1, GPRegister op2){super(op1, op2);}

}
