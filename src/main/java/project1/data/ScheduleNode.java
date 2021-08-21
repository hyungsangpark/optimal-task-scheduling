package project1.data;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import project1.IO.GraphReader;
import project1.algorithm.TotalFCostCalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ScheduleNode {
    GraphReader _graphReader = GraphReader.getInstance();
    private final HashMap<Integer, Processor> _scheduleMap = new HashMap<>();
    private double _FCost = 0;

    //For root schedule node(node with no tasks assigned)
    public ScheduleNode(int _processors) {
        for (int i = 0; i < _processors; i++) {
            _scheduleMap.put(i, new Processor(i));
        }
    }

    //For normal schedule nodes
    public ScheduleNode(ScheduleNode parent) {
        copyParent(parent);
    }

    //Expand the tree and return newly created ScheduleNodes
    public HashSet<ScheduleNode> expandTree() {
        HashSet<ScheduleNode> newSchedules = new HashSet<>();

        for (String nodeId : getTaskToSchedule()) {
            for (int j = 0; j < _scheduleMap.size(); j++) {
                ScheduleNode newChildSchedule = new ScheduleNode(this);
                newChildSchedule.addNewNodeTask(j,nodeId);
                newSchedules.add(newChildSchedule);
            }
        }

        return newSchedules;
    }

    private void addNewNodeTask(int pNum, String nodeId) {
        _scheduleMap.get(pNum).addNode(nodeId, findEarliestStartTime(pNum,nodeId), _graphReader.getNodeWeightsMap().get(nodeId));
        TotalFCostCalculator.getInstance().calculateAndSetFCost(this);
    }

    public int findEarliestStartTime(int pNum, String nodeId) {
        if (_graphReader.getParentsOfNodeMap().get(nodeId) == null) {
            return _scheduleMap.get(pNum).getCurrentFinishTIme();
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

    private int earliestStartTimeHelper(String parentNode, String nodeId, int pNum, Processor p) {
        int bestStarTime = _scheduleMap.get(pNum).getCurrentFinishTIme();

        if (p.getPid() != pNum) {
            int parentFinishTIme = p.getNodesInScheduleMap().get(parentNode) + _graphReader.getNodeWeightsMap().get(parentNode);
            int transitionCost = _graphReader.getEdgeWeightMap().get(parentNode + "->" + nodeId);
            if ((bestStarTime <= transitionCost + parentFinishTIme) || bestStarTime <= parentFinishTIme)
                bestStarTime = parentFinishTIme + transitionCost;
        }

        return bestStarTime;
    }

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

    public Set<String> getTasksInScheduleNode() {
        Set<String> scheduledNodes = new HashSet<>();

        for (Processor p : _scheduleMap.values()) {
            scheduledNodes.addAll(p.getNodesInScheduleMap().keySet());
        }

        return scheduledNodes;
    }

    public boolean isTarget() {
        Set<String> tasksDone = getTasksInScheduleNode();

        for (String nodeId : _graphReader.getNodeIdArr()) {
            if (!tasksDone.contains(nodeId)) {
                return false;
            }
        }

        return true;
    }

    public double getTotalIdleTime() {
        double totalIdleTime = 0;

        for (Processor p : _scheduleMap.values()) {
            totalIdleTime += p.getIdleTime();
        }

        return totalIdleTime;
    }

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

    public double getFCost() {
        return _FCost;
    }

    public void setFCost(double _FCost) {
        this._FCost = _FCost;
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
