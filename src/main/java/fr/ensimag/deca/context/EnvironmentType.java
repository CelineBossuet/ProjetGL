package fr.ensimag.deca.context;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.HashMap;

import fr.ensimag.deca.tools.SymbolTable.Symbol;


public class EnvironmentType {

    private HashMap<Symbol, TypeDefinition> environment;

    EnvironmentType parentEnvironment;

    public EnvironmentType(EnvironmentType parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.environment = new HashMap<Symbol, TypeDefinition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }


    public TypeDefinition get(Symbol key) {
        TypeDefinition result = environment.get(key); // first look in current block
        if (result != null || parentEnvironment == null)
            return result;
        return parentEnvironment.get(key);
    }


    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
        // symbols are unique
        if (environment.containsKey(name))
            throw new DoubleDefException();
        else
            environment.put(name, def); // create symbol in this environment without modify upper block

    }

}
