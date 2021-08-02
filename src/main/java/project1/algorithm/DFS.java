package main.java.project1.algorithm;
import org.graphstream.graph.Graph;
import project1.data.Node;

import java.util.ArrayList;
import java.util.List;

public class DFS {
    private Graph _graph;
    private int _numOfProcessors;

    public DFS(Graph graph, int numOfProcessors) {
        _graph = graph;
        _numOfProcessors = numOfProcessors;
    }

    public void branchAndBound() {
        List<List<Character>> processors = new ArrayList<>();
        for (int i = 0; i < _numOfProcessors; i++) {
            processors.add(new ArrayList<>());
        }

        Node root = new Node(processors, tasks, numOfChildren, null);

    }

    // carry on by making schedules then adding to the arraylist. Another good way to carry on will be to research how
    // to use the Graph class to do everything.
}
