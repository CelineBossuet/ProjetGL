package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.Type;

public abstract class AbstractMethodBody extends Tree{

    public abstract void verifyBody(DecacCompiler compiler, Environment localEnv, ClassDefinition current, Type t) throws ContextualError;
}
