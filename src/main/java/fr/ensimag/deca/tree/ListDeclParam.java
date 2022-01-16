package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam>{
    @Override
    public void decompile(IndentPrintStream s) {

    }

    public Signature verifyParameters(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam p : this.getList()){
            Type type = p.verifParam(compiler);
            sig.add(type);
        }
        return sig;
    }

    public void verifyBody(DecacCompiler compiler, Environment localEnv) throws ContextualError {
        for (AbstractDeclParam p : this.getList()){
            p.verifBody(compiler, localEnv);
        }
    }

}
