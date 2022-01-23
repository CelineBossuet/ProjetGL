package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

import java.io.PrintStream;

public class Cast extends AbstractExpr{
    private AbstractIdentifier typeToCheck;
    private AbstractExpr expr;

    public Cast(AbstractIdentifier typeToCheck, AbstractExpr expr){
        this.expr=expr;
        this.typeToCheck=typeToCheck;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, Environment<ExpDefinition> localEnv, ClassDefinition currentClass) throws ContextualError {
        Type toCast = this.expr.verifyExpr(compiler, localEnv, currentClass);
        Type t = this.typeToCheck.verifyType(compiler);

        if(t.sameType(toCast) || t.isInt() && toCast.isFloat() || t.isFloat() && toCast.isInt()){
            this.setType(toCast);
            return getType();
        }

         else if(this.typeToCheck.getDefinition().isClass() && this.expr.getType().isClass()) {
            ClassType tToCast = toCast.asClassType("Mauvais transtypage", this.getLocation());
            ClassType tt = t.asClassType("Mauvais transtypage", this.getLocation());
            if(tt.isSubClassOf(tToCast)){
                this.setType(toCast);
                return this.getType();
            }else{
                throw new ContextualError("Class"+toCast + " and " + t + " are not compatible", this.getLocation());
            }
        }
         else{
             throw new ContextualError("Types "+t+" and "+toCast+" are not compatible for cast", this.getLocation());
        }

    }

    @Override
    protected DVal codeGenNoReg(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Methode pas possible pour un Cast");
    }

    @Override
    protected GPRegister codeGenReg(DecacCompiler compiler){
        Type typeToCast = typeToCheck.getDefinition().getType();
        GPRegister reg = expr.codeGenReg(compiler);
        if( typeToCast.isFloat() && expr.getType().isInt()){
            compiler.addInstruction(new FLOAT(reg, reg), "cast a int to float");
        }

        else if(expr.getType().sameType(typeToCast)
                && (typeToCast.isFloat() || typeToCast.isInt() || typeToCast.isBoolean())){
            //nothing to do
        }
        else if(typeToCast.isInt() && expr.getType().isFloat()){
            compiler.addInstruction(new INT(reg, reg), "cast a float to int");
        }

        return reg;
        //TODO cas type est une class
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        typeToCheck.decompile(s);
        s.print(") (");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        typeToCheck.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        typeToCheck.iter(f);
        expr.iter(f);
    }
}
