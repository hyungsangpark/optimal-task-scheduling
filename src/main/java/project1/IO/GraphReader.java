package project1.IO;

import java.io.*;
import java.util.*;

public class GraphReader {
    private String _graphId;

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

    // For testing purpose
    // Resets the singleton
    public void resetGraphReader() {
        _instance = new GraphReader();
    }

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

//    public void addDummies() {
//        Set<String> nodeSet = getNodeWeightsMap().keySet();
//        Set<String> finalSet = new HashSet<>();
//        Set<String> finalSet2 = new HashSet<>();
//        for (String s : nodeSet) {
//            finalSet.add(s);
//            finalSet2.add(s);
//        }
//        Set<String> tempSet = getEdgeWeightMap().keySet();
//        Set<String> childSet = new HashSet<>();
//        Set<String> parentSet = new HashSet<>();
//
//        // Find all nodes with incoming edge(has parents) and all nodes with outgoing edge(has children)
//        for (String s : tempSet) {
//            String child = s.substring(s.indexOf(">") + 1);
//            String parent = s.substring(0,s.indexOf("-"));
//            childSet.add(child);
//            parentSet.add(parent);
//        }
//
//        // Find starting nodes
//        finalSet.removeAll(childSet);
//
//        // Find ending nodes
//        finalSet2.removeAll(parentSet);
//
//        String start = "start";
//        String end = "end";
//        _nodeWeightsMap.putIfAbsent(start,0);
//        _nodeWeightsMap.putIfAbsent(end,0);
//        int length = _nodeIdArr.length;
//        _nodeIdArr = Arrays.copyOf(_nodeIdArr, length + 2);
//        _nodeIdArr[length] = start;
//        _nodeIdArr[length+1] = end;
//
//        for (String s : finalSet) {
//            _edgeWeightMap.putIfAbsent(start + "->" + s, 0);
//            addNodeToMap(s,start, _parentsOfNodeMap);
//            addNodeToMap(start,s, _childrenOfNodeMap);
//        }
//        for (String s : finalSet2) {
//            _edgeWeightMap.putIfAbsent(s + "->" + end, 0);
//            addNodeToMap(end,s, _parentsOfNodeMap);
//            addNodeToMap(s,end, _childrenOfNodeMap);
//        }
//    }

    public String getGraphId() {
        return _graphId;
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
