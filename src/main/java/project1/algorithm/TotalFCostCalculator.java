package project1.algorithm;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.IO.GraphReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TotalFCostCalculator {
    private static HashMap<String,Integer> test = new HashMap<>();

    public static int calculateTotalF(List<List<String>> schedule) {
        int totalCost = 0;

        for (List<String> p:schedule) {
            for (int i = p.size()-1; i >= 0; i--) {
                String node = p.get(i);
                if (i - 1 > 0 && !p.get(i-1).equals(node) || i - 1 < 0 && !node.equals("-1")) {
                    int bottomLevelCost = bottomLevelCost(node) + i;
                    if (bottomLevelCost > totalCost) {
                        totalCost = bottomLevelCost;
                    }
                    break;
                }
//                if (!node.equals("-1") && i > 0 && !node.equals(p.get(i-1)) || i == 0 && !node.equals("-1")) {
//                    int bottomLevelCost = bottomLevelCost(node) + i;
//                    if (bottomLevelCost > totalCost) {
//                        totalCost = bottomLevelCost;
//                    }
//                }
            }
        }

        return totalCost;
    }

    private static int bottomLevelCost(String nodeId) {

        if (test.containsKey(nodeId)) {
            return test.get(nodeId);
        }

        if (GraphReader.getInstance().getChildrenOfNodeMap().get(nodeId) != null) {
            int largestBtmLvl = 0;
            int currentBtmLvl;

            for (String child : GraphReader.getInstance().getChildrenOfNodeMap().get(nodeId)) {
                if (test.containsKey(child)) {
                    currentBtmLvl = test.get(child);
                }
                else {
                    currentBtmLvl = bottomLevelCost(child);
                    test.put(child,currentBtmLvl);
                }

                if (currentBtmLvl > largestBtmLvl) {
                    largestBtmLvl = currentBtmLvl;
                }
            }

            return largestBtmLvl + GraphReader.getInstance().getNodeWeightsMap().get(nodeId);
        }
        else {
            return GraphReader.getInstance().getNodeWeightsMap().get(nodeId);
        }
    }
}
