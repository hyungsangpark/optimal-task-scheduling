package project1.main;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import project1.algorithm.Astar;
import project1.util.GraphLoader;

import java.io.IOException;
import java.util.LinkedList;

public class Main {

	private Graph graph;

    public static void main(String[] args) {
		Main main = new Main();
    	main.createGraph();
    }

    public void createGraph() {
		try {
			graph = GraphLoader.readGraph("./src/test/graphs/sample.dot");
		} catch (IOException e) {
			e.printStackTrace();
		}

//		Astar newSearch = new Astar(graph, 2);
//		newSearch.aStarSearch();
	}
}