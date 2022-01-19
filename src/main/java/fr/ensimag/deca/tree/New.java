package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr{
    private AbstractIdentifier name;

    public New(AbstractIdentifier name){
        this.name=name;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("Not yet implemented");
        Type t = this.name.verifyTypeClass(compiler);
        setType(t);
        return t;
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new UnsupportedOperationException("codeGenNoReg impossible pour New"); //besoin du tas voir p223
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler){
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        ClassDefinition classdef = name.getClassDefinition();
        int size=classdef.getNumberOfFields() +1; //taille de mot à allouer dans le tas
        compiler.addInstruction(new NEW(size, reg));
        compiler.addInstruction(new BOV(compiler.getLabelManager().getPilePleineLabel()));
        compiler.addInstruction(new LEA(classdef.getvTable().getOperand(), Register.getR(0)));
        //LEA : Load Effective Address
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(0, reg)));
        //set vtable for the new object
        compiler.addInstruction(new PUSH(reg));
        Label label = classdef.getConstructorLabel();
        compiler.getMemoryManager().allocBSR();
        compiler.addInstruction(new BSR(new LabelOperand(label)));
        compiler.addInstruction(new POP(reg));



        return reg;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        name.decompile(s);
        s.print("()");
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
    }
}
