package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

/**
 * Instruction
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractInst extends Tree {

    private static final Logger LOG = Logger.getLogger(AbstractInst.class);

    public static Logger getLOG() {
        return LOG;
    }

    /**
     * Implements non-terminal "inst" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass
     *                     corresponds to the "class" attribute (null in the main
     *                     bloc).
     * @param returnType
     *                     corresponds to the "return" attribute (void in the main
     *                     bloc).
     */
    protected abstract void verifyInst(DecacCompiler compiler,
            Environment<ExpDefinition> localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError;

    /**
     * Generate assembly code for the instruction.
     * 
     * @param compiler
     */
    protected abstract void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local);

    protected abstract void codeGenInstJasmin(DecacCompiler compiler, Label returnLabel, Label local);
    // TODO A FAIRE

    /**
     * Decompile the tree, considering it as an instruction.
     *
     * In most case, this simply calls decompile(), but it may add a semicolon if
     * needed
     */
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
    }
}
