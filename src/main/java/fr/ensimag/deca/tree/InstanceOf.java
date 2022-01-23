//Ajouté pour InstanceOf de DecaParser
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class InstanceOf extends AbstractExpr {
    private AbstractExpr expr;
    private AbstractIdentifier type;
    private static final Logger LOG = Logger.getLogger(InstanceOf.class);

    public InstanceOf(AbstractExpr expr, AbstractIdentifier type) {
        super();
        this.expr = expr;
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = this.expr.verifyExpr(compiler, localEnv, currentClass);
        Type typeToCheck = type.verifyType(compiler);
        if (!t.isClass() || !typeToCheck.isClass()) {
            throw new ContextualError("Operands of InstanceOf should be a class type", this.getLocation());
        }
        this.setType(compiler.getEnvironmentType().get(compiler.getSymbolTable().create("boolean")).getType());
        // le résultat est un boolean soit c'est instanceOf soit non
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print('(');
        this.expr.decompile(s);
        s.print(" instanceof ");
        this.type.decompile(s);
        s.print(')');
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        type.iter(f);
    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new DecacInternalError("méthode codeGenNoReg pas instantiable pour InstanceOf");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler) {
        LOG.info("CodeGenReg de instanceOf");
        GPRegister reg = expr.codeGenReg(compiler);
        GPRegister res = compiler.getRegisterManager().getCurrent();
        /*
         * compiler.addInstruction(new
         * LEA(type.getClassDefinition().getvTable().getOperand(), getR(1)));
         * compiler.addInstruction(new LOAD(reg, getR(0)));
         * 
         * Label InstanceOf = compiler.getLabelManager().newLabel("InstanceOf");
         * compiler.addInstruction(new BSR(InstanceOf));
         * 
         * compiler.addLabel(InstanceOf);
         * LOG.
         * info("appel fonction qui code la comparaison des classes et stocke l'info dans R0"
         * );
         * codeGenInstanceOf(compiler);
         * 
         * compiler.addInstruction(new LOAD(getR(0), res)); //on récupère le résultat
         * stocké dans R0
         */
        return res;

    }

    protected void codeGenInstanceOf(DecacCompiler compiler) {
        /*
         * Label isInstanceOf = compiler.getLabelManager().newLabel("isInstanceOf");
         * Label notInstanceOf = compiler.getLabelManager().newLabel("notInstanceOf");
         * Label compareClass =
         * compiler.getLabelManager().newLabel("instanceOf_Compare_Class");
         * 
         * compiler.addInstruction(new BEQ(notInstanceOf)); //si objet est nul c'est pas
         * instanceOf d'une class
         * 
         * compiler.addLabel(compareClass); //comparaison des classes
         * compiler.addInstruction(new CMP(getR(1), getR(0)));
         * compiler.addInstruction(new BEQ(isInstanceOf)); //si égal c'est instanceOf de
         * l'autre
         * compiler.addInstruction(new LOAD(new RegisterOffset(0, getR(0)), getR(0)));
         * //sinon on load un nouveau offset et on continue
         * compiler.addInstruction(new BNE(compareClass)); //on recommance à comparer
         * 
         * compiler.addLabel(notInstanceOf);
         * compiler.addInstruction(new LOAD(0, getR(0))); //on mets 0 dans R0 car pas
         * instanceOf
         * compiler.addInstruction(new RTS());
         * 
         * compiler.addLabel(isInstanceOf);
         * compiler.addInstruction(new LOAD(1, getR(0))); //on mets 1 dans R0 car
         * instanceOf de l'autre
         * compiler.addInstruction(new RTS());
         */
    }

    @Override
    protected void codeGenStack(DecacCompiler compiler) {
        getLOG().trace("InstanceOf codeGenStack");
        // TODO
    }

    @Override
    protected void codeGenJasminJump(DecacCompiler compiler, Label l, boolean jump) {
        throw new DecacInternalError("Can't jump with instanceof");
    }
}
