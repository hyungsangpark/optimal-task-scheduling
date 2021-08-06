package project1.algorithm;

import project1.data.ScheduleNode;
import java.util.Comparator;

public class PriorityQueueComparator implements Comparator<ScheduleNode> {
    public int compare(ScheduleNode sn1, ScheduleNode sn2) {
        if (sn1.get_totalF() < sn2.get_totalF()) {
            return -1;
        } else
            return 1;
        }
}

