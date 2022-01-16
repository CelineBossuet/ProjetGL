package fr.ensimag.deca.codegen;


import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.LabelOperand;

import java.util.HashMap;

/**
 * Virtual Method Table : Data Structure représentant la table des methodes
 */
public class VTable {
    private DAddr operand;
    private HashMap<LabelOperand, String> content;
    private int taille;

    public DAddr getOperand(){return operand;}

    public VTable(int nbMethods, VTable parent){
        taille = nbMethods;
        content  = new HashMap<>();
        if(parent !=null){
            this.content.putAll(parent.content);

        }
    }

    protected boolean put(LabelOperand value, String comment){
        if(this.content.size()<taille){
            this.content.put(value, comment);
            return true;
        }
        return false;
    }
}
