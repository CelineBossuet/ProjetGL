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
        boolean first = true;
        for (AbstractExpr expr : getList()) {
            s.print(first ? "" : ",");
            expr.decompileInst(s); // decompileInst to print ; in case of instruction
            first = false;
        }
    }
}
