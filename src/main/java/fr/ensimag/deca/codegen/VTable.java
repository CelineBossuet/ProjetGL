package fr.ensimag.deca.codegen;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.LabelOperand;

/**
 * Virtual Method Table : Data Structure représentant la table des methodes
 */
public class VTable {
    private DAddr operand;
    private LabelOperand[] content;
    private int taille;

    public DAddr getOperand(){return operand;}

    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    public VTable(int nbMethods, VTable parent){
        taille = nbMethods;
        content  = new LabelOperand[nbMethods];
        if(parent !=null){
            System.arraycopy(parent.content, 0, content, 0, parent.taille);

        }
    }


    public void set(int i, LabelOperand l){
        content[i]=l;}

    public void codeGen(DecacCompiler compiler){
        for(int i=0; i<taille; i++){
            compiler.getMemoryManager().createConstant(content[i], compiler.getCurrentBlock());
        }
    }
}
