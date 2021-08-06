package project1.main;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import project1.algorithm.DFS;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.createGraph();
    }

    public void createGraph() {
        //System.setProperty("org.graphstream.ui", "javafx");

        Graph graph = new SingleGraph("Test");

        graph.addNode("A").setAttribute("Weight", 2);
        graph.addNode("B").setAttribute("Weight", 3);
        graph.addNode("C").setAttribute("Weight", 3);
        graph.addNode("D").setAttribute("Weight", 2);
        graph.addEdge("(A;B)", "A", "B", true).setAttribute("Weight", 1);
        graph.addEdge("(A;C)", "A", "C", true).setAttribute("Weight", 2);
        graph.addEdge("(B;D)", "B", "D", true).setAttribute("Weight", 2);
        graph.addEdge("(C;D)", "C", "D", true).setAttribute("Weight", 1);

        //int num = (int) graph.getEdge("AB").getAttribute("weight");
        //System.out.println(num);

        //graph.display();
        DFS dfs = new DFS(graph, 3);
        graph = dfs.branchAndBoundStart();
        graph.display();
    }
}