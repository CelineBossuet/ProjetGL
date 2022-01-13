package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        Type ope = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (ope.isBoolean()){
            this.setType(ope);
            return ope;
        }else{
            throw new ContextualError("Une négation peut être faite seulement sur un booléen", this.getLocation());
        }
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected Instruction geneInstru(GPRegister reg) {
        throw new DecacInternalError("Pas de génération d'instruction possible pour Not");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        // TODO factoriser ce code avec celui dans AbstractOpBool
        Label elseLabel = compiler.getLabelManager().newLabel("elseC2R");
        Label end = compiler.getLabelManager().newLabel("endC2R");
        GPRegister r = compiler.getRegisterManager().getCurrent();

        compiler.addInstruction(new CMP(0, r));
        compiler.addInstruction(new BNE(elseLabel));

        compiler.addInstruction(new BRA(end));
        compiler.addLabel(elseLabel);

        compiler.addInstruction(new LOAD(1, r));
        compiler.addLabel(end);
        return r;
    }

    @Override
    protected void codeGenCond(DecacCompiler compiler, Label l, boolean saut){
        getLOG().info("le Not inverse la logique dans codeGenCond");
        getOperand().codeGenCond(compiler, l, !saut);
    }
}
