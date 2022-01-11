package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentType.DoubleDefException;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

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

        // A FAIRE TODO 3 passes pour langage complet
        // getClasses().verifyListClass(compiler);
        // LOG.debug("Pass 1 verification");
        // LOG.debug("Pass 2 verification");
        // LOG.debug("Pass 3 verification");

        LOG.debug("verify program: start");

        // declare language types :
        declareBuiltinTypes(compiler);

        getMain().verifyMain(compiler);

        LOG.debug("verify program: end");
    }

    private void declareBuiltinTypes(DecacCompiler compiler) {
        try {
            compiler.getEnvironmentType().declare(compiler.getSymbolTable().create("void"),
                    new TypeDefinition(new VoidType(compiler.getSymbolTable().create("void")), Location.BUILTIN));
            compiler.getEnvironmentType().declare(compiler.getSymbolTable().create("boolean"),
                    new TypeDefinition(new BooleanType(compiler.getSymbolTable().create("boolean")), Location.BUILTIN));
            compiler.getEnvironmentType().declare(compiler.getSymbolTable().create("float"),
                    new TypeDefinition(new FloatType(compiler.getSymbolTable().create("float")), Location.BUILTIN));
            compiler.getEnvironmentType().declare(compiler.getSymbolTable().create("int"),
                    new TypeDefinition(new IntType(compiler.getSymbolTable().create("int")), Location.BUILTIN));
            compiler.getEnvironmentType().declare(compiler.getSymbolTable().create("string"),
                    new TypeDefinition(new StringType(compiler.getSymbolTable().create("string")), Location.BUILTIN));
            compiler.getEnvironmentType().declare(compiler.getSymbolTable().create("null"),
                    new TypeDefinition(new NullType(compiler.getSymbolTable().create("null")), Location.BUILTIN));
        } catch (DoubleDefException e) {
            throw new DecacInternalError("Double built in type definition");
        }
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
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
