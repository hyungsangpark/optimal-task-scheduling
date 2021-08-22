package project1.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Initiates Visualiser JavaFX application.
 */
public class Visualiser extends Application {

    // Main class needed to launch JavaFX Application.
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage program) throws Exception {
        // Create a scene loader from Visualiser FXML file.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Visualiser.fxml"));

        // Configure the visualiser screen, and show it on the program.
        program.setScene(new Scene(loader.load(), 400, 600));
        program.setTitle("Task Scheduler Visualiser");
        program.setResizable(false);
        program.show();

        // Closing the GUI and algorithm.
        program.setOnCloseRequest(event -> System.exit(0));
    }
}
