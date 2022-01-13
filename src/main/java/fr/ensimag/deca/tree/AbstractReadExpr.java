package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import static fr.ensimag.ima.pseudocode.Register.getR;

/**
 * read...() statement.
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    protected abstract NullaryInstruction geneInstru();

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new DecacInternalError("pas possible car pas feuille de AbstractEpression");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        //System.out.println("AbsReadExpr codeGenReg");
        getLOG().info("redéfinition de geneInstru() dans AbstractReadExpr");
        //// redéfinition de la méthode car il faut pouvoir utiliser geneInstru() de
        //// AbstractReadExpr
        // d'apres exemple p19 on doit pouvoir généré les instructions RINT (ou RFLOAT),
        //// BOV io_error et LOAD
        compiler.addInstruction(geneInstru());

        // on branche le label io_error avec BOV
        Label label = compiler.getLabelManager().getIErrorLabel();
        compiler.addInstruction(new BOV(label));

        // on LOAD dans le registre courrant la valeur de R1
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        compiler.addInstruction(new LOAD(getR(1), reg));

        return reg;
    }
}
