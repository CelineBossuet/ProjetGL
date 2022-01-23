package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.Objects;

public class New extends AbstractExpr {
    private AbstractIdentifier name;

    public New(AbstractIdentifier name) {
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // throw new UnsupportedOperationException("Not yet implemented");
        Type t = this.name.verifyTypeClass(compiler);
        if (!t.isClass()) {
            throw new ContextualError("Argument " + this.name.getName() + " is not a class", getLocation());
        }
        setType(t);
        return t;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new UnsupportedOperationException("codeGenNoReg impossible pour New"); // besoin du tas voir p223
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        ClassDefinition classdef = name.getClassDefinition();
        int size = classdef.getNumberOfFields() + 1; // taille de mot à allouer dans le tas
        compiler.addInstruction(new NEW(size, reg));
        compiler.addInstruction(
                new BOV(compiler.getLabelManager().getTasPleinLabel(), compiler.getCompilerOptions().getNoCheck()));

        if (!(Objects.equals(classdef.getType().getName().getName(), "Object"))) {
            compiler.addInstruction(new LEA(classdef.getvTable().getOperand(), Register.getR(0)));
        }
        // LEA : Load Effective Address
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(0, reg)));
        // set vtable for the new object
        compiler.addInstruction(new PUSH(reg));
        if (!(Objects.equals(classdef.getType().getName().getName(), "Object"))) {
            Label label = classdef.getConstructorLabel();
            compiler.getMemoryManager().allocBSR();
            compiler.addInstruction(new BSR(new LabelOperand(label)));
        }

        compiler.addInstruction(new POP(reg));

        return reg;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        name.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
