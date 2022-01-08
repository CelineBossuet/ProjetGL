package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    private Label label;
    private Label io_errorLabel;
    private int counter=0;


    public Label newLabel(String name){
        counter++;
        return new Label(name);
    }

    public Label getIoErrorLabel() {
        if (io_errorLabel == null) {
            io_errorLabel = new Label("error");
        }
        return io_errorLabel;
    }
}
