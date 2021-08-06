package project1.algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.DfsScheduleNode;
import project1.data.NewScheduleNode;

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
        Node rootNode = _graph.getNode(0);
        NewScheduleNode[] schedule = new NewScheduleNode[(int)_graph.nodes().count()];
        NewScheduleNode[] optimalSchedule = findLeftmostSchedule();
        int size = 0;

        for (int i = 0; i < _numOfProcessors; i++) {
            schedule[0] = new NewScheduleNode(0, (int)rootNode.getAttribute("Weight"), i);
            branchAndBound(rootNode, schedule, optimalSchedule, size++);
        }
    }

    public NewScheduleNode[] findLeftmostSchedule() {
        int startTime;
        int endTime;
        int size = 0;
        Node currentNode = _graph.getNode(0);
        NewScheduleNode[] schedule = new NewScheduleNode[(int)_graph.nodes().count()];

        schedule[0] = new NewScheduleNode(0, (int)currentNode.getAttribute("Weight"), 0);
        size++;
        currentNode = currentNode.getEdgeToward(0).getTargetNode();

        while (currentNode.getOutDegree() != 0) {
            startTime = schedule[size - 1].getEndTime();
            endTime = startTime + (int)currentNode.getAttribute("Weight");

            schedule[size] = new NewScheduleNode(startTime, endTime, 0);

            currentNode = currentNode.getEdgeToward(0).getTargetNode();
            size++;
        }

        startTime = schedule[size - 1].getEndTime();
        endTime = startTime + (int)currentNode.getAttribute("Weight");
        schedule[size] = new NewScheduleNode(startTime, endTime, 0);

        return schedule;
    }

    // Assume that the nodes are sorted.
    public NewScheduleNode[] branchAndBound(Node currentNode, NewScheduleNode[] currentSchedule,
                                            NewScheduleNode[] optimalSchedule, int size) {
        if (currentNode.getOutDegree() == 0) {
            if (currentSchedule[currentSchedule.length - 1].getEndTime()
                    < optimalSchedule[optimalSchedule.length - 1].getEndTime()) {
                return currentSchedule;
            }
        }

        // Bounding.
        if (currentSchedule[currentSchedule.length - 1].getEndTime()
                >= optimalSchedule[optimalSchedule.length - 1].getEndTime()) {
            return null;
        }

        Node childNode;
        Edge edge;
        int startTime;
        int endTime;
        NewScheduleNode[] solution;

        // Branching.
        for (int i = 0; i < _numOfProcessors; i++) {
            for (int j = 0; j < currentNode.getOutDegree(); j++) {
                edge = currentNode.getEdgeToward(j);
                childNode = edge.getTargetNode();

                if (currentSchedule[currentSchedule.length - 1].getProcessorNum() == i) {
                    startTime = currentSchedule[currentSchedule.length - 1].getEndTime();
                } else {
                    startTime = currentSchedule[currentSchedule.length - 1].getEndTime()
                            + (int)edge.getAttribute("Weight");;
                }
                endTime = startTime + (int)childNode.getAttribute("Weight");

                currentSchedule[currentSchedule.length] = new NewScheduleNode(startTime, endTime, i);
                solution = branchAndBound(childNode, currentSchedule, optimalSchedule, size++);
                if (solution != null) {
                    optimalSchedule = solution;
                }
            }
        }

        return optimalSchedule;
    }

    /*
    public List<List<String>> addTask(List<List<String>> schedule, int processorNum, String task,
                                         int waitTime, int weight) {
        for (int i = 0; i < waitTime; i++) {
            schedule.get(processorNum).add(null);
        }

        for (int i = 0; i < weight; i++) {
            schedule.get(processorNum).add(task);
        }
        return schedule;
    }

    public List<List<String>> createEmptySchedule() {
        List<List<String>> emptySchedule = new ArrayList<>();
        for (int i = 0; i < _numOfProcessors; i++) {
            emptySchedule.add(new ArrayList<>());
        }

        return emptySchedule;
    }
    */
}
