package project1.IO;

import project1.data.Processor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * This class writes the output of the A* algorithm into a dot file.
 */

public class GraphWriter {

    /**
     * This method changes the format of the optimal schedule to a format that can eb used for the output dot file.
     * @param schedule  The optimal schedule.
     * @return  HashMap that is right formatted for the output dot file.
     */

    private HashMap<String, int[]> formatOutputGraph(HashMap<Integer, Processor> schedule) {
        HashMap<String, int[]> nodeStartAndPNumMap = new HashMap<>();

        for (int i = 0; i < schedule.size(); i++) {
            Processor p = schedule.get(i);

            for (String nodeId : p.getNodesInScheduleMap().keySet()) {
                int startTime = p.getNodesInScheduleMap().get(nodeId);

                int[] startPnumArr = new int[2];
                startPnumArr[0] = startTime;
                startPnumArr[1] = p.getPid() + 1;

                nodeStartAndPNumMap.put(nodeId, startPnumArr);
            }
        }

        return nodeStartAndPNumMap;
    }

    /**
     * This method writes the optimal schedule into a new output dot file.
     * @param outputFileName    The name of the output file.
     * @param schedule  HashMap that represents the optimal schedule.
     */

    public void outputGraphData(String outputFileName, HashMap<Integer, Processor> schedule) {

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

            // Print edges
            for (String edge : edgeWeightMap.keySet()) {
                String sourceNodeId = edge.substring(0, edge.indexOf("-"));
                String destinationNodeId = edge.substring(edge.indexOf(">") + 1);

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
