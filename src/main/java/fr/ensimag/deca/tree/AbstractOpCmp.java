package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type sortie = new BooleanType(compiler.getSymbolTable().create("boolean"));
        setType(sortie);
        return sortie;
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("Instruction non implémentable pour les comparaisons");
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
    protected void geneOneOrMoreInstru(DecacCompiler compiler, DVal val, GPRegister reg, boolean usefull){
        //System.out.println("AbsOpCmp geneOneOrMoreInstru");
        compiler.addInstruction(new CMP(val, reg));
        if(usefull){
            compiler.addInstruction(genSccInstruction(reg));
        }
    }
}
