package project1.data;

/**
 * Node of the DFS schedule tree. A node contains the schedule of tasks and the processors that the tasks are allocated
 * to.
 *
 * Author: Dave Shin
 */
public class NewScheduleNode implements Comparable<NewScheduleNode> {
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

    /**
     * Method implemented for Comparable interface.
     * It compares two NewScheduleNode instances using two standards;
     * 1. Processor Number
     * 2. Start Time
     *
     * If a current instance should go before the instance given,
     * it would return a negative int.
     * If a current instance should go after the instance given,
     * it would return a positive int.
     * If a current instance is on equal order to the instance given,
     * it would return 0.
     *
     * @param anotherInstance Another instance of NewScheduleNode that is being compared.
     * @return An integer, based on description provided above.
     */
    @Override
    public int compareTo(NewScheduleNode anotherInstance) {
        if (_processor - anotherInstance._processor == 0) {
            return _startTime - anotherInstance._startTime;
        } else {
            return anotherInstance._processor - _processor;
        }
    }
}
