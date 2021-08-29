package project1.main;

import org.apache.commons.cli.*;
import project1.IO.GraphReader;
import project1.IO.GraphWriter;
import project1.algorithm.AStar;
import project1.data.ScheduleNode;
import project1.gui.Visualiser;

import java.io.IOException;

/**
 * This Main class provides the run of the entire task scheduler.
 */
public class Main {

    /**
     * This method gets the input graph, schedules the tasks with the input number of processors,
     * and visualises the search of the solution through a GUI.
     */
    public static void main(String[] args) {

        // Parse arguments into parameters instance.
        Parameters parameters = parseArguments(args);

        // Read graph, run algorithm, then write graph.
        try {
            GraphReader graphReader = GraphReader.getInstance();
            graphReader.loadGraphData(parameters.getGraphFileName());

            if (parameters.isVisualised()) {
                Visualiser.main(new String[0]);
            } else {
                // Print line to notify the user that the schedule is being produced.
                System.out.println("Scheduling...");

                // Record the start time.
                long startTime = System.nanoTime();

                // Run ALGORITHM to receive schedule.
                AStar newSearch = new AStar(parameters.getNumProcessors(), parameters.getNumParallelCores());
                ScheduleNode result = newSearch.aStarSearch();

                // Measure the duration of the program and parse it into seconds and milliseconds.
                long duration = System.nanoTime() - startTime;
                int sec = (int) (duration / 1000000000);
                int ms = (int) (duration / 1000000) - sec * 1000;

                // Write the graph into an output file.
                GraphWriter graphWriter = new GraphWriter();
                graphWriter.outputGraphData(parameters.getOutputName(),result.getScheduleMap());

                // Output results.
                System.out.println("\nOutput written to file named: " + parameters.getOutputName());
                System.out.println("Time taken: " + sec + "." + String.format("%3s", ms).replace(' ', '0') + "s");
                System.out.println("Finish time of best schedule: " + result.getOptimalTime());
            }

        } catch (IOException e) {
            System.err.println("ERROR: Graph file with the provided name is not found.");
            System.exit(1);
        }
    }

    /**
     * Parses arguments in a string array format, into Parameters instance.
     * @param args String array of scheduler program arguments.
     * @return Parameters instance containing arguments parsed.
     */
    private static Parameters parseArguments(String[] args) {
        // Do a simple check on whether at least two arguments have been inputted.
        if (args == null || args.length < 2) {
            System.err.println("ERROR: Invalid number of arguments. " +
                    "Please provide the mandatory arguments in the following form:  " +
                    "java -jar scheduler.jar INPUT.dot P [OPTIONS]");
            System.exit(1);
        }

        // Instantiate Parameters singleton instance.
        Parameters parameters = Parameters.getInstance();

        // First, parse num of processors, if invalid num of processors, print error and exit program.
        try {
            parameters.setNumProcessors(Integer.parseInt(args[1]));
            if (parameters.getNumProcessors() < 1) {
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

        // Parse optional parameters using CommandLineParser
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
            }

            // Check status of isVisualised based on presence of "-v" parameter.
            parameters.setVisualised(cmd.hasOption("v"));

            // Parse parameter "-o" when available. Output name can be anything hence doesn't require checking.
            if (cmd.hasOption("o")) {
                parameters.setOutputName(cmd.getOptionValue("o"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Parse graph file name, and retrieve output name, either default or specified one.
        parameters.setGraphFileName(args[0]);
        if (parameters.getOutputName() == null) {
            parameters.setOutputName(parameters.getGraphFileName().replace(".dot", "-output.dot"));
        }

        return parameters;
    }
}