package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;
import java.util.HashSet;

public class DeclParam extends AbstractDeclParam{
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private HashSet<String> declared = new HashSet<>();

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name){
        this.type=type;
        this.name=name;
    }

    @Override
    protected Type verifParam(DecacCompiler compiler) throws ContextualError {
        Type type = this.type.verifyType(compiler);
        if(type.isVoid()){
            throw new ContextualError("Paramètres ne peuvent pas être void", getLocation());
        }
        return type;
    }

    @Override
    protected void verifBody(DecacCompiler compiler, Environment<ExpDefinition> localENv) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        ParamDefinition definition = new ParamDefinition(t, this.getLocation());
        this.name.setDefinition(definition);
        try{
            localENv.declare(this.name.getName(), definition);
        } catch (Environment.DoubleDefException e) {
            if (declared.contains(this.name.getName().getName())){
                throw new ContextualError("Paramètre déjà présent", this.getLocation());
            }else{
                declared.add(this.name.getName().getName());
            }
        }
    }

    @Override
    protected void codeGenParam(DecacCompiler compiler, int i) {
        name.getExpDefinition().setOperand(new RegisterOffset(-3-i, Register.LB));
    }


    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet implemented");
        s.print(this.type.getName().getName());
        s.print(this.name.getName().getName());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
