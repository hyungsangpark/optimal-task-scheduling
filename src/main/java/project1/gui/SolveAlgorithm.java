package project1.gui;

import project1.algorithm.AStar;
import project1.data.Processor;
import project1.data.ScheduleNode;
import project1.main.Parameters;

import java.util.HashMap;

public class SolveAlgorithm extends Thread {
    private boolean isChanged = false;
    private boolean isFinished = false;
    private HashMap<Integer, Processor> scheduleMap;
    private int optimalTime;

    @Override
    public void run() {
        Parameters parameters = Parameters.getInstance();

        AStar newSearch = new AStar(parameters.getNumProcessors(), parameters.getNumParallelCores(), this);
        ScheduleNode schedule = newSearch.aStarSearch();

        scheduleMap = schedule.getScheduleMap();
        optimalTime = schedule.getOptimalTime();

        isFinished = true;
    }

    public void changeApplied() {
        isChanged = false;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public int getOptimalTime() {
        return optimalTime;
    }

    public HashMap<Integer, Processor> getSchedule() {
        return scheduleMap;
    }

    public void updateSchedule(HashMap<Integer, Processor> scheduleMap) {
        this.scheduleMap = scheduleMap;
        isChanged = true;
    }
}
