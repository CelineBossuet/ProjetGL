package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import javax.lang.model.util.ElementScanner6;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fstore;
import fr.ensimag.ima.pseudocode.instructions.jasmin.istore;

import org.apache.commons.lang.Validate;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type expr = getExpression().verifyExpr(compiler, localEnv, currentClass);
        if (!expr.sameType(t) && !(expr.isInt() && t.isFloat())) {
            throw new ContextualError(
                    t + " isn't a subtype of " + expr + " and can't be assign to it", getLocation());
        }
        setExpression(expression.verifyRValue(compiler, localEnv, currentClass, t));
    }

    @Override
    protected void codeGeneInit(DecacCompiler compiler, DAddr target) {
        // System.out.println("Init");
        compiler.addInstruction(new STORE(expression.codeGenReg(compiler), target));

        // la valeur du registre expression.codeGenReg() est stored dans l'adresse
        // target
    }

    @Override
    protected void codeGeneInitJasmin(DecacCompiler compiler, DAddr target) {
        // put expression result in top stack
        expression.codeGenStack(compiler);

        if (expression.getType().isInt()) {
            compiler.addInstruction(new istore(target));
        } else if (expression.getType().isFloat()) {
            compiler.addInstruction(new fstore(target));
        } else if (expression.getType().isBoolean()) {
            compiler.addComment("init boolean");
            compiler.addInstruction(new istore(target));
        } else {
            throw new DecacInternalError("Type " + expression.getType() + " not supported.");
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        getExpression().decompile(s);
    }

    @Override
    protected boolean hasInit() {
        return true;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
