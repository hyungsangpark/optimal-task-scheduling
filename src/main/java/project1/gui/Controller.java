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
import project1.IO.GraphWriter;
import project1.data.Processor;
import project1.main.Parameters;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    // The following elements represent and update information in the GUI.
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

        // Create a thread which would start scheduling.
        SchedulingThread thread = new SchedulingThread();

        // Measure start time.
        startTime = System.nanoTime();

        // Start the thread which would start solving the algorithm.
        thread.start();

        // Every 100 millisecond, check to see if there is an update in the schedule from SolveAlgorithm thread.
        // A frequency of updating every 100 millisecond was deliberately chosen, with considerations of
        // minimising decrease in performance and updating quickly enough to produce a correct elapsed time.
        Timeline statusUpdater = new Timeline();
        statusUpdater.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                event -> {
                    // Calculate time elapsed and update timeElapsed.
                    long timeElapsedValue = System.nanoTime() - startTime;

                    int sec = (int) (timeElapsedValue / 1000000000);
                    int ms = (int) (timeElapsedValue / 1000000) - sec * 1000;

                    timeElapsed.setText(sec + "." + String.format("%3s", ms).replace(' ', '0') + "s");

                    // If a schedule is changed, update schedule graph accordingly.
                    if (thread.isChanged()) {
                        thread.changeApplied();
                        updateSchedule(thread.getSchedule());
                    }

                    // If a thread has finished its task, stop updating status and run end of schedule procedure.
                    if (thread.isFinished()) {
                        statusUpdater.stop();
                        schedulingEnded(thread.getSchedule(), thread.getOptimalTime());
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

        // For each processor, plot the schedule
        for (int processorIndex = numProcessors; processorIndex > 0; processorIndex--) {
            Processor processor = schedules.get(processorIndex - 1);

            // If a processor does not have any schedule, add a transparent placeholder.
            if (processor.getCurrentFinishTime() == 0) {
                addBlock(schedulesToPlot.getData(), processorIndex, 1, true);
            } else {
                int finalProcessorIndex = processorIndex;

                // For each scheduled node in a processor
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
     * Writes the graph into an output dot file, then changes elements that needs to be updated such as
     * optimal time produced and status. It also enables the button which was disabled.
     *
     * @param optimalTimeValue Optimal time retrieved from the output of the scheduler. i.e. max height in the graph.
     */
    private void schedulingEnded(HashMap<Integer, Processor> scheduleMap, int optimalTimeValue) {
        GraphWriter graphWriter = new GraphWriter();
        graphWriter.outputGraphData(Parameters.getInstance().getOutputName(), scheduleMap);

        optimalTime.setText(String.valueOf(optimalTimeValue));
        status.setText("READY");
        startButton.setDisable(false);
    }

}
