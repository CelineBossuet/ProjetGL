package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    protected BinaryInstruction geneInstru(DVal val, GPRegister reg) {
        throw new DecacInternalError("mothode non implémentable pour Assign");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler){
        AbstractLValue left = getLeftOperand();
        AbstractExpr right =getRightOperand();

        GPRegister reg = compiler.getRegisterManager().getCurrent();
        GPRegister rightReg;
        DAddr val = left.codeGenAddr(compiler);
        if(compiler.getRegisterManager().getLastUsed()-compiler.getRegisterManager().getCurrentv()+1 <=0){
            rightReg=reg;
            //pas de registre disponible TODO
        }
        else{
            GPRegister alloc = compiler.allocate();
            rightReg = right.codeGenReg(compiler);
            compiler.release(alloc);
        }
        compiler.addInstruction(new STORE(rightReg, val));
        compiler.addInstruction(new LOAD(rightReg, reg));
        return reg;
    }
}
