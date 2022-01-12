package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

/**
 * Fichier permetant de gerer la création des Labels
 * @author gl13
 * @date 10/01/2022
 */
public class LabelManager {
    private Label label;
    private Label io_errorLabel;
    private int counter=0;


    public Label newLabel(String name){
        counter++;
        return new Label(name, counter);
    }

    public Label getIoErrorLabel() {
        if (io_errorLabel == null) {
            io_errorLabel = new Label("error", 0);
        }
        return io_errorLabel;
    }
}
