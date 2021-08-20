package project1.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import project1.data.NewScheduleNode;
import project1.main.Parameters;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class Controller {

    // The following labels represent and update information in the GUI.
    @FXML
    private Text status;
    @FXML
    private Text optimalTime;
    @FXML
    private Text timeElapsed;
    @FXML
    private Button startButton;
    @FXML
    private StackedBarChart<String, Number> scheduleGraph;

    private int numProcessors;

    /**
     * Update the schedule graph according to the list of schedule nodes given.
     *
     * @param schedules List of schedules to be plotted in the graph.
     */
    public void updateSchedule(NewScheduleNode[] schedules) {
        // Clear previous data from the graph.
        scheduleGraph.getData().clear();

        // List of the earliest start time possible for each processor.
        // Index = processor number - 1. (due to general coding convention of index starting at 0)
        int[] earliestStartTimes = new int[numProcessors];

        // Essentially a list to store schedule blocks to plot.
        XYChart.Series<String, Number> schedulesToPlot = new XYChart.Series<>();

        // The blocks are sorted in descending order of firstly their processor number, then their start time.
        // This is due to how JavaFX plots their graph.
        Stream<NewScheduleNode> sortedSchedules = Arrays.stream(schedules).sorted();

        // Add each schedule block into series.
        sortedSchedules.forEach(schedule -> {
            // Define repeatedly used values into variables.
            XYChart.Data<String, Number> block;
            String blockNum = String.valueOf(schedule.getProcessorNum());

            // If there is a gap between the earliest possible start time and current schedule's start time,
            // Create a transparent block which would calibrate the actual schedule block into a correct position.
            if (earliestStartTimes[schedule.getProcessorNum() - 1] < schedule.getStartTime()) {
                block = new XYChart.Data<>(blockNum, schedule.getStartTime() - earliestStartTimes[schedule.getProcessorNum() - 1]);
                // Make block transparent.
                block.nodeProperty().addListener((ov, oldNode, node) -> node.setStyle("-fx-bar-fill: transparent"));
                schedulesToPlot.getData().add(block);
            }

            // Plot a block representing a schedule to be plotted on to the schedule graph.
            block = new XYChart.Data<>(blockNum, schedule.getEndTime() - schedule.getStartTime());
            // Change block style.
            block.nodeProperty().addListener((ov, oldNode, node) -> node.setStyle("-fx-border-color: rgba(39, 92, 161, 0.2); -fx-bar-fill: rgba(39, 92, 161, 0.8)"));
            schedulesToPlot.getData().add(block);

            // Update the earliest start time the corresponding processor to the end time of current schedule plotted.
            earliestStartTimes[schedule.getProcessorNum() - 1] = schedule.getEndTime();
        });

        // Add the series of blocks to be plotted to the graph.
        scheduleGraph.getData().add(schedulesToPlot);
    }

    /**
     * Changes elements that needs to be updated such as optimal time produced and status.
     * It also enables the button which was disabled.
     *
     * @param optimalTimeValue Optimal time retrieved from the output of the scheduler. i.e. max height in the graph.
     */
    private void schedulingEnded(int optimalTimeValue) {
        optimalTime.setText(String.valueOf(optimalTimeValue));
        status.setText("READY");
        startButton.setDisable(false);
    }

}
