package project1.algorithm;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.NewScheduleNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * DFS branch-and-bound algorithm. Creates an optimal schedule for tasks and processors.
 *
 * Author: Dave Shin
 */
public class DFS {
    private static final int END_TIME_IDX = 0; // For indexing the schedule. Index 0 of a schedule is its endTime.
    private static final int IGNORE = -1; // -1 is used as substitute for null and insignificant values.

    private Graph _graph;
    private int _numProcessors;

    public DFS(Graph graph, int numOfProcessors) {
        _graph = graph;
        _numProcessors = numOfProcessors;
    }

    /**
     * Starting point for the DFS branch-and-bound scheduling algorithm.
     *
     * @return _graph - graph with tasks that contains the start times and used processor numbers according to the
     *                  optimal schedule that is created by the algorithm.
     */
    public Graph branchAndBoundStart() {
        Node rootNode = _graph.getNode(0);
        // Index 0 is for the endTime of the schedule and the rest are for the tasks. That is why 1 is added after
        //(int)_graph.nodes().count()
        NewScheduleNode[] schedule = new NewScheduleNode[(int)_graph.nodes().count() + 1];
        NewScheduleNode[] solution;
        NewScheduleNode[] optimalSchedule = new NewScheduleNode[(int)_graph.nodes().count() + 1];
        optimalSchedule[END_TIME_IDX] =
                new NewScheduleNode("endTime", IGNORE, findLeftmostScheduleEndTime(0), IGNORE);
        int size = 1; // Because endTime is at index 0.

        for (int i = 0; i < _numProcessors; i++) {
            schedule[size] = new NewScheduleNode(rootNode.getId(), 0,
                    (int)rootNode.getAttribute("Weight"), i);
            solution = branchAndBound(rootNode, schedule, optimalSchedule, size++);
            if (solution != null) {
                optimalSchedule = solution;
            }
        }

        ScheduleToGraph(optimalSchedule);
        return _graph;
    }

    /**
     * Finds the endTime of the leftmost schedule. Leftmost schedule is the schedule that is the leftmost in the DFS
     * schedule tree.
     *
     * @param endTime - zero as an argument because the weights of the tasks will be added as the method executes.
     * @return endTime
     */
    public int findLeftmostScheduleEndTime(int endTime) {
        for (Node task : (Node[])_graph.nodes().toArray()) {
            endTime += (int)task.getAttribute("Weight");
        }

        return endTime;
    }

    /**
     * Recursive method for DFS branch-and-bound algorithm.
     *
     * @param currentTask - current task node in the task graph.
     * @param currentSchedule - current ScheduleNode in the DFS branch-and-bound tree.
     * @param optimalSchedule - current most optimal schedule the algorithm has found.
     * @param size - number of tasks in currentSchedule.
     * @return optimalSchedule
     */
    // Assume that the nodes are sorted.
    public NewScheduleNode[] branchAndBound(Node currentTask, NewScheduleNode[] currentSchedule,
                                            NewScheduleNode[] optimalSchedule, int size) {
        // Replace the current optimalSchedule with more optimal schedule.
        if (currentTask.getOutDegree() == 0) {
            if (currentSchedule[size - 1].getEndTime() <= optimalSchedule[END_TIME_IDX].getEndTime()) {
                return currentSchedule;
            }
        }

        // Bounding. Return null if currentSchedule is getting slower than optimalSchedule.
        if (currentSchedule[size - 1].getEndTime() >= optimalSchedule[END_TIME_IDX].getEndTime()) {
            return null;
        }

        int startTime;
        int endTime;
        List<String> schedulableTasks = findSchedulableTasks(_graph, currentSchedule);
        NewScheduleNode[] solution;

        // Branching. Based on number of processors and tasks.
        for (int i = 0; i < schedulableTasks.size(); i++) {
            for (int j = 0; j < _numProcessors; j++) {
                if (currentSchedule[size - 1].getProcessorNum() == i) {
                    startTime = currentSchedule[size - 1].getEndTime();
                } else {
                    // I can increase the readability of this part by doing List<Node> schedulableTasks?
                    int waitTime = (int)_graph.getNode(schedulableTasks.get(i)).getEdgeFrom(currentSchedule[size - 1]
                            .getId()).getAttribute("Weight");
                    startTime = currentSchedule[size - 1].getEndTime() + waitTime;
                }
                // Same with this part.
                endTime = startTime + (int)_graph.getNode(schedulableTasks.get(i)).getAttribute("Weight");

                currentSchedule[size] = new NewScheduleNode(currentTask.getId(), startTime, endTime, j);
                solution = branchAndBound(currentTask, currentSchedule, optimalSchedule, size++);
                if (solution != null) {
                    optimalSchedule = solution;
                }

                return optimalSchedule;
            }
        }

        return optimalSchedule;
    }

    /**
     * Returns a List of schedulable tasks for a given schedule.
     *
     * @param graph - Graph representation of the tasks and their orders.
     * @param schedule - current schedule of the scheduled tasks.
     * @return schedulableTasks
     */
    public List<String> findSchedulableTasks(Graph graph, NewScheduleNode[] schedule) {
        List<String> schedulableTasks = new ArrayList<>();

        // get task graph as input ,remove the node in the graph if it is already done? Maybe make a simple data
        // structure that keeps track of which nodes are not scheduled yet?
        for (Node task : graph) {
            boolean areParentsComplete = true;
            List<String> scheduledTasks = getScheduledTasks(schedule);

            // If the task is not already scheduled.
            if (!scheduledTasks.contains(task.getId())) {
                // Check if its parents have already been done.
                Iterator<Edge> iterator = task.enteringEdges().iterator();

                while(iterator.hasNext()) {
                    if (!scheduledTasks.contains(iterator.next().getSourceNode().getId())) {
                        areParentsComplete = false;
                        break;
                    }
                }

                if (areParentsComplete) {
                    schedulableTasks.add(task.getId());
                }
            }
        }

        return schedulableTasks;
    }

    /**
     * Return a List of scheduled tasks in the input schedule.
     *
     * @param schedule
     * @return scheduledTasks
     */
    public List<String> getScheduledTasks(NewScheduleNode[] schedule) {
        List<String> scheduledTasks = new ArrayList<>();

        for (NewScheduleNode task : schedule) {
            scheduledTasks.add(task.getId());
        }

        return scheduledTasks;
    }

    /**
     * Inserts the start times and processor numbers used for the tasks in _graph according to the input schedule.
     *
     * @param schedule - schedule containing the tasks with start times and used processor numbers.
     */
    public void ScheduleToGraph(NewScheduleNode[] schedule) {
        for (int i = 0; i < schedule.length; i++) {
            _graph.getNode(schedule[i].getId()).setAttribute("Start", schedule[i].getStartTime());
            _graph.getNode(schedule[i].getId()).setAttribute("Processor", schedule[i].getProcessorNum());
        }
    }
}
