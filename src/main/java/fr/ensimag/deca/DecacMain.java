package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

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
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
            System.exit(0);
        }
        if (options.getParser()) {
            LOG.info("Decac compiler will stop after parsing");
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    // A FAIRE
                    System.exit(0);
                }
            }
        }
        if (options.getVerif()) {
            LOG.info("Decac compiler will stop after verification");
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    // A FAIRE
                    System.exit(0);
                }
            }
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else { // Normal execution
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile())
                    System.exit(0);
            }
        }
    }
}
