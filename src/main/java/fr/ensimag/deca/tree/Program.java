package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl13
 * @date 01/01/2022
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        // declare language types :

        getClasses().verifyListClass(compiler); // Passe 1
        getClasses().verifyListClassMembers(compiler); // Passe 2
        getClasses().verifyListClassBody(compiler); // Passe 3
        // LOG.debug("Pass 1 verification");
        // LOG.debug("Pass 2 verification");
        // LOG.debug("Pass 3 verification");

        LOG.debug("verify program: start");

        getMain().verifyMain(compiler);

        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {

        compiler.startBlock();
        getClasses().codeGenListClass(compiler);
        // A FAIRE: compléter ce squelette très rudimentaire de code
        compiler.addComment("Main program");
        main.codeGenMain(compiler);

        int sizeGlobal = compiler.getMemoryManager().getCurrentGB();
        compiler.endBlock(false, false, sizeGlobal, null);

        compiler.addInstruction(new HALT());

        getClasses().codeGenListClassBody(compiler);
    }

    @Override
    public void codeGenProgramJasmin(DecacCompiler compiler) {
        compiler.addComment("Main program");
        main.codeGenMainJasmin(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
