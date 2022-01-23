package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

import static fr.ensimag.ima.pseudocode.Register.getR;

/**
 * Binary expressions.
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    /**
     * Fonction pour générer les instruction pour les Opération Arithmétiques
     * Si est appelé pour autre chose renvoi une erreur
     * 
     * @param val
     * @param reg
     * @return
     */
    protected abstract BinaryInstruction geneInstru(DVal val, GPRegister reg);

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new DecacInternalError("pas possible car pas feuille de AbstractEpression");
    }

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AbstractExpr.class);

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        getLOG().trace("AbsBinary Expr codeGenReg");
        return codeGenRegInternal(compiler, true);
    }

    protected GPRegister codeGenRegInternal(DecacCompiler compiler, boolean useful) {
        getLOG().trace("AbsBinaryExpr codeGenRegInternal");
        AbstractExpr right = getRightOperand();
        AbstractExpr left = getLeftOperand();
        GPRegister result;
        GPRegister leftValue = left.codeGenReg(compiler);
        DVal rightValue;
        try {
            if (left.getType().isInt() && right.getType().isFloat()) {
                compiler.addInstruction(new FLOAT(leftValue, leftValue));
                left.setType(right.getType());
            }
        } catch (Exception e) {

        }

        if (!right.NeedsRegister()) {
            getLOG().info("cas ou pas besoin de registre");
            rightValue = right.codeGenNoReg(compiler);
            geneOneOrMoreInstru(compiler, rightValue, leftValue, useful);
            result = leftValue;
        } else if (compiler.getRegisterManager().getMax() - compiler.getRegisterManager().getCurrentv() + 1 > 1) {

            getLOG().info("cas ou il y a des registres libres qu'on peut allouer");
            GPRegister r = compiler.allocate(); // on alloue un registre
            rightValue = right.codeGenReg(compiler);
            compiler.release(r);
            geneOneOrMoreInstru(compiler, rightValue, leftValue, useful);
            result = leftValue;
        } else {
            getLOG().info("cas ou pas de registre libre ");
            getLOG().info("on essaye d'utiliser les registres LB de la zone pile");
            compiler.getMemoryManager().allocLB(1);
            compiler.addInstruction(new PUSH(leftValue));
            // PUSH décrémente le pointeur de la pile et entrepose leftValue en haut de la
            // pile
            rightValue = right.codeGenReg(compiler);

            compiler.addInstruction(new POP(getR(0)));
            // POP permet de désempiler de la pile un mot et la met dans R0
            geneOneOrMoreInstru(compiler, rightValue, getR(0), useful);
            result = compiler.getRegisterManager().getCurrent();
            if (useful) {
                getLOG().info("besoin de mettre le résultat dans le bon registre car résultat dans R0");
                compiler.addInstruction(new LOAD(getR(0), result));
            }
        }
        try {
            if (left.getType().isFloat() && right.getType().isInt()) {
                System.out.println("hello");
                compiler.addInstruction(new FLOAT(rightValue, (GPRegister) rightValue));
                right.setType(left.getType());
            }
        } catch (Exception e) {
        }

        if (canOverFlow()) {
            getLOG().info("si erreur rentre dans le Label OverFlow");
            compiler.addInstruction(
                    new BOV(compiler.getLabelManager().getOverFlowLabel(), compiler.getCompilerOptions().getNoCheck()));
        }

        return result;
    }

    protected boolean canOverFlow() {
        return getType().isFloat();
    }

    protected void geneOneOrMoreInstru(DecacCompiler compiler, DVal val, GPRegister reg, boolean useful) {
        getLOG().trace("AbsBinaryExpr geneOneOrMoreInstru");
        compiler.addInstruction(geneInstru(val, reg));
    }
}
