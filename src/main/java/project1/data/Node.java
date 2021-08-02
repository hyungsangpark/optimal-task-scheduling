package project1.data;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<List<Character>> _processors;
    private List<Node> _children = new ArrayList<>();
    private Node _parent;

    public Node(List<List<Character>> processors, List<Character> tasks, int numOfChildren, Node parent) {
        _processors = processors;
        _parent = parent;
    }

    public void addChildren(int numOfChildren) {
        for (int i = 0; i < numOfChildren; i++) {
            System.out.println("Do something");
        }
    }
}
