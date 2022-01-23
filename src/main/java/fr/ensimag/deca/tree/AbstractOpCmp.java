package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.jasmin.f2l;
import fr.ensimag.ima.pseudocode.instructions.jasmin.fload;
import fr.ensimag.ima.pseudocode.instructions.jasmin.i2l;
import fr.ensimag.ima.pseudocode.instructions.jasmin.iadd;
import fr.ensimag.ima.pseudocode.instructions.jasmin.iload;
import fr.ensimag.ima.pseudocode.instructions.jasmin.lcmp;
import fr.ensimag.ima.pseudocode.jasmin.VarID;

import org.apache.log4j.Logger;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);

    public static Logger getLOG() {
        return LOG;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type right = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type left = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!left.sameType(right) && !(left.isFloat() && right.isInt()) && !(left.isInt() && right.isFloat())) {
            throw new ContextualError(
                    "Comparaison non supportée entre types " + left + " et " + right, getLocation());
        }
        Type sortie = new BooleanType(compiler.getSymbolTable().create("boolean"));
        setType(sortie);
        return sortie;
        // throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("Instruction non implémentable pour les comparaisons");
    }

    @Override
    protected void codeGenArithJasmin(DecacCompiler compiler) {
        throw new DecacInternalError("Can't implement for comparisons");
    }

    /**
     * Modification de codeGenCond pour empêcher de faire Scc puis un autre CMP dans
     * le cas des comparaisons
     */
    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut) {
        codeGenRegInternal(compiler, false);
        compiler.addInstruction(geneBranchInstru(saut, l));
    }

    /**
     * Génère les instructions pour le branchement, dépend du type de comparaison
     * faite d'où le abstract
     * 
     * @param saut boolean
     * @param l    Label
     * @return Instruction
     */
    protected abstract Instruction geneBranchInstru(boolean saut, Label l);

    /**
     * Génère les instructions pour notre comparaison
     */
    protected abstract Instruction genSccInstruction(GPRegister result);

    @Override
    protected void geneOneOrMoreInstru(DecacCompiler compiler, DVal val, GPRegister reg, boolean usefull) {
        getLOG().trace("AbsOpCmp geneOneOrMoreInstru");
        compiler.addInstruction(new CMP(val, reg));
        if (usefull) {
            compiler.addInstruction(genSccInstruction(reg));
        }
    }

    @Override
    protected void geneOneOrMoreInstruJasmin(DecacCompiler compiler, VarID rightVar, Type rightType) {
        getLOG().trace("AbsOpCmp geneOneOrMoreInstruJasmin");

        if (rightType.isInt() || rightType.isBoolean()) {
            compiler.addInstruction(new i2l());// convert left to long
            compiler.addInstruction(new iload(rightVar));
            compiler.addInstruction(new i2l()); // and right
        } else if (rightType.isFloat()) {
            compiler.addInstruction(new f2l());
            compiler.addInstruction(new fload(rightVar));
            compiler.addInstruction(new f2l());
        } else {
            throw new DecacInternalError("Type " + rightType + "not supported.");
        }

        // compare (automatically reconvert to int)
        // give 0 for equality, else -1 or 1
        compiler.addInstruction(new lcmp());

    }
}
