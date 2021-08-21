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

        // To be replaced with a proper algorithm runner.
//        methodForTesting();
    }

//    // Remove this method as it is just for testing purposes.
//    private void methodForTesting() {
//        try {
//            sleep(1340);
//            schedule = new NewScheduleNode[10];
//            schedule[0] = new NewScheduleNode("A", 0, 3, 1);
//            schedule[1] = new NewScheduleNode("B", 2, 3, 2);
//            schedule[2] = new NewScheduleNode("C", 3, 5, 1);
//            schedule[3] = new NewScheduleNode("D", 4, 6, 3);
//            schedule[4] = new NewScheduleNode("E", 4, 6, 2);
//            schedule[5] = new NewScheduleNode("F", 1, 6, 4);
//            schedule[6] = new NewScheduleNode("G", 4, 6, 5);
//            schedule[7] = new NewScheduleNode("H", 4, 6, 6);
//            schedule[8] = new NewScheduleNode("I", 4, 6, 7);
//            schedule[9] = new NewScheduleNode("J", 4, 6, 8);
//            isChanged = true;
//            sleep(1365);
//            schedule[0] = new NewScheduleNode("K", 0, 3, 6);
//            schedule[1] = new NewScheduleNode("L", 2, 3, 4);
//            schedule[3] = new NewScheduleNode("N", 4, 6, 6);
//            schedule[4] = new NewScheduleNode("O", 4, 6, 1);
////            schedule[5] = new NewScheduleNode("P", 1, 6, 7);
//            schedule[6] = new NewScheduleNode("Q", 1, 2, 3);
//            schedule[7] = new NewScheduleNode("R", 2, 5, 2);
//            schedule[8] = new NewScheduleNode("S", 4, 5, 3);
//            schedule[9] = new NewScheduleNode("T", 1, 5, 5);
//            isChanged = true;
//            sleep(884);
//            schedule[2] = new NewScheduleNode("M", 3, 5, 8);
//            isChanged = true;
//            optimalTime = 6;
//            isFinished = true;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

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
    }
}
