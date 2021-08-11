package project1.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Visualiser Class is responsible for loading the GUI.
 * Will add more comments
 */

public class Visualiser extends Application {

    private Controller controller;
    Scene scheduleScene;
    public Stage program;
    @Override
    public void start(Stage program) throws Exception{
        FXMLLoader loader = new FXMLLoader((getClass().getResource("Visualiser.fxml")));
        Parent root= loader.load();
        this.controller= loader.getController();

        scheduleScene= new Scene(root, 400, 600);
        program.setScene(scheduleScene);
        program.setTitle("Schedule Visualiser");
        program.show();

        // Closing the GUI and algorithm.
        program.setOnCloseRequest(event -> System.exit(0));


    }
    /**
     * This method sets up the fields for the Controller class that are to \
     * be displayed on the GUI.
     */
    public void ArgsSetup(int numProcessors, int numTasks, String graphName){
        controller.ArgsSetup(numProcessors, numTasks, graphName);
    }
}
