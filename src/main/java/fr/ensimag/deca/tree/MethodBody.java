package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

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
        variablesLocales.iter(f);
        instructions.iter(f);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void verifyBody(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition current, Type t) throws ContextualError {
        this.variablesLocales.verifyListDeclVariable(compiler, localEnv, current);
        this.instructions.verifyListInst(compiler, localEnv, current, t);
    }

    @Override
    public int codeGenBody(DecacCompiler compiler, boolean error, Label labelReturn) {
        int size = variablesLocales.codeGenListVar(compiler, true);
        if (error){
            instructions.codeGenListInst(compiler, labelReturn, null);
        }else{
            instructions.codeGenListInst(compiler, labelReturn, labelReturn);
        }

        return size;
    }
}
