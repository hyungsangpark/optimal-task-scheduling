package project1.data;

public class NewScheduleNode {
    private int _startTime;
    private int _endTime;
    private int _processor;

    public NewScheduleNode(int startTime, int endTime, int processor) {
        _startTime = startTime;
        _endTime = endTime;
        _processor = processor;
    }

    public int getEndTime() {
        return _endTime;
    }

    public int getProcessorNum() {
        return _processor;
    }
}
