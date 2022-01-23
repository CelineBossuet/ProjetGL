package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        // throw new UnsupportedOperationException(" Not yest implemented");
        for (AbstractDeclField df : this.getList()) {
            df.decompile(s);
            s.println();
        }
    }
}
