package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);

    public static Logger getLOG() {
        return LOG;
    }

    /**
     * @return true if the expression does not correspond to any concrete token
     *         in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed
     * by contextual verification).
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

    // TODO Tout ce qui est verify c'est la partie B !
    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     (contains the "env_types" attribute)
     * @param localEnv
     *                     Environment in which the expression should be checked
     *                     (corresponds to the "env_exp" attribute)
     * @param currentClass
     *                     Definition of the class containing the expression
     *                     (corresponds to the "class" attribute)
     *                     is null in the main bloc.
     * @return the Type of the expression
     *         (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            Environment<ExpDefinition> localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
        Type type = this.verifyExpr(compiler, localEnv, currentClass);
        if (type.sameType(expectedType)) {
            return this;
        } else if (type.isInt() && expectedType.isFloat()) {
            AbstractExpr abs = new ConvFloat(this);
            abs.verifyExpr(compiler, localEnv, currentClass);
            return abs;
        } else {
            throw new ContextualError("Type incompatible", this.getLocation());
        }
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.type = this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *                     Environment in which the condition should be checked.
     * @param currentClass
     *                     Definition of the class containing the expression, or
     *                     null in
     *                     the main program.
     */
    void verifyCondition(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type =this.verifyExpr(compiler, localEnv, currentClass);
        if(!type.isBoolean()){
            throw new ContextualError("la condition doit être booléenne", getLocation());
        }
    }

    /////////////////////////// Part C //////////////////////////////////

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        if (getType().isInt()) {
            compiler.addInstruction(new LOAD(this.codeGenReg(compiler), Register.getR(1)));
            compiler.addInstruction(new WINT());
        } else if (getType().isFloat()) {
            compiler.addInstruction(new LOAD(this.codeGenReg(compiler), Register.getR(1)));

            compiler.addInstruction(new WFLOAT());
        } else {
            throw new DecacInternalError("Print pas supporté pour le type " + getType());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrintHexa(DecacCompiler compiler) {
        if (getType().isInt()) {
            compiler.addInstruction(new LOAD(this.codeGenReg(compiler), Register.getR(1)));
            compiler.addInstruction(new WINT());
        } else if (getType().isFloat()) {
            compiler.addInstruction(new LOAD(this.codeGenReg(compiler), Register.getR(1)));

            compiler.addInstruction(new WFLOATX());
        } else if(getType().isString()){
            compiler.addInstruction(new WSTR(this.decompile()));
        }
        else {
            throw new DecacInternalError("Printx pas supporté pour le type" + getType());
        }
    }

    /**
     * */
    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("AbsExpr codeGenInst");
        codeGenExprIgnored(compiler);
        // peut être ajouter des labels en paramètre...
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
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
     */
    protected boolean NeedsRegister() {
        return true;
    }

    /**
     * Store la valeur dans un DVal sans toucher à un Registre
     * Applicable que pour les feuilles si pas le cas renvoi une erreur
     * 
     * @param compiler
     * @return DVal ou se trouve notre valeur
     */
    protected abstract DVal codeGenNoReg(DecacCompiler compiler);
    // Abstract car dépend du type et faut que ça soit une feuille

    /**
     * genere le code à mettre dans le registre current
     * 
     * @param compiler
     * @return Registre ou se trouve notre code généré
     */
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        getLOG().trace("AbsExpr codeGenReg");
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        compiler.addInstruction(new LOAD(codeGenNoReg(compiler), reg));
        // cette instruction permet de charger une valeur dans un registre ici le
        // Registre Current
        return reg;
    }

    /**
     * Genere le code comme une condition en utilisant le control-flow
     * est utilisée que pour les expressions booléennes
     *
     * @param compiler
     * @apram l le label vers lequel on va sauter
     * @param saut booléen qui régit le saut vers le label
     * @return GPRegister reg
     */
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut) {
        getLOG().trace("AbsExpr codeGenCond");
        compiler.addInstruction(new CMP(0, codeGenReg(compiler)));
        // Cette instruction permet d'effectuer une comparaison comme si une
        // soustraction avait été effectuée.
        LOG.info("Vérification du résultat de l'évaluation avec codeGenCond()");
        if (saut) {
            // Cette instruction permet de faire un saut à l'emplacement spécifié si le
            // drapeau d'égalité vaut 0.
            compiler.addInstruction(new BNE(l));
        } else {
            compiler.addInstruction(new BEQ(l));
            // Cette instruction permet de faire un saut à l'emplacement spécifié si le
            // drapeau d'égalité vaut 1.
        }

    }

    /**
     * Génère code pour l'expression mais seulement pour les effets et ignore
     * l'expression
     * 
     * @param compiler
     * @return rien
     */
    protected void codeGenExprIgnored(DecacCompiler compiler) {
        compiler.addComment("value in " + codeGenReg(compiler) + " ignored");
        LOG.info("Génère code pour l'expression avec codeGenExprIgnored");
    }

}
