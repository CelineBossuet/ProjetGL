package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractLValue extends AbstractExpr {

    public abstract DAddr codeGenAddr(DecacCompiler compiler);
}
