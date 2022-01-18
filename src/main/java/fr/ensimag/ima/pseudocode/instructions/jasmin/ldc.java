package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.jasmin.Constant;

public class ldc extends UnaryInstruction {

    public ldc(Constant operand) {
        super(operand);
    }
}
