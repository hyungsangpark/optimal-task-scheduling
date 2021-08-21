import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import project1.util.GraphLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ValidOutputTester {

    private int finishTime;

    public static void main(String[] args) {
        ValidOutputTester solutionValidator = new ValidOutputTester();

        File[] files = new File("./src/test/graphs/").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String input = file.getPath();
                String output = file.getPath()
                        .replace("graphs", "actualOutputs")
                        .replace(".dot", "-output.dot");
                solutionValidator.isValid(input,output, 2);
            }
        }
    }

    public boolean isValid(String inputFile, String outputFile, int numOfProcessors) {
        GraphLoader graphLoader = new GraphLoader();

        Graph inputGraph = null;
        Graph outputGraph = null;

        try {
            inputGraph = graphLoader.readGraph(inputFile);
            outputGraph = graphLoader.readGraph(outputFile);
        } catch (IOException e) {
            System.out.println("The graphs were unable to be loaded due to some error. ");
            System.out.println(inputFile);
            System.out.println(outputFile);
        }

        // Check if they both have the same number of nodes
        if (inputGraph.getNodeCount() != outputGraph.getNodeCount()) {
            System.out.print("The input graph has: " + inputGraph.getNodeCount() + " nodes but output has: " + outputGraph.getNodeCount() + " nodes");
            return false;
        }
        // For empty graphs always return true
        else if (inputGraph.getNodeCount() == 0) {
            return true;
        }

        ArrayList<Processor> processors = new ArrayList<>();

        for (int i = 0; i < numOfProcessors; i++) {
            processors.add(i, new Processor());
        }

        HashMap<String,int[]> startAndPNumOfNode = new HashMap<>();

        // Check the two graphs have the same node weights
        for(int i = 0; i < inputGraph.getNodeCount(); i++) {
            Node inputNode = inputGraph.getNode(i);
            Node outputNode = outputGraph.getNode(i);

            int inputNodeWeight = (int)inputNode.getAttribute("Weight");
            int outputNodeWeight = (int)outputNode.getAttribute("Weight");

            if (inputNodeWeight != outputNodeWeight) {
                System.out.println("Input node id: " + inputGraph.getNode(i).getId() + "and Output node id: " + outputGraph.getNode(i).getId() + "have different weight values");
            }

            int startTime = ((Double)outputNode.getAttribute("Start")).intValue();
            int pNum = ((Double)outputNode.getAttribute("Processor")).intValue();

            startAndPNumOfNode.putIfAbsent(outputNode.getId(),new int[]{startTime,pNum,outputNodeWeight});

            if (!processors.get(pNum-1).checkIfProcessorFree(startTime,outputNodeWeight)) {
                System.out.println("A processor node overlaps with another node on processor " + pNum + " at start time: " + startTime + " and node weight " + outputNodeWeight);
                return false;
            }

            finishTime = Math.max(finishTime,startTime + outputNodeWeight);
        }

        for (int i = 0; i < inputGraph.getEdgeCount(); i++) {
            Edge edge = inputGraph.getEdge(i);

            String parent = edge.getSourceNode().getId();
            String child = edge.getTargetNode().getId();

            int[] parentValues = startAndPNumOfNode.get(parent);
            int[] childValues = startAndPNumOfNode.get(child);

            int transCost = 0;

            if (parentValues[1] != childValues[1]) {
                transCost = (int)edge.getAttribute("Weight");
            }

            if (childValues[0] < parentValues[0] + parentValues[2] + transCost) {
                System.out.println("The child: " + child + " was scheduled before the parent: " + parent + " was complete.");
                return false;
            }
        }


        return true;
    }

    private class Processor {
        ArrayList<Integer> startTimes = new ArrayList<>();
        ArrayList<Integer> finishTimes = new ArrayList<>();

        public boolean checkIfProcessorFree (int newStartTime, int weight) {
            for (int i = 0; i < startTimes.size(); i++) {
                if (newStartTime < finishTimes.get(i) && startTimes.get(i) < (newStartTime + weight)) {
                    return false;
                }
            }

            // Add new values
            startTimes.add(newStartTime);
            finishTimes.add(newStartTime + weight);

            return true;
        }
    }

    public int getFinishTime() {
        return finishTime;
    }
}
