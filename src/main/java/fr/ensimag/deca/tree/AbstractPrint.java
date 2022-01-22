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
            Type type=a.verifyExpr(compiler, localEnv, currentClass);
            if(!type.isInt() && !type.isFloat() && !type.isString()){
                throw new ContextualError(
                        "Type "+type+" is not supported by print or println",
                        getLocation());
            }
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("AbsPrint codeGenInst");
        for (AbstractExpr a : getArguments().getList()) {
            if (this.getPrintHex())
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
        s.print(")");
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
