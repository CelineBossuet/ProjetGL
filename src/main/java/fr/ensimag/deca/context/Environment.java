package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.HashMap;

/**
 * Dictionary associating identifier's D to their names.
 * 
 * This is actually a linked list of dictionaries: each Environment has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 * 
 * Insertion (through method declare) is always done in the "current"
 * dictionary.
 * 
 * @author gl13
 * @date 10/01/2022
 */
public class Environment<D extends Definition> {

    private HashMap<Symbol, D> environment;

    Environment<D> parentEnvironment;

    public Environment(Environment parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.environment = new HashMap<Symbol, D>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    public D defOfType(Symbol s) {
        return environment.get(s);
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public D get(Symbol key) {
        D result = environment.get(key); // first look in current block
        if (result != null || parentEnvironment == null)
            return result;
        return parentEnvironment.get(key);
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *             Name of the symbol to define
     * @param def
     *             D of the symbol
     * @throws DoubleDefException
     *                            if the symbol is already defined at the "current"
     *                            dictionary
     *
     */
    public void declare(Symbol name, D def) throws DoubleDefException {
        // symbols are unique
        if (environment.containsKey(name))
            throw new DoubleDefException();
        else
            environment.put(name, def); // create symbol in this environment without modify upper block

    }

    public void declareClass(Symbol name, D def) throws Environment.DoubleDefException {
        D previousDef = this.environment.get(def);
        if (previousDef != null) {
            this.environment.put(name, def);
        } else {
            throw new DoubleDefException();
        }
    }

}
