package project1.IO;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class GraphWriter {

    private HashMap<String, int[]> formatOutputGraph(List<List<String>> schedule) {
        HashMap<String, int[]> nodeStartAndPNumMap = new HashMap<>();

        for (int i = 0; i < schedule.size(); i++) {
            for (int j = 0; j < schedule.get(i).size(); j++) {
                String value = schedule.get(i).get(j);
                if (!value.equals("-1")) {
                    if (!nodeStartAndPNumMap.containsKey(value)) {
                        int[] startPnumArr = new int[2];
                        startPnumArr[0] = j;
                        startPnumArr[1] = i+1;
                        nodeStartAndPNumMap.put(value, startPnumArr);
                    }
                }
            }
        }

        return nodeStartAndPNumMap;
    }

    public void outputGraphData(String outputFileName, List<List<String>> schedule) {
        GraphReader graphReader = GraphReader.getInstance();

        String graphId = graphReader.getGraphId();

        String[] nodeIds = graphReader.getNodeIdArr();
        HashMap<String, Integer> nodeWeightsMap = graphReader.getNodeWeightsMap();
        HashMap<String, Integer> edgeWeightMap = graphReader.getEdgeWeightMap();

        HashMap<String, int[]> startAndPNumMap = formatOutputGraph(schedule);


        try {
            PrintWriter graphWriter = new PrintWriter(outputFileName);

            // Header
            graphWriter.println("digraph \"" + graphId + "\" {");

            // Print Nodes.
            for (int i = 0; i < nodeIds.length; i++) {
                String nodeId = nodeIds[i];

                graphWriter.println("    " +
                        nodeId +
                        " [Weight=" +
                        nodeWeightsMap.get(nodeId) +
                        ",Start=" +
                        startAndPNumMap.get(nodeId)[0] +
                        ",Processor=" +
                        startAndPNumMap.get(nodeId)[1] +
                        "];"
                );
            }

//            // Print edges
            for (String edge : edgeWeightMap.keySet()) {
                String sourceNodeId = edge.substring(0, edge.indexOf("-"));
                String destinationNodeId = edge.substring(edge.indexOf("-") + 1);

                graphWriter.println("    " +
                        sourceNodeId +
                        " -> " +
                        destinationNodeId +
                        " [Weight=" +
                        edgeWeightMap.get(edge) +
                        "];"
                );
            }

            graphWriter.println("}");
            graphWriter.close();
        } catch (FileNotFoundException e) {
            // Here, since all we are doing is creating a new file to write it, this error is not expected to happen.
            e.printStackTrace();
        }
    }
}
