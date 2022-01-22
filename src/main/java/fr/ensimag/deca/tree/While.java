package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("While codeGenInst");
        Label debutwhile = compiler.getLabelManager().newLabel("while");
        Label condwhile = compiler.getLabelManager().newLabel("condWhile");
        compiler.addInstruction(new BRA(condwhile));
        compiler.addLabel(debutwhile);
        this.body.codeGenListInst(compiler, returnLabel, condwhile);
        compiler.addLabel(condwhile);
        this.condition.codeGenCond(compiler, debutwhile, true);
        getLOG().info("création et fixation du Label de début du while");

        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        Type cond = this.getCondition().verifyExpr(compiler, localEnv, currentClass);
        if (!cond.isBoolean()){
            throw new ContextualError("The condition of a while loop should be a bool but here it is a: " + cond, this.getLocation());
        }

        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
