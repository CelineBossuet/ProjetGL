package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

import java.io.PrintStream;

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

    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet implemented");
        s.print(this.type.getName().getName() + " ");
        s.print(this.name.getName().getName());
        s.print("(");
        this.param.decompile(s);
        s.println(") {");
        s.indent();
        this.body.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        param.iter(f);
        body.iter(f);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyMembers(DecacCompiler compiler, Environment<ExpDefinition> env, ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("Not yet implemented");
        Type type = this.type.verifyMethodType(compiler);
        Signature sig = this.param.verifyParameters(compiler);
        int toVerify = currentClass.incNumberOfMethods() - 1 ;
        ExpDefinition parent = currentClass.getSuperClass().getMembers().get(this.name.getName());
        if (parent != null){
            MethodDefinition superMethod = parent.asMethodDefinition("Method declared in the super class", this.getLocation());
            toVerify = superMethod.getIndex();
            currentClass.decNumberOfMethods();
            if(!type.sameType(parent.getType())){
                throw new ContextualError("Can't modify the type of an override mehtode. Expected was " + parent.getType() +" but given was " + type, this.getLocation());
            }
            superMethod.getSignature().verifySameSignature(sig, this.getLocation());

        }
        MethodDefinition newDef= new MethodDefinition(type, this.getLocation(), sig, toVerify);
        newDef.setLabel(compiler.getLabelManager().newLabel(name.getName().getName()));
        try{
            currentClass.getMembers().declare(this.name.getName(), newDef);
        } catch (Environment.DoubleDefException e) {
            throw new ContextualError(this.name + " already declared ", this.getLocation());
        }
        this.name.setDefinition(newDef);
    }

    @Override
    public void verifyBody(DecacCompiler compiler, ClassDefinition currentDef) throws ContextualError{
        //throw new UnsupportedOperationException("Not yet implemeted");
        Environment<ExpDefinition> localEnv = new Environment<ExpDefinition>(currentDef.getMembers());
        this.param.verifyBody(compiler, localEnv);
        this.body.verifyBody(compiler, localEnv, currentDef, this.type.getType());
    }
    private static final Logger LOG = Logger.getLogger(DeclMethod.class);

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {

        LOG.info("On recupère tous les paramètres de la méthode");
        param.codeGenParam(compiler); //on récupère les paramètres
        compiler.addLabel(name.getMethodDefinition().getLabel());

        //on ajoute le label du début de la méthode puis on commence donc un nouveau block
        compiler.startBlock();
        Label returnLabel =compiler.getLabelManager().newLabel("endMethod");
        boolean noReturn = name.getDefinition().getType().isVoid();
        int size = body.codeGenBody(compiler,!noReturn , returnLabel); //taille pour le TSTO et ADDDSP
        compiler.endBlock(!noReturn, true, size, returnLabel);
        //si la fonction n'est pas void on doit avoir une erreur de non return donc on met !noReturn

    }
}
