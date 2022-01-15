package fr.ensimag.deca.tree;

public class MethodBody extends AbstractMethodBody{
    private ListDeclVar variablesLocales;
    private ListInst instructions;

    public MethodBody(ListDeclVar var, ListInst instruc){
        this.instructions=instruc;
        this.variablesLocales =var;
    }
}
