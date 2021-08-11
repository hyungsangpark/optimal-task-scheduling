package project1.algorithm;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.IO.GraphReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TotalFCostCalculator {

    public int calculateTotalF(List<List<String>> schedule) {
        int totalCost = 0;

        for (List<String> p:schedule) {
            for (int i = 0; i < p.size(); i++) {
                String node = p.get(i);

                if (!node.equals("-1") && i > 0 && !node.equals(p.get(i-1))) {
                    int bottomLevelCost = bottomLevelCost(node) + i;
                    totalCost = Math.max(bottomLevelCost,totalCost);
                }
            }
        }

        return totalCost;
    }

    private int bottomLevelCost(String nodeId) {
        GraphReader graphReader = GraphReader.getInstance();
        String[] childrenOfNode = graphReader.getChildrenOfNodeMap().get(nodeId);

        if (childrenOfNode != null) {
            int largestBtmLvl = 0;
            int currentBtmLvl = 0;

            for (String child : childrenOfNode) {
                currentBtmLvl = bottomLevelCost(child);
                if (currentBtmLvl > largestBtmLvl) {
                    largestBtmLvl = currentBtmLvl;
                }
            }

            return largestBtmLvl + graphReader.getNodeWeightsMap().get(nodeId);
        }
        else {
            return graphReader.getNodeWeightsMap().get(nodeId);
        }
    }
}
