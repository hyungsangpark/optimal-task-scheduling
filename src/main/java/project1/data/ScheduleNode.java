package project1.data;

import java.util.ArrayList;
import java.util.List;

public class ScheduleNode {
    private List<List<Character>> _schedule;
    private List<ScheduleNode> _children = new ArrayList<>();
    private ScheduleNode _parent;

    public ScheduleNode(List<List<Character>> schedule, ScheduleNode parent) {
        _schedule = schedule;
        _parent = parent;
    }

    public void addChildren(int numOfChildren) {
        for (int i = 0; i < numOfChildren; i++) {
            System.out.println("Do something");
        }
    }

    public List<List<Character>> getSchedule() {
        return _schedule;
    }
}
