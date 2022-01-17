package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod{
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListDeclParam param;
    private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam param, AbstractMethodBody body){
        this.body = body;
        this.name=name;
        this.param=param;
        this.type=type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        param.iter(f);
        body.iter(f);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyMembers(DecacCompiler compiler, Environment env, ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("Not yet implemented");
        Type type = this.type.verifyMethodType(compiler);
        Signature sig = this.param.verifyParameters(compiler);
        int toVerify = currentClass.getNumberOfMethods();
        MethodDefinition newDef= new MethodDefinition(type, this.getLocation(), sig, toVerify);
        newDef.setLabel(compiler.getLabelManager().newLabel(name.getName().getName()));
        try{
            currentClass.getMembers().declare(this.name.getName(), newDef);
        } catch (Environment.DoubleDefException e) {
            throw new ContextualError("Méthode déjà créée", this.getLocation());
        }
        this.name.setDefinition(newDef);
    }

    @Override
    public void verifyBody(DecacCompiler compiler, ClassDefinition currentDef) throws ContextualError{
        //throw new UnsupportedOperationException("Not yet implemeted");
        Environment<ExpDefinition> localEnv = new Environment<ExpDefinition>(currentDef.getMembers());
        this.param.verifyBody(compiler, localEnv);
        this.body.verifyBody(compiler, localEnv, currentDef, this.type.getType());
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        param.codeGenParam(compiler);
        compiler.addLabel(name.getMethodDefinition().getLabel());
        compiler.startBlock();
        Label returnLabel =compiler.getLabelManager().newLabel("endMethod");
        boolean noReturn = name.getDefinition().getType().isVoid();
        int size = body.codeGenBody(compiler,!noReturn , returnLabel);
        compiler.endBlock(!noReturn, true, size, returnLabel);


        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
