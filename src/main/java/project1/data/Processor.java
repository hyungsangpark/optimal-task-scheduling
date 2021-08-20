package project1.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import project1.IO.GraphReader;

import java.util.HashMap;

public class Processor {
    // node to start time
    private HashMap<String, Integer> _nodesInScheduleMap;
    // e.g. 0 -> node1,1 -> node2,2 -> node3
    private HashMap<Integer, String> _nodesOrderMap;

    private int _pid;
    private int _currentFinishTIme;
    private int _idleTime;

    // New processor with no parent constructor
    public Processor(int pid) {
        _pid = pid;
        _nodesInScheduleMap = new HashMap<>();
        _nodesOrderMap = new HashMap<>();
    }

    // New child processor with a parent
    public Processor(Processor parentProcessor) {
        copyParent(parentProcessor);
    }

    public void addNode(String nodeId, int startTime, int weight) {
        _nodesInScheduleMap.put(nodeId,startTime);
        _currentFinishTIme = startTime + weight;

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

    private void copyParent(Processor parentProcessor) {
        _pid = parentProcessor.getPid();
        _nodesOrderMap = new HashMap<>(parentProcessor.getNodesOrderMap());
        _currentFinishTIme = parentProcessor.getCurrentFinishTIme();
        _nodesInScheduleMap = new HashMap<>(parentProcessor.getNodesInScheduleMap());
    }

    public HashMap<String, Integer> getNodesInScheduleMap() {
        return _nodesInScheduleMap;
    }

    public HashMap<Integer, String> getNodesOrderMap() {
        return _nodesOrderMap;
    }

    public int getPid() {
        return _pid;
    }

    public int getCurrentFinishTIme() {
        return _currentFinishTIme;
    }

    public int getIdleTime() {
        return _idleTime;
    }

    @Override
    public int hashCode() {
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
                .append(_nodesInScheduleMap, secondProcessor.getNodesInScheduleMap()).isEquals()
                && (_currentFinishTIme == secondProcessor.getCurrentFinishTIme());
    }
}
