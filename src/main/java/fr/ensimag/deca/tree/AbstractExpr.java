package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    //TODO Tout ce qui est verify c'est la partie B !
    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        /*
        if(this.type==INT) ==> compiler.addInstruction(new LOAD...)
        DVal val =this.codeGenReg(compiler)
        Ajouter un booléen si écrire en hexa ou pas pour float
         */
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenExprIgnored(compiler);
        //throw new UnsupportedOperationException("not yet implemented");
    }
    

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
    /**
     * Fonction qui dit si pour généré le code on a besoin d'un registre
     * par défaut à true et est modifiée a false pour les feuilles
     * ie pour les classes où la fonction codeGenNoReg est possible
     * */
    protected boolean NeedsRegister(){
        return true;
    }

    /**
     * Store la valeur dans un DVal sans toucher à un Registre
     * Applicable que pour les feuilles si pas le cas renvoi une erreur
     * @param compiler
     * @return DVal ou se trouve notre valeur
     */
    protected abstract DVal codeGenNoReg(DecacCompiler compiler);
    //Abstract car dépend du type et faut que ça soit une feuille


    /**
     * genere le code à mettre dans le registre current
     * @param compiler
     * @return Registre ou se trouve notre code généré
     */
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(codeGenNoReg(compiler), compiler.getRegisterManager().getCurrent()));
        return compiler.getRegisterManager().getCurrent();
    }


    /**
     * genere code pour une condition
     * @param compiler
     * @return GPRegister reg
     */
    protected GPRegister codeGenCond( DecacCompiler compiler){
        return null; //TODO
    }

    /**
     *
     * @param compiler
     * @return GPRegister reg
     */
    protected void codeGenExprIgnored(DecacCompiler compiler){
        GPRegister reg =codeGenReg(compiler);
        compiler.addComment("value in "+ reg +" ignored");
    }

}
