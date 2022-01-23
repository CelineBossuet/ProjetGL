package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

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

    private static final Logger LOG = Logger.getLogger(AbstractPrint.class);

    public static Logger getLOG() {
        return LOG;
    }

    abstract String getSuffix();

    public ListExpr getArguments() {
        return arguments;
    }

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr a : getArguments().getList()) {
            Type type = a.verifyExpr(compiler, localEnv, currentClass);
            if (!type.isInt() && !type.isFloat() && !type.isString()) {
                throw new ContextualError(
                        "Type " + type + " is not supported by print or println",
                        getLocation());
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("AbsPrint codeGenInst");
        for (AbstractExpr a : getArguments().getList()) {
            if (this.getPrintHex())
                // print en hexa
                a.codeGenPrintHexa(compiler);
            else
                // print normal
                a.codeGenPrint(compiler);

        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    protected void codeGenInstJasmin(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("AbsPrint codeGenInstJasmin");
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrintJasmin(compiler, getSuffix());
        }
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print" + getSuffix() + (getPrintHex() ? "x" : "") + "(");
        getArguments().decompile(s);
        s.print(");");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
