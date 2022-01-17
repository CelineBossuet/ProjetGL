package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.ima.pseudocode.GPRegister;

public abstract class AbstractDeclField extends Tree{

    /**
     * Verifie la passe 2
     * @param compiler
     * @param superClass
     * @param currentClass
     * @throws ContextualError
     */
    protected abstract void verifyMembers(DecacCompiler compiler, Environment env, ClassDefinition currentClass) throws ContextualError;

    /**
     * Vérifie la passe 3
     * @param compiler
     * @param superClass
     * @param currenClass
     * @throws ContextualError
     */
    protected abstract void verifyBody(DecacCompiler compiler, ClassDefinition currenClass) throws ContextualError;


    protected abstract boolean codeFieldNeedsInit(DecacCompiler compiler, GPRegister reg);

    protected abstract void codeGenFieldBody(DecacCompiler compiler, GPRegister reg);
}
