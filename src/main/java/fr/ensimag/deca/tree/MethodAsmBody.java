package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class MethodAsmBody extends AbstractMethodBody{
    private AbstractStringLiteral asmCode;

    public MethodAsmBody(AbstractStringLiteral code){
        super();
        this.asmCode=code;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmCode.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
