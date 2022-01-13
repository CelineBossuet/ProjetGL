package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl13
 * @date 01/01/2022
 */
public class ListExpr extends TreeList<AbstractExpr> {

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet implemented");
        int compteur = 1;
        for (AbstractExpr i : getList()) {
            if (compteur!= getList().size()){
                i.decompileInst(s);
                s.print(",");
            }else{
                i.decompile(s);
            }
        }
    }
}
