package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.*;

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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented"); //TODO
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg){
        throw new DecacInternalError("Instruction non implémentable pour les comparaisons");
    }


    /**
     * Modification de codeGenCond pour empêcher de faire Scc puis un autre CMP dans le cas des comparaisons
     */
    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut) {

        codeGenRegInternal(compiler, false);
        compiler.addInstruction(geneBranchInstru(saut, l));
    }

    /**
     * Génère les instructions pour le branchement, dépend du type de comparaison faite d'où le abstract
     * @param saut boolean
     * @param l Label
     * @return Instruction
     * */
    protected abstract Instruction geneBranchInstru(boolean saut, Label l);

    /**
     * Génère les instructions pour notre comparaison */
    protected abstract Instruction genSccInstruction(GPRegister result);
}
