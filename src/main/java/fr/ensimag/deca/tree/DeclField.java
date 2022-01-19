package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField{
    private Visibility visibility;
    private AbstractIdentifier type;
    private AbstractIdentifier fieldName;
    private AbstractInitialization init;

    public DeclField(Visibility visi, AbstractIdentifier type, AbstractIdentifier fieldName, AbstractInitialization init){
        this.visibility=visi;
        this.type=type;
        this.fieldName=fieldName;
        this.init=init;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet implemented");
        s.print(this.visibility.name());
        s.print(" ");
        this.type.decompile(s);
        s.print(" ");
        this.fieldName.decompile(s);
        s.print(" ");
        if (init.hasInit()){
            init.decompile(s);
        }
    }

    @Override
    public String prettyPrintNode(){
        return this.visibility + " " + super.prettyPrintNode();
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix){
        this.type.prettyPrint(s, prefix, false );
        this.fieldName.prettyPrint(s, prefix, false);
        this.init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        init.iter(f);
        //throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    protected void verifyMembers(DecacCompiler compiler, Environment<ExpDefinition> env, ClassDefinition currentClass) throws ContextualError {
        currentClass.incNumberOfFields();
        FieldDefinition field = new FieldDefinition(this.type.verifyType(compiler), this.getLocation(), this.visibility, currentClass, currentClass.getNumberOfFields() ); // +1 car nouvelle déclaration
        try{
            currentClass.getMembers().declare(this.fieldName.getName(), field);
        } catch (Environment.DoubleDefException e) {
            throw new ContextualError("La variable a déjà été déclaré", this.getLocation());
        }
        this.fieldName.verifyExpr(compiler, env, currentClass);
        fieldName.setDefinition(field);
    }

    @Override
    protected void verifyBody(DecacCompiler compiler, ClassDefinition currenClass) throws ContextualError {
        this.init.verifyInitialization(compiler, this.type.verifyType(compiler), currenClass.getMembers(), currenClass);
    }

    @Override
    protected boolean codeFieldNeedsInit(DecacCompiler compiler, GPRegister reg) {
        GPRegister value =type.initDefaultValue(compiler, reg);
        FieldDefinition def =fieldName.getFieldDefinition();
        DAddr field = new RegisterOffset(def.getIndex(), reg);
        compiler.addInstruction(new STORE(value, field));
        return init.hasInit();
    }

    @Override
    protected void codeGenFieldBody(DecacCompiler compiler, GPRegister reg) {
        FieldDefinition def =fieldName.getFieldDefinition();
        DAddr field = new RegisterOffset(def.getIndex(), reg);
        init.codeGeneInit(compiler, field);

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
