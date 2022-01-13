package fr.ensimag.deca.tree;


import org.apache.log4j.Logger;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    private static final Logger LOG = Logger.getLogger(AbstractOpExactCmp.class);

    public static Logger getLOG() {
        return LOG;
    }

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


}
