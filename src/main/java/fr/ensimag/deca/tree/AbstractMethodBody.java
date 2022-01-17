package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.Label;

public abstract class AbstractMethodBody extends Tree{

    public abstract void verifyBody(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition current, Type t) throws ContextualError;

    public abstract int codeGenBody(DecacCompiler compiler, boolean error, Label labelReturn);
}
