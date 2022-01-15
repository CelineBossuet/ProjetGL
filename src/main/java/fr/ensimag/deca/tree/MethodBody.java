package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodBody extends AbstractMethodBody{
    private ListDeclVar variablesLocales;
    private ListInst instructions;

    public MethodBody(ListDeclVar var, ListInst instruc){
        this.instructions=instruc;
        this.variablesLocales =var;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        variablesLocales.prettyPrint(s, prefix, false);
        instructions.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
