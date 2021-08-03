package main.java.project1.algorithm;
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

    // Assume that the nodes are sorted.
    public void branchAndBound() {
        List<List<Character>> processors = new ArrayList<>();
        for (int i = 0; i < _numOfProcessors; i++) {
            processors.add(new ArrayList<>());
        }

        Node node;

        for (int i = 0; i < _graph.getNodeCount(); i++) {
            node = _graph.getNode(i);

            for (int j = 0; j < _numOfProcessors; j++) {
                for (int k = 0; k < node.getOutDegree(); k++) {

                }
            }
        }

        for (int i = 0; i < _numOfProcessors; i++) {
            for (int j = 0; j <)
        }

        ScheduleNode root = new ScheduleNode(processors, tasks, null);

    }

    // carry on by making schedules then adding to the arraylist. Another good way to carry on will be to research how
    // to use the Graph class to do everything.
}
