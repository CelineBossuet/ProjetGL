package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl13
 * @date 01/01/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public boolean getParser() {
        return parser;
    }

    public boolean getVerif() {
        return verif;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private boolean parser = false;
    private boolean verif = false;

    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        for (String arg : args) {
            // A FAIRE : exclure -v et -p
            switch (arg) {
                case "-b":
                    printBanner = true;
                    break;
                case "-p":
                    parser = true;
                    break;
                case "-v":
                    verif = true;
                    break;
                case "-n":
                    // A FAIRE
                    break;
                case "-r": // A FAIRE
                case "-d": // A FAIRE
                case "-P":
                    parallel = true;
                    break;
                default:
                    sourceFiles.add(new File(arg));
            }
        }
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
            case QUIET:
                break; // keep default
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

    }

    protected void displayUsage() {
        System.out.println("Utilisation : decac [OPTION] [FILE]");
        System.out.println("Options disponibles :");
        System.out.println(". -b (banner) : affiche une bannière indiquant le nom de l'équipe\n" +
                ". -p (parse) : arrête decac après l'étape de construction de" +
                "l'arbre, et affiche la décompilation de ce dernier" +
                "(i.e. s'il n'y a qu'un fichier source à" +
                "compiler, la sortie doit être un programme" +
                "deca syntaxiquement correct)\n" +
                ". -v (verification) : arrête decac après l'étape de vérifications" +
                "(ne produit aucune sortie en l'absence d'erreur)\n" +
                ". -n (no check) : supprime les tests à l'exécution spécifiés dans" +
                "les points 11.1 et 11.3 de la sémantique de Deca.\n" +
                ". -r X (registers) : limite les registres banalisés disponibles à" +
                "R0 ... R{X-1}, avec 4 <= X <= 16\n" +
                ". -d (debug) : active les traces de debug. Répéter" +
                "l'option plusieurs fois pour avoir plus de" +
                "traces.\n" +
                ". -P (parallel) : s'il y a plusieurs fichiers sources," +
                "lance la compilation des fichiers en" +
                "parallèle (pour accélérer la compilation)");
    }
}
