package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AffichagePersonne {

    @FXML
    private Label nomID;

    @FXML
    private Label prenomID;

    public void setNomID(TextField tf){
        nomID.setText(tf.getText());
    }
    public void setPreNomID(TextField tf){
        prenomID.setText(tf.getText());
    }

}
