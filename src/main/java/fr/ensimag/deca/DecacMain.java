package fr.ensimag.deca;

import java.io.File;

import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
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
        // example log4j message.
        LOG.info("Decac compiler started");
        System.out.println("Bienvenue dans Decac :");
        boolean error = false;
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
            System.out.println("Le nom de l'équipe !");
            System.out.println("By the way c'est l'équipe 13");
        }
        if (options.getParser()){
            System.out.println("Décompilation de l'arbre :");
            for (File f : options.getSourceFiles()){
                DecaLexer lexer = new DecaLexer((CharStream) f);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                DecaParser parser = new DecaParser(tokens);
                System.out.println(parser);
            }
            //des trucs
        }
        if (options.getVerif()){
            System.out.println("S'arrete après l'étape de vérification :");
            //d'autres trucs
        }
        if (options.getSourceFiles().isEmpty()) {
            System.out.println("--------------------------------------");
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
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
