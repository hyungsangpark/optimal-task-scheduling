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
    private final String testGraphDir = System.getProperty("user.dir") + System.getProperty("file.separator") + "src" +
            System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "graphs" +
            System.getProperty("file.separator");

    int numOfP = 2;

    @Rule
    public Timeout globalTimeout = new Timeout(5000);

    // TESTS

    @Test
    public void testSample() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testEmptyGraph() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test1NodeTwo() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5Nodes0Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(78, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5Nodes7Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5NodesMaximalEdges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5NodesSequentialEdges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test15Nodes10Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(271, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test15Nodes80Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes7OutTree() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(28, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void TestNodes8Random() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes9SeriesParallel() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes10Random() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void TestNodes11OutTree() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,1));
        assertEquals(350, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * PARALLEL
     */

    @Test
    public void testEmptyGraphP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test1NodeTwoP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5Nodes0EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(78, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5Nodes7EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5NodesMaximalEdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test5NodesSequentialEdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test15Nodes10EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(271, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void test15Nodes80EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes7OutTreeP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(28, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void TestNodes8RandomP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes9SeriesParallelP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testNodes10RandomP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void TestNodes11OutTreeP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(350, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    @Test
    public void testSampleP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, numOfP, outputFileName,4));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    private boolean testValidity(ValidOutputTester validOutputTester, String graphFileName, int numProcessors, String outputFileName, int cores) {
        GraphReader graphReader = GraphReader.getInstance();
        try {
            graphReader.loadGraphData(graphFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Only for testing
        TotalFCostCalculator.getInstance().resetGraphReader();

        AStar aStar2 = new AStar(numOfP,cores);
        ScheduleNode result = aStar2.aStarSearch();

        GraphWriter graphWriter = new GraphWriter();
        graphWriter.outputGraphData(outputFileName,result.getScheduleMap());

        GraphReader.getInstance().resetGraphReader();

        return validOutputTester.isValid(graphFileName, outputFileName, numProcessors);
    }

    private void deleteOutput(String outputFileName) {
        new File(outputFileName).delete();
    }
}
