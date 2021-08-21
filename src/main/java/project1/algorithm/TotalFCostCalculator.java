package project1.algorithm;

import project1.IO.GraphReader;
import project1.data.Processor;
import project1.data.ScheduleNode;

import java.util.HashMap;

public class TotalFCostCalculator {
    private static HashMap<String, Integer> _bottomLevelMap = null;
    private GraphReader _graphReader = GraphReader.getInstance();

    private static TotalFCostCalculator _instance = null;

    public static TotalFCostCalculator getInstance() {
        if (_instance == null) {
            _instance = new TotalFCostCalculator();
            _bottomLevelMap = new HashMap<>();
            calculateBottomLevels();
        }

        return _instance;
    }

    public void resetGraphReader() {
        _instance = new TotalFCostCalculator();
        _bottomLevelMap = new HashMap<>();
        calculateBottomLevels();
    }

    public void calculateAndSetFCost(ScheduleNode sn) {
        sn.setFCost(Math.max(bottomLevelCost(sn), (sn.getTotalIdleTime() + _graphReader.getTotalWeight()) / ((double)sn.getScheduleMap().size())));
    }

    private double bottomLevelCost(ScheduleNode sn) {
        double maxBtmLvl = 0;

        for (Processor processor : sn.getScheduleMap().values()) {
            for (String nodeId : processor.getNodesInScheduleMap().keySet()) {
                maxBtmLvl = Math.max(maxBtmLvl, _bottomLevelMap.get(nodeId) + processor.getNodesInScheduleMap().get(nodeId));
            }
        }

        return maxBtmLvl;
    }

    private static void calculateBottomLevels() {
        // Go through every node id and get bottom level
        for (String nodeId : GraphReader.getInstance().getNodeIdArr()) {
            _bottomLevelMap.putIfAbsent(nodeId, findAndSaveBottomLevel(nodeId));
        }
    }

    private static int findAndSaveBottomLevel(String nodeId) {
        String[] childrenOfNode = GraphReader.getInstance().getChildrenOfNodeMap().get(nodeId);

        if (childrenOfNode != null) {
            int largestBtmLvl = 0;
            int currentBtmLvl = 0;

            for (String child : childrenOfNode) {
                if (_bottomLevelMap.containsKey(child)) {
                    currentBtmLvl = _bottomLevelMap.get(child);
                }
                else {
                    currentBtmLvl = findAndSaveBottomLevel(child);
                    _bottomLevelMap.put(child, currentBtmLvl);
                }
                largestBtmLvl = Math.max(currentBtmLvl, largestBtmLvl);
            }

            _bottomLevelMap.put(nodeId, currentBtmLvl + GraphReader.getInstance().getNodeWeightsMap().get(nodeId));
            return largestBtmLvl + GraphReader.getInstance().getNodeWeightsMap().get(nodeId);
        } else {
            return GraphReader.getInstance().getNodeWeightsMap().get(nodeId);
        }
    }

}
