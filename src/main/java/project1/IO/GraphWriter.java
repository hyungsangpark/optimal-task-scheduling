package project1.IO;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class GraphWriter {

    public GraphWriter() {

    }

    public void outputGraphData(String outputFileName,List<List<String>> schedule) {
//        try {
//            PrintWriter graphWriter = new PrintWriter(outputFileName);
//
//            // Header
//            graphWriter.println("digraph \"" + _graphID + "\" {");
//
//            // Print Nodes.
//            graph.nodes().forEach(node -> {
//                graphWriter.println(new StringBuilder()
//                        .append("    ")
//                        .append(node.getId())
//                        .append(" [Weight=")
//                        .append((int)node.getAttribute("Weight"))
//                        .append(",Start=")
//                        .append((int)node.getAttribute("Start"))
//                        .append(",Processor=")
//                        .append((int)node.getAttribute("Processor"))
//                        .append("];")
//                );
//            });
//
//            // Print edges
//            graph.edges().forEach(edge -> {
//                graphWriter.println(new StringBuilder()
//                        .append("    ")
//                        .append(edge.getSourceNode().getId())
//                        .append(" -> ")
//                        .append(edge.getTargetNode().getId())
//                        .append(" [Weight=")
//                        .append((int)edge.getAttribute("Weight"))
//                        .append("];")
//                );
//            });
//
//            graphWriter.println("}");
//            graphWriter.close();
//        } catch (FileNotFoundException e) {
//            // Here, since all we are doing is creating a new file to write it, this error is not expected to happen.
//            e.printStackTrace();
//        }
    }
}
