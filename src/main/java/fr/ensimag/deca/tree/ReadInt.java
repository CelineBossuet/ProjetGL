package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullaryInstruction;
import fr.ensimag.ima.pseudocode.instructions.RINT;

import java.io.PrintStream;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        setType(new IntType(compiler.getSymbolTable().create("int")));
        return this.getType();
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected NullaryInstruction geneInstru() {
        return new RINT();
    }
}
