package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class MethodCall extends AbstractExpr{
    private  AbstractExpr implicitParam;
    private  AbstractIdentifier methodName;
    private  ListExpr param;

    public MethodCall(AbstractExpr implicitParameter, AbstractIdentifier methodName, ListExpr params) {
        this.implicitParam = implicitParameter;
        this.methodName = methodName;
        this.param = params;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("Not yet implemented");
        Type tparami = this.implicitParam.verifyExpr(compiler, localEnv, currentClass);
        ClassType methodType; // init obligatoire si false
        try {
            methodType = tparami.asClassType("Ceci n'est pas un objet", this.getLocation());
        }catch (ContextualError e){
            throw e;
        }
        MethodDefinition methodDefinition; //Pareil que précedemment
        try {
            methodDefinition = methodType.getDefinition().getMembers().get(this.methodName.getName()).asMethodDefinition("Ceci n'est pas une méthode", this.getLocation());
        }catch (ContextualError e){
            throw e;
        }
        for (AbstractExpr p : this.param.getList()){
            p.verifyExpr(compiler, localEnv, currentClass);
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
    protected GPRegister codeGenReg(DecacCompiler compiler){
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        GPRegister thisReg = implicitParam.codeGenReg(compiler);
        if(param.size()+1!=0){ //taille des variables différente de zero
            compiler.getMemoryManager().allocLB(param.size()+1);
            compiler.addInstruction(new ADDSP(param.size()+1));
        }
        compiler.addInstruction(new STORE(thisReg, new RegisterOffset(0, Register.SP)));
        int offset=-1;
        for(AbstractExpr p : param.getList()) {
            GPRegister paramReg=p.codeGenReg(compiler);

            compiler.addInstruction((new STORE(paramReg, new RegisterOffset(offset, Register.SP))));
            offset--;
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), thisReg)); //Re load si jamais le registre a été utilise entre temps
        //TODO ajout Label erreur si thisReg est de type null
        compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));

        compiler.getMemoryManager().allocBSR();
        compiler.addInstruction(new BSR(new RegisterOffset(methodName.getMethodDefinition().getIndex()+1, reg)));
        //l'appel de la méthode
        compiler.getMemoryManager().deallocLB(param.size()+1);
        compiler.addInstruction(new SUBSP(param.size()+1));

        compiler.addInstruction(new LOAD(Register.getR(0), reg));
        return reg;

    }

    @Override
    public void decompile(IndentPrintStream s) {
        if(!implicitParam.isImplicit()){
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
}
