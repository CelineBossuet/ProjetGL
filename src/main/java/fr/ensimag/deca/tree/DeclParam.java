package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam{
    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name){
        this.type=type;
        this.name=name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
