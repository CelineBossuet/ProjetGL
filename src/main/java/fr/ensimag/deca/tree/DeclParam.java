package fr.ensimag.deca.tree;

public class DeclParam extends AbstractDeclParam{
    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name){
        this.type=type;
        this.name=name;
    }
}
