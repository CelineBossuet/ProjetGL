package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import static fr.ensimag.ima.pseudocode.Register.R0;
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
     * @param val
     * @param reg
     * @return
     * */
    protected abstract BinaryInstruction geneInstru(DVal val, GPRegister reg);

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler){
        throw new DecacInternalError("pas possible car pas feuille de AbstractEpression");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        //System.out.println("AbsBinary Expr codeGenReg");
        return codeGenRegInternal(compiler, true);
    }

    protected GPRegister codeGenRegInternal(DecacCompiler compiler, boolean useful){
        AbstractExpr right = getRightOperand();
        AbstractExpr left = getLeftOperand();
        GPRegister result;
        GPRegister leftValue = left.codeGenReg(compiler);
        //System.out.println("AbsBinaryExpr codeGenRegInternal");
        if(!right.NeedsRegister()){
            geneOneOrMoreInstru(compiler, right.codeGenNoReg(compiler), leftValue, useful);
            getLOG().info("cas ou pas besoin de registre");
            result =leftValue;
        }
        else if (compiler.getRegisterManager().getMax() -compiler.getRegisterManager().getCurrentv() +1 > 1) {

            GPRegister r = compiler.allocate();
            DVal rightValue = right.codeGenReg(compiler);
            compiler.release(r);
            compiler.addComment("non-trivial expression, registers available");
            geneOneOrMoreInstru(compiler, rightValue, leftValue, useful);
            result = leftValue;
        }
        else{
            compiler.getMemoryManager().allocLB(1);
            compiler.addInstruction(new PUSH(leftValue));

            DVal rightValue = right.codeGenReg(compiler);

            compiler.addInstruction(new POP(getR(0)));
            geneOneOrMoreInstru(compiler, rightValue, getR(0), useful);
            result = compiler.getRegisterManager().getCurrent();
            if (useful) {
                // The result was computed in R0, move it to the right register.
                compiler.addInstruction(new LOAD(getR(0), result));
            }
        }
        /*if (!compiler.getCompilerOptions().getNoRunTimeCheck() &&
                needsOverflowCheck()) {
            compiler.addInstruction(new BOV(compiler.getLabelManager()
                    .getOverflowLabel()));*/
        //System.out.println("fin AbsBinaryExpr codeGenRegInternal");
        return result;
    }


    protected void geneOneOrMoreInstru(DecacCompiler compiler, DVal val, GPRegister reg, boolean usefull){
        //System.out.println("AbsBinaryExpr geneOneOrMoreInstru");
        compiler.addInstruction(geneInstru(val, reg));
    }
}
