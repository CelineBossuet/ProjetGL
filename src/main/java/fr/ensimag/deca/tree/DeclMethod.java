package fr.ensimag.deca.tree;

public class DeclMethod extends AbstractDeclMethod{
    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private ListDeclParam param;
    private AbstractMethodBody body;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam param, AbstractMethodBody body){
        this.body = body;
        this.name=name;
        this.param=param;
        this.type=type;
    }
}
