package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import org.apache.log4j.Logger;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractIdentifier extends AbstractLValue {

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *                            if the definition is not a class definition.
     */
    public abstract ClassDefinition getClassDefinition();

    public abstract Definition getDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *                            if the definition is not a field definition.
     */
    public abstract FieldDefinition getFieldDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *                            if the definition is not a method definition.
     */
    public abstract MethodDefinition getMethodDefinition();

    public abstract SymbolTable.Symbol getName();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *                            if the definition is not a field definition.
     */
    public abstract ExpDefinition getExpDefinition();

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *                            if the definition is not a field definition.
     */
    public abstract VariableDefinition getVariableDefinition();

    public abstract void setDefinition(Definition definition);

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * 
     * @param compiler contains "env_types" attribute
     * @return the type corresponding to this identifier
     *         (corresponds to the "type" attribute)
     */
    public abstract Type verifyType(DecacCompiler compiler) throws ContextualError;

    private static final Logger LOG = Logger.getLogger(AbstractIdentifier.class);

    public abstract Type verifyTypeClass(DecacCompiler compiler) throws ContextualError;

    public abstract Type verifyMethodType(DecacCompiler compiler) throws ContextualError;

    public abstract FieldDefinition verifyField(DecacCompiler compiler, ClassType type)throws ContextualError;


    public abstract GPRegister initDefaultValue(DecacCompiler compiler, GPRegister reg);
}
