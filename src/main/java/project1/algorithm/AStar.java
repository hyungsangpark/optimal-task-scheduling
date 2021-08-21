package project1.algorithm;

import project1.data.ScheduleNode;
import project1.gui.SolveAlgorithm;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;

public class AStar {
    public PriorityQueue<ScheduleNode> _openList = new PriorityQueue<>(new PriorityQueueComparator());
    private final HashSet<ScheduleNode> scheduleNodesHashSet = new HashSet<>();
    private final int _processors;
    private final int _numOfCores;
    private SolveAlgorithm _caller;

    // Constructor for non-visualised
    public AStar(int processors, int numOfCores) {
        _processors = processors;
        _numOfCores = numOfCores;
    }

    // Constructor for visualised
    public AStar(int processors, int numOfCores, SolveAlgorithm caller) {
        _processors = processors;
        _numOfCores = numOfCores;
        _caller = caller;
    }

    public ScheduleNode aStarSearch() {
        ScheduleNode.threadPoolExecutor = Executors.newWorkStealingPool(_numOfCores);

        // Populate the initial open list
        ScheduleNode schedule = new ScheduleNode(_processors);
        _openList.addAll(schedule.expandTree(_numOfCores));

        while (!_openList.isEmpty()) {
            // pick a node n from O with the best value for f
            ScheduleNode chosenSchedule = _openList.peek();

            if (chosenSchedule == null) {
                continue;
            }

            if (_caller != null) {
                _caller.updateSchedule(chosenSchedule.getScheduleMap());
            }

            // goal state
            if (chosenSchedule.isTarget()) {
                ScheduleNode.threadPoolExecutor.shutdown();
                return chosenSchedule;
            }

            HashSet<ScheduleNode> childrenOfChosen = new HashSet<>(chosenSchedule.expandTree(_numOfCores));

            // calculate and set the cost of each one

            // Only add unique schedules to the open list so no repeats
            for (ScheduleNode sn : childrenOfChosen) {
                if (!scheduleNodesHashSet.contains(sn)) {
                    _openList.add(sn);
                    scheduleNodesHashSet.add(sn);
                }
            }

            _openList.remove(chosenSchedule);
        }
        // return that there's no solution
        ScheduleNode.threadPoolExecutor.shutdown();
        return new ScheduleNode(_processors);
    }
}
