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

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix){

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }
}
