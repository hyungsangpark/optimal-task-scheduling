//package project1.main;
//
//import org.graphstream.graph.Edge;
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.SingleGraph;
//import project1.algorithm.Astar;
//
//import java.util.LinkedList;
//
//public class Main {
//
//    public static void main(String[] args) {
//		Main main = new Main();
//    	main.createGraph();
//    }
//
//    public void createGraph() {
//		//System.setProperty("org.graphstream.ui", "javafx");
//
//		Graph graph = new SingleGraph("Test");
//
//		graph.addNode("A").setAttribute("weight", 2);
//		graph.addNode("B").setAttribute("weight", 3);
//		graph.addNode("C").setAttribute("weight", 3);
//		graph.addNode("D").setAttribute("weight", 2);
//		graph.addEdge("AB", "A", "B", true).setAttribute("weight", 1);
//		graph.addEdge("AC", "A", "C", true).setAttribute("weight", 2);
//		graph.addEdge("BD", "B", "D", true).setAttribute("weight", 2);
//		graph.addEdge("CD", "C", "D", true).setAttribute("weight", 1);
//
////		int num = (int) graph.getEdge("AB").getAttribute("weight");
////		System.out.println(num);
//
//		//graph.display();
//		Astar newSearch = new Astar(graph, 2);
//		newSearch.aStarSearch();
//	}
//}