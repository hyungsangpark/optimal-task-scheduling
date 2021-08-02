package project1.util;

//import project1.graph.Graph;
//import project1.graph.Task;

import org.graphstream.graph.Graph;

import java.io.*;

/**
 * Loads and unloads graph from .dot file.
 */
public class GraphLoader {

    /**
     * Prases a string text with weight notation from .dot file, and retrieves weight value in integer.
     * @param text A string text with weight notation.
     * @return A weight value in integer.
     */
    private static int parseWeight(String text) {
        return Integer.parseInt(text.replaceAll("[^0-9]+", ""));
    }

    /**
     * Reads a graph from a file with .dot file extension.
     * @param linkToGraphFile A link to the graph file, relative to the parent directory of the source folder.
     * @return A graph with nodes and edges read from the given file.
     * @throws IOException when a file with a given link to graph file cannot be found.
     */
    public static Graph readGraph(String linkToGraphFile) throws IOException {
        // Set up file reader.
        BufferedReader graphFileReader = new BufferedReader(new FileReader(linkToGraphFile));

        // Read header line and create a graph using the name provided.
        String header = graphFileReader.readLine();
        String[] headerList = header.split(" ");
        Graph graph = new Graph(headerList[1].replaceAll("\"", ""));

        // A flag to check whether the loop
        boolean readNextLine = true;

        // Read next line until the graph notation ends with a curly braces.
        while (readNextLine) {
            String line = graphFileReader.readLine();
            String[] lineComponents = line.trim().split(" +");
            // Pre-initialization to reduce time reinitializing.
            int weight;
            switch (lineComponents.length) {
                // Line containing task information.
                case 2:
                    weight = parseWeight(lineComponents[1]);
                    graph.addTask(lineComponents[0].charAt(0));
                    break;
                // Line containing edge information.
                case 4:
                    weight = parseWeight(lineComponents[3]);
                    graph.addEdge(lineComponents[0].charAt(0), lineComponents[2].charAt(0), weight);
                    break;
                // Line containing close curly braces.
                case 1:
                    readNextLine = false;
                    break;
            }
        }
        return graph;
    }

    public static void writeGraph(Graph graph, String fileName) throws FileNotFoundException {
        PrintWriter graphFileWriter = new PrintWriter(fileName);

        graphFileWriter.println("digraph \"" + graph.getName() + "\" {");

        // TODO: Write tasks in a graph
//        for (Task task : graph.getAllEdges) {
//            graphFileWriter.println("    " + task.getName() + "" + task.getDuration());
//        }

        // TODO: Write edges

    }

}
