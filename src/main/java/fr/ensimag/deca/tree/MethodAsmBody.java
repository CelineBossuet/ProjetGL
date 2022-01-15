package fr.ensimag.deca.tree;

public class MethodAsmBody extends AbstractMethodBody{
    private AbstractStringLiteral asmCode;

    public MethodAsmBody(AbstractStringLiteral code){
        super();
        this.asmCode=code;
    }
}
