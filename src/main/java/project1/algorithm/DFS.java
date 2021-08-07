package project1.algorithm;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.NewScheduleNode;

/**
 * DFS branch-and-bound algorithm. Creates an optimal schedule for tasks and processors.
 *
 * Author: Dave Shin
 */
public class DFS {
    private Graph _graph;
    private int _numProcessors;

    public DFS(Graph graph, int numOfProcessors) {
        _graph = graph;
        _numProcessors = numOfProcessors;
    }

    public Graph branchAndBoundStart() {
        Node rootNode = _graph.getNode(0);
        NewScheduleNode[] schedule = new NewScheduleNode[(int)_graph.nodes().count()];
        NewScheduleNode[] optimalSchedule = findLeftmostSchedule();
        NewScheduleNode[] solution;
        int size = 0;

        for (int i = 0; i < _numProcessors; i++) {
            schedule[0] = new NewScheduleNode(rootNode.getId(), 0,
                    (int)rootNode.getAttribute("Weight"), i);
            solution = branchAndBound(rootNode, schedule, optimalSchedule, size++);
            if (solution != null) {
                optimalSchedule = solution;
            }
        }

        ScheduleToGraph(optimalSchedule);
        return _graph;
    }

    public NewScheduleNode[] findLeftmostSchedule(Node currentNode, NewScheduleNode[] schedule, int size) {
        int startTime;
        int endTime;
        Node childNode;
        Object[] edges = currentNode.leavingEdges().toArray();

        for (Object edge : edges) {
            childNode = ((Edge)edge).getTargetNode();

            startTime = schedule[size - 1].getEndTime();
            endTime = startTime + (int)childNode.getAttribute("Weight");

            schedule[size] = new NewScheduleNode(currentNode.getId(), startTime, endTime, 0);
            schedule = findLeftmostSchedule(childNode, schedule, size++);
        }

        return schedule;
    }

    /**
     * Recursive method for DFS branch-and-bound algorithm.
     *
     * @param currentTask - current task node in the task graph.
     * @param currentSchedule - current ScheduleNode in the DFS branch-and-bound tree.
     * @param optimalSchedule - current most optimal schedule the algorithm has found.
     * @param size - current number of tasks in the optimal schedule.
     * @return optimalSchedule
     */
    // Assume that the nodes are sorted.
    public NewScheduleNode[] branchAndBound(Node currentTask, NewScheduleNode[] currentSchedule,
                                            NewScheduleNode[] optimalSchedule, int size) {
        if (currentTask.getOutDegree() == 0) {
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

        // Branching.
        for (int i = 0; i < _numProcessors; i++) {
            optimalSchedule = Branch(currentTask, currentSchedule, optimalSchedule, size);
        }

        return optimalSchedule;
    }

    public NewScheduleNode[] Branch(Node currentNode, NewScheduleNode[] currentSchedule,
                       NewScheduleNode[] optimalSchedule, int size) {
        Node childNode;
        Edge edge;
        int startTime;
        int endTime;
        NewScheduleNode[] solution;

        for (int i = 0; i < currentNode.getOutDegree(); i++) {
            edge = currentNode.getEdgeToward(i);
            childNode = edge.getTargetNode();

            if (currentSchedule[currentSchedule.length - 1].getProcessorNum() == i) {
                startTime = currentSchedule[currentSchedule.length - 1].getEndTime();
            } else {
                startTime = currentSchedule[currentSchedule.length - 1].getEndTime()
                        + (int)edge.getAttribute("Weight");;
            }
            endTime = startTime + (int)childNode.getAttribute("Weight");

            currentSchedule[currentSchedule.length] =
                    new NewScheduleNode(currentNode.getId(), startTime, endTime, i);
            solution = branchAndBound(childNode, currentSchedule, optimalSchedule, size++);
            if (solution != null) {
                optimalSchedule = solution;
            }
        }

        return optimalSchedule;
    }

    public void ScheduleToGraph(NewScheduleNode[] schedule) {
        for (int i = 0; i < schedule.length; i++) {
            _graph.getNode(schedule[i].getId()).setAttribute("Start", schedule[i].getStartTime());
            _graph.getNode(schedule[i].getId()).setAttribute("Processor", schedule[i].getProcessorNum());
        }
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
