package fr.ensimag.deca.context;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.*;
import fr.ensimag.deca.tree.Location;

import java.awt.geom.FlatteningPathIterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Fichier de la structure de donnée représentant un environnement (association nom -> définition, avec possibilité
 *  d'empilement).
 *  Intinialement composé de différents type, on peut en ajouter
 */
public class EnvironmentType {
    private final Map<SymbolTable.Symbol, TypeDefinition> envType;
    public IntType INT;
    public FloatType FLOAT;
    public BooleanType BOOLEAN;
    public VoidType VOID;
    public StringType STRING;
    public NullType NULL;

    public EnvironmentType(DecacCompiler compiler){
        super();
        envType = new HashMap<SymbolTable.Symbol, TypeDefinition>();
        SymbolTable.Symbol symbInt = compiler.getSymbolTable().create("int");
        SymbolTable.Symbol symbFloat = compiler.getSymbolTable().create("float");
        SymbolTable.Symbol symbBool  = compiler.getSymbolTable().create("boolean");
        SymbolTable.Symbol symbVoid = compiler.getSymbolTable().create("void");
        SymbolTable.Symbol symbString = compiler.getSymbolTable().create("string");
        SymbolTable.Symbol symbNull = compiler.getSymbolTable().create("null");

        INT = new IntType(symbInt);
        FLOAT = new FloatType(symbFloat);
        BOOLEAN =  new BooleanType(symbBool);
        VOID = new VoidType(symbVoid);
        STRING = new StringType(symbString);
        NULL = new NullType(symbNull);

        envType.put(symbInt, new TypeDefinition(INT, Location.BUILTIN));
        envType.put(symbFloat, new TypeDefinition(FLOAT, Location.BUILTIN));
        envType.put(symbBool, new TypeDefinition(BOOLEAN, Location.BUILTIN));
        envType.put(symbNull, new TypeDefinition(NULL, Location.BUILTIN));
    }

    public TypeDefinition defOfType(SymbolTable.Symbol symb){
        return envType.get(symb);
    }
}
