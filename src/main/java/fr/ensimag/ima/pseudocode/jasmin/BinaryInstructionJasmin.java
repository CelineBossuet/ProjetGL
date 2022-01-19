package fr.ensimag.ima.pseudocode.jasmin;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.Operand;

public class BinaryInstructionJasmin extends BinaryInstruction {

    protected BinaryInstructionJasmin(Operand op1, Operand op2) {
        super(op1, op2);
    }

    @Override
    protected void displayOperands(PrintStream s) {
        s.print(" ");
        s.print(getOperand1());
        s.print(" ");
        s.print(getOperand2());
    }
}
