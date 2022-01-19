package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam>{
    @Override
    public void decompile(IndentPrintStream s) {
        int compteur = 1;
        for (AbstractDeclParam p : this.getList()){
            p.decompile(s);
            if (compteur != this.size()){
                s.print(", ");
                compteur+=1;
            }
        }
    }

    public Signature verifyParameters(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam p : this.getList()){
            Type type = p.verifParam(compiler);
            sig.add(type);
        }
        return sig;
    }

    public void verifyBody(DecacCompiler compiler, Environment<ExpDefinition> localEnv) throws ContextualError {
        for (AbstractDeclParam p : this.getList()){
            p.verifBody(compiler, localEnv);
        }
    }

    public void codeGenParam(DecacCompiler compiler){
        int i=0;
        for (AbstractDeclParam p : getList()){
            p.codeGenParam(compiler, i);
            i++;
        }
    }

}
