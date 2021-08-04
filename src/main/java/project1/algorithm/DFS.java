package project1.algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.ScheduleNode;

import java.util.ArrayList;
import java.util.List;

public class DFS {
    private Graph _graph;
    private int _numOfProcessors;

    public DFS(Graph graph, int numOfProcessors) {
        _graph = graph;
        _numOfProcessors = numOfProcessors;
    }

    public void branchAndBoundStart() {
        Node node = _graph.getNode(0);
    }

    // Assume that the nodes are sorted.
    public void branchAndBound(ScheduleNode current, Node currentNode) {
        List<List<Character>> schedule = new ArrayList<>();
        for (int i = 0; i < _numOfProcessors; i++) {
            schedule.add(new ArrayList<>());
        }

        Node child;

        for (int i = 0; i < _numOfProcessors; i++) {
            for (int j = 0; j < currentNode.getOutDegree(); j++) {
                child = currentNode.getEdge(j).getTargetNode(); // resume from here. What about the weights?
                List<List<Character>> childSchedule = addTask(current.getSchedule(), i, (char)child.getAttribute("Name"), (int)child.getAttribute("Weight"));
                branchAndBound(new ScheduleNode(_graph, childSchedule, current, current.getScheduleNodeName()), child);
            }
        }

        // now think about how to do the bounding
    }

    public List<List<Character>> addTask(List<List<Character>> schedule, int processorNum, char task, int weight) {
        for (int i = 0; i < weight; i++) {
            schedule.get(processorNum).add(task);
        }

        return schedule;
    }
}
