package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclField extends Tree{

    /**
     * Verifie la passe 2
     * @param compiler
     * @param superClass
     * @param currentClass
     * @throws ContextualError
     */
    protected abstract void verifyMembers(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition currentClass) throws ContextualError;

    /**
     * Vérifie la passe 3
     * @param compiler
     * @param superClass
     * @param currenClass
     * @throws ContextualError
     */
    protected abstract void verifyBody(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition currenClass) throws ContextualError;

}
