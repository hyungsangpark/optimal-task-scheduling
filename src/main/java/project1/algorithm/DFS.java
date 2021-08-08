package project1.algorithm;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.NewScheduleNode;

import java.util.*;

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

    /**
     * Starting point for the DFS branch-and-bound scheduling algorithm.
     *
     * @return _graph - graph with tasks that contains the start times and used processor numbers according to the
     *                  optimal schedule that is created by the algorithm.
     */
    public Graph branchAndBoundStart() {
        if (_graph.nodes().count() == 0) {
            return _graph;
        }

        NewScheduleNode[] optimalSchedule = findLeftmostSchedule();
        NewScheduleNode[] solution;
        Node rootNode = getStartNode();
        int size = 0;

        for (int i = 0; i < _numProcessors; i++) {
            NewScheduleNode[] schedule = new NewScheduleNode[(int)_graph.nodes().count()];
            schedule[size] = new NewScheduleNode(rootNode.getId(), 0,
                    (int)rootNode.getAttribute("Weight"), i);
            solution = branchAndBound(schedule, optimalSchedule, size + 1);
            if (solution != null) {
                optimalSchedule = solution;
            }
        }

        ScheduleToGraph(optimalSchedule);
        return _graph;
    }

    public Node getStartNode() {
        for (Object task : _graph.nodes().toArray()) {
            if (((Node)task).getInDegree() == 0) {
                return (Node)task;
            }
        }

        return null;
    }

    /**
     * Finds the leftmost schedule. Leftmost schedule is the schedule that is the leftmost in the DFS schedule tree.
     *
     * @return leftmostScheduleNode
     */
    public NewScheduleNode[] findLeftmostSchedule() {
        NewScheduleNode[] leftmostSchedule = new NewScheduleNode[(int)_graph.nodes().count()];
        int idx = 0;
        int startTime = 0;

        for (Object taskObject : _graph.nodes().toArray()) {
            Node task = (Node)taskObject;
            leftmostSchedule[idx] = new NewScheduleNode(task.getId(), startTime, startTime
                    + (int)task.getAttribute("Weight"), 0);
            startTime += (int)task.getAttribute("Weight");
            idx++;
        }

        return leftmostSchedule;
    }

    /**
     * Recursive method for DFS branch-and-bound algorithm.
     *
     * @param currentSchedule - current ScheduleNode in the DFS branch-and-bound tree.
     * @param optimalSchedule - current most optimal schedule the algorithm has found.
     * @param size - number of tasks in currentSchedule.
     * @return optimalSchedule
     */
    // Assume that the nodes are sorted.
    public NewScheduleNode[] branchAndBound(NewScheduleNode[] currentSchedule, NewScheduleNode[] optimalSchedule,
                                            int size) {
        // Replace the current optimalSchedule with more optimal schedule when a schedule is finished creating. Return
        // null if not optimal.
        if (size == _graph.getNodeCount()) {
            if (currentSchedule[size - 1].getEndTime() < optimalSchedule[optimalSchedule.length - 1].getEndTime()) {
                return currentSchedule;
            }

            return null;
        }

        // Bounding. Return null if currentSchedule is getting slower than optimalSchedule.
        if (currentSchedule[size - 1].getEndTime() >= optimalSchedule[optimalSchedule.length - 1].getEndTime()) {
            return null;
        }

        int startTime;
        int endTime;
        List<String> schedulableTasks = findSchedulableTasks(_graph, currentSchedule);
        NewScheduleNode[] solution;

        // Branching. Based on number of processors and tasks.
        for (String task : schedulableTasks) {
            for (int i = 0; i < _numProcessors; i++) {
                startTime = calculateStartTime(arrayToHashMap(currentSchedule), getScheduledTasks(currentSchedule),
                        task, i);
                endTime = startTime + (int)_graph.getNode(task).getAttribute("Weight");

                NewScheduleNode[] nextSchedule = currentSchedule.clone();
                nextSchedule[size] = new NewScheduleNode(task, startTime, endTime, i);
                solution = branchAndBound(nextSchedule, optimalSchedule, size + 1);
                if (solution != null) {
                    optimalSchedule = solution;
                }
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
                for (Object edge : task.enteringEdges().toArray()) {
//                    if (!scheduledTasks.contains(task.))
//                }
//                        bject edge : task.enteringEdges().toArray()) {
                    if (!scheduledTasks.contains(((Edge)edge).getSourceNode().getId())) {
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
            if (task == null) { // use -1 instead?
                break;
            }

            scheduledTasks.add(task.getId());
        }

        return scheduledTasks;
    }

    // I can increase the readability of this part by doing List<Node> schedulableTasks?
    public int calculateStartTime(HashMap<String, NewScheduleNode> schedule, List<String> scheduledTasks, String task,
                                  int processor) {
        int tempStartTime;
        int maxStartTime = 0;
        for (int i = scheduledTasks.size() - 1; i >= 0; i--) {
            if (schedule.get(scheduledTasks.get(i)).getProcessorNum() == processor) {
                maxStartTime = schedule.get(scheduledTasks.get(i)).getEndTime();
                break;
            }
        }
        NewScheduleNode prevTask;

        for (String scheduledTask : scheduledTasks) {
            if (_graph.getNode(scheduledTask).hasEdgeToward(task)) { // Change the code so that we don't have to traverse through _graph? e.g. Include the parents information in NewScheduleNode.
                prevTask = schedule.get(scheduledTask);
                if (prevTask.getProcessorNum() == processor) {
                    tempStartTime = prevTask.getEndTime();
                } else {
                    tempStartTime = prevTask.getEndTime() + (int)_graph.getNode(scheduledTask)
                            .getEdgeToward(_graph.getNode(task)).getAttribute("Weight");
                }

                if (tempStartTime > maxStartTime) {
                    maxStartTime = tempStartTime;
                }
            }
        }

        return maxStartTime;
    }

    public HashMap<String, NewScheduleNode> arrayToHashMap(NewScheduleNode[] schedule) {
        HashMap<String, NewScheduleNode> map = new HashMap<>();

        for (NewScheduleNode task : schedule) {
            if (task == null) {
                break;
            }

            map.put(task.getId(), task);
        }

        return map;
    }

    /**
     * Inserts the start times and processor numbers used for the tasks in _graph according to the input schedule.
     *
     * @param schedule - schedule containing the tasks with start times and used processor numbers.
     */
    public void ScheduleToGraph(NewScheduleNode[] schedule) {
        for (NewScheduleNode task : schedule) {
            _graph.getNode(task.getId()).setAttribute("Start", task.getStartTime());
            _graph.getNode(task.getId()).setAttribute("Processor", task.getProcessorNum());
        }
    }
}
