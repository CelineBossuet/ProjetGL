package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

import static fr.ensimag.deca.tree.AbstractExpr.getLOG;

/**
 * Full if/else if/else statement.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public void setElseBranch(ListInst elseBranch) {
        this.elseBranch = elseBranch;
    }

    public void setElseBranch(AbstractInst elseBranch) {
        Validate.notNull(elseBranch);
        this.elseBranch.add(elseBranch);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        // System.out.println("IfThenElse codeGenInst");
        Label endIf;
        if (local == null) {
            getLOG().info("on est pas déjà dans une boucle de if donc faut créer le label de fin de if");
            endIf = compiler.getLabelManager().newLabel("endif");

        } else {
            getLOG().info("notre condition correspond au else d'une autre condition");
            endIf = local;
        }
        if (!elseBranch.isEmpty()) {
            getLOG().debug("On a une branche Else dans notre condition");
            getLOG().info("brache Else donc il faut créer le Label correspondant");
            Label startElse = compiler.getLabelManager().newLabel("startelse");

            condition.codeGenCond(compiler, startElse, false);
            thenBranch.codeGenListInst(compiler, returnLabel, endIf);
            compiler.addInstruction(new BRA(endIf));
            compiler.addLabel(startElse);
            elseBranch.codeGenListInst(compiler, returnLabel, endIf);
        } else {
            getLOG().debug("Il y a pas de branche Else dans la condition");
            condition.codeGenCond(compiler, endIf, false);
            thenBranch.codeGenListInst(compiler, returnLabel, endIf);
        }
        if (local == null) {
            getLOG().debug("on ajoute le Label de la fin du if à la fin des instructions");
            compiler.addLabel(endIf);
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        this.condition.decompile(s);
        s.println("){");
        s.indent();
        this.thenBranch.decompile(s);
        s.unindent();
        s.println("} else {");
        s.indent();
        this.elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
