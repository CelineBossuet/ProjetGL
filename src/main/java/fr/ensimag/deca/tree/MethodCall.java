package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr {
    private AbstractExpr implicitParam;
    private AbstractIdentifier methodName;
    private ListExpr param;

    private static final Logger LOG = Logger.getLogger(MethodCall.class);

    public MethodCall(AbstractExpr implicitParameter, AbstractIdentifier methodName, ListExpr params) {
        this.implicitParam = implicitParameter;
        this.methodName = methodName;
        this.param = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // throw new UnsupportedOperationException("Not yet implemented");
        Type tparami = this.implicitParam.verifyExpr(compiler, localEnv, currentClass);
        ClassType methodType; // init obligatoire si false
        try {
            methodType = tparami.asClassType("Ceci n'est pas un objet", this.getLocation());
        } catch (ContextualError e) {
            throw e;
        }
        if (methodType.getDefinition().getMembers().get(methodName.getName()) == null) {
            throw new ContextualError("The method " + methodName.getName()
                    + " doesn't exist", getLocation());
        }

        MethodDefinition methodDefinition; // Pareil que précedemment
        try {
            methodDefinition = methodType.getDefinition().getMembers().get(this.methodName.getName())
                    .asMethodDefinition(
                            this.methodName.getName().getName() + " isn't a method", this.getLocation());
        } catch (ContextualError e) {
            throw e;
        }
        Signature sig = methodDefinition.getSignature();
        int index = 0;
        for (AbstractExpr p : this.param.getList()) {
            Type t = p.verifyExpr(compiler, localEnv, currentClass);
            if (index >= sig.size()) {
                throw new ContextualError("Wrong number of arguments", getLocation());
            }
            if (!t.sameType(sig.paramNumber(index))) {
                throw new ContextualError("Parameter given hasn't the same type as in it's definition",
                        getLocation());
            }
            index++;
        }
        if (index != sig.size()) {
            throw new ContextualError("Wrong number of arguments", getLocation());
        }
        this.setType(methodDefinition.getType());
        this.methodName.setDefinition(methodDefinition);
        return this.getType();
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Pas possible pour MethodCall");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        GPRegister thisReg = implicitParam.codeGenReg(compiler);
        if (param.size() + 1 != 0) { // taille des variables différente de zero
            compiler.getMemoryManager().allocLB(param.size() + 1);
            compiler.addInstruction(new ADDSP(param.size() + 1));
        }
        compiler.addInstruction(new STORE(thisReg, new RegisterOffset(0, Register.SP)));
        int offset = -1;
        for (AbstractExpr p : param.getList()) {
            GPRegister paramReg = p.codeGenReg(compiler);

            compiler.addInstruction((new STORE(paramReg, new RegisterOffset(offset, Register.SP))));
            offset--;
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), thisReg));
        LOG.info("Re-load si jamais le registre a été utilise entre temps");
        // TODO ajout Label erreur si thisReg est de type null
        compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));

        compiler.getMemoryManager().allocBSR();
        compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex() + 1, reg)));
        // l'appel de la méthode
        compiler.getMemoryManager().deallocLB(param.size() + 1);
        compiler.addInstruction(new SUBSP(param.size() + 1));

        compiler.addInstruction(new LOAD(Register.getR(0), reg));
        return reg;

    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (!implicitParam.isImplicit()) {
            implicitParam.decompile(s);
            s.print(".");
        }
        methodName.decompile(s);
        s.print("(");
        param.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        implicitParam.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        implicitParam.iter(f);
        methodName.iter(f);
        param.iter(f);
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
