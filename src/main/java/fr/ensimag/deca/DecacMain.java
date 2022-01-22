package fr.ensimag.deca;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args) {
        LOG.info("Decac compiler started");
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("Nom de l'équipe : 13");
        }
        if (options.getSourceFiles().isEmpty() && !options.getPrintBanner()) {
            options.displayUsage();
            System.exit(0);
        }
        if (options.getParser()) {
            LOG.debug("Decac compiler will stop after parsing");
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                System.exit(compiler.compile() ? 1 : 0); // if no errors, tree displayed by compile function
            }
        }
        if (options.getVerif()) {
            LOG.debug("Decac compiler will stop after verification");
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                System.exit(compiler.compile() ? 1 : 0); // no display if no errors
            }
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else { // Normal execution
            LOG.debug("Decac compiler will fully compile");
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                System.exit(compiler.compile() ? 1 : 0);
            }
        }
    }
}
