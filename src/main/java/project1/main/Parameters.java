package project1.main;


/**
 * This class enables the encapsulation of data, in which they can be accessed by getters and setters.
 */

public class Parameters {

    private static Parameters _instance;

    private int _numProcessors = 1;
    private int _numParallelCores = 1;
    private boolean _isVisualised = false;
    private String _outputName = null;

    /**
     * Constructor method for the Parameters class.
     */

    private Parameters() {
    }

    /**
     * This method gets the instance of the Parameters object.
     * @return Parameters object.
     */

    public static Parameters getInstance() {
        if (_instance == null) _instance = new Parameters();
        return _instance;
    }

    /**
     * This method gets the number of processors to be used for scheduling.
     * @return Number of processors to be used for scheduling.
     */

    public int getNumProcessors() {
        return _numProcessors;
    }

    /**
     * This method sets the number processors to be used for scheduling.
     * @param numProcessors Number of processors to be used for scheduling.
     */

    public void setNumProcessors(int numProcessors) {
        _numProcessors = numProcessors;
    }

    /**
     * This method gets the number of parallel cores to be used for scheduling.
     * @return Number of parallel cores to be used for scheduling.
     */

    public int getNumParallelCores() {
        return _numParallelCores;
    }

    /**
     * This method sets the number of parallel cores to be used for scheduling.
     * @param numParallelCores Number of parallel cores to be used for scheduling.
     */

    public void setNumParallelCores(int numParallelCores) {
        _numParallelCores = numParallelCores;
    }

    /**
     * This method gets whether the scheduled task is visualised in the GUI.
     * @return A boolean value of whether the scheduled task is displayed in the GUI.
     */

    public boolean isVisualised() {
        return _isVisualised;
    }

    /**
     * This method sets whether the scheduled task is visualised in the GUI.
     * @param isVisualised Boolean value of whether the scheduled task is displayed in the GUI.
     */

    public void setVisualised(boolean isVisualised) {
        _isVisualised = isVisualised;
    }

    /**
     * This method gets the output name of the graph file.
     * @return The output name of the graph file.
     */

    public String getOutputName() {
        return _outputName;
    }

    /**
     * This method sets the output name of the graph file.
     * @param outputName The output name of the graph file.
     */

    public void setOutputName(String outputName) {
        _outputName = outputName;
    }


}
