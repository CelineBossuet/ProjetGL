package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        boolean error = false;
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
        }
        if (options.getVerif()) {
            LOG.debug("Decac compiler will stop after verification");
        }
        if (options.getParallel()) {

            int cpuThreads = Runtime.getRuntime().availableProcessors();
            ExecutorService eS = Executors.newFixedThreadPool(cpuThreads);
            ArrayList<Future<Boolean>> results = new ArrayList<Future<Boolean>>();
            for (File source : options.getSourceFiles()) {
                Callable<Boolean> compilerLauncher = () -> {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    return compiler.compile();
                };
                results.add(eS.submit(compilerLauncher)); // submit Callable task
            }
            for (Future<Boolean> result : results) {
                try {
                    if (result.get()) // finish execution and get results
                        error = true;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    error = true;
                }
            }

        } else { // Normal execution
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                try {
                    if (compiler.compile())
                        error = true;
                } catch (Exception e) { // finish other files
                    e.printStackTrace();
                    error = true;
                }
            }
        }

        System.exit(error ? 1 : 0);
    }
}
