package fr.ensimag.deca;

import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.codegen.MemoryManager;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import jasmin.Main;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl13
 * @date 01/01/2022
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    public static Logger getLOG() {
        return LOG;
    }

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        // System.out.println("DecacCompiler add Instru : " +instruction);
        program.addInstruction(instruction);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     *      java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    private final RegisterManager registerManager = new RegisterManager();
    private final MemoryManager memoryManager = new MemoryManager();
    private final LabelManager labelManager = new LabelManager();
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();
    private final SymbolTable symbolTable = new SymbolTable();
    private Environment<TypeDefinition> environmentType = new Environment<TypeDefinition>(null);

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public LabelManager getLabelManager() {
        return labelManager;
    }

    public IMAProgram getProgram() {
        return program;
    }

    /**
     * @return Type environment which includes in particular builtin types.
     */
    public Environment<TypeDefinition> getEnvironmentType() {
        return environmentType;
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;

        destFile = sourceFile.substring(0, sourceFile.lastIndexOf('.')) + (compilerOptions.getJava() ? ".j" : ".ass");
        // TODO A FAIRE générer .class

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to " + (compilerOptions.getJava() ? "jasmin" : "assembly")
                + " file " + destFile);
        // TODO A FAIRE générer class file
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName   name of the destination (assembly) file
     * @param out        stream to use for standard output (output of decac -p)
     * @param err        stream to use to display compilation errors
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {

        // set registers limit
        getRegisterManager().setMax(
                compilerOptions.getRegisters() == -1 ? getRegisterManager().getMax() : compilerOptions.getRegisters()); // registers
                                                                                                                        // limit

        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }

        if (compilerOptions.getParser()) { // Stop compiling if -p option
            prog.decompile(out);
            return false;
        }

        // assert(prog.checkAllLocations()); A FAIRE
        LOG.info("Starting verification");
        prog.verifyProgram(this);
        // assert(prog.checkAllDecorations()); A FAIRE

        if (compilerOptions.getVerif()) // Stop compiling if -v option
            return false;

        LOG.info("Starting generation");

        return compilerOptions.getJava() ? doGenerateBytecode(prog, sourceName, destName, out, err)
                : doGenerate(prog, sourceName, destName, out, err);
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err        Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError    When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     *                            compiler.
     * @throws LocationException  When a compilation error (incorrect program)
     *                            occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            LOG.info("Starting lexing");
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        LOG.info("Starting parsing");
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    public GPRegister allocate() {
        return registerManager.allocRegister();
    }

    public void release(GPRegister reg) {
        registerManager.releaseRegister(reg);
    }

    private boolean doGenerate(AbstractProgram prog, String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");

        // Génération des Label d'erreurs
        this.addLabel(this.getLabelManager().getIErrorLabel());
        this.addInstruction(new WSTR("Error: Input/Output Error"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());

        this.addLabel(this.labelManager.getOverFlowLabel());
        this.addInstruction(new WSTR("Error: Overflow during arithmetic operation"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());

        this.addLabel(this.labelManager.getStack_overflowLabel());
        this.addInstruction(new WSTR("Error: Stack Overflow"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());

        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");

        return false;
    }

    private boolean doGenerateBytecode(AbstractProgram prog, String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        addComment("start main program");
        prog.codeGenProgramJasmin(this);
        addComment("end main program");

        LOG.debug("Generated jasmin assembly code:" + nl + program.display());

        String className = getClassName(sourceName);
        destName = destName.substring(0, destName.lastIndexOf('/') + 1) + className + ".j";

        LOG.info("Output file assembly file is: " + destName);
        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing jasmin assembler file ...");
        PrintStream pS = new PrintStream(fstream);
        writeJasminStart(pS, className);
        program.display(pS);
        writeJasminEnd(pS);
        LOG.info("Compilation of " + sourceName + " successful.");

        // .j conversion
        Main.main(new String[] { destName });
        LOG.info("Conversion in class file successful.");

        File file = new File(destName);
        if (file.delete())
            LOG.info(".j file deleted successfully");
        else
            LOG.info("Failed to delete the .j file");

        return false;
    }

    private static String getClassName(String sourceFile) {
        String className = sourceFile.substring(sourceFile.lastIndexOf('/') + 1, sourceFile.lastIndexOf('.'));
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = className.replaceAll("[^a-zA-Z]", "");
        return className;
    }

    // A FAIRE methods addPUSH, addADDSP, addSUBSP, ...
    private static void writeJasminStart(PrintStream pS, String className) {
        pS.println(".class public " + className);
        pS.println(".super java/lang/Object");
        pS.println(".method public <init>()V");
        pS.println("aload_0");
        pS.println("invokenonvirtual java/lang/Object/<init>()V");
        pS.println("return");
        pS.println(".end method");
        pS.println(".method public static main([Ljava/lang/String;)V");
        pS.println(".limit stack 2"); // TODO A FAIRE
    }

    private static void writeJasminEnd(PrintStream pS) {
        pS.println("return");
        pS.println(".end method");
    }

}
