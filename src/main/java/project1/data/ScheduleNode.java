package project1.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import project1.IO.GraphReader;
import project1.algorithm.TotalFCostCalculator;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * This ScheduleNode class stores all the information of a tree node in the A* algorithm. It is also used for the
 * expansion of the search tree. The package java.util.concurrent is used for parallelisation of the search.
 */
public class ScheduleNode {
    GraphReader _graphReader = GraphReader.getInstance();
    private final HashMap<Integer, Processor> _scheduleMap = new HashMap<>();
    public static ExecutorService threadPoolExecutor;
    private double _FCost = 0;

    /**
     * Constructor for root schedule node(node with no tasks assigned).
     * @param _processors   Number of processors.
     */

    public ScheduleNode(int _processors) {
        for (int i = 0; i < _processors; i++) {
            _scheduleMap.put(i, new Processor(i));
        }
    }

    /**
     * Constructor for normal schedule nodes.
     * @param parent    The parent ScheduleNode.
     */

    public ScheduleNode(ScheduleNode parent) {
        copyParent(parent);
    }

    /**
     * Expand the tree and return newly created ScheduleNodes. Parallelisation is done in this method.
     * @param numOfCores    The number of cores used for parallelisation.
     * @return  A HashSet of ScheduleNodes that has been expanded.
     */

    public HashSet<ScheduleNode> expandTree(int numOfCores) {
        HashSet<ScheduleNode> newSchedules = new HashSet<>();
        Set<String> schedulableNodes = getTaskToSchedule();

        // If parallelisation is not turned on.
        if (numOfCores == 1) {
            for (String nodeId : schedulableNodes) {
                newSchedules.addAll(createChildren(nodeId));
            }
        }
        // If there is parallelisation.
        else {
            ArrayList<Callable<HashSet<ScheduleNode>>> tasksToComplete = new ArrayList<>();
            schedulableNodes.forEach(nodeId -> tasksToComplete.add(() -> createChildren(nodeId)));

            try {
                for (Future<HashSet<ScheduleNode>> task : threadPoolExecutor.invokeAll(tasksToComplete)) {
                    newSchedules.addAll(task.get());
                }

            } catch (Exception  e) {
                e.printStackTrace();
            }

        }

        return newSchedules;
    }

    /**
     * Method that creates children ScheduleNode.
     * @param nodeId    Name/ID of the task.
     * @return  HashSet of ScheduleNodes(children that got expanded).
     */

    private HashSet<ScheduleNode> createChildren(String nodeId) {
        HashSet<ScheduleNode> newChildSchedule = new HashSet<>();

        for (int j = 0; j < _scheduleMap.size(); j++) {
            ScheduleNode newSN = new ScheduleNode(this);
            newSN.addNewNodeTask(j,nodeId);
            newChildSchedule.add(newSN);
        }

        return newChildSchedule;
    }

    /**
     * This method adds new task nodes to _scheduleMap.
     * @param pNum  processor number.
     * @param nodeId    name/ID of a task node.
     */

    private void addNewNodeTask(int pNum, String nodeId) {
        _scheduleMap.get(pNum).addNode(nodeId, findEarliestStartTime(pNum,nodeId), _graphReader.getNodeWeightsMap().get(nodeId));
        TotalFCostCalculator.getInstance().calculateAndSetFCost(this);
    }

    /**
     * Method to find the earliest start time.
     * @param pNum  processor number.
     * @param nodeId    name/ID of a task node.
     * @return  The earliest possible starting time for next task.
     */

    public int findEarliestStartTime(int pNum, String nodeId) {
        if (_graphReader.getParentsOfNodeMap().get(nodeId) == null) {
            return _scheduleMap.get(pNum).getCurrentFinishTime();
        }

        int earliestPossibleStartTime = 0;
        String[] parentsOfNode = _graphReader.getParentsOfNodeMap().get(nodeId);

        for (int i = 0; i < _scheduleMap.size(); i++) {
            Processor p = _scheduleMap.get(i);

            for (int j = 0; j < parentsOfNode.length; j++) {
                String parentNode = parentsOfNode[j];
                if (p.getNodesInScheduleMap().containsKey(parentNode)) {
                    earliestPossibleStartTime = Math.max(earliestPossibleStartTime,earliestStartTimeHelper(parentNode,nodeId,pNum,p));
                }
            }
        }

        return earliestPossibleStartTime;
    }

    /**
     * Helper method for findEarliestStartTime.
     * @param parentNode    The parent task node of current task.
     * @param nodeId    Current Task node.
     * @param pNum  Processor number.
     * @param p Processor object.
     * @return  The best starting time.
     */

