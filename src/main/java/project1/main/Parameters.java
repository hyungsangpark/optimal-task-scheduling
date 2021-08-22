package project1.main;

/**
 * Encapsulates program arguments such as number of processors,
 * number of parallel cores, is visualised, and custom output name.
 */
public class Parameters {

    // Singleton instance of this class.
    private static Parameters _instance;

    private int _numProcessors = 1;
    private int _numParallelCores = 1;
    private boolean _isVisualised = false;
    private String _graphFileName = null;
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
     * Gets the number of processors to schedule tasks.
     * @return Number of processors to schedule tasks.
     */
    public int getNumProcessors() {
        return _numProcessors;
    }

    /**
     * Sets the number of processors to schedule tasks.
     * @param numProcessors Number of processors to schedule tasks.
     */
    public void setNumProcessors(int numProcessors) {
        _numProcessors = numProcessors;
    }

    /**
     * Gets the number of parallel cores used for scheduling.
     * @return Number of parallel cores used for scheduling.
     */
    public int getNumParallelCores() {
        return _numParallelCores;
    }

    /**
     * Sets the number of parallel cores used for scheduling.
     * @param numParallelCores Number of prallel cores used for scheduling.
     */
    public void setNumParallelCores(int numParallelCores) {
        _numParallelCores = numParallelCores;
    }

    /**
     * Checks whether the scheduler program is visualised.
     * @return True if the program is visualized, otherwise false.
     */
    public boolean isVisualised() {
        return _isVisualised;
    }

    /**
     * Sets whether the scheduler should be visualized.
     * @param isVisualised True if program should be visualized, otherwise false.
     */
    public void setVisualised(boolean isVisualised) {
        _isVisualised = isVisualised;
    }

    /**
     * Gets the output name of the graph file.
     * @return The output name of the graph file.
     */
    public String getOutputName() {
        return _outputName;
    }

    /**
     * Sets the output name of the graph file.
     * @param outputName The output name of the graph file.
     */
    public void setOutputName(String outputName) {
        _outputName = outputName;
    }

    public String getGraphFileName() {
        return _graphFileName;
    }

    public void setGraphFileName(String graphFileName) {
        _graphFileName = graphFileName;
    }

}
