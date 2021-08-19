package project1.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Set;

public class Processor {

    // node to start time
    private HashMap<String, Integer> _nodesInScheduleMap;
    // e.g. 0 -> node1,1 -> node2,2 -> node3
    private HashMap<Integer, String> _nodesOrderMap;

    private int _pid;
    private int _currentFinishTIme;

    // New processor with no parent constructor
    public Processor(int pid) {
        _pid = pid;
         _nodesInScheduleMap = new HashMap<>();
         _nodesOrderMap = new HashMap<>();
    }

    // New child processor with a parent
    public Processor(Processor parentProcessor) {
        _nodesOrderMap = parentProcessor._nodesOrderMap;
        _nodesInScheduleMap = parentProcessor._nodesInScheduleMap;
        _pid = parentProcessor._pid;
        _currentFinishTIme = parentProcessor._currentFinishTIme;
    }

    public void addNode(String nodeId, int startTime, int nodeWeight) {

    }

    public double getIdleTime() {
        return 0.0;
    }

    public HashMap<String, Integer> getNodesInScheduleMap() {
        return _nodesInScheduleMap;
    }

    public Set<String> getNodesScheduled() {
        return _nodesInScheduleMap.keySet();
    }

    public int getPid() {
        return _pid;
    }

    public int getCurrentFinishTIme() {
        return _currentFinishTIme;
    }

    @Override
    public int hashCode() {
        // Hash table prime numbers from https://planetmath.org/goodhashtableprimes
        return new HashCodeBuilder(805306457, 402653189).append(_nodesInScheduleMap).append(_currentFinishTIme).toHashCode();
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
                .append(_nodesInScheduleMap, secondProcessor._nodesInScheduleMap).isEquals()
                && checkCurrentCost(secondProcessor._currentFinishTIme);
    }

    private boolean checkCurrentCost(int currentCost) {
        return _currentFinishTIme == currentCost;
    }

}
