package project1.algorithm;

import project1.data.ScheduleNode;

import java.util.HashSet;
import java.util.PriorityQueue;

public class AStar {
    public PriorityQueue<ScheduleNode> _openList = new PriorityQueue<>(new PriorityQueueComparator());
    private final HashSet<ScheduleNode> scheduleNodesHashSet = new HashSet<>();
    private final int _processors;

    public AStar(int processors) {
        _processors = processors;
    }

    public ScheduleNode aStarSearch() {
        // Populate the initial open list
        ScheduleNode schedule = new ScheduleNode(_processors);
        _openList.addAll(schedule.expandTree());

        while (!_openList.isEmpty()) {
            // pick a node n from O with the best value for f
            ScheduleNode chosenSchedule = _openList.peek();

            if (chosenSchedule == null) {
                continue;
            }

            // goal state
            if (chosenSchedule.isTarget())
                return chosenSchedule;

            HashSet<ScheduleNode> childrenOfChosen = new HashSet<>(chosenSchedule.expandTree());

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
        return new ScheduleNode(_processors);
    }
}
