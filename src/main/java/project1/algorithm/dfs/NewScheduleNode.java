package project1.algorithm.dfs;

/**
 * Node of the DFS schedule tree. A node contains the schedule of tasks and the processors that the tasks are allocated
 * to.
 *
 * Author: Dave Shin
 */
public class NewScheduleNode {
    private String _id;
    private int _startTime;
    private int _endTime;
    private int _processor;

    public NewScheduleNode(String id, int startTime, int endTime, int processor) {
        _id = id;
        _startTime = startTime;
        _endTime = endTime;
        _processor = processor;
    }

    public String getId() {
        return _id;
    }

    public int getStartTime() {
        return _startTime;
    }

    public int getEndTime() {
        return _endTime;
    }

    public int getProcessorNum() {
        return _processor;
    }

    public void setEndTime(int newEndTime) {
        _endTime = newEndTime;
    }
}
