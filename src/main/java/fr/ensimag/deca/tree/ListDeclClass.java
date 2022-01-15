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
            System.out.println("Hey");
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
        for (AbstractDeclClass c : getList()){
            c.verifyClassBody(compiler);
        }
        //throw new UnsupportedOperationException("not yet implemented");
    }

}
