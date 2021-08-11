package project1.gui;

import javafx.fxml.FXML;

import java.awt.*;

public class Controller {

    // The following labels represent and update information in the GUI.
    @FXML
    private Label graphNameLabel;
    @FXML
    private Label numTasksLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Button startButton;

    private int numProcessors;

    public void ArgsSetup (int numProcessors, int numTasks, String graphName) {
        graphNameLabel.setText(graphName);
        numTasksLabel.setText(numTasks + "");
        this.numProcessors = numProcessors;
    }

}