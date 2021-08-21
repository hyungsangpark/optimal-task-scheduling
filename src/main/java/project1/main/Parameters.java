package project1.main;

public class Parameters {

    private static Parameters _instance;

    private int _numProcessors = 1;
    private int _numParallelCores = 1;
    private boolean _isVisualised = false;
    private String _outputName = null;

    private Parameters() {
    }

    public static Parameters getInstance() {
        if (_instance == null) _instance = new Parameters();
        return _instance;
    }

    public int getNumProcessors() {
        return _numProcessors;
    }

    public void setNumProcessors(int numProcessors) {
        _numProcessors = numProcessors;
    }

    public int getNumParallelCores() {
        return _numParallelCores;
    }

    public void setNumParallelCores(int numParallelCores) {
        _numParallelCores = numParallelCores;
    }

    public boolean isVisualised() {
        return _isVisualised;
    }

    public void setVisualised(boolean isVisualised) {
        _isVisualised = isVisualised;
    }

    public String getOutputName() {
        return _outputName;
    }

    public void setOutputName(String outputName) {
        _outputName = outputName;
    }


}