    private int earliestStartTimeHelper(String parentNode, String nodeId, int pNum, Processor p) {
        int bestStarTime = _scheduleMap.get(pNum).getCurrentFinishTime();

        if (p.getPid() != pNum) {
            int parentFinishTIme = p.getNodesInScheduleMap().get(parentNode) + _graphReader.getNodeWeightsMap().get(parentNode);
            int transitionCost = _graphReader.getEdgeWeightMap().get(parentNode + "->" + nodeId);
            if ((bestStarTime <= transitionCost + parentFinishTIme) || bestStarTime <= parentFinishTIme)
                bestStarTime = parentFinishTIme + transitionCost;
        }

        return bestStarTime;
    }

    /**
     *  This method finds tasks that are available to be scheduled.
     * @return  A set of Tasks that are available to be scheduled.
     */

    public Set<String> getTaskToSchedule() {
        Set<String> schedulableNodes = new HashSet<>();
        Set<String> tasksInSchedule = getTasksInScheduleNode();

        for (String nodeId : _graphReader.getNodeIdArr()) {
            if (!tasksInSchedule.contains(nodeId) && checkIfParentsComplete(nodeId,tasksInSchedule)) {
                schedulableNodes.add(nodeId);
            }
        }

        return schedulableNodes;
    }

    /**
     * This method checks if the parent/parents of current task has/have been completed or not.
     * @param nodeId    Current task node.
     * @param tasksInSchedule   The tasks that has been scheduled already in current schedule.
     * @return  A boolean that determine if all parents of this current task node has been scheduled or not.
     */

    private boolean checkIfParentsComplete(String nodeId,Set<String> tasksInSchedule) {
        HashMap<String, String[]> parentsOfNodeMap = _graphReader.getParentsOfNodeMap();

        if (parentsOfNodeMap.containsKey(nodeId)) {
            String[] parents = parentsOfNodeMap.get(nodeId);
            for (String parent : parents) {
                if (!tasksInSchedule.contains(parent)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * This method gets all tasks nodes that have been scheduled in the schedule.
     * @return  A Set of tasks nodes.
     */

    public Set<String> getTasksInScheduleNode() {
        Set<String> scheduledNodes = new HashSet<>();

        for (Processor p : _scheduleMap.values()) {
            scheduledNodes.addAll(p.getNodesInScheduleMap().keySet());
        }

        return scheduledNodes;
    }

    /**
     * This method checks if all tasks node has been scheduled yet, if yes return true, if no return false.
     * @return  boolean that determines if all tasks node have been scheduled or not.
     */

    public boolean isTarget() {
        Set<String> tasksDone = getTasksInScheduleNode();

        for (String nodeId : _graphReader.getNodeIdArr()) {
            if (!tasksDone.contains(nodeId)) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method gets the total idle time.
     * @return  The total idle time.
     */

    public double getTotalIdleTime() {
        double totalIdleTime = 0;

        for (Processor p : _scheduleMap.values()) {
            totalIdleTime += p.getIdleTime();
        }

        return totalIdleTime;
    }

    /**
     * Method to copy the information of the parent's ScheduleNode to its own.
     * @param parent    Parent ScheduleNode.
     */

    private void copyParent(ScheduleNode parent) {
        for (int i = 0; i < parent.getScheduleMap().size(); i++) {
            Processor parentProcessor = parent.getScheduleMap().get(i);
            _scheduleMap.put(parentProcessor.getPid(), new Processor(parentProcessor));
        }

        _FCost = parent.getFCost();
    }


    public HashMap<Integer, Processor> getScheduleMap() {
        return _scheduleMap;
    }

    /**
     * Getter for _FCost.
     * @return  The f(n) cost.
     */

    public double getFCost() {
        return _FCost;
    }

    /**
     * Setter for _FCost.
     * @param _FCost    The f(n) cost.
     */

    public void setFCost(double _FCost) {
        this._FCost = _FCost;
    }

    /**
     * This method gets the optimal time.
     * @return  The optimal time.
     */

    public int getOptimalTime() {
        // Since variable changes inside a lambda function, the value is wrapped in an int array.
        int[] optimalTime = {-1};
        _scheduleMap.forEach(((processorNumber, processor) -> {
            if (processor.getCurrentFinishTime() > optimalTime[0]) optimalTime[0] = processor.getCurrentFinishTime();
        }));
        return optimalTime[0];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ScheduleNode secondSchedule = (ScheduleNode) obj;
        return (_scheduleMap.hashCode() == (secondSchedule.getScheduleMap().hashCode()));

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_scheduleMap).toHashCode();
    }
}
