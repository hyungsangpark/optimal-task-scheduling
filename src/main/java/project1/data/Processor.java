package project1.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import project1.IO.GraphReader;

import java.util.HashMap;

/**
 * This Processor class is used to represent a schedule of one processor. One ScheduleNode would have multiple Processor
 * objects to represent its full schedule.
 */

public class Processor {
    // node to start time
    private HashMap<String, Integer> _nodesInScheduleMap;
    // e.g. 0 -> node1,1 -> node2,2 -> node3
    private HashMap<Integer, String> _nodesOrderMap;

    private int _pid;
    private int _currentFinishTime;
    private int _idleTime;

    /**
     * New processor with no parent constructor.
     * @param pid   processor number/ID.
     */

    public Processor(int pid) {
        _pid = pid;
        _nodesInScheduleMap = new HashMap<>();
        _nodesOrderMap = new HashMap<>();
    }

    /**
     * New child processor with a parent.
     * @param parentProcessor   parent's processor object.
     */

    public Processor(Processor parentProcessor) {
        copyParent(parentProcessor);
    }

    /**
     * addNode adds tasks nodes into _nodesInScheduleMap HashMap.
     * @param nodeId    name/ID of the task node.
     * @param startTime schedule start time of the task.
     * @param weight    weight of the node.
     */

    public void addNode(String nodeId, int startTime, int weight) {
        _nodesInScheduleMap.put(nodeId,startTime);
        _currentFinishTime = startTime + weight;

        int index = _nodesOrderMap.size();

        _nodesOrderMap.put(index,nodeId);

        if (_nodesInScheduleMap.size() == 1) {
            _idleTime = _nodesInScheduleMap.get(_nodesOrderMap.get(0));
        }
        else {
            int weightOfPrevNode = GraphReader.getInstance().getNodeWeightsMap().get(_nodesOrderMap.get(index - 1));
            int startOfPrevNode = _nodesInScheduleMap.get(_nodesOrderMap.get(index - 1));

            if (startTime != weightOfPrevNode + startOfPrevNode) {
                _idleTime += startTime - startOfPrevNode + weightOfPrevNode;
            }
        }
    }

    /**
     * Copy information of the parent's processor object to its own.
     * @param parentProcessor   parent's processor object.
     */

    private void copyParent(Processor parentProcessor) {
        _pid = parentProcessor.getPid();
        _nodesOrderMap = new HashMap<>(parentProcessor.getNodesOrderMap());
        _currentFinishTime = parentProcessor.getCurrentFinishTime();
        _nodesInScheduleMap = new HashMap<>(parentProcessor.getNodesInScheduleMap());
    }

    /**
     * Getter of _nodesInScheduleMap.
     * @return  A HashMap of tasks nodes in this schedule.
     */

    public HashMap<String, Integer> getNodesInScheduleMap() {
        return _nodesInScheduleMap;
    }

    /**
     * Getter of _nodesOrderMap.
     * @return  A HashMap of tasks nodes in this schedule in order.
     */

    public HashMap<Integer, String> getNodesOrderMap() {
        return _nodesOrderMap;
    }

    /**
     * Getter of _pid.
     * @return  processor id.
     */

    public int getPid() {
        return _pid;
    }

    /**
     * Getter of _currentFinishTIme.
     * @return The current finish time of the schedule.
     */

    public int getCurrentFinishTime() {
        return _currentFinishTime;
    }

    /**
     * Getter of _idleTime.
     * @return  The idle time.
     */

    public int getIdleTime() {
        return _idleTime;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(805306457, 402653189).append(_nodesInScheduleMap).append(_currentFinishTime).toHashCode();
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

        Processor secondProcessor = (Processor) obj;
        return new EqualsBuilder()
                .append(_nodesInScheduleMap, secondProcessor.getNodesInScheduleMap()).isEquals()
                && (_currentFinishTime == secondProcessor.getCurrentFinishTime());
    }
}
