package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    private Label label;
    private int counter=0;


    public Label newLabel(String name){
        counter++;
        return new Label(name);
    }

}
