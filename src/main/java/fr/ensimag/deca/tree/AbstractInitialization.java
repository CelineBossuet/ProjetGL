package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractInitialization extends Tree {

    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler     contains "env_types" attribute
     * @param t            corresponds to the "type" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass
     *                     corresponds to the "class" attribute (null in the main
     *                     bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Genération du code assembleur pour l'initialisation de variables
     * 
     * @param compiler
     * @param target   adresse ou Store la valeur du registre
     */
    protected abstract void codeGeneInit(DecacCompiler compiler, DAddr target);

    // TODO A FAIRE
    // protected abstract void codeGeneInitBytecode(DecacCompiler compiler, DAddr
    // target);

}
