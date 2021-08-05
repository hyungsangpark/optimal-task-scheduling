package project1.main;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import project1.algorithm.Astar;
import project1.data.ScheduleNode;
import project1.util.GraphLoader;

import java.io.IOException;
import java.util.LinkedList;
import org.apache.commons.cli.*;
import org.graphstream.graph.Graph;
import project1.util.GraphLoader;

import java.io.IOException;

public class Main {

	private Graph graph;

    public static void main(String[] args) {
    	GraphLoader graphLoader = new GraphLoader();

        // Part 1: Parse arguments.

        int numParallelCores = 1;
        boolean isVisualized = false;
        String outputName = null;
        int numProcessors;

        try {
            numProcessors = Integer.parseInt(args[1]);
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

        String graphFileName = args[0];
        outputName = outputName == null ? graphFileName.replace(".dot", "-output.dot") : outputName;

        try {
            Graph graph = graphLoader.readGraph(graphFileName);

            // TODO: Run ALGORITHM to receive schedule.
            Astar newSearch = new Astar(graph, 2);
            ScheduleNode result = newSearch.aStarSearch();
            System.out.println(result);

            graphLoader.writeGraph(graph, outputName);
        } catch (IOException e) {
            System.err.println("ERROR: Graph file with the provided name is not found.");
            System.exit(1);
        }
    }
}