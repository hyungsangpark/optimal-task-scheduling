import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import project1.IO.GraphReader;
import project1.IO.GraphWriter;
import project1.algorithm.AStar;
import project1.algorithm.TotalFCostCalculator;
import project1.data.ScheduleNode;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ValidityAndOptimalityTester {
    private final String graphDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" +
            System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "graphs" +
            System.getProperty("file.separator");

    int numOfP = 2;

    @Rule
    public Timeout globalTimeout = new Timeout(5000);

    @Test
    public void testEmptyGraph() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test1NodeTwo() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5Nodes0Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(78, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5Nodes7Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5NodesMaximalEdges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5NodesSequentialEdges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test15Nodes10Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(271, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test15Nodes80Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes7OutTree() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(28, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void TestNodes8Random() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes9SeriesParallel() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes10Random() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void TestNodes11OutTree() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(350, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testSample() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = graphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    private boolean testValidity(ValidOutputTester validOutputTester, String graphFileName, int numProcessors, String outputFileName) {
        GraphReader graphReader = GraphReader.getInstance();
        try {
            graphReader.loadGraphData(graphFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Only for testing
        TotalFCostCalculator.getInstance().resetGraphReader();
//        GraphLoader graphLoader = new GraphLoader();
//
//        Graph graph = null;
//        try {
//            graph = graphLoader.readGraph(graphFileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        DFS dfs = new DFS(graph, numOfP);
//        dfs.branchAndBoundStart();
        AStar aStar2 = new AStar(numOfP);
        ScheduleNode result = aStar2.aStarSearch();

        GraphWriter graphWriter = new GraphWriter();
        graphWriter.outputGraphData(outputFileName,result.getScheduleMap());

//        graphLoader.writeGraph(graph, outputFileName);

        GraphReader.getInstance().resetGraphReader();

        return validOutputTester.isValid(graphFileName, outputFileName, numProcessors);
    }

    private void deleteOutput(String outputFileName) {
        new File(outputFileName).delete();
    }
}
