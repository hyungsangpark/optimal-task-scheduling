package project1.data;

import java.util.ArrayList;
import java.util.List;

public class DfsScheduleNode {
    private List<List<Character>> _schedule;
    private List<DfsScheduleNode> _children = new ArrayList<>();
    private DfsScheduleNode _parent;
    private int _lastUsedProcessor;
    private int _endTime; // start from here

    public DfsScheduleNode(List<List<Character>> schedule, DfsScheduleNode parent, int lastUsedProcessor,
                        int finishTime) {
        _schedule = schedule;
        _parent = parent;
        _lastUsedProcessor = lastUsedProcessor;
        _endTime = finishTime;
    }

    public void addChildren(int numOfChildren) {
        for (int i = 0; i < numOfChildren; i++) {
            System.out.println("Do something");
        }
    }

    public int getLastUsedProcessorNum() {
        return _lastUsedProcessor;
    }

    public List<List<Character>> getSchedule() {
        return _schedule;
    }

    public int getFinishTime() {
        return _endTime;
    }
}
