package project1.main;

import org.apache.commons.cli.*;
import org.graphstream.graph.Graph;
import project1.util.GraphLoader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // Part 1: Parse arguments.

        // Configurations available in this code, in their default values.
        int numParallelCores = 1;
        boolean isVisualized = false;
        String outputName = null;
        // numProcessors does not have a default value since it must be specified when the program runs.
        int numProcessors;

        // First, parse num of processors, if invalid num of processors, print error and exit program.
        try {
            numProcessors = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.err.println("ERROR: Invalid number of processors. Please enter an integer number of processors to schedule the graph on.");
            System.exit(1);
        }

        // Parse optional parameters.
        Options parameters = new Options();
        parameters.addOption(new Option("p", true, "number of cores for execution in parallel."));
        parameters.addOption(new Option("v", false, "visualise the search"));
        parameters.addOption(new Option("o", true, "set output file name"));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(parameters, args);

            // Parse parameter "-p" when available. If parameter present, also check its validity. If not, exit program.
            if (cmd.hasOption("p")) {
                try {
                    int numCores = Integer.parseInt(cmd.getOptionValue("p"));
                    System.out.println("Great!, Num cores = " + numCores);
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid number of cores provided.");
                    System.exit(1);
                }
            }

            // Check status of isVisualised based on presence of "-v" parameter.
            isVisualized = cmd.hasOption("v");

            // Parse parameter "-o" when available. Output name can be anything hence doesn't require checking.
            if (cmd.hasOption("o")) {
                outputName = cmd.getOptionValue("o");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Parse graph file name, and retrieve output name, either default or specified one.
        String graphFileName = args[0];
        outputName = outputName == null ? graphFileName.replace(".dot", "-output.dot") : outputName;

        // Read graph, run algorithm, then write graph.
        try {
            Graph graph = GraphLoader.readGraph(graphFileName);

            // TODO: Run ALGORITHM to receive schedule.

            GraphLoader.writeGraph(graph, outputName);
        } catch (IOException e) {
            System.err.println("ERROR: Graph file with the provided name is not found.");
            System.exit(1);
        }

        // Output results.
        System.out.println("\nOutput written to file named: " + outputName);
        System.out.println("Time taken: " + 0);
        System.out.println("Finish time of best schedule: " + 0);
    }

}