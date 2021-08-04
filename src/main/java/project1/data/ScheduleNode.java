package project1.data;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.List;

public class ScheduleNode {

    private Node _self;
    private List<List<Character>> _schedule;
    private List<ScheduleNode> _children = new ArrayList<>();
    private ScheduleNode _parent;
    private String _name;
    private Graph _graph;
    private int _heuristics;
    private int _totalScheduleTime = 0;

    public ScheduleNode(Graph graph, List<List<Character>> schedule, ScheduleNode parent, String name) {
        _graph = graph;
        _schedule = schedule;
        _parent = parent;
        _name = name;
        _self = _graph.addNode(name);
    }

    public void addChildren(int numOfChildren, Node node) {
        for (int i = 0; i< numOfChildren; i++){
            if (node.getDegree() == 0) {
                System.out.println("No children found");
            } else {
                System.out.println("Children found");
            }
        }
    }

    public List<List<Character>> getSchedule() {
        return _schedule;
    }

    public String getScheduleNodeName() {
        return _name;
    }

    public void setHeuristics(int num) {
        _heuristics = num;
    }

    public void set_totalScheduleTime(int time) {
        _totalScheduleTime = _totalScheduleTime + time;
    }

    public Node getScheduleNodeReference() {
        return _self;
    }

    public Graph getGraphForNextScheduleNode() {
        return _graph;
    }
}