package project1.util;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.*;
import java.util.List;

/**
 * This class loads and unloads the graph from the .dot file.
 */
public class GraphLoader {

    private String _graphID;

    /**
     * This method reads a graph from a file with .dot file extension.
     * @param linkToGraphFile A link to the graph file, relative to the parent directory of the source folder.
     * @return A graph with nodes and edges read from the given file.
     * @throws IOException when a file with a given link to graph file cannot be found.
     */
    public Graph readGraph(String linkToGraphFile) throws IOException {

        // Read graph ID manually, due to graphstream not loading graph ID properly.
        BufferedReader graphFileReader = new BufferedReader(new FileReader(linkToGraphFile));
        String header = graphFileReader.readLine();
        // Extract graph ID from header line of .dot file.
        _graphID = header.substring(9, header.length() - 3);
        graphFileReader.close();

        // Create graph and load the information from graph file to the graph instance.
        Graph graph = new SingleGraph(linkToGraphFile);
        FileSource fileSource = new FileSourceDOT();

        fileSource.addSink(graph);
        fileSource.readAll(linkToGraphFile);

        // Change file type of weight attribute of nodes and edges into integer.
        graph.nodes().forEach(node -> {
            Double weight = (Double)node.getAttribute("Weight");
            node.setAttribute("Weight", weight.intValue());
        });
        graph.edges().forEach(edge -> {
            Double weight = (Double)edge.getAttribute("Weight");
            edge.setAttribute("Weight", weight.intValue());
        });

        return graph;
    }

    /**
     * This method creates the output graph in the desired format.
     * @param graph A Hashmap in the right format representing the scheduled tasks.
     * @param schedule A list of list of strings containing tasks scheduled on each processor.
     */
    public void formatOutputGraph(Graph graph, List<List<String>> schedule) {
        for (int i = 0; i < schedule.size(); i++) {
            for (int j = 0; j < schedule.get(i).size(); j++) {
                if (!schedule.get(i).get(j).equals("-1")) {
                    Node node = graph.getNode(schedule.get(i).get(j));
                    if(!node.hasAttribute("Start")) {
                        node.setAttribute("Start",j);
                        node.setAttribute("Processor",i+1);
                    }
                }
            }
        }
    }

    /**
     * This method writes a given graph instance in a form of .dot file.
     * @param graph A graph to write the .dot file from.
     * @param outputFileName Name of the output graph file.
     */
    public void writeGraph(Graph graph, String outputFileName) {
        try {
            PrintWriter graphWriter = new PrintWriter(outputFileName);

            // Header
            graphWriter.println("digraph \"" + _graphID + "\" {");

            // Print nodes
            graph.nodes().forEach(node -> {
                graphWriter.println(new StringBuilder()
                        .append("    ")
                        .append(node.getId())
                        .append(" [Weight=")
                        .append((int)node.getAttribute("Weight"))
                        .append(",Start=")
                        .append((int)node.getAttribute("Start"))
                        .append(",Processor=")
                        .append((int)node.getAttribute("Processor"))
                        .append("];")
                );
            });

            // Print edges
            graph.edges().forEach(edge -> {
                graphWriter.println(new StringBuilder()
                        .append("    ")
                        .append(edge.getSourceNode().getId())
                        .append(" -> ")
                        .append(edge.getTargetNode().getId())
                        .append(" [Weight=")
                        .append((int)edge.getAttribute("Weight"))
                        .append("];")
                );
            });

            graphWriter.println("}");
            graphWriter.close();
        } catch (FileNotFoundException e) {
            // Here, since all we are doing is creating a new file to write it, this error is not expected to happen.
            e.printStackTrace();
        }
    }

}