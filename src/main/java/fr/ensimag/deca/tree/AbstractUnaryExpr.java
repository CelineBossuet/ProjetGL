package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Unary expression.
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }

    private AbstractExpr operand;

    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    private static final Logger LOG = Logger.getLogger(AbstractUnaryExpr.class);

    public static Logger getLOG() {
        return LOG;
    }

    protected abstract String getOperatorName();

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        s.print(getOperatorName() + " ");
        getOperand().decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

    /**
     * Code pour la génération des instructions pour les expressions unaires de
     * conversion et du moins unaire
     * Si appelé pour autre chose renvoi une erreur (ex pour le Not car pas
     * d'instructions pour lui)
     *
     * @param reg
     * @return
     **/
    protected abstract Instruction geneInstru(GPRegister reg);

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new DecacInternalError("Peut pas utiliser codeGenNoReg pour AbstractUnaryExpr car pas une feuille");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        // redéfini la méthode car il faut pouvoir utiliser geneInstru() pour pouvoir
        // générer les instructions
        // pour les expressions unaires et apres l'ajouter aux instructions du
        // compilateur
        GPRegister reg = getOperand().codeGenReg(compiler);
        compiler.addInstruction(geneInstru(reg));
        return reg;
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("AbsUnaryExpr codeGenStack");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
