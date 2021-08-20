package project1.algorithm;

import project1.IO.GraphReader;
import project1.data.Processor;
import project1.data.ScheduleNode;

import java.util.HashMap;
import java.util.Set;

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
        sn.setFCost(Math.max(Math.max(bottomLevelCost(sn), findDRT(sn)), (sn.getTotalIdleTime() + _graphReader.getTotalWeight()) / (double) sn.getScheduleMap().size()));
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

    // TODO test and maybe change
    private double findDRT(ScheduleNode sn) {
        Set<String> nodesToSchedule = sn.getTaskToSchedule();

        double bmtLvl = 0;
        double largestDRT = 0;
        for (String nodeId : nodesToSchedule) {
            double minStartTime = Double.POSITIVE_INFINITY;

            for (Processor p : sn.getScheduleMap().values()) {
                int dataReady = sn.findEarliestStartTime(p.getPid(), nodeId);

                if (dataReady < minStartTime) {
                    bmtLvl = _bottomLevelMap.get(nodeId);
                    minStartTime = dataReady;
                }
            }

            largestDRT = Math.max(bmtLvl+minStartTime,largestDRT);
        }
        return largestDRT;
    }

    private static void calculateBottomLevels() {
        // Go through every node id and get bottom level
        for (String nodeId : GraphReader.getInstance().getNodeIdArr()) {
            _bottomLevelMap.put(nodeId, findAndSaveBottomLevel(nodeId));
        }
    }

    private static int findAndSaveBottomLevel(String nodeId) {
        String[] childrenOfNode = GraphReader.getInstance().getChildrenOfNodeMap().get(nodeId);

        if (childrenOfNode != null) {
            int largestBtmLvl = 0;
            int currentBtmLvl = 0;

            for (String child : childrenOfNode) {
                currentBtmLvl = findAndSaveBottomLevel(child);
                _bottomLevelMap.put(child, currentBtmLvl);
                largestBtmLvl = Math.max(currentBtmLvl, largestBtmLvl);
            }

            _bottomLevelMap.put(nodeId, currentBtmLvl + GraphReader.getInstance().getNodeWeightsMap().get(nodeId));
            return largestBtmLvl + GraphReader.getInstance().getNodeWeightsMap().get(nodeId);
        } else {
            return GraphReader.getInstance().getNodeWeightsMap().get(nodeId);
        }
    }

}
