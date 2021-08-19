package project1.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Visualiser Class is responsible for loading the GUI.
 * Will add more comments
 */
public class Visualiser extends Application {

    // Main class needed to launch JavaFX Application.
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage program) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Visualiser.fxml"));

        program.setScene(new Scene(loader.load(), 400, 600));
        program.setTitle("Task Scheduler Visualiser");
        program.show();

        // Closing the GUI and algorithm.
        program.setOnCloseRequest(event -> System.exit(0));
    }
}
