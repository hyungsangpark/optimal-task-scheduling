package project1.algorithm;

import project1.IO.GraphReader;
import project1.data.ScheduleNode;

import java.util.HashMap;
import java.util.List;

public class TotalFCostCalculator {
    private HashMap<String, Integer> bottomLevels = new HashMap<>();

    public TotalFCostCalculator() {
        // Initialize map with all zeros
        for (String node : GraphReader.getInstance().getNodeIdArr()) {
            bottomLevels.put(node, 0);
        }
    }

    public int calculateTotalF(ScheduleNode sn) {
        int totalCost = 0;

        List<List<String>> schedule = sn.getSchedule();

        for (List<String> p:schedule) {
            for (int i = 0; i < p.size(); i++) {
                String node = p.get(i);
                if (!node.equals("-1") && i > 0 && !node.equals(p.get(i-1))) {
                    int bottomLevelCost = bottomLevelCost(node) + i;
                    totalCost = Math.max(bottomLevelCost,totalCost);
                }
            }
        }

        int maxDRT = findDTROfScheduleNode(sn);

        return Math.max(totalCost,maxDRT);
    }

    private int bottomLevelCost(String nodeId) {
        GraphReader graphReader = GraphReader.getInstance();
        String[] childrenOfNode = graphReader.getChildrenOfNodeMap().get(nodeId);

        if (childrenOfNode != null) {
            int largestBtmLvl = 0;
            int currentBtmLvl = 0;

            for (String child : childrenOfNode) {
                currentBtmLvl = bottomLevelCost(child);
                bottomLevels.put(child, currentBtmLvl);
                if (currentBtmLvl > largestBtmLvl) {
                    largestBtmLvl = currentBtmLvl;
                }
            }

            bottomLevels.put(nodeId, currentBtmLvl + graphReader.getNodeWeightsMap().get(nodeId));
            return largestBtmLvl + graphReader.getNodeWeightsMap().get(nodeId);
        }
        else {
            return graphReader.getNodeWeightsMap().get(nodeId);
        }
    }

    private int findDTROfScheduleNode(ScheduleNode sn) {
        List<String> nodesToSchedule = sn.getTaskToSchedule();

        int btmLvl = 0;
        int maxCurDRT = 0;

        for (int j = 0; j < nodesToSchedule.size(); j++) {
            String currNode = nodesToSchedule.get(j);
            int minStartTime = Integer.MAX_VALUE;

            for (int i = 0; i < sn.getSchedule().size(); i++) {
                int dataReady = sn.findEarliestStartTime(i, currNode, GraphReader.getInstance().getParentsOfNodeMap().get(currNode));

                if (minStartTime > dataReady) {
                    btmLvl = bottomLevels.get(currNode);
                    minStartTime = dataReady;
                }
            }

            maxCurDRT = Math.max(btmLvl + minStartTime, maxCurDRT);
        }

        return maxCurDRT;
    }

}
