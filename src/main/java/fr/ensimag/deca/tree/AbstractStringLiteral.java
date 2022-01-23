package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractStringLiteral extends AbstractExpr {

    public abstract String getValue();

    private static final Logger LOG = Logger.getLogger(AbstractStringLiteral.class);

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with string literals");
    }

}
