package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg){
        throw new DecacInternalError("Instruction non implémentable sur les binaires");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler){
        Label elseBranch = compiler.getLabelManager().newLabel("elseC2R");
        Label endBranch = compiler.getLabelManager().newLabel("endC2R");
        GPRegister r = compiler.getRegisterManager().getCurrent();

        compiler.addInstruction(new CMP(0, r));
        //Cette instruction permet d'effectuer une comparaison, val indique l'opérande à comparer

        compiler.addInstruction(new BNE(elseBranch));
        //Cette instruction permet de faire un saut à l'emplacement spécifié si le drapeau d'égalité vaut 0.

        compiler.addInstruction(new BRA(endBranch));
        //cette instruction permet de faire un saut à l'emplacement, saut relatif à la position courrante
        compiler.addLabel(elseBranch);

        compiler.addInstruction(new LOAD(1, r));
        compiler.addLabel(endBranch);
        return r;
    }

    /**
     * Redéfinition de la méthode car dépend du type d'opération booléenne et donc est abstract
     * */
    @Override
    protected abstract void codeGenCond( DecacCompiler compiler, Label l, boolean saut);
}
