package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullaryInstruction;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;

import java.io.PrintStream;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class ReadFloat extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        setType(new FloatType(compiler.getSymbolTable().create("float")));
        return this.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readFloat()");
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
        return new RFLOAT();
    }

}
