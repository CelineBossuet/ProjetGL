package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacCompiler.JasminStaticVars;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullaryInstruction;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.jasmin.aload;
import fr.ensimag.ima.pseudocode.instructions.jasmin.invokevirtual;
import fr.ensimag.ima.pseudocode.jasmin.ScannerRead;
import fr.ensimag.ima.pseudocode.jasmin.VarID;

import org.apache.log4j.Logger;

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

    private static final Logger LOG = Logger.getLogger(AbstractReadExpr.class);

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new DecacInternalError("pas possible car pas feuille de AbstractEpression");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        getLOG().trace("AbsReadExpr codeGenReg");
        getLOG().info("redéfinition de geneInstru() dans AbstractReadExpr");
        //// redéfinition de la méthode car il faut pouvoir utiliser geneInstru() de
        //// AbstractReadExpr
        // d'apres exemple p19 on doit pouvoir généré les instructions RINT (ou RFLOAT),
        //// BOV io_error et LOAD
        compiler.addInstruction(geneInstru());

        // on branche le label io_error avec BOV
        Label label = compiler.getLabelManager().getIErrorLabel();
        compiler.addInstruction(new BOV(label, compiler.getCompilerOptions().getNoCheck()));

        // on LOAD dans le registre courant la valeur de R1
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        compiler.addInstruction(new LOAD(getR(1), reg));

        return reg;
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("AbsReadExpr codeGenStack");
        // load scanner
        compiler.addInstruction(new aload(new VarID(JasminStaticVars.SYSTEM_IN.id())));
        compiler.addInstruction(new invokevirtual(new ScannerRead(getType())));
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with read instructions");
    }
}
