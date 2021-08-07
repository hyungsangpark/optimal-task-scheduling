package project1.algorithm;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TotalFCostCalculator {

    public int calculateTotalF(Graph graph, List<List<String>> schedule) {
        int totalCost = 0;

        for (List<String> p:schedule) {
            for (int i = 0; i < p.size(); i++) {
                String node = p.get(i);

                if (!node.equals("-1") && i > 0 && !node.equals(p.get(i-1))) {
                    int bottomLevelCost = bottomLevelCost(graph,node) + i;
                    totalCost = Math.max(bottomLevelCost,totalCost);
                }
            }
        }

        return totalCost;
    }

    private int bottomLevelCost(Graph graph, String nodeId) {
        Node node = graph.getNode(nodeId);

        if (node.getOutDegree() != 0) {
            int largestBtmLvl = 0;
            int currentBtmLvl = 0;

            List<Node> children = new ArrayList<>();

            for(int i = 0; i < node.getOutDegree(); i++) {
                children.add(node.getLeavingEdge(i).getTargetNode());
            }

            for (Node child : children) {
                currentBtmLvl = bottomLevelCost(graph, child.getId());
                if (currentBtmLvl > largestBtmLvl) {
                    largestBtmLvl = currentBtmLvl;
                }
            }

            return largestBtmLvl + (int)(double)node.getAttribute("Weight");
        }
        else {
            return (int)(double)node.getAttribute("Weight");
        }
    }
}
