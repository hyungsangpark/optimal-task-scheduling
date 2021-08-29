package project1.algorithm.dfs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

/**
 * DFS branch-and-bound algorithm. Creates an optimal schedule for tasks and processors.
 *
 * Author: Dave Shin
 */
class DFS {
    private static final int NOT_USED = -1;
    private static final String END_TIME_INDICATOR = "schedule endtime";

    private Graph _graph;
    private int _numProcessors;
    private int _endTimeIdx;

    public DFS(Graph graph, int numOfProcessors) {
        _graph = graph;
        _numProcessors = numOfProcessors;
        _endTimeIdx = (int)_graph.nodes().count(); // The last element of the schedule array only stores the
                                                   // endTime of the whole schedule.
    }

    /**
     * Starting point for the DFS branch-and-bound scheduling algorithm.
     *
     * @return _graph - graph with tasks that contains the start times and used processor numbers according to the
     *                  optimal schedule that is created by the algorithm.
     */
    public Graph branchAndBoundStart() {
        if (_endTimeIdx == 0) {
            return _graph;
        }

        NewScheduleNode[] optimalSchedule = findLeftmostSchedule(new NewScheduleNode[_endTimeIdx + 1]);
        NewScheduleNode[] solution;
        Node rootNode = getStartNode();
        int size = 0;

        for (int i = 0; i < _numProcessors; i++) {
            NewScheduleNode[] schedule = new NewScheduleNode[_endTimeIdx + 1];
            schedule[size] = new NewScheduleNode(rootNode.getId(), 0,
                    (int)rootNode.getAttribute("Weight"), i);
            schedule[_endTimeIdx] = new NewScheduleNode(END_TIME_INDICATOR, NOT_USED,
                                                        schedule[size].getEndTime(), NOT_USED);

            solution = branchAndBound(schedule, optimalSchedule, size + 1);
            if (solution != null) {
                optimalSchedule = solution;
            }
        }

        ScheduleToGraph(optimalSchedule);
        return _graph;
    }

    /**
     * Find the start of the input graph.
     *
     * @return task - return null if there is no start node.
     */
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
    public NewScheduleNode[] findLeftmostSchedule(NewScheduleNode[] emptySchedule) {
        NewScheduleNode[] leftmostSchedule = new NewScheduleNode[_endTimeIdx + 1];
        int idx = 0;
        int startTime = 0;

        LinkedList<String> schedulableTasks = findSchedulableTasks(_graph, emptySchedule);
        while (schedulableTasks.peekFirst() != null) { // while the queue is not empty
            for (String task : schedulableTasks) {
                leftmostSchedule[idx] = new NewScheduleNode(task, startTime, startTime
                        + (int)_graph.getNode(task).getAttribute("Weight"), 0);

                startTime += (int)_graph.getNode(task).getAttribute("Weight");
                idx++;
            }

            schedulableTasks = findSchedulableTasks(_graph, leftmostSchedule);
        }

        leftmostSchedule[_endTimeIdx] = new NewScheduleNode(END_TIME_INDICATOR, NOT_USED,
                leftmostSchedule[_endTimeIdx - 1].getEndTime(), NOT_USED);

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
    public NewScheduleNode[] branchAndBound(NewScheduleNode[] currentSchedule, NewScheduleNode[] optimalSchedule,
                                            int size) {
        // Replace the current optimalSchedule with more optimal schedule when a schedule is finished creating. Return
        // null if not optimal.
        if (size == _graph.getNodeCount()) {
            if (currentSchedule[_endTimeIdx].getEndTime() < optimalSchedule[_endTimeIdx].getEndTime()) {
                return currentSchedule;
            }

            return null;
        }

        // Bounding. Return null if currentSchedule is getting slower than optimalSchedule.
        if (currentSchedule[_endTimeIdx].getEndTime() >= optimalSchedule[_endTimeIdx].getEndTime()) {
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

                if (endTime > nextSchedule[_endTimeIdx].getEndTime()) {
                    nextSchedule[_endTimeIdx] = new NewScheduleNode(END_TIME_INDICATOR, NOT_USED, endTime, NOT_USED);
                } else {
                    nextSchedule[_endTimeIdx] = new NewScheduleNode(END_TIME_INDICATOR, NOT_USED,
                                                                   currentSchedule[_endTimeIdx].getEndTime(), NOT_USED);
                }

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
    public LinkedList<String> findSchedulableTasks(Graph graph, NewScheduleNode[] schedule) {
        LinkedList<String> schedulableTasks = new LinkedList<>();

        for (Node task : graph) {
            boolean areParentsComplete = true;
            List<String> scheduledTasks = getScheduledTasks(schedule);

            // If the task is not already scheduled.
            if (!scheduledTasks.contains(task.getId())) {
                // Check if its parents have already been done.
                for (Object edge : task.enteringEdges().toArray()) {
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
            if (task == null) {
                break;
            }

            scheduledTasks.add(task.getId());
        }

        return scheduledTasks;
    }

    /**
     * Calculate the startTime for an input task.
     *
     * @param schedule - currentScheudle.
     * @param scheduledTasks - currently scheduled tasks in the solution.
     * @param task - task that is going to be scheduled.
     * @param processor - processor number that the task is going to be scheduled on.
     * @return maxStartTime - valid startTime.
     */
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
            if (_graph.getNode(scheduledTask).hasEdgeToward(task)) {
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

    /**
     * Convert the input array to HashMap so that the schedule can be used to calculate the startTime.
     *
     * @param schedule - current schedule array.
     * @return map
     */
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
            // If it is the node that only indicates the schedule endtime,
            // break the loop to prevent NullPointerException.
            if (task.getId().equals(END_TIME_INDICATOR)) {
                break;
            }

            _graph.getNode(task.getId()).setAttribute("Start", task.getStartTime());
            _graph.getNode(task.getId()).setAttribute("Processor", task.getProcessorNum());
        }
    }
}
