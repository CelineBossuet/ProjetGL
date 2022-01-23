package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractLValue extends AbstractExpr {

    public abstract DAddr codeGenAddr(DecacCompiler compiler);

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with abstract value");
    }
}
