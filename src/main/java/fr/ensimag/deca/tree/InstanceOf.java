//Ajouté pour InstanceOf de DecaParser
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr {
    private AbstractExpr expr;
    private AbstractIdentifier type;

    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        super();
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print('(');
        this.expr.decompile(s);
        s.print(" instanceof ");
        this.type.decompile(s);
        s.print(')');
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {

    }

    @Override
    protected void iterChildren(TreeFunction f) {

    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new DecacInternalError("méthode codeGenNoReg pas instantiable pour InstanceOf");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        return null;
        // TODO
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("InstanceOf codeGenStack");
        // TODO
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with instanceof");
    }
}
