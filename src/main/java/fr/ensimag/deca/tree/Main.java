package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacCompiler.JasminStaticVars;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.jasmin.astore;
import fr.ensimag.ima.pseudocode.instructions.jasmin.dup;
import fr.ensimag.ima.pseudocode.instructions.jasmin.getstatic;
import fr.ensimag.ima.pseudocode.instructions.jasmin.invokespecial;
import fr.ensimag.ima.pseudocode.instructions.jasmin.newI;
import fr.ensimag.ima.pseudocode.jasmin.IOStream;
import fr.ensimag.ima.pseudocode.jasmin.ScannerObject;
import fr.ensimag.ima.pseudocode.jasmin.SpecialScanner;
import fr.ensimag.ima.pseudocode.jasmin.SystemIO;
import fr.ensimag.ima.pseudocode.jasmin.VarID;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl13
 * @date 01/01/2022
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private ListDeclVar declVariables;
    private ListInst insts;

    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        Environment<ExpDefinition> envi = new Environment<ExpDefinition>(null);
        declVariables.verifyListDeclVariable(compiler, envi, null); // no parent to env
                                                                    // this is in
        // main
        insts.verifyListInst(compiler, envi, null, null);
        LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        // System.out.println("Main");
        declVariables.codeGenListVar(compiler, false);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler, null, null);
    }

    @Override
    protected void codeGenMainJasmin(DecacCompiler compiler) {
        // declare useful variables
        compiler.addInstruction(new getstatic(new SystemIO(false), new IOStream(false))); // system out
        compiler.addInstruction(new astore(new VarID(JasminStaticVars.SYSTEM_OUT.id())));
        compiler.addInstruction(new newI(new ScannerObject())); // system in
        compiler.addInstruction(new dup());
        compiler.addInstruction(new getstatic(new SystemIO(true), new IOStream(true)));
        compiler.addInstruction(new invokespecial(new SpecialScanner()));
        compiler.addInstruction(new astore(new VarID(JasminStaticVars.SYSTEM_IN.id())));

        // program
        declVariables.codeGenListVarJasmin(compiler);
        compiler.addComment("Beginning of main instructions");
        insts.codeGenListInstJasmin(compiler, null, null);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
