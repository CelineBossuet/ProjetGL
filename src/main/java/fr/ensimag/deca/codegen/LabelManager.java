package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

/**
 * Fichier permetant de gerer la création des Labels
 * @author gl13
 * @date 10/01/2022
 */
public class LabelManager {
    private Label input_errorLabel; //label permettant de gérer un mauvais input pour readInt, readFloat
    private Label overFlowLabel; //label gérant les overflow error
    private Label stack_overflowLabel;
    private Label pilePleineLabel; //utilisée pour le NEW()
    private Label noReturnLabel; //erreur pas de return dans une méthode non void
    private int counter=0; //compteur pour générer des Labels numérotés


    public Label newLabel(String name){
        this.counter++;
        return new Label(name, this.counter);
    }

    public Label getNoReturnLabel(){
        if(this.noReturnLabel ==null){
            this.noReturnLabel =new Label("noReturnLabel", 0);
        }
        return noReturnLabel;
    }

    public Label getStack_overflowLabel(){
        if(this.stack_overflowLabel==null){
            this.stack_overflowLabel = new Label("stackOverflow", 0);
        }
        return this.stack_overflowLabel;
    }

    public Label getIErrorLabel() {
        if (this.input_errorLabel == null) {
            this.input_errorLabel = new Label("inputError", 0);
        }
        return input_errorLabel;
    }

    public Label getOverFlowLabel(){
        if (this.overFlowLabel ==null){
            this.overFlowLabel =new Label("overFlowError", 0);
        }
        return this.overFlowLabel;
    }

    public Label getPilePleineLabel(){
        if(this.pilePleineLabel ==null){
            this.pilePleineLabel = new Label("pilePleineLabel", 0);
        }
        return pilePleineLabel;
    }
}
