package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclField extends Tree{

    protected abstract void verifyPass2(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition currentClass) throws ContextualError;

    protected abstract void verifyPass3(DecacCompiler compiler, ClassDefinition superClass, ClassDefinition currenClass) throws ContextualError;

}
