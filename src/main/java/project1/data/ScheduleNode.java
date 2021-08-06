package project1.data;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.*;

public class ScheduleNode {

    private List<List<String>> _schedule;
    private ScheduleNode _parent;
    private int _totalF;
    private boolean isSchedEmpty;

    //For root schedule node(node with no tasks assigned)
    public ScheduleNode(int _processors) {
        _schedule = new ArrayList<>();

        for (int i = 0; i < _processors; i++) {
            _schedule.add(new ArrayList<>());
        }

        _totalF = 0;

        isSchedEmpty = true;
    }

    //For normal schedule nodes
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

    //Expand the tree and return newly created ScheduleNodes
    public List<ScheduleNode> expandTree(Graph taskGraph) {
        List<ScheduleNode> newSchedules = new ArrayList<>();

        List<String> scheduleableNodes = getTaskToSchedule(taskGraph);

        // go over each node that needs to be scheduled
        for(String n : scheduleableNodes) {
            // go over all the processors
            for(int i = 0; i < _schedule.size(); i++) {
                ScheduleNode newChildSchedule = new ScheduleNode(_schedule);

                // add new node task depending on whether transition cost is required
                newChildSchedule.addNewNodeTask(i,taskGraph.getNode(n),taskGraph);
                // calculate its heuristic
                newChildSchedule.setTotalFCost(taskGraph);
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

    // Find f(n) = g(n) + h(n)
    public void setTotalFCost(Graph graph) {
        //Find G cost
        int GCost = getCost();

        //Find Heuristics Cost
        int HCost = 0;
        for (String n: tasksLeft(graph)) {
            HCost += (int)(double)graph.getNode(n).getAttribute("Weight");
        }

        _totalF = GCost + HCost;
    }

//    private int calculateBottomLevelOfNode(Graph graph) {
//        int output = 0;
//
//        // find finishing task
//        Node finishingNode;
//
//        for(int i = 0; i < graph.nodes().count(); i++) {
//
//        }
//
//        return output;
//    }

    //Find G cost
    private int getCost() {
        int tempCost = 0;

        for(int i = 0; i < _schedule.size(); i++) {
            tempCost = Math.max(tempCost,_schedule.get(i).size());
        }

        return tempCost;
    }

    // Getter for f(n) pf this ScheduleNode
    public int get_totalF() {
        return _totalF;
    }

    // Scheduler to schedule tasks into _schedule
    private void addNewNodeTask(int pNum,Node taskNode,Graph graph) {
        // find all its parents
        List<String> parentsOfNode = new ArrayList<>();

        for(int i = 0; i < taskNode.enteringEdges().count(); i++) {
            parentsOfNode.add(taskNode.getEnteringEdge(i).getSourceNode().getId());
        }

        // if the node has no parents then add it
        if (parentsOfNode.size() == 0) {
            addNewNodeHelper(pNum,taskNode.getId(),(int)(double)taskNode.getAttribute("Weight"),0);
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
        int transitionTime = (int)(double)graph.getEdge("("+_schedule.get(parentPNum).get(latestEndTime-1)+";"+taskNode.getId()+")").getAttribute("Weight");

        // if same pNum then schedule node at first -1 * weight times
        if(parentPNum == pNum) {
            addNewNodeHelper(pNum,taskNode.getId(),(int)(double)taskNode.getAttribute("Weight"),0);
        }
        // else find first -1 then add transition time then add node weight times
        else {
            addNewNodeHelper(pNum,taskNode.getId(),(int)(double)taskNode.getAttribute("Weight"),transitionTime+latestEndTime);
        }
    }

    private void addNewNodeHelper(int pNum, String node, int weight, int edgeCost) {
        List<String> processor = _schedule.get(pNum);

        for (int i = 0; i < edgeCost; i++) {
            processor.add("-1");
        }
        for(int i = 0; i < weight; i++) {
            processor.add(node);
        }
    }

    // find the remaining tasks that needs to be scheduled
    private List<String> tasksLeft(Graph graph) {
        List<String> nodesDone = getTasksInScheduleNode();
        List<String> output = new ArrayList<>();

        for (int i = 0; i < graph.nodes().count(); i++) {
            if (!nodesDone.contains(graph.getNode(i).getId())) {
                output.add(graph.getNode(i).getId());
            }
        }
        return output;
    }

    // may or may not work currently plz check back again later
    public List<String> getTaskToSchedule(Graph taskGraph) {
        List<String> schedulableNodes = new ArrayList<>();

        // get task graph as input
        for (Node n : taskGraph) {
            boolean parentsComplete = true;

            // if the node is not already scheduled
            if (!getTasksInScheduleNode().contains(n.getId())) {
                // check if its parents have already been done
                for (int i = 0; i < n.enteringEdges().count(); i++) {
                    if (!getTasksInScheduleNode().contains(n.getEnteringEdge(i).getSourceNode().getId())) {
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

    // Return a List of tasks that has been scheduled
    public List<String> getTasksInScheduleNode() {
        List<String> output = new ArrayList<>();

        for (List<String> processor : _schedule) {
            for (String partialTask: processor) {
                if (!partialTask.equals("-1")) {
                    output.add(partialTask);
                }
            }
        }

        // Find unique tasks
        Set<String> set = new HashSet<>(output);
        output.clear();
        output.addAll(set);

        return output;
    }

    // Check if there is any tasks left in this schedule node, it there is not, this is a goal state
    public boolean isTarget(Graph graph) {
        return tasksLeft(graph).size() == 0;
    }

    // Getter for _schedule
    public List<List<String>> getSchedule() {
        return _schedule;
    }
}
//package project1.data;
//
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ScheduleNode {
//
//    private List<List<String>> _schedule;
//    //private List<ScheduleNode> _children = new ArrayList<>();
//    private ScheduleNode _parent;
//    private String _name;
//    private int _totalF;
//    private int _totalScheduleTime = 0;
//
//    //For normal schedule nodes
//    public ScheduleNode(List<List<String>> schedule, ScheduleNode parent, String name) {
//        _schedule = schedule;
//        _parent = parent;
//        _name = name;
//    }
//
//    //For root schedule node(node with no tasks assigned)
//    public ScheduleNode(List<List<String>> schedule, String name) {
//        _schedule = schedule;
//        _name = name;
//    }
//
//    public void findTaskChildren() {
//
//    }
//
//    public List<List<String>> getSchedule() {
//        return _schedule;
//    }
//
//    public String getScheduleNodeName() {
//        return _name;
//    }
//
//    public void setHeuristics(int num) {
//        _totalF = num;
//    }
//
//    public void set_totalScheduleTime(int time) {
//        _totalScheduleTime = _totalScheduleTime + time;
//    }
//}
