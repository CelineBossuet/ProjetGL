package fr.ensimag.deca;

import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.codegen.MemoryManager;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

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

    public ClassDefinition OBJECT;
    public EqualsDefinition EQUALS;

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
        declareBuiltinTypes(this);
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
            IMAProgram newProg = new IMAProgram();
            ObjectType objectType = new ObjectType(compiler.getSymbolTable().create("Object"), null, newProg);
            newProg.addInstruction(new RTS());
            ClassDefinition newt = new ClassDefinition(objectType, Location.BUILTIN, null);
            this.OBJECT = objectType.getDefinition();

            IMAProgram equalsProgram = new IMAProgram();
            // gérer la comparaison de registre TODO
            equalsProgram.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
            equalsProgram.addInstruction(new CMP(new RegisterOffset(-3, Register.LB), Register.R0));
            equalsProgram.addInstruction(new SEQ(Register.R0));
            equalsProgram.addInstruction(new RTS());

            Signature sig = new Signature();
            sig.add(objectType);
            EqualsDefinition equals = new EqualsDefinition(
                    compiler.environmentType.get(getSymbolTable().create("boolean")).getType(), Location.BUILTIN, sig,
                    0, equalsProgram);
            equals.setLabel(compiler.getLabelManager().getEqualsLabel());
            OBJECT.getMembers().declare(compiler.getSymbolTable().create("equals"), equals);
            OBJECT.incNumberOfMethods();
            compiler.getEnvironmentType().declareClass(compiler.getSymbolTable().create("Object"), newt);

        } catch (Environment.DoubleDefException e) {
            throw new DecacInternalError("Double built in type definition");
        }
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
        getCurrentBlock().add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        getCurrentBlock().addComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        getCurrentBlock().addLabel(label);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        // System.out.println("DecacCompiler add Instru : " +instruction);
        getCurrentBlock().addInstruction(instruction);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     *      java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        getCurrentBlock().addInstruction(instruction, comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;

    private final LabelManager labelManager = new LabelManager();
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();

    /**
     * IMA code Block ou sont généré les instructions, cela peut changé en fonction
     * de si c'est pour une méthode ou c'est le main programme.
     * Par défaut il correspond au main program
     */
    private IMAProgram currentBlock = program;
    private final SymbolTable symbolTable = new SymbolTable();
    private Environment<TypeDefinition> environmentType = new Environment<TypeDefinition>(this.getEnvironmentType());

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public IMAProgram getCurrentBlock() {
        return currentBlock;
    }

    private final RegisterManager registerManager = new RegisterManager();
    private final MemoryManager memoryManager = new MemoryManager();

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

    public void startBlock() {
        assert (currentBlock == program);
        currentBlock = new IMAProgram(); // on créé un nouveau block
        getMemoryManager().initLGB(); // et on reinitialise tout
        getRegisterManager().initRegister();
    }

    public void endBlock(boolean error, boolean saveReg, int size, Label returnLabel) {
        if (error) { // correspond a un oubli de return dans une fonction non void
            addInstruction(new BRA(getLabelManager().getNoReturnLabel()));
        }
        if (returnLabel != null) {
            addLabel(returnLabel);
        }
        int nbReg = 0; // on compte le nombre de registres utilisés pour TSTO
        if (saveReg) {
            for (int i = 2; i <= getRegisterManager().getLastUsed() + 1; ++i) {
                currentBlock.addFirst(new PUSH(Register.getR(i)), "je push le registre");
                currentBlock.addInstruction(new POP(Register.getR(i)));
                nbReg++;
            }
        }
        if (size != 0) {
            currentBlock.addFirst(new ADDSP(size));
        }

        if (getMemoryManager().getMaxLB() + size + nbReg != 0) {
            currentBlock
                    .addFirst(new BOV(getLabelManager().getStack_overflowLabel(), getCompilerOptions().getNoCheck()));
            currentBlock.addFirst(new TSTO(getMemoryManager().getMaxLB() + size + nbReg + 1));
        }

        if (returnLabel != null) {
            addInstruction(new RTS());
        }
        program.append(currentBlock); // on ajout notre block au reste des blocks
        currentBlock = program;
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

        assert (prog.checkAllLocations());
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

        this.addLabel(this.labelManager.getTasPleinLabel());
        this.addInstruction(new WSTR("Error: Heap full"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());

        this.addLabel(this.labelManager.getNoReturnLabel());
        this.addInstruction(new WSTR("Error: No return in the method"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());

        this.addLabel(this.labelManager.getCastFailedLabel());
        this.addInstruction(new WSTR("Error: Cast Failed"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());

        this.addLabel(this.labelManager.getEqualsLabel());
        this.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(0)));
        this.addInstruction(new CMP(new RegisterOffset(-3, Register.LB), Register.getR(0)));
        this.addInstruction(new SEQ(Register.getR(0)));
        this.addInstruction(new RTS());

        LOG.debug("Generated assembly code:" + nl + currentBlock.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        getCurrentBlock().display(new PrintStream(fstream));

        // program.display(new PrintStream(fstream));
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
        Main.main(new String[] { "-d", destName.substring(0, destName.lastIndexOf('/')), destName });
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
        pS.println(".limit stack 256"); // feel the magic in the air
        pS.println(".limit locals 256"); // allez allez allez allez
    }

    private static void writeJasminEnd(PrintStream pS) {
        pS.println("return");
        pS.println(".end method");
    }

    public enum JasminStaticVars {
        SYSTEM_OUT(1),
        SYSTEM_IN(2);

        private int id;

        private JasminStaticVars(int id) {
            this.id = id;
        }

        public int id() {
            return id;
        }

        public static int getLastUsed() {
            return 2; // 2 used by SYSTEM_IN
        }

    }

}
