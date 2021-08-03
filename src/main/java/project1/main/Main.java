package project1.main;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import project1.algorithm.Astar;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
		Main main = new Main();
    	main.createGraph();
    }

    public void createGraph() {
		//System.setProperty("org.graphstream.ui", "javafx");

		Graph graph = new SingleGraph("Test");

		graph.addNode("A");
		graph.addNode("B");
		graph.addNode("C");
		graph.addNode("D");
		graph.addEdge("AB", "A", "B", true);
		graph.addEdge("AC", "A", "C", true);
		graph.addEdge("BD", "B", "D", true);
		graph.addEdge("CD", "C", "D", true);

		Node n = graph.getNode("A");
		n.setAttribute("weight", 2);
		Node n1 = graph.getNode("B");
		n1.setAttribute("weight", 3);
		Node n2 = graph.getNode("C");
		n2.setAttribute("weight", 3);
		Node n3 = graph.getNode("D");
		n3.setAttribute("weight", 2);


		Edge e = graph.getEdge("AB");
		e.setAttribute("weight", 1);
		Edge e1 = graph.getEdge("AC");
		e1.setAttribute("weight", 2);
		Edge e2 = graph.getEdge("BD");
		e2.setAttribute("weight", 2);
		Edge e3 = graph.getEdge("CD");
		e3.setAttribute("weight", 1);

		int num = (int) graph.getEdge("AB").getAttribute("weight");
		//System.out.println(num);

		//graph.display();
		Astar newSearch = new Astar(graph, 2);
		newSearch.aStarSearch();
	}
}