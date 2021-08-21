package project1.algorithm;

import project1.data.ScheduleNode;

import java.util.Comparator;

/**
 * Comparator to compare all ScheduleNodes in the priority queues based on the f(n) cost
 */

public class PriorityQueueComparator implements Comparator<ScheduleNode> {
    public int compare(ScheduleNode sn1, ScheduleNode sn2) {
        if (sn1.getFCost() > sn2.getFCost()) {
            return 1;
        } else if (sn1.getFCost() < sn2.getFCost()) {
            return -1;
        } else {
            if (sn1.getTasksInScheduleNode().size() <= sn2.getTasksInScheduleNode().size()) {
                return 1;
            }
            else {
                return -1;
            }
        }

    }
}
