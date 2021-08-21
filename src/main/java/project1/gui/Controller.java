package project1.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;
import project1.IO.GraphReader;
import project1.data.Processor;
import project1.main.Parameters;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class Controller implements Initializable {

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
    private long startTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numProcessors = Parameters.getInstance().getNumProcessors();
    }

    /**
     * Runs when start button is pressed.
     * It essentially modifies appropriate elements in the application screen,
     * measures start time, starts the scheduler algorithm in a new background thread,
     * then initiates an status updater which regularly updates information such as
     * time elapsed and schedule graph.
     */
    public void handleStartButton() {

        // Configure current screen to running state.
        status.setText("RUNNING");
        startButton.setDisable(true);

        // Create a thread which would solve the algorithm.
        SolveAlgorithm thread = new SolveAlgorithm();

        // Measure start time.
        startTime = System.nanoTime();

        // Start the thread which would start solving the algorithm.
        thread.start();

        // Every millisecond, check to see if there is an update in the schedule from SolveAlgorithm thread.
        Timeline statusUpdater = new Timeline();
        statusUpdater.getKeyFrames().add(new KeyFrame(Duration.millis(1),
                event -> {
                    // Calculate time elapsed and update timeElapsed.
                    long timeElapsedValue = System.nanoTime() - startTime;

                    int sec = (int) (timeElapsedValue / 1000000000);
                    int ms = (int) (timeElapsedValue / 1000000) - sec * 1000;

                    timeElapsed.setText(sec + "." + String.format("%3s", ms).replace(' ', '0') + " sec");

                    // If a schedule is changed, update schedule graph accordingly.
                    if (thread.isChanged()) {
                        thread.changeApplied();
                        updateSchedule(thread.getSchedule());
                    }

                    // If a thread has finished its task, stop updating status and run end of schedule procedure.
                    if (thread.isFinished()) {
                        statusUpdater.stop();
                        schedulingEnded(thread.getOptimalTime());
                    }
                }));
        statusUpdater.setCycleCount(Timeline.INDEFINITE);
        statusUpdater.play();
    }

    /**
     * Update the schedule graph according to the list of schedule nodes given.
     *
     * @param schedules List of schedules to be plotted in the graph.
     */
    public void updateSchedule(HashMap<Integer, Processor> schedules) {
        // Clear previous data from the graph.
        scheduleGraph.getData().clear();

        // List of the earliest start time possible for each processor.
        // Index = processor number - 1. (due to general coding convention of index starting at 0)
        int[] earliestStartTimes = new int[numProcessors];

        // Essentially a list to store schedule blocks to plot.
        XYChart.Series<String, Number> schedulesToPlot = new XYChart.Series<>();

        for (int processorIndex = numProcessors; processorIndex > 0; processorIndex--) {
            Processor processor = schedules.get(processorIndex);
            if (processor == null) {
                addBlock(schedulesToPlot.getData(), processorIndex, 1, true);
            } else {
                int finalProcessorIndex = processorIndex;

                processor.getNodesOrderMap().forEach((index, node) -> {
                    int startTime = processor.getNodesInScheduleMap().get(node);
                    int weight = GraphReader.getInstance().getNodeWeightsMap().get(node);

                    // If there is a gap between the earliest possible start time and current schedule's start time,
                    // Create a transparent block which would calibrate the actual schedule block into a correct position.
                    if (earliestStartTimes[finalProcessorIndex - 1] < startTime) {
                        addBlock(schedulesToPlot.getData(),
                                finalProcessorIndex,
                                startTime - earliestStartTimes[finalProcessorIndex - 1],
                                true);
                    }

                    // Plot a block representing a schedule to be plotted on to the schedule graph.
                    addBlock(schedulesToPlot.getData(),
                            finalProcessorIndex,
                            weight,
                            false);

                    // Update the earliest start time the corresponding processor to the end time of current schedule plotted.
                    earliestStartTimes[finalProcessorIndex - 1] = startTime + weight;
                });
            }
        }

        // Add the series of blocks to be plotted to the graph.
        scheduleGraph.getData().add(schedulesToPlot);






        // The blocks are sorted in descending order of firstly their processor number, then their start time.
        // This is due to how JavaFX plots their graph.
//        Stream<NewScheduleNode> sortedSchedules = Arrays.stream(schedules).sorted();

        /* A list of last processor added to the graph.
           It starts with a value of total number of processors + 1,
           since we assume that this value will normally be current processor number + 1.

           Also, this is an array which is expected to only have one value which suits the array name,
           since the array acts as a wrapper class to the integer value to be used in lambda expressions. */
    /*  int[] lastProcessorAdded = {numProcessors + 1};

        // Add each schedule block into series.
        sortedSchedules.forEach(schedule -> {
            // If previous processor added to the graph is not current processor + 1,
            // add a transparent block on those processors in between as a placeholder.
            if (lastProcessorAdded[0] - 1 > schedule.getProcessorNum()) {
                for (int i = lastProcessorAdded[0] - 1; i > schedule.getProcessorNum(); i--) {
                    addBlock(schedulesToPlot.getData(), i, 1, true);
                }
            }
            // Update last processor added to the current processor being handled.
            lastProcessorAdded[0] = schedule.getProcessorNum();

            // If there is a gap between the earliest possible start time and current schedule's start time,
            // Create a transparent block which would calibrate the actual schedule block into a correct position.
            if (earliestStartTimes[schedule.getProcessorNum() - 1] < schedule.getStartTime()) {
                addBlock(schedulesToPlot.getData(),
                        schedule.getProcessorNum(),
                        schedule.getStartTime() - earliestStartTimes[schedule.getProcessorNum() - 1],
                        true);
            }

            // Plot a block representing a schedule to be plotted on to the schedule graph.
            addBlock(schedulesToPlot.getData(),
                    schedule.getProcessorNum(),
                    schedule.getEndTime() - schedule.getStartTime(),
                    false);

            // Update the earliest start time the corresponding processor to the end time of current schedule plotted.
            earliestStartTimes[schedule.getProcessorNum() - 1] = schedule.getEndTime();
        });

        // If the last processor added even after plotting every schedule is bigger than 1,
        // plot a transparent blocks on each of those processors in between them as a placeholder.
        if (lastProcessorAdded[0] > 1) {
            for (int i = lastProcessorAdded[0] - 1; i >= 1; i--) {
                addBlock(schedulesToPlot.getData(), i, 1, true);
            }
        }
    */



    }

    /**
     * Creates an appropriate block according to the values given, and adds it to the list of data blocks given as well.
     *
     * @param blockList     List of XYChart.Data instances where a schedule block should be added to.
     * @param processor     The processor number of the schedule block in int.
     * @param blockLength   The length of the schedule block.
     * @param isTransparent Whether the block creating should be a transparent block or not.
     */
    private void addBlock(ObservableList<XYChart.Data<String, Number>> blockList, int processor, int blockLength, boolean isTransparent) {
        XYChart.Data<String, Number> block = new XYChart.Data<>(String.valueOf(processor), blockLength);
        // Style the block created. Should block be transparent, make it transparent; otherwise color it.
        block.nodeProperty().addListener((ov, oldNode, node) ->
                node.setStyle("-fx-bar-fill: " + (isTransparent ? "transparent;" : "rgba(39, 92, 161, 0.9); -fx-border-color: rgb(22,70,130);")));
        blockList.add(block);
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
