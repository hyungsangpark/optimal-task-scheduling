package project1.algorithm;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import project1.data.ScheduleNode;

import java.util.*;

public class Astar {

    public Graph _taskGraph;
    public int _processors;
//    public LinkedList<Node> _taskNodeList = new LinkedList<>();
//    public LinkedList<Node> _openList;

    public PriorityQueue<ScheduleNode> _tempOpenList = new PriorityQueue<>(new PriorityQueueComparator());

    public LinkedList<Node> _initialTasks;

    public Astar(Graph taskGraph, int processors) {
        _taskGraph = taskGraph;
        _processors = processors;
    }

    public ScheduleNode aStarSearch() {
//        make a list O of open nodes and their respective f values containing the start node

        // create and add root to open
        ScheduleNode root = new ScheduleNode(_processors);
        _tempOpenList.add(root);

//        while O isn't empty:
        while(!_tempOpenList.isEmpty()) {
//          pick a node n from O with the best value for f
            ScheduleNode chosenSchedule = _tempOpenList.peek();

//          if n is target:
            if (chosenSchedule.isTarget(_taskGraph)) {
                // return solution
                return chosenSchedule;
            }

            // expand chosenSchedule
            List<ScheduleNode> childrenOfChosen = chosenSchedule.expandTree(_taskGraph);

//          for every m which is a neighbor of n:
            for (ScheduleNode sn : childrenOfChosen) {
                _tempOpenList.add(sn);
            }

            _tempOpenList.remove(chosenSchedule);
        }
//
//        return that there's no solution

        return null;

//        //Initialise taskNodeList
//        for (int i=0; i<_taskGraph.getNodeCount();i++) {
//            _taskNodeList.add(_taskGraph.getNode(i));
//        }
//
//        //Find initial task/tasks
//        _initialTasks = findInitialTasks();
//
//        //Pick one initial task from _initialTasks based on minimum weight of the task node
//        Node task = pickOneInitial();
//
//        //Create a graph for schedule nodes, create List to hold the partial schedules
//        Graph scheduleGraph = new SingleGraph("schedule");
//        List<List<String>> schedule = new ArrayList<>();
//        for (int i=0; i<_processors; i++) {
//            List<String> oneProcess = new ArrayList<>();
//            schedule.add(oneProcess);
//        }
//
//        //Create root node(empty schedule)
//        ScheduleNode root = new ScheduleNode(scheduleGraph, schedule, "root");
//        _tempOpenList.add(root);
//        //Add node represents ScheduleNode to scheduleGraph
//
//        //Heuristics of the node
//        root.setHeuristics(findHeuristic(root));
//
//        ArrayList<String> edgeWeights = new ArrayList<String>();
//
//        // Expand tree
//        expandTree(scheduleGraph,root,_initialTasks,edgeWeights);
//
//        Node chosen;
//
//        Node node = _taskGraph.getNode(0);
//        _closedList.add(node);
//        LinkedList<Node> childrenList = getChildren(node);
//
//        //if (!childrenList.isEmpty()) {
//        chosen = childrenList.get(0);
//        int nodeWeight = (int)((double)chosen.getAttribute("Weight"));
//        String edge = "("+node.toString()+";"+ chosen+")";
//        int totalWeight = nodeWeight + (int)((double)_taskGraph.getEdge(edge).getAttribute("Weight"));
//        //}
//
//        for(int i=0; i<childrenList.size() - 1; i++) {
//            Node next = childrenList.get(i+1);
//            String nextEdge = "("+node +";"+ next.toString()+")";
//            int nextTotalWeight = (int)((double)node.getAttribute("Weight")) + (int)((double)_taskGraph.getEdge(nextEdge).getAttribute("Weight"));
//            if (nextTotalWeight < totalWeight) {
//                chosen = next;
//            }
//        }
//        _closedList.add(chosen);
//        //System.out.println(_closedList);
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
        int minWeight = (int)((double)_initialTasks.get(0).getAttribute("Weight"));
        Node task = null;
        if(_initialTasks.size()!=1) {
            for (int i = 1; i < _initialTasks.size(); i++) {
                int temp = (int)((double)_initialTasks.get(i).getAttribute("Weight"));
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
}
