package fr.ensimag.ima.pseudocode.instructions.jasmin;

import fr.ensimag.ima.pseudocode.UnaryInstruction;
import fr.ensimag.ima.pseudocode.jasmin.StringConstant;

public class ldc extends UnaryInstruction {

    public ldc(StringConstant operand) {
        super(operand);
    }
}
