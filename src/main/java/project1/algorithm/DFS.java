package project1.algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.data.DfsScheduleNode;

import java.util.ArrayList;
import java.util.List;

public class DFS {
    private Graph _graph;
    private int _numOfProcessors;

    public DFS(Graph graph, int numOfProcessors) {
        _graph = graph;
        _numOfProcessors = numOfProcessors;
    }

    public void branchAndBoundStart() {
        Node rootNode = _graph.getNode(0);

        List<List<String>> rootSchedule = createEmptySchedule();
        // Parent is null because this is the root and lastUsedProcessor
        // is -1 because no processors were used previously
        DfsScheduleNode rootScheduleNode = new DfsScheduleNode(rootSchedule, null, -1, 0);

        for (int i = 0; i < _numOfProcessors; i++) {
            List<List<String>> childSchedule = addTask(createEmptySchedule(), i, rootNode.getId(), 0,
                    (int)rootNode.getAttribute("Weight"));

            branchAndBound(new DfsScheduleNode(childSchedule, rootScheduleNode, i,
                    (int)rootNode.getAttribute("Weight")), rootNode);
        }
    }

    // Assume that the nodes are sorted.
    public List<> branchAndBound(DfsScheduleNode currentSchedule, Node currentNode) {
        Node childNode;
        Edge edge;
        int waitTime = 0;
        int endTime = 0;

        for (int i = 0; i < _numOfProcessors; i++) {
            for (int j = 0; j < currentNode.getOutDegree(); j++) {
                edge = currentNode.getEdgeToward(j);
                childNode = edge.getTargetNode();

                if (currentSchedule.getLastUsedProcessorNum() != currentSchedule.getLastUsedProcessorNum()) {
                    waitTime = (int)edge.getAttribute("Weight");
                    endTime = waitTime + (int) childNode.getAttribute("Weight");
                }

                List<List<String>> childSchedule = addTask(currentSchedule.getSchedule(), i,
                        childNode.getId().substring(0, 1), waitTime, (int) childNode.getAttribute("Weight"));
                branchAndBound(new DfsScheduleNode(childSchedule, currentSchedule, i, endTime), childNode);
            } 
        }

        // now think about how to do bounding.
        // Carry on by changing the implementation so it fits the purpose that Hyung explained to me about.
    }

    public List<List<String>> addTask(List<List<String>> schedule, int processorNum, String task,
                                         int waitTime, int weight) {
        for (int i = 0; i < waitTime; i++) {
            schedule.get(processorNum).add(null);
        }

        for (int i = 0; i < weight; i++) {
            schedule.get(processorNum).add(task);
        }
        return schedule;
    }

    public List<List<String>> createEmptySchedule() {
        List<List<String>> emptySchedule = new ArrayList<>();
        for (int i = 0; i < _numOfProcessors; i++) {
            emptySchedule.add(new ArrayList<>());
        }

        return emptySchedule;
    }
}
