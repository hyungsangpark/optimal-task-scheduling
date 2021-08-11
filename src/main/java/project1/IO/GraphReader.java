package project1.IO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GraphReader {

    private String graphId;

    private String[] _nodeIdArr;
    private HashMap<String,Integer> _nodeWeightsMap = new HashMap<>();
    private HashMap<String,String[]> _parentsOfNodeMap = new HashMap<>();
    private HashMap<String,String[]> _childrenOfNodeMap = new HashMap<>();
    private HashMap<String,Integer> _edgeWeightMap = new HashMap<>();

    private static GraphReader _instance = null;

    public static GraphReader getInstance() {
        if (_instance == null) {
            _instance = new GraphReader();
        }

        return _instance;
    }

    public void loadGraphData(String fileName) throws IOException {
        InputStreamReader isr = new FileReader(fileName);
        BufferedReader bfr = new BufferedReader(isr);

        ArrayList<String> tempNodeIdList = new ArrayList<>();

        String currentLine = bfr.readLine();
        // TODO: output line

        while ((currentLine = bfr.readLine()) != null) {
            if (currentLine.substring(0,1).equalsIgnoreCase("}")) {
                break;
            }

            if (currentLine.contains("Weight")) {

                // get the weight of edge or node as it uses the same method
                int weightOfEdgeOrNode = Integer.parseInt(currentLine
                        .substring(currentLine.indexOf("=") + 1, currentLine.indexOf("]"))
                        .replaceAll(" ", ""));

                // check for edge
                if (currentLine.contains("->")) {
                    // get the source node
                    String sourceNode = currentLine
                            .substring(0,currentLine.indexOf("-"))
                            .replaceAll(" ","");
                    // get the destination node
                    String destinationNode = currentLine
                            .substring(currentLine.indexOf(">")+1,currentLine.indexOf("["))
                            .replaceAll(" ","");
                    // add weight to map
                    _edgeWeightMap.putIfAbsent(sourceNode+"->"+destinationNode,weightOfEdgeOrNode);
                    // add to parent map
                    addNodeToMap(destinationNode,sourceNode, _parentsOfNodeMap);
                    // add to child map
                    addNodeToMap(sourceNode,destinationNode, _childrenOfNodeMap);
                    // TODO: output edge
                }
                else {
                    // get node id
                    String nodeId = currentLine.substring(0,currentLine.indexOf("[")).replaceAll(" ","");
                    // add it to the node id list
                    tempNodeIdList.add(nodeId);
                    // set its weight
                    _nodeWeightsMap.putIfAbsent(nodeId,weightOfEdgeOrNode);
                    // TODO: output node
                }
            }
            else {
                // TODO: output the line even though it's not needed
            }
        }

        _nodeIdArr = new String[tempNodeIdList.size()];

        for (int i = 0; i < tempNodeIdList.size(); i++) {
            _nodeIdArr[0] = tempNodeIdList.get(i);
        }

        bfr.close();
    }

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

    public String[] getNodeIdArr() {
        return _nodeIdArr;
    }

    public HashMap<String, Integer> getNodeWeightsMap() {
        return _nodeWeightsMap;
    }

    public HashMap<String, String[]> getParentsOfNodeMap() {
        return _parentsOfNodeMap;
    }

    public HashMap<String, String[]> getChildrenOfNodeMap() {
        return _childrenOfNodeMap;
    }

    public HashMap<String, Integer> getEdgeWeightMap() {
        return _edgeWeightMap;
    }
}
