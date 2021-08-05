package project1.data;

import javafx.scene.Parent;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleNode {

    private List<List<String>> _schedule;
    private ScheduleNode _parent;

    private int _heuristics;

    public ScheduleNode(int _processors) {
        for (int i = 0; i < _processors; i++) {
            _schedule.add(new ArrayList<>());
        }

        _heuristics = 0;
    }

    public ScheduleNode(List<List<String>> schedule) {
        _schedule = schedule;
    }

    public List<ScheduleNode> expandTree(Graph taskGraph) {
        List<ScheduleNode> newSchedules = new ArrayList<>();

        List<String> schedulableNodes = nodesToSchedule(taskGraph);

        // go over each node that needs to be scheduled
        for(String n : schedulableNodes) {
            // go over all the processors
            for(int i = 0; i < _schedule.size(); i++) {
                ScheduleNode newChildeSchedule = new ScheduleNode(_schedule);

                // add new node task depending on whether transition cost is required

                // calculate its heuristic
            }

        }

        return newSchedules;
    }

    private void addNewNodeTask(int pNum,Node node) {
        // find all its parents
        List<Node> parentsOfNode = new ArrayList<>();

        

        // if the node has no parents then add it

        // else

        // check last parent to complete

        // find its end time, pNum and transition time

        // if same pNum then schedule node at first -1 * weight times

        // else find first -1 then add transition time then add node weight times
    }

    private void addNewNodeHelper(int pNum, String node, int weight, int transCost) {
        for (int i = 0; i < transCost; i++) {
            _schedule.get(pNum).add("-1");
        }
        for(int i = 0; i < weight; i++) {
            _schedule.get(pNum).add(node);
        }
    }


    // may or may not work currently plz check back again later
    public List<String> nodesToSchedule(Graph taskGraph) {
        List<String> schedulableNodes = new ArrayList<>();

        // get task graph as input
        for (Node n : taskGraph) {
            boolean parentsComplete = true;

            // if the node is not already scheduled
            if (!getNodesInSchedule().contains(n.getId())) {
                // check if its parents have already been done
                for (int i = 0; i < n.enteringEdges().count(); i++) {
                    if (!getNodesInSchedule().contains(n.getEnteringEdge(i).getSourceNode().getId())) {
                        parentsComplete = false;
                    }
                }
            }

            if (parentsComplete) {
                schedulableNodes.add(n.getId());
            }
        }

        return schedulableNodes;
    }

    // potential exceptions with this plz fix later
    public List<String> getNodesInSchedule() {
        List<String> output = new ArrayList<>();

        for (List<String> processor : _schedule) {
            for (String partialTask: processor) {
                if (!partialTask.equals("-1")) {
                    output.add(partialTask);
                }
            }
        }

        Set<String> set = new HashSet<>(output);
        output.clear();
        output.addAll(set);

        return output;
    }

    public void setHeuristics(int num) {
        _heuristics = num;
    }

    public int getCost() {
        // returns cost;
        return 0;
    }

    public boolean isTarget() {
        return false;
    }

    public List<List<String>> getSchedule() {
        return _schedule;
    }
}
