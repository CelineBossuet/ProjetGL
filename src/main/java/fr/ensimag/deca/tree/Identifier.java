package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Deca Identifier
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Identifier extends AbstractIdentifier {

    private static final Logger LOG = Logger.getLogger(Identifier.class);
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

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
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

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
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

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
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

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
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

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
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if(localEnv.get(this.getName())!=null){
            Definition def =localEnv.get(getName()); //TODO
            this.setDefinition(def);
            this.setType(def.getType());
            return getType();
        }
        else{
            throw new ContextualError(
                    "this variable: "+this.getName()+" is not declared yet ", this.getLocation());
        }
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * 
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        Definition def = compiler.getEnvironmentType().defOfType(getName());
        if(def==null){
            throw new ContextualError("No such type "+getType(), getLocation());
        }
        if(def.getType().isVoid()){
            throw new ContextualError("Variables, Parameters or Field can't be void type", getLocation());
        }
        setType(def.getType());
        setDefinition(def);
        return getType();

    }

    public Type verifyTypeClass(DecacCompiler compiler) throws ContextualError {
        /*
        System.out.println(this.name.getName());
        System.out.println(compiler.getSymbolTable().create(name.getName()));
        System.out.println(compiler.getEnvironmentType().get(compiler.getSymbolTable().create(name.getName())));
         */
        TypeDefinition defClass = compiler.getEnvironmentType().get(compiler.getSymbolTable().create(this.getName().getName()));
        if (defClass == null){
            throw new ContextualError("class null", this.getLocation());
        }
        setDefinition(defClass);
        setType(defClass.getType());
        return defClass.getType();
    }

    public Type verifyMethodType(DecacCompiler compiler) throws ContextualError{
        TypeDefinition typeMethode = compiler.getEnvironmentType().get(compiler.getSymbolTable().create(getName().getName()));
        if (typeMethode == null){
            LOG.info("le type de la class est null");
            throw new ContextualError("class null", this.getLocation());
        }

        setDefinition(typeMethode);
        setType(typeMethode.getType());
        return typeMethode.getType();
    }


    public FieldDefinition verifyField(DecacCompiler compiler, ClassType type) throws ContextualError {
        Definition fieldDefinition = type.getDefinition().getMembers().get(this.getName());
        if (fieldDefinition == null){
            throw new ContextualError("This field is not declared yet", this.getLocation());
        }
        try{
            fieldDefinition = fieldDefinition.asFieldDefinition(fieldDefinition + "is not a field", this.getLocation());
        }catch (ContextualError c){
            throw c;
        }
        return (FieldDefinition) fieldDefinition;
    }

    private Definition definition;

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public boolean NeedsRegister(){
        return getDefinition().isField();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }
    private GPRegister defaultValue=null;
    @Override
    public GPRegister initDefaultValue(DecacCompiler compiler, GPRegister reg){
        if(defaultValue!=null){
            return defaultValue;
        }
        defaultValue = Register.getR(0);
        compiler.addInstruction(new LOAD(this.getType().getDefaultValue(), defaultValue));
        //a changer pour initialisation var de type int x,y,z
        return defaultValue;
        //throw new UnsupportedOperationException("Not yet implmented");
    }

    @Override
    protected DAddr codeGenNoReg(DecacCompiler compiler) {

        DAddr ope = this.getExpDefinition().getOperand();
        if(ope==null){
            throw new DecacInternalError("Operande null pour l'identifier "+getName()+" "+getDefinition());
        }
        return ope;
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler){
        if(getDefinition().isField()){
            //cas particulier pour les fields qui ne peuvent pas être générés avec un seul opérand
            getLOG().info("cas particulier pour les fields, génération par plusieurs opérandes");
            GPRegister current = compiler.getRegisterManager().getCurrent();
            compiler.addInstruction(new LOAD(codeGenAddr(compiler), current));
            return current;
        }
        return super.codeGenReg(compiler);
    }


    @Override
    public DAddr codeGenAddr(DecacCompiler compiler) {
        if(getDefinition().isField()){
            getLOG().info("cas particulier pour les fields, génération par plusieurs opérandes");
            GPRegister current = compiler.getRegisterManager().getCurrent();
            RegisterOffset offset = new RegisterOffset(-2, Register.LB);
            RegisterOffset field = new RegisterOffset(getFieldDefinition().getIndex() , current);
            compiler.addInstruction(new LOAD(offset, current));
            return field;
        }
        DAddr ope = codeGenNoReg(compiler);
        return ope;
    }
}
