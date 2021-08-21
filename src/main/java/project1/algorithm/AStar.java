package project1.algorithm;

import project1.data.ScheduleNode;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;

/**
 * This AStar class contains the main logic of an A* algorithm, it uses PriorityQueue to pick the ScheduleNode with
 * lowest f(n) and return an optimal solution. A HashSet is used to make ScheduleNodes in the open list unique, it will
 * ease up the memory usage.
 */

public class AStar {
    public PriorityQueue<ScheduleNode> _openList = new PriorityQueue<>(new PriorityQueueComparator());
    private final HashSet<ScheduleNode> scheduleNodesHashSet = new HashSet<>();
    private final int _processors;
    private final int _numOfCores;

    /**
     * Constructor of the AStar class.
     * @param processors    number of processors for the scheduler problem.
     * @param numOfCores    number of cores used for parallelisation.
     */

    public AStar(int processors, int numOfCores) {
        _processors = processors;
        _numOfCores = numOfCores;
    }

    /**
     * The main A* search algorithm, it keeps track of all options of ScheduleNodes in the open list.
     * ScheduleNode is chosen based on their f(n).
     * @return optimal schedule and its information.
     */

    public ScheduleNode aStarSearch() {
        ScheduleNode.threadPoolExecutor = Executors.newWorkStealingPool(_numOfCores);

        // Populate the initial open list
        ScheduleNode schedule = new ScheduleNode(_processors);
        _openList.addAll(schedule.expandTree(_numOfCores));

        while (!_openList.isEmpty()) {
            // pick a node n from O with the best value for f(n).
            ScheduleNode chosenSchedule = _openList.peek();

            if (chosenSchedule == null) {
                continue;
            }

            // check if it is goal state
            if (chosenSchedule.isTarget()) {
                ScheduleNode.threadPoolExecutor.shutdown();
                return chosenSchedule;
            }

            // Execute expandTree function in ScheduleNode class to find children schedules of the current ScheduleNode.
            // Calculate and set the f(n) cost of each child ScheduleNode.
            HashSet<ScheduleNode> childrenOfChosen = new HashSet<>(chosenSchedule.expandTree(_numOfCores));

            // Only add unique schedules to the open list so there are no repeats.
            for (ScheduleNode sn : childrenOfChosen) {
                if (!scheduleNodesHashSet.contains(sn)) {
                    _openList.add(sn);
                    scheduleNodesHashSet.add(sn);
                }
            }

            // Remove from open list after it is expanded.
            _openList.remove(chosenSchedule);
        }
        // return that there's no solution.
        ScheduleNode.threadPoolExecutor.shutdown();
        return new ScheduleNode(_processors);
    }
}
