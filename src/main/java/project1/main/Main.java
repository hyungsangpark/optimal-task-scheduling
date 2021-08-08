package project1.main;

import org.apache.commons.cli.*;
import org.graphstream.graph.Graph;
import project1.util.GraphLoader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        // Do a simple check on whether at least two arguments have been inputted.
        if (args == null || args.length < 2) {
            System.err.println("ERROR: Invalid number of arguments. " +
                    "Please provide the mandatory arguments in the following form:  " +
                    "java -jar scheduler.jar INPUT.dot P [OPTIONS]");
            System.exit(1);
        }

        // Part 1: Parse arguments.

        // Default value for the number of processors.
        // Since number of processors MUST be specified, it is deliberately set to 0 so that it would cause an error
        // with its default form.
        int numProcessors = 1;

        // Default values for optionals.
        int numParallelCores = 1;
        boolean isVisualized = false;
        String outputName = null;

        // First, parse num of processors, if invalid num of processors, print error and exit program.
        try {
            numProcessors = Integer.parseInt(args[1]);
            if (numProcessors < 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            System.err.println("ERROR: Invalid number of processors. Please enter an integer number of processors that is greater than 1 to schedule the graph.");
            System.exit(1);
        }

        // Parse optional parameters.
        Options parameters = new Options();
        parameters.addOption(new Option("p", true, "number of cores for execution in parallel."));
        parameters.addOption(new Option("v", false, "visualise the search."));
        parameters.addOption(new Option("o", true, "set output file name, including file extension."));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(parameters, args);

            // Parse parameter "-p" when available. If parameter present, also check its validity. If not, exit program.
            if (cmd.hasOption("p")) {
                try {
                    numParallelCores = Integer.parseInt(cmd.getOptionValue("p"));
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
            GraphLoader graphLoader = new GraphLoader();

            Graph graph = graphLoader.readGraph(graphFileName);

            // TODO: Run ALGORITHM to receive schedule.

            graphLoader.writeGraph(graph, outputName);
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