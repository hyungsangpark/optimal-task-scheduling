package project1.algorithm;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;

public class Astar {

    public Graph _graph;
    public int _processors;
    public LinkedList<Node> _openList = new LinkedList<>();
    public LinkedList<Node> _closedList = new LinkedList<>();

    public Astar(Graph graph, int processors) {
        _graph = graph;
        _processors = processors;
    }

    public void aStarSearch() {

        Node chosen;

        Node node = _graph.getNode(0);
        _closedList.add(node);
        LinkedList<Node> childrenList = getChildren(node);

        //if (!childrenList.isEmpty()) {
        chosen = childrenList.get(0);
        int nodeWeight = (int) chosen.getAttribute("weight");
        String edge = node.toString() + chosen;
        int totalWeight = nodeWeight + (int) _graph.getEdge(edge).getAttribute("weight");
        //}

        for(int i=0; i<childrenList.size() - 1; i++) {
            Node next = childrenList.get(i+1);
            String nextEdge = node + next.toString();
            int nextTotalWeight = (int) node.getAttribute("weight") + (int) _graph.getEdge(nextEdge).getAttribute("weight");
            if (nextTotalWeight < totalWeight) {
                chosen = next;
            }
        }
        _closedList.add(chosen);
        System.out.println(_closedList);
    }

    public LinkedList<Node> getChildren(Node node) {

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
