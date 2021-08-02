package project1.util;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkDOT;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.*;

/**
 * Loads and unloads graph from .dot file.
 */
public class GraphLoader {

    /**
     * Reads a graph from a file with .dot file extension.
     * @param linkToGraphFile A link to the graph file, relative to the parent directory of the source folder.
     * @return A graph with nodes and edges read from the given file.
     * @throws IOException when a file with a given link to graph file cannot be found.
     */
    public static Graph readGraph(String linkToGraphFile) throws IOException {
        Graph graph = new SingleGraph(linkToGraphFile);
        FileSource fileSource = new FileSourceDOT();

        fileSource.addSink(graph);
        fileSource.readAll(linkToGraphFile);

        return graph;
    }

    /**
     * Writes a given graph instance in a form of .dot file.
     * @param graph A graph to write .dot file from.
     * @param outputFileName Name of the output graph file.
     */
    public void writeGraph(Graph graph, String outputFileName) {
        FileSink fileSink = new FileSinkDOT(true);

        try {
            fileSink.writeAll(graph, outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
