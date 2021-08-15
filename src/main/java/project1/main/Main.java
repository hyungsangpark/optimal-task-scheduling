package project1.main;

import org.apache.commons.cli.*;
import org.graphstream.graph.Graph;
import project1.algorithm.Astar;
import project1.data.ScheduleNode;
import project1.util.GraphLoader;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

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
//        int numProcessors = 1;
//
//        // Default values for optionals.
//        int numParallelCores = 1;
//        boolean isVisualized = false;
//        String outputName = null;

        Parameters parameters = Parameters.getInstance();

        // First, parse num of processors, if invalid num of processors, print error and exit program.
        try {
            parameters.setNumProcessors(Integer.parseInt(args[1]));
            if (parameters.getNumProcessors() < 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException nfe) {
            System.err.println("ERROR: Invalid number of processors. Please enter an integer number of processors that is greater than 1 to schedule the graph.");
            System.exit(1);
        }

        // Parse optional parameters.
        Options options = new Options();
        options.addOption(new Option("p", true, "number of cores for execution in parallel."));
        options.addOption(new Option("v", false, "visualise the search."));
        options.addOption(new Option("o", true, "set output file name, including file extension."));

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            // Parse parameter "-p" when available. If parameter present, also check its validity. If not, exit program.
            if (cmd.hasOption("p")) {
                try {
                    parameters.setNumParallelCores(Integer.parseInt(cmd.getOptionValue("p")));
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid number of cores provided.");
                    System.exit(1);
                }
                System.out.println("ERROR: Parallelization is unimplemented yet. The scheduler will run with a single core.");
            }

            // Check status of isVisualised based on presence of "-v" parameter.
            parameters.setVisualised(cmd.hasOption("v"));
            if (parameters.isVisualised()) System.out.println("ERROR: Visualisation is unimplemented yet. The scheduler will run without a visualiser.");

            // Parse parameter "-o" when available. Output name can be anything hence doesn't require checking.
            if (cmd.hasOption("o")) {
                parameters.setOutputName(cmd.getOptionValue("o"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Parse graph file name, and retrieve output name, either default or specified one.
        String graphFileName = args[0];
        if (parameters.getOutputName() == null) {
            parameters.setOutputName(graphFileName.replace(".dot", "-output.dot"));
        }

        // Read graph, run algorithm, then write graph.
        try {
            GraphLoader graphLoader = new GraphLoader();

            Graph graph = graphLoader.readGraph(graphFileName);

            // Record the start time.
            long startTime = System.nanoTime();

            // Run ALGORITHM to receive schedule.
            Astar newSearch = new Astar(graph, parameters.getNumProcessors());
            ScheduleNode result = newSearch.aStarSearch();
            graphLoader.formatOutputGraph(graph, result.getSchedule());

            // Record the end time.
            long endTime = System.nanoTime();
            long duration = (endTime - startTime)/1000;

            graphLoader.writeGraph(graph, parameters.getOutputName());

            // Output results.
            System.out.println("\nOutput written to file named: " + parameters.getOutputName());
            System.out.println("Time taken: " + duration + " ms");

            // Find out the finish time.
            result.getSchedule().sort(Comparator.comparingInt(List::size));
            System.out.println("Finish time of best schedule: " + result.getSchedule().get(result.getSchedule().size() - 1).size());
        } catch (IOException e) {
            System.err.println("ERROR: Graph file with the provided name is not found.");
            System.exit(1);
        }
    }
}