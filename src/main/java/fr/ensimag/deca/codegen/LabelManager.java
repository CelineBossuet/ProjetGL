package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

/**
 * Fichier permetant de gerer la création des Labels
 * @author gl13
 * @date 10/01/2022
 */
public class LabelManager {
    private Label label;
    private Label i_errorLabel; //label permettant de gérer un mauvais input pour readInt, readFloat
    private int counter=0;


    public Label newLabel(String name){
        counter++;
        return new Label(name, counter);
    }

    public Label getIErrorLabel() {
        if (i_errorLabel == null) {
            i_errorLabel = new Label("error", 0);
        }
        return i_errorLabel;
    }
}
