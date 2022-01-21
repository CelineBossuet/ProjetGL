package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

//import de Iterator
import java.util.Iterator;

/**
 *
 * @author gl13
 * @date 01/01/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);


    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (Iterator<AbstractDeclClass> i = this.iterator(); i.hasNext();) {
            i.next().verifyClass(compiler);
        }

        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify ClassMembers: start");
        for (AbstractDeclClass c : getList()){
            c.verifyClassMembers(compiler);
        }
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify CLassBody: start");
        for (AbstractDeclClass c : getList()){
            c.verifyClassBody(compiler);
        }
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Programme qui correspond à la première passe pour la génération de code.
     * Donc elle parcourt les classes du programme en s’intéressant à leurs méthodes,
     * de façon à générer du code pour construire la table des méthodes ie VTable
     * @param compiler
     */
    public void codeGenListClass(DecacCompiler compiler){
        int first=0;
        for (AbstractDeclClass c : getList()){
            c.codeGenClass(compiler, first);
            first++;
            if(first ==1){
                c.codeGenClass(compiler, first);
            }
        }
    }

    /**
     * Programme qui correspond à la 2eme passe pour la génération de code.
     * ie s'occupe de l'initialisation de chaque classe, le codage des différentes méthodes
     * et du programme principal
     * @param compiler
     */
    public void codeGenListClassBody(DecacCompiler compiler){
        for (AbstractDeclClass c : getList()){
            c.codeGenClassBody(compiler);
        }
    }

}
