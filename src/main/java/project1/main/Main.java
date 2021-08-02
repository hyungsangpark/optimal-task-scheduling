package project1.main;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

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
		graph.addEdge("AB", "A", "B");
		graph.addEdge("AC", "A", "C");
		graph.addEdge("BD", "B", "D");
		graph.addEdge("CD", "C", "D");

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

		aStarSearch(graph, 2);
	}

	public void aStarSearch(Graph graph, int processors) {

    	LinkedList<Node> openList = new LinkedList<>();
		LinkedList<Node> closedList = new LinkedList<>();
		Node chosen;

		Node node = graph.getNode(0);
		closedList.add(node);
		LinkedList<Node> childrenList = getChildren(graph, node);
		//if (!childrenList.isEmpty()) {
			chosen = childrenList.get(0);
			int nodeWeight = (int) chosen.getAttribute("weight");
			String edge = node.toString() + chosen.toString();
			int totalWeight = nodeWeight + (int) graph.getEdge(edge).getAttribute("weight");
		//}
		for(int i=0; i<childrenList.size() - 1; i++) {
			Node next = childrenList.get(i+1);
			String nextEdge = node.toString() + next.toString();
			int nextTotalWeight = (int) node.getAttribute("weight") + (int) graph.getEdge(nextEdge).getAttribute("weight");
			if (nextTotalWeight < totalWeight) {
				chosen = next;
			}
		}
		closedList.add(chosen);
		System.out.println(closedList);
	}

	public LinkedList<Node> getChildren(Graph graph, Node node) {

		LinkedList<Node> children = new LinkedList<>();

		for(int i=0; i<node.getOutDegree(); i++) {
			Node n = node.getEdge(i).getNode1();
			children.add(n);
		}
		//for(int i=0; i<node.getOutDegree(); i++) {
		//	System.out.println(children.get(i));
		//}
		return children;
	}
}