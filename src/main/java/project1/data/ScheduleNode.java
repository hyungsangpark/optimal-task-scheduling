package project1.data;

import project1.IO.GraphReader;
import project1.algorithm.TotalFCostCalculator;

import java.util.*;

public class ScheduleNode {

    private List<List<String>> _schedule;
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

        for (int i = 0; i < schedule.size(); i++) {
            _schedule.add(new ArrayList<>());
            for (int j = 0; j < schedule.get(i).size(); j++) {
                _schedule.get(i).add(schedule.get(i).get(j));
            }
        }

        isSchedEmpty = false;
    }

    //Expand the tree and return newly created ScheduleNodes
    public List<ScheduleNode> expandTree() {
        List<ScheduleNode> newSchedules = new ArrayList<>();

        List<String> scheduleableNodes = getTaskToSchedule();

        // go over each node that needs to be scheduled
        for (String n : scheduleableNodes) {
            // go over all the processors
            for (int i = 0; i < _schedule.size(); i++) {
                ScheduleNode newChildSchedule = new ScheduleNode(_schedule);

                // add new node task depending on whether transition cost is required
                newChildSchedule.addNewNodeTask(i,n);
                // calculate its heuristic
                newChildSchedule.setTotalFCost();
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
    public void setTotalFCost() {
        // Bottom levels method
        TotalFCostCalculator totalFCostCalculator = new TotalFCostCalculator();
        _totalF = totalFCostCalculator.calculateTotalF(_schedule);

        // Old method

        //Find G cost
//        int GCost = getCost();
//
//        //Find Heuristics Cost
//        GraphReader graphReader = GraphReader.getInstance();
//        int HCost = 0;
//        for (String n : tasksLeft()) {
//            HCost += graphReader.getNodeWeightsMap().get(n);
//        }
//
//        _totalF = GCost + HCost;
    }

    //Find G cost
    private int getCost() {
        int tempCost = 0;

        for (int i = 0; i < _schedule.size(); i++) {
            tempCost = Math.max(tempCost, _schedule.get(i).size());
        }

        return tempCost;
    }

    // Getter for f(n) pf this ScheduleNode
    public int get_totalF() {
        return _totalF;
    }

    // Scheduler to schedule tasks into _schedule
    private void addNewNodeTask(int pNum, String nodeId) {

        // Get the nodes parents
        GraphReader graphReader = GraphReader.getInstance();
        HashMap<String,String[]> parentsOfNodeMap = graphReader.getParentsOfNodeMap();
        String[] parentsOfNodeToAdd = parentsOfNodeMap.get(nodeId);

        // if the node has no parents then add it
        if (parentsOfNodeToAdd == null) {
            addNewNodeHelper(pNum, nodeId,0);
            return;
        }

        // else check last parent to complete
        int earliestPossbileStartTime = 0;

        for (int i = 0; i < _schedule.size(); i++) {
            int pTotalTime = _schedule.get(i).size();

            for (int j = 0; j < pTotalTime; j++) {
                String parent = getParentFromArr(parentsOfNodeToAdd, _schedule.get(i).get(j));
                if (parent != null) {
                    int totalC = j;

                    if (i != pNum) {
                        int transitionTime = graphReader.getEdgeWeightMap().get(parent + "->" + nodeId);
                        totalC += transitionTime;
                    }

                    if (totalC > earliestPossbileStartTime) {
                        earliestPossbileStartTime = totalC;
                    }
                }
            }
        }

        earliestPossbileStartTime++;

        addNewNodeHelper(pNum, nodeId,earliestPossbileStartTime);
    }

    private String getParentFromArr(String[] arr, String value) {
        for (String s : arr) {
            if (s.equals(value)) {
                return s;
            }
        }

        return null;
    }

    private void addNewNodeHelper(int pNum, String nodeId, int earliestStartTime) {
        List<String> processor = _schedule.get(pNum);
        GraphReader graphReader = GraphReader.getInstance();
        int weight = graphReader.getNodeWeightsMap().get(nodeId);

        if (processor.size() > earliestStartTime) {
            for (int i = 0; i < weight; i++) {
                processor.add(nodeId);
            }
        } else {
            while (processor.size() != earliestStartTime) {
                processor.add("-1");
            }
            for (int i = 0; i < weight; i++) {
                processor.add(nodeId);
            }
        }
    }

    // find the remaining tasks that needs to be scheduled
    private List<String> tasksLeft() {
        List<String> nodesDone = getTasksInScheduleNode();
        List<String> output = new ArrayList<>();

        GraphReader graphReader = GraphReader.getInstance();

        String[] nodeIds = graphReader.getNodeIdArr();

        for (String nodeId : nodeIds) {
            if (!nodesDone.contains(nodeId)) {
                output.add(nodeId);
            }
        }
        return output;
    }

    // may or may not work currently plz check back again later
    public List<String> getTaskToSchedule() {
        List<String> schedulableNodes = new ArrayList<>();
        List<String> tasksInScheduleNode = getTasksInScheduleNode();

        GraphReader graphReader = GraphReader.getInstance();

        String[] nodeIds = graphReader.getNodeIdArr();
        HashMap<String,String[]> parentsOfNodeMap = graphReader.getParentsOfNodeMap();

        for (String nodeId : nodeIds) {
            boolean parentsComplete = true;

            // if the task is not already scheduled
            if (!tasksInScheduleNode.contains(nodeId)) {
                // check if its parents have already been done
                if (parentsOfNodeMap.containsKey(nodeId)) {
                    String[] parents = parentsOfNodeMap.get(nodeId);
                    for (String parent : parents) {
                        if (!tasksInScheduleNode.contains(parent)) {
                            parentsComplete = false;
                            break;
                        }
                    }

                }

                if (parentsComplete) {
                    schedulableNodes.add(nodeId);
                }
            }
        }

        return schedulableNodes;
    }

    // Return a List of tasks that has been scheduled
    public List<String> getTasksInScheduleNode() {
        List<String> output = new ArrayList<>();

        for (List<String> processor : _schedule) {
            for (String partialTask : processor) {
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
    public boolean isTarget() {
        return tasksLeft().size() == 0;
    }

    // Getter for _schedule
    public List<List<String>> getSchedule() {
        return _schedule;
    }

    public void setTotalF(int totalF) {
        _totalF = totalF;
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
