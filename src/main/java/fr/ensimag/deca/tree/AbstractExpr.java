package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacCompiler.JasminStaticVars;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.jasmin.aload;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fload;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fstore;
import fr.ensimag.ima.pseudocode.instructions.jasmin.iload;
import fr.ensimag.ima.pseudocode.instructions.jasmin.invokestatic;
import fr.ensimag.ima.pseudocode.instructions.jasmin.invokevirtual;
import fr.ensimag.ima.pseudocode.instructions.jasmin.istore;
import fr.ensimag.ima.pseudocode.instructions.jasmin.ldc;
import fr.ensimag.ima.pseudocode.jasmin.PrintInvoked;
import fr.ensimag.ima.pseudocode.jasmin.StringValueOf;
import fr.ensimag.ima.pseudocode.jasmin.Constant;
import fr.ensimag.ima.pseudocode.jasmin.VarID;

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

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }


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
            if (type.isClass()) {
                ClassType classType = (ClassType) type;
                ClassType expectedClassType = (ClassType) expectedType;
                if (!(classType.isSubClassOf(expectedClassType))) {
                    throw new ContextualError(type.getName().getName() + " isn't a subtype of "
                            + expectedType.getName().getName() + " so it cannot be assigned to it", this.getLocation());
                }

            }

            return this;
        } else if (type.isInt() && expectedType.isFloat() || type.isFloat() && expectedType.isInt()) {
            return this;
        } else {
            throw new ContextualError(this.type.getName().getName() + " isn't a subtype of "
                    + expectedType.getName().getName() + " so it cannot be assign to it", this.getLocation());
        }
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
        Type type = this.verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError(this.type.getName().getName() + " should have been boolean ", getLocation());
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
            throw new DecacInternalError("Print not supported for type: " + getType());
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
        } else if (getType().isString()) {
            compiler.addInstruction(new WSTR(this.decompile()));
        } else {
            throw new DecacInternalError("Printx pas supporté pour le type" + getType());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("AbsExpr codeGenInst");
        codeGenExprIgnored(compiler);
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";"); // print with semicolon
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
        compiler.addInstruction(new LOAD(codeGenNoReg(compiler), reg), "AbsExpr");
        // cette instruction permet de charger une valeur dans un registre ici le
        // Registre Current
        return reg;
    }

    protected boolean getBool() {
        return false;
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
        GPRegister reg = codeGenReg(compiler);
        compiler.addInstruction(new CMP(0, reg), "oupsi");
        // Cette instruction permet d'effectuer une comparaison
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
        LOG.trace("AbstractExpr codeGenExprIgnored");
    }

    ///////////////// Jasmin
    @Override
    protected void codeGenInstJasmin(DecacCompiler compiler, Label returnLabel, Label local) {
        getLOG().trace("AbsExpr codeGenInstJasmin");
        codeGenStack(compiler);
    }

    /**
     * Génère la valeur à mettre dans le haut de pile
     * 
     * @param compiler
     */
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("AbsExpr codeGenStack");
        // nothing
    }

    /**
     * Generate jasmin code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrintJasmin(DecacCompiler compiler, String suffix) {

        if (getType().isInt() || getType().isFloat()) {
            this.codeGenStack(compiler);
            VarID store = compiler.getMemoryManager().allocJasmin();
            // store result
            if (getType().isInt())
                compiler.addInstruction(new istore(store));
            else if (getType().isFloat())
                compiler.addInstruction(new fstore(store));
            // load system out
            compiler.addInstruction(new aload(new VarID(JasminStaticVars.SYSTEM_OUT.id())));
            // load result
            if (getType().isInt())
                compiler.addInstruction(new iload(store));
            else if (getType().isFloat())
                compiler.addInstruction(new fload(store));
            compiler.addInstruction(new invokestatic(new StringValueOf(getType())));// convert result to String
            compiler.addInstruction(new invokevirtual(new PrintInvoked(suffix)));
        } else if (getType().isString()) {
            compiler.addInstruction(new aload(new VarID(JasminStaticVars.SYSTEM_OUT.id())));
            compiler.addInstruction(new ldc(new Constant(this.decompile())));
            compiler.addInstruction(new invokevirtual(new PrintInvoked(suffix)));
        } else {
            throw new DecacInternalError("Print pas supporté pour le type" + getType());
        }
    }

    /**
     * Genere le code comme une condition en utilisant le control-flow
     * est utilisée que pour les expressions booléennes
     *
     * @param compiler
     * @param l        le label vers lequel on va sauter
     * @param jump     booléen qui régit le saut vers le label
     * @return GPRegister reg
     */
    protected void codeGenCondJasmin(DecacCompiler compiler, Label l, boolean jump) {
        getLOG().trace("AbsExpr codeGenCondJasmin");

        // compute expression
        codeGenStack(compiler);

        codeGenJasminJump(compiler, l, jump);

        // if (saut) {
        // // Cette instruction permet de faire un saut à l'emplacement spécifié si le
        // // drapeau d'égalité vaut 0.
        // compiler.addInstruction(new ifne(l));
        // } else {
        // compiler.addInstruction(new ifeq(l));
        // // Cette instruction permet de faire un saut à l'emplacement spécifié si le
        // // drapeau d'égalité vaut 1.
        // }

    }

    protected abstract void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump);

    protected GPRegister codeGenCondToReg(DecacCompiler compiler) {
        Label elseLabel = compiler.getLabelManager().newLabel("elseC2R");
        Label end = compiler.getLabelManager().newLabel("endC2R");
        GPRegister r = compiler.getRegisterManager().getCurrent();

        codeGenCond(compiler, elseLabel, true);

        compiler.addInstruction(new LOAD(0, r));
        compiler.addInstruction(new BRA(end));
        compiler.addLabel(elseLabel);

        compiler.addInstruction(new LOAD(1, r));
        compiler.addInstruction(new BRA(end));

        compiler.addLabel(end);

        return r;
    }

}
