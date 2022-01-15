package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField{
    private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier fieldName;
    private AbstractInitialization init;

    public DeclField(Visibility visi, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization init){
        this.visibility=visi;
        this.type=type;
        this.fieldName=fieldName;
        this.init=init;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix){
        this.type.prettyPrintType(s, prefix);
        this.fieldName.prettyPrint(s, prefix, true);
        this.init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
