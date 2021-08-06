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
