package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class Selection extends AbstractLValue{
    private AbstractExpr object;
    private AbstractIdentifier field;

    public Selection(AbstractExpr object, AbstractIdentifier field){
        this.field=field;
        this.object=object;
    }

    private static final Logger LOG = Logger.getLogger(Selection.class);
    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("Not yet implemented");
        Type t = this.object.verifyExpr(compiler, localEnv, currentClass);
        ClassType type;
        try{
            type = t.asClassType("Must be a Class Type before the selection", this.getLocation());
        }catch (ContextualError c){
            throw c;
        }
        FieldDefinition fieldDefinition = field.verifyField(compiler, type);
        if (fieldDefinition.getVisibility() == Visibility.PROTECTED){
            if(currentClass ==null){
                throw new ContextualError("Access to the protected member "+this.field.getName()+" denied", this.getLocation());
            }
            if(!verifySubType(t, currentClass.getType())){
                throw new ContextualError("Access to the protected member "+this.field.getName()+" denied", this.getLocation());
            }
        }
        field.setDefinition(fieldDefinition);
        setType(fieldDefinition.getType());
        return this.getType();
    }

    protected boolean verifySubType( Type subType, Type superType){
        if(subType.sameType(superType)){
            return true;
        }
        else if(!subType.isClass() || !superType.isClass()){
            return false;
        }
        else if(subType.isNull() && superType.isClass()){
            return true;
        }
        return ((ClassType)subType).isSubClassOf((ClassType) superType);
    }



    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not allowed for Selection");
    }

    @Override
    public DAddr codeGenAddr(DecacCompiler compiler) {
        GPRegister regObject =object.codeGenReg(compiler);
        compiler.addInstruction(new CMP(new NullOperand(), regObject));
        //CMP si on a une selection sur un objet de type null alors BEQ
        //ajout BEQ vers label d'erreur de null_deref
        compiler.addInstruction(new BEQ(compiler.getLabelManager().getStack_overflowLabel()));
        return new RegisterOffset(field.getFieldDefinition().getIndex(), regObject);
    }

    @Override
    public GPRegister codeGenReg(DecacCompiler compiler){
        LOG.info("codeGenReg pour la Selection");
        DAddr addr=codeGenAddr(compiler);
        GPRegister reg = compiler.getRegisterManager().getCurrent();
        compiler.addInstruction(new LOAD(addr, reg));
        return reg;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        object.decompile(s);
        s.print(".");
        field.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        object.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        object.iter(f);
        field.iter(f);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
