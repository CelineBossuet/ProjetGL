package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Label;

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
        asmCode.iter(f);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void verifyBody(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition current, Type t) throws ContextualError {

    }

    @Override
    public int codeGenBody(DecacCompiler compiler, boolean error, Label labelReturn) {
        compiler.add(new InlinePortion(asmCode.getValue())); //on ajoute tout le code
        return 0; //pas de variable donc 0
    }
}
