package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Environment;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl13
 * @date 01/01/2022
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar var : this.getList()) {
            var.decompile(s);
            s.println();
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler     contains the "env_types" attribute
     * @param localEnv
     *                     its "parentEnvironment" corresponds to "env_exp_sup"
     *                     attribute
     *                     in precondition, its "current" dictionary corresponds to
     *                     the "env_exp" attribute
     *                     in postcondition, its "current" dictionary corresponds to
     *                     the "env_exp_r" attribute
     * @param currentClass
     *                     corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclVariable(DecacCompiler compiler, Environment<ExpDefinition> localEnv,
            ClassDefinition currentClass) throws ContextualError {
        // A FAIRE TODO manage current class

        // verify each declared variable
        for (AbstractDeclVar dV : this.getList())
            dV.verifyDeclVar(compiler, localEnv, currentClass);
    }

    private static final Logger LOG = Logger.getLogger(ListDeclVar.class);

    protected int codeGenListVar(DecacCompiler compiler, boolean local) {
        int size = 0;
        for (AbstractDeclVar var : this.getList()) {
            LOG.info("On récupère la taille de toutees les variables");
            size += var.codeGenVar(compiler, local, size + 1);
        }
        return size;
    }

    protected int codeGenListVarJasmin(DecacCompiler compiler) {
        int size = 0; // TODO fini ?
        for (AbstractDeclVar var : this.getList())
            size += var.codeGenVarJasmin(compiler);
        return size;
    }

}
