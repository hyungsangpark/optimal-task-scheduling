package project1.algorithm;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.ScheduleNode;

import java.util.*;

public class Astar {

    public Graph _taskGraph;
    public int _processors;

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
    }
}
//package project1.algorithm;
//
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.SingleGraph;
//import project1.data.ScheduleNode;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//
//public class Astar {
//
//    public Graph _taskGraph;
//    public int _processors;
//    public LinkedList<Node> _taskNodeList = new LinkedList<>();
//    public LinkedList<Node> _closedList = new LinkedList<>();
//
//    public LinkedList<ScheduleNode> _tempOpenList = new LinkedList<>();
//
//    public LinkedList<Node> _initialTasks;
//
//    public Astar(Graph taskGraph, int processors) {
//        _taskGraph = taskGraph;
//        _processors = processors;
//    }
//
//    public void aStarSearch() {
//
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
//
//
//
//    }
//
//
//
//
//
//
//
//    public LinkedList<Node> getChildren(Node node) {
//
//        LinkedList<Node> children = new LinkedList<>();
//
//        for(int i=0; i<node.getOutDegree(); i++) {
//            Node n = node.getEdge(i).getNode1();
//            children.add(n);
//        }
////        for(int i=0; i<node.getOutDegree(); i++) {
////        	System.out.println(children.get(i));
////        }
//        return children;
//    }
//
//
//    //Need to change this, reference from notes: if two or more node schedule has same total schedule time, pick the schedule
//    //with min(scheduleTotalTime + larger edge cost prevented or how close is it to goal state(how many more tasks left))
//    public int findHeuristic(ScheduleNode node) {
//        //Find occurrence of unique names to figure out the amount of tasks not scheduled yet
//        List<List<String>> currentSchedule = node.getSchedule();
//        int count=0;
//
//        //Clone taskNodeList
//        LinkedList<Node> taskNodeList = _taskNodeList;
//
//        //Find heuristics
//        for (int i=0; i<_processors;i++) {
//            List<String> oneProcess = currentSchedule.get(i);
//            HashSet<String> uniqueList = new HashSet(oneProcess);
//            taskNodeList.removeAll(uniqueList);
//        }
//
//        for (Node n: taskNodeList) {
//            String s = n.toString();
//            if (!s.equals("-1")) {
//                int temp = (int) _taskGraph.getNode(s).getAttribute("weight");
//                count = count + temp;
//            }
//        }
//        return count;
//    }
//
//    public List<Node> findRemaindingTasks(ScheduleNode node) {
//        //Find occurrence of unique names to figure out the amount of tasks not scheduled yet
//        List<List<String>> currentSchedule = node.getSchedule();
//
//        //Clone taskNodeList
//        LinkedList<Node> taskNodeList = _taskNodeList;
//
//        //Find heuristics
//        for (int i = 0; i < _processors; i++) {
//            List<String> oneProcess = currentSchedule.get(i);
//            HashSet<String> uniqueList = new HashSet(oneProcess);
//            taskNodeList.removeAll(uniqueList);
//        }
//        return taskNodeList;
//    }
//
//    public LinkedList<Node> findInitialTasks() {
//        LinkedList<Node> initialTasks = new LinkedList<>();
//
//        //Find initial task/tasks by finding tasks with 0 income degree
//        for(int i=0; i<_taskGraph.getNodeCount(); i++) {
//            Node n = _taskGraph.getNode(i);
//            if (n.getInDegree() == 0) {
//                initialTasks.add(n);
//            }
//        }
//        return initialTasks;
//    }
//
//    public Node pickOneInitial() {
//        //pick initial task
//        int minWeight = (int)_initialTasks.get(0).getAttribute("weight");
//        Node task = null;
//        if(_initialTasks.size()!=1) {
//            for (int i = 1; i < _initialTasks.size(); i++) {
//                int temp = (int) _initialTasks.get(i).getAttribute("weight");
//                if (temp < minWeight) {
//                    minWeight = temp;
//                    task = _initialTasks.get(i);
//                }
//            }
//        }
//        else {
//            task = _initialTasks.get(0);
//        }
//        return task;
//    }
//
//    public Node pickOne(List<Node> taskNodeList) {
//
//        return null;
//    }
//
//    //perhaps this function is same as scheduler? or maybe run one function in another?
//    public int findTimeAfterSchedule() {
//        return -1;
//    }
//
//    public void scheduler () {
//
//    }
//
//    //Figure out if there are duplicate schedule nodes or not
//    //(might not need it because of heuristic function)
//    public boolean findDuplicateSchedule() {
//        //something to do with _tempClosedList
//        return false;
//    }
//}
