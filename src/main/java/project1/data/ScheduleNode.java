package project1.data;

import javafx.scene.Parent;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class ScheduleNode {

    private List<List<String>> _schedule;
    private ScheduleNode _parent;

    private int _heuristics;

    private boolean isSchedEmpty;

    public ScheduleNode(int _processors) {
        _schedule = new ArrayList<>();

        for (int i = 0; i < _processors; i++) {
            _schedule.add(new ArrayList<>());
        }

        _heuristics = 0;

        isSchedEmpty = true;
    }

    public ScheduleNode(List<List<String>> schedule) {
        _schedule = new ArrayList<>();

        for (int i = 0;i < schedule.size(); i++) {
            _schedule.add(new ArrayList<>());
            for (int j = 0; j < schedule.get(i).size(); j++) {
                _schedule.get(i).add(schedule.get(i).get(j));
            }
        }

        isSchedEmpty = false;
    }

    public List<ScheduleNode> expandTree(Graph taskGraph) {
        List<ScheduleNode> newSchedules = new ArrayList<>();

        List<String> scheduleableNodes = getNodesToSchedule(taskGraph);

        // go over each node that needs to be scheduled
        for(String n : scheduleableNodes) {
            // go over all the processors
            for(int i = 0; i < _schedule.size(); i++) {
                ScheduleNode newChildSchedule = new ScheduleNode(_schedule);

                // add new node task depending on whether transition cost is required
                newChildSchedule.addNewNodeTask(i,taskGraph.getNode(n),taskGraph);
                // calculate its heuristic
                newChildSchedule.setHeuristics(taskGraph);
                // add it to new schedules list
                newSchedules.add(newChildSchedule);

                // if schedule is empty and only 1 scheduleable node then only do it once
                if (isSchedEmpty && scheduleableNodes.size() == 1) {
                    break;
                }
            }

        }

        return newSchedules;
    }

    public void setHeuristics(Graph graph) {
        int cost = getCost();

        int totalH = 0;
        for (String n: nodesLeft(graph)) {
            totalH += (int)(double)graph.getNode(n).getAttribute("Weight");
        }

        _heuristics = cost + totalH;
    }

    private int getCost() {
        int tempCost = 0;

        for(int i = 0; i < _schedule.size(); i++) {
            tempCost = Math.max(tempCost,_schedule.get(i).size());
        }

        return tempCost;
    }

    public int getHeristic() {
        return _heuristics;
    }

    private void addNewNodeTask(int pNum,Node node,Graph graph) {
        // find all its parents
        List<String> parentsOfNode = new ArrayList<>();

        for(int i = 0; i < node.enteringEdges().count(); i++) {
            parentsOfNode.add(node.getEnteringEdge(i).getSourceNode().getId());
        }

        // if the node has no parents then add it
        if (parentsOfNode.size() == 0) {
            addNewNodeHelper(pNum,node.getId(),(int)(double)node.getAttribute("Weight"),0);
            return;
        }
        // else check last parent to complete
        int latestEndTime = 0;
        int parentPNum = 0;

        for (int i = 0; i < _schedule.size(); i++) {
            int pTotalTime = _schedule.get(i).size();

            for(int j = 0; j < pTotalTime; j++) {
                if (parentsOfNode.contains(_schedule.get(i).get(j)) && j > latestEndTime) {
                    latestEndTime = j;
                    parentPNum = i;
                }
            }
        }

        latestEndTime++;

        // find its end time, pNum and transition time
        int transitionTime = (int)(double)graph.getEdge("("+_schedule.get(parentPNum).get(latestEndTime-1)+";"+node.getId()+")").getAttribute("Weight");

        // if same pNum then schedule node at first -1 * weight times
        if(parentPNum == pNum) {
            addNewNodeHelper(pNum,node.getId(),(int)(double)node.getAttribute("Weight"),0);
        }
        // else find first -1 then add transition time then add node weight times
        else {
            addNewNodeHelper(pNum,node.getId(),(int)(double)node.getAttribute("Weight"),transitionTime+latestEndTime);
        }
    }

    private List<String> nodesLeft(Graph graph) {
        List<String> nodesDone = getNodesInSchedule();
        List<String> output = new ArrayList<>();

        for (int i = 0; i < graph.nodes().count(); i++) {
            if (!nodesDone.contains(graph.getNode(i).getId())) {
                output.add(graph.getNode(i).getId());
            }
        }

        return output;
    }

    private void addNewNodeHelper(int pNum, String node, int weight, int transCost) {
        List<String> processor = _schedule.get(pNum);

        for (int i = 0; i < transCost; i++) {
            processor.add("-1");
        }
        for(int i = 0; i < weight; i++) {
            processor.add(node);
        }
    }


    // may or may not work currently plz check back again later
    public List<String> getNodesToSchedule(Graph taskGraph) {
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

                if (parentsComplete) {
                    schedulableNodes.add(n.getId());
                }
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

    public boolean isTarget() {
        return false;
    }

    public List<List<String>> getSchedule() {
        return _schedule;
    }
}
