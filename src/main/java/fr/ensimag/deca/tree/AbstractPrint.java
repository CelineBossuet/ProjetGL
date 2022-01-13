package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr a : getArguments().getList()) {
            a.verifyInst(compiler, localEnv, currentClass, returnType);
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        //System.out.println("AbsPrint codeGenInst");
        for (AbstractExpr a : getArguments().getList()) {
            if (this.printHex)
                //print en hexa
                a.codeGenPrintHexa(compiler);
            else
                //print normal
                a.codeGenPrint(compiler);

        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("println(");
        this.arguments.decompile(s);
        s.print(");");
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
