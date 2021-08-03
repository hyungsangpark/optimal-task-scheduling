package project1.algorithm;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import project1.data.ScheduleNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Astar {

    public Graph _taskGraph;
    public int _processors;
    public LinkedList<Node> _taskNodeList = new LinkedList<>();
    public LinkedList<Node> _openList = new LinkedList<>();
    public LinkedList<Node> _closedList = new LinkedList<>();

    public LinkedList<ScheduleNode> _tempOpenList = new LinkedList<>();
    public LinkedList<ScheduleNode> _tempClosedList = new LinkedList<>();

    public Astar(Graph taskGraph, int processors) {
        _taskGraph = taskGraph;
        _processors = processors;
    }

    public void aStarSearch() {

        //Initialise taskNodeList
        for (int i=0; i<_taskGraph.getNodeCount();i++) {
            _taskNodeList.add(_taskGraph.getNode(i));
        }

        //Create a graph for schedule nodes, create List to hold the partial schedules
        Graph scheduleGraph = new SingleGraph("schedule");
        List<List<String>> schedule = new ArrayList<>();
        for (int i=0; i<_processors; i++) {
            List<String> oneProcess = new ArrayList<>();
            schedule.add(oneProcess);
        }

        //Create root node(empty schedule)
        ScheduleNode root = new ScheduleNode(scheduleGraph, schedule, "root");
        _tempOpenList.add(root);
        //Heuristics of the node
        root.setHeuristics(findHeuristic(root));
        System.out.println(findHeuristic(root));

        Node chosen;

        Node node = _taskGraph.getNode(0);
        _closedList.add(node);
        LinkedList<Node> childrenList = getChildren(node);

        //if (!childrenList.isEmpty()) {
        chosen = childrenList.get(0);
        int nodeWeight = (int) chosen.getAttribute("weight");
        String edge = node.toString() + chosen;
        int totalWeight = nodeWeight + (int) _taskGraph.getEdge(edge).getAttribute("weight");
        //}

        for(int i=0; i<childrenList.size() - 1; i++) {
            Node next = childrenList.get(i+1);
            String nextEdge = node + next.toString();
            int nextTotalWeight = (int) node.getAttribute("weight") + (int) _taskGraph.getEdge(nextEdge).getAttribute("weight");
            if (nextTotalWeight < totalWeight) {
                chosen = next;
            }
        }
        _closedList.add(chosen);
        System.out.println(_closedList);
    }

    public LinkedList<Node> getChildren(Node node) {

        LinkedList<Node> children = new LinkedList<>();

        for(int i=0; i<node.getOutDegree(); i++) {
            Node n = node.getEdge(i).getNode1();
            children.add(n);
        }
        //for(int i=0; i<node.getOutDegree(); i++) {
        //	System.out.println(children.get(i));
        //}
        return children;
    }

    public int findHeuristic(ScheduleNode node) {
        //Find occurrence of unique names to figure out the amount of tasks not scheduled yet
        List<List<String>> currentSchedule = node.getSchedule();
        int count=0;

        //Clone taskNodeList
        LinkedList<Node> taskNodeList = _taskNodeList;

        //Find heuristics
        for (int i=0; i<_processors;i++) {
            List<String> oneProcess = currentSchedule.get(i);
            HashSet<String> uniqueList = new HashSet(oneProcess);
            taskNodeList.removeAll(uniqueList);
        }

        for (Node n: taskNodeList) {
            String s = n.toString();
            if (!s.equals("-1")) {
                int temp = (int) _taskGraph.getNode(s).getAttribute("weight");
                count = count + temp;
            }
        }
        return count;
    }
}
