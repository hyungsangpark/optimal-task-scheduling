package project1.main;

import org.graphstream.graph.Graph;
import project1.algorithm.Astar;
import project1.data.ScheduleNode;
import project1.util.GraphLoader;

import java.io.File;
import java.io.IOException;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
//        File[] files = new File("./src/test/graphs/").listFiles();
//
//        for (File file : files) {
//            if (file.isFile()) {
//                String test = file.getPath();
//                run(args,file.getAbsolutePath());
//            }
//        }
        run(args,"./src/test/graphs/sample.dot");
    }

    public static void run(String[] args,String gFileName) {
        GraphLoader graphLoader = new GraphLoader();

        // Part 1: Parse arguments.

        int numParallelCores = 1;
        boolean isVisualized = false;
        String outputName = null;
        int numProcessors = 2;

        try {
            if ((args != null) && (args.length >= 1)) {
                numProcessors = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("ERROR: Invalid number of processors. Please enter an integer number of processors to schedule the graph on.");
            System.exit(1);
        }

        Options parameters = new Options();
        parameters.addOption(new Option("p", true, "number of cores for execution in parallel."));
        parameters.addOption(new Option("v", false, "visualise the search"));
        parameters.addOption(new Option("o", true, "set output file name"));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(parameters, args);

            if (cmd.hasOption("p")) {
                try {
                    int numCores = Integer.parseInt(cmd.getOptionValue("p"));
                    System.out.println("Great!, Num cores = " + numCores);
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid number of cores provided.");
                    System.exit(1);
                }
            }

            isVisualized = cmd.hasOption("v");

            if (cmd.hasOption("o")) {
                outputName = cmd.getOptionValue("o");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String graphFileName = gFileName;
        if ((args != null) && (args.length > 0)){
            graphFileName = args[0];
        }
        outputName = outputName == null ? graphFileName.replace(".dot", "-output.dot") : outputName;

        try {
            Graph graph = graphLoader.readGraph(graphFileName);

            // TODO: Run ALGORITHM to receive schedule.

            // temp
            Astar newSearch = new Astar(graph, numProcessors);
            ScheduleNode result = newSearch.aStarSearch();
            graphLoader.formatOutputGraph(graph,result.getSchedule());

            graphLoader.writeGraph(graph, outputName.replace("graphs","actualOutputs"));
        } catch (IOException e) {
            System.err.println("ERROR: Graph file with the provided name is not found.");
            System.exit(1);
        }
    }
}
//package project1.main;
//
//import org.graphstream.graph.Edge;
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.SingleGraph;
//import project1.algorithm.Astar;
//
//import java.util.LinkedList;
//
//public class Main {
//
//    public static void main(String[] args) {
//		Main main = new Main();
//    	main.createGraph();
//    }
//
//    public void createGraph() {
//		//System.setProperty("org.graphstream.ui", "javafx");
//
//		Graph graph = new SingleGraph("Test");
//
//		graph.addNode("A").setAttribute("weight", 2);
//		graph.addNode("B").setAttribute("weight", 3);
//		graph.addNode("C").setAttribute("weight", 3);
//		graph.addNode("D").setAttribute("weight", 2);
//		graph.addEdge("AB", "A", "B", true).setAttribute("weight", 1);
//		graph.addEdge("AC", "A", "C", true).setAttribute("weight", 2);
//		graph.addEdge("BD", "B", "D", true).setAttribute("weight", 2);
//		graph.addEdge("CD", "C", "D", true).setAttribute("weight", 1);
//
////		int num = (int) graph.getEdge("AB").getAttribute("weight");
////		System.out.println(num);
//
//		//graph.display();
//		Astar newSearch = new Astar(graph, 2);
//		newSearch.aStarSearch();
//	}
//}
