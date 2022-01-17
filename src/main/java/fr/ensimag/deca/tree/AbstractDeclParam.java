package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.Type;

public abstract class AbstractDeclParam extends Tree{

    protected abstract Type verifParam(DecacCompiler compiler) throws ContextualError;

    protected abstract void verifBody(DecacCompiler compiler, Environment localEnv) throws ContextualError;

    protected abstract void codeGenParam(DecacCompiler compiler, int i);
}
