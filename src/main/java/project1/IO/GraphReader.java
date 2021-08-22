package project1.IO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This class reads the input dot graph into the data structure it uses for the A* search implementation.
 */

public class GraphReader {
    private String _graphId;

    private String[] _nodeIdArr;
    private HashMap<String,Integer> _nodeWeightsMap = new HashMap<>();
    private HashMap<String,String[]> _parentsOfNodeMap = new HashMap<>();
    private HashMap<String,String[]> _childrenOfNodeMap = new HashMap<>();
    private HashMap<String,Integer> _edgeWeightMap = new HashMap<>();
    private double totalWeight = 0;

    private static GraphReader _instance = null;

    /**
     * Get instance of the GraphReader object.
     * @return  GraphReader object.
     */

    public static GraphReader getInstance() {
        if (_instance == null) {
            _instance = new GraphReader();
        }

        return _instance;
    }

    /**
     * This method is for testing purpose. It resets the singleton.
     */

    public void resetGraphReader() {
        _instance = new GraphReader();
    }

    /**
     * This method reads the graph and load it into the data structure A* and the GUI use.
     * @param inputFileName The name of the input dot file.
     * @throws IOException  Error occurred during the reading of data from the input file.
     */

    public void loadGraphData(String inputFileName) throws IOException {
        InputStreamReader isr = new FileReader(inputFileName);
        BufferedReader bfr = new BufferedReader(isr);

        ArrayList<String> tempNodeIdList = new ArrayList<>();

        String currentLine = bfr.readLine();
        _graphId = currentLine.substring(currentLine.indexOf("\"")+1,currentLine.lastIndexOf("\""));

        while ((currentLine = bfr.readLine()) != null) {
            if (currentLine.isEmpty()) {
                continue;
            }

            if (currentLine.substring(0,1).equalsIgnoreCase("}")) {
                break;
            }

            if (currentLine.contains("Weight")) {

                // get the weight of edge or node as it uses the same method
                int weightOfEdgeOrNode = Integer.parseInt(currentLine
                        .substring(currentLine.indexOf("=") + 1, currentLine.indexOf("]"))
                        .replaceAll("\\s",""));

                // check for edge
                if (currentLine.contains("->")) {
                    // get the source node
                    String sourceNode = currentLine
                            .substring(0,currentLine.indexOf("-"))
                            .replaceAll("\\s","");
                    // get the destination node
                    String destinationNode = currentLine
                            .substring(currentLine.indexOf(">")+1,currentLine.indexOf("["))
                            .replaceAll("\\s","");
                    // add weight to map
                    _edgeWeightMap.putIfAbsent(sourceNode+"->"+destinationNode,weightOfEdgeOrNode);
                    // add to parent map
                    addNodeToMap(destinationNode,sourceNode, _parentsOfNodeMap);
                    // add to child map
                    addNodeToMap(sourceNode,destinationNode, _childrenOfNodeMap);
                }
                else {
                    // get node id
                    String nodeId = currentLine.substring(0,currentLine.indexOf("[")).replaceAll("\\s","");
                    // add it to the node id list
                    tempNodeIdList.add(nodeId);
                    // set its weight
                    _nodeWeightsMap.putIfAbsent(nodeId,weightOfEdgeOrNode);
                    totalWeight += weightOfEdgeOrNode;
                }
            }
            else {
                // TODO: output the line even though it's not needed
            }
        }

        _nodeIdArr = new String[tempNodeIdList.size()];

        for (int i = 0; i < tempNodeIdList.size(); i++) {
            _nodeIdArr[i] = tempNodeIdList.get(i);
        }

        bfr.close();
    }

    /**
     * This method is executed in loadGraphData. It maps the relationship between two tasks nodes(edges).
     * @param node1 Either child or parent task node.
     * @param node2 Either child or parent task node.
     * @param nodeMap   Either _childrenOfNodeMap or _parentsOfNodeMap.
     */

    private void addNodeToMap(String node1, String node2, HashMap<String,String[]> nodeMap) {
        String[] newNodesOfNode;

        if (nodeMap.containsKey(node1)) {
            String[] currentNodesOfNode = nodeMap.get(node1);
            // Create a new trimmed parent string array
            newNodesOfNode = Arrays.copyOf(currentNodesOfNode,currentNodesOfNode.length + 1);
            newNodesOfNode[newNodesOfNode.length-1] = node2;
        }
        else {
            newNodesOfNode = new String[] {node2};
        }
        nodeMap.put(node1,newNodesOfNode);
    }

    /**
     * Getter for _graphId.
     * @return  The ID of the input dot graph.
     */

    public String getGraphId() {
        return _graphId;
    }

    /**
     * Getter for _nodeIdArr.
     * @return  The array of IDs of the task nodes.
     */

    public String[] getNodeIdArr() {
        return _nodeIdArr;
    }

    /**
     * Getter of _nodeWeightsMap.
     * @return  HashMap that represents the task nodes' weight.
     */

    public HashMap<String, Integer> getNodeWeightsMap() {
        return _nodeWeightsMap;
    }

    /**
     * Getter of _parentsOfNodeMap.
     * @return  HashMap that represents the parent task nodes.
     */

    public HashMap<String, String[]> getParentsOfNodeMap() {
        return _parentsOfNodeMap;
    }

    /**
     * Getter of _childrenOfNodeMap.
     * @return  HashMap that represents the children task nodes.
     */

    public HashMap<String, String[]> getChildrenOfNodeMap() {
        return _childrenOfNodeMap;
    }

    /**
     * Getter of _edgeWeightMap.
     * @return  HashMap that represents the edge weights of task nodes.
     */

    public HashMap<String, Integer> getEdgeWeightMap() {
        return _edgeWeightMap;
    }

    /**
     * Getter of totalWeight.
     * @return  Total weight of all task nodes.
     */

    public double getTotalWeight() {
        return totalWeight;
    }
}
