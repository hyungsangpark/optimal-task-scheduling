package project1.gui;

import project1.algorithm.AStar;
import project1.data.Processor;
import project1.data.ScheduleNode;
import project1.main.Parameters;

import java.util.HashMap;

/**
 * Starts scheduling using A* algorithm.
 * It also encapsulates files needed for updating the produced schedule on the chart,
 * such as isChanged & isFinished flags and scheduleMap.
 */
public class SchedulingThread extends Thread {
    private boolean _isChanged = false;
    private boolean _isFinished = false;
    private HashMap<Integer, Processor> _scheduleMap;
    private int _optimalTime;

    /**
     * Runs when this thread starts.
     * Starts the A* algorithm and handles post-scheduling procedures.
     *
     * NOTICE: _scheduleMap field is expected to change over the course of solving schedule node.
     */
    @Override
    public void run() {
        Parameters parameters = Parameters.getInstance();

        // Set up AStar for visualisation, and start scheduling.
        AStar newSearch = new AStar(parameters.getNumProcessors(), parameters.getNumParallelCores(), this);
        // During the execution of this method, _scheduleMap is expected to be updated regularly.
        ScheduleNode schedule = newSearch.aStarSearch();

        // Once scheduling has finished, get the optimal time out of the schedule.
        _optimalTime = schedule.getOptimalTime();

        _isFinished = true;
    }

    /**
     * Clear schedule changed flag,
     * essentially declaring that current schedule map in this instance has already been updated.
     */
    public void changeApplied() {
        _isChanged = false;
    }

    /**
     * Getter of schedule changed flag.
     * If the schedule has been updated and its changes have yet been applied, it returns true;
     * Otherwise, it returns false.
     *
     * @return boolean variable of whether the schedule has been changed.
     */
    public boolean isChanged() {
        return _isChanged;
    }

    /**
     * Getter of scheduling finished flag.
     * If the scheduling has been finished, it returns true, otherwise false.
     *
     * @return boolean variable of whether the scheduling has been finished.
     */
    public boolean isFinished() {
        return _isFinished;
    }

    /**
     * Getter of optimal time produced from the scheduling.
     * Optimal time is essentially the latest time when none of the processors are scheduled.
     *
     * @return int value of optimal time produced from the schedule.
     */
    public int getOptimalTime() {
        return _optimalTime;
    }

    /**
     * Getter of the current state of schedule.
     * @return
     */
    public HashMap<Integer, Processor> getSchedule() {
        return _scheduleMap;
    }

    /**
     * Sets schedule in the instance with the schedule map given, then raise schedule changed flag.
     * @param scheduleMap
     */
    public void updateSchedule(HashMap<Integer, Processor> scheduleMap) {
        this._scheduleMap = scheduleMap;
        _isChanged = true;
    }
}
