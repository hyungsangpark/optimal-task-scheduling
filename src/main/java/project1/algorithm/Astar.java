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
    public LinkedList<Node> _closedList = new LinkedList<>();

    public LinkedList<ScheduleNode> _tempOpenList = new LinkedList<>();

    public LinkedList<Node> _initialTasks;

    public Astar(Graph taskGraph, int processors) {
        _taskGraph = taskGraph;
        _processors = processors;
    }

    public void aStarSearch() {

        //Initialise taskNodeList
        for (int i=0; i<_taskGraph.getNodeCount();i++) {
            _taskNodeList.add(_taskGraph.getNode(i));
        }

        //Find initial task/tasks
        _initialTasks = findInitialTasks();

        //Pick one initial task from _initialTasks based on minimum weight of the task node
        Node task = pickOneInitial();

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
        //Add node represents ScheduleNode to scheduleGraph

        //Heuristics of the node
        root.setHeuristics(findHeuristic(root));



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
        //System.out.println(_closedList);
    }

    public LinkedList<Node> getChildren(Node node) {

        LinkedList<Node> children = new LinkedList<>();

        for(int i=0; i<node.getOutDegree(); i++) {
            Node n = node.getEdge(i).getNode1();
            children.add(n);
        }
//        for(int i=0; i<node.getOutDegree(); i++) {
//        	System.out.println(children.get(i));
//        }
        return children;
    }


    //Need to change this, reference from notes: if two or more node schedule has same total schedule time, pick the schedule
    //with min(scheduleTotalTime + larger edge cost prevented or how close is it to goal state(how many more tasks left))
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

    public List<Node> findRemaindingTasks(ScheduleNode node) {
        //Find occurrence of unique names to figure out the amount of tasks not scheduled yet
        List<List<String>> currentSchedule = node.getSchedule();

        //Clone taskNodeList
        LinkedList<Node> taskNodeList = _taskNodeList;

        //Find heuristics
        for (int i = 0; i < _processors; i++) {
            List<String> oneProcess = currentSchedule.get(i);
            HashSet<String> uniqueList = new HashSet(oneProcess);
            taskNodeList.removeAll(uniqueList);
        }
        return taskNodeList;
    }

    public LinkedList<Node> findInitialTasks() {
        LinkedList<Node> initialTasks = new LinkedList<>();

        //Find initial task/tasks by finding tasks with 0 income degree
        for(int i=0; i<_taskGraph.getNodeCount(); i++) {
            Node n = _taskGraph.getNode(i);
            if (n.getInDegree() == 0) {
                initialTasks.add(n);
            }
        }
        return initialTasks;
    }

    public Node pickOneInitial() {
        //pick initial task
        int minWeight = (int)_initialTasks.get(0).getAttribute("weight");
        Node task = null;
        if(_initialTasks.size()!=1) {
            for (int i = 1; i < _initialTasks.size(); i++) {
                int temp = (int) _initialTasks.get(i).getAttribute("weight");
                if (temp < minWeight) {
                    minWeight = temp;
                    task = _initialTasks.get(i);
                }
            }
        }
        else {
            task = _initialTasks.get(0);
        }
        return task;
    }

    public Node pickOne(List<Node> taskNodeList) {

        return null;
    }

    //perhaps this function is same as scheduler? or maybe run one function in another?
    public int findTimeAfterSchedule() {
        return -1;
    }

    public void scheduler () {

    }

    //Figure out if there are duplicate schedule nodes or not
    //(might not need it because of heuristic function)
    public boolean findDuplicateSchedule() {
        //something to do with _tempClosedList
        return false;
    }
}
