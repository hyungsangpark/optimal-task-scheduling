package project1.data;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class ScheduleNode {
    private Node _self;
    private List<List<String>> _schedule;
    private List<ScheduleNode> _children = new ArrayList<>();
    private ScheduleNode _parent;
    private String _name;
    private Graph _graph;
    private int _heuristics;

    //For normal schedule nodes
    public ScheduleNode(Graph graph, List<List<String>> schedule, ScheduleNode parent, String name) {
        _graph = graph;
        _schedule = schedule;
        _parent = parent;
        _name = name;
        _self = graph.addNode(name);
    }

    //For root schedule node(node with no tasks assigned)
    public ScheduleNode(Graph graph, List<List<String>> schedule, String name) {
        _graph = graph;
        _schedule = schedule;
        _name = name;
        _self = graph.addNode(name);
    }

    public void addChildren(int numOfChildren) {
        for (int i = 0; i < numOfChildren; i++) {
            System.out.println("Do something");
        }
    }

    public List<List<String>> getSchedule() {
        return _schedule;
    }

    public String getScheduleNodeName() {
        return _self.toString();
    }

    public void setHeuristics(int num) {
        _heuristics = num;
    }
}
