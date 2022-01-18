package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.Environment.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class DeclVar extends AbstractDeclVar {

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // type
        type.setType(type.verifyType(compiler));

        // name
        try {
            varName.setDefinition(new VariableDefinition(type.getType(), getLocation()));
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError("Double definition of this identifier", getLocation());
        }

        // A FAIRE TODO verifier initialisation
        // Initialization
        initialization.verifyInitialization(compiler, this.type.getType(), localEnv, currentClass);

    }

    @Override
    protected int codeGenVar(DecacCompiler compiler, boolean local, int offsetLocal) {
        //TODO
        //System.out.println("DeclVar codeGenVar");
        DAddr o;
        VariableDefinition d = varName.getVariableDefinition();
        if(local){
            //les varaibles sont déclarées localements dans une méthode donc on créé un RegisterOffset
            o = new RegisterOffset(offsetLocal, Register.LB);
        }else{
            //les variables sont globales
            o = compiler.getMemoryManager().allocGB(1);
        }
        d.setOperand(o);
        initialization.codeGeneInit(compiler, d.getOperand());
        return 1;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
