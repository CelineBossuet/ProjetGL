package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

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
    protected void verifBody(DecacCompiler compiler, Environment localENv) throws ContextualError {
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
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
