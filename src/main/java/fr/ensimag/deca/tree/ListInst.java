package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;

import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * 
 * @author gl13
 * @date 01/01/2022
 */
public class ListInst extends TreeList<AbstractInst> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler     contains "env_types" attribute
     * @param localEnv     corresponds to "env_exp" attribute
     * @param currentClass
     *                     corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *                     corresponds to "return" attribute (void in the main
     *                     bloc).
     */
    public void verifyListInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // verify each instructions
        for (AbstractInst i : this.getList())
            i.verifyInst(compiler, localEnv, currentClass, returnType);
    }

    public void codeGenListInst(DecacCompiler compiler, Label returnLabel, Label local) {
        //System.out.println("LinstInst codeGenListInst");
        int compteur = 1;
        for (AbstractInst i : getList()) {
            if (compteur != this.getList().size()){
                i.codeGenInst(compiler, returnLabel, null);
                compteur += 1;
            }else{
                i.codeGenInst(compiler, returnLabel, local);
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }
}
