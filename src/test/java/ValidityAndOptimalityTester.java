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

    @Rule
    public Timeout globalTimeout = new Timeout(5000);

    // ----------------------------------TESTING: 1 processes 1 core----------------------------------

    /**
     * Testing sample graph with the above requirement
     */
    @Test
    public void testSampleOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(9, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing EmptyGraph graph with the above requirement
     */
    @Test
    public void testEmptyGraphOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 1Node graph with the above requirement
     */
    @Test
    public void test1NodeTwoOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes0edges graph with the above requirement
     */
    @Test
    public void test5Nodes0EdgesOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes7Edges graph with the above requirement
     */
    @Test
    public void test5Nodes7EdgesOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesMaximalEdges graph with the above requirement
     */
    @Test
    public void test5NodesMaximalEdgesOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesSequentialEdges graph with the above requirement
     */
    @Test
    public void test5NodesSequentialEdgesOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes10Edges graph with the above requirement
     */
    @Test
    public void test15Nodes10EdgesOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(542, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes80Edges graph with the above requirement
     */
    @Test
    public void test15Nodes80EdgesOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(542, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_7_OutTree graph with the above requirement
     */
    @Test
    public void testNodes7OutTreeOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(40, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_8_Random graph with the above requirement
     */
    @Test
    public void TestNodes8RandomOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(969, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_9_SeriesParallel graph with the above requirement
     */
    @Test
    public void testNodes9SeriesParallelOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_10_Random graph with the above requirement
     */
    @Test
    public void testNodes10RandomOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(63, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_11_OutTree graph with the above requirement
     */
    @Test
    public void TestNodes11OutTreeOneProcessOneCore() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 1, outputFileName,1));
        assertEquals(640, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    // ----------------------------------TESTING: 2 processes 1 core----------------------------------

    /**
     * Testing sample graph with the above requirement
     */
    @Test
    public void testSample() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing EmptyGraph graph with the above requirement
     */
    @Test
    public void testEmptyGraph() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 1Node graph with the above requirement
     */
    @Test
    public void test1NodeTwo() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes0edges graph with the above requirement
     */
    @Test
    public void test5Nodes0Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(78, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes7Edges graph with the above requirement
     */
    @Test
    public void test5Nodes7Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesMaximalEdges graph with the above requirement
     */
    @Test
    public void test5NodesMaximalEdges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesSequentialEdges graph with the above requirement
     */
    @Test
    public void test5NodesSequentialEdges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes10Edges graph with the above requirement
     */
    @Test
    public void test15Nodes10Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(271, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes80Edges graph with the above requirement
     */
    @Test
    public void test15Nodes80Edges() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_7_OutTree graph with the above requirement
     */
    @Test
    public void testNodes7OutTree() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(28, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_8_Random graph with the above requirement
     */
    @Test
    public void TestNodes8Random() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_9_SeriesParallel graph with the above requirement
     */
    @Test
    public void testNodes9SeriesParallel() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_10_Random graph with the above requirement
     */
    @Test
    public void testNodes10Random() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_11_OutTree graph with the above requirement
     */
    @Test
    public void TestNodes11OutTree() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,1));
        assertEquals(350, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    // ----------------------------------TESTING: 2 processes 4 core----------------------------------

    /**
     * Testing sample graph with the above requirement
     */
    @Test
    public void testSampleP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing EmptyGraph graph with the above requirement
     */
    @Test
    public void testEmptyGraphP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 1Node graph with the above requirement
     */
    @Test
    public void test1NodeTwoP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes0edges graph with the above requirement
     */
    @Test
    public void test5Nodes0EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(78, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes7Edges graph with the above requirement
     */
    @Test
    public void test5Nodes7EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesMaximalEdges graph with the above requirement
     */
    @Test
    public void test5NodesMaximalEdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesSequentialEdges graph with the above requirement
     */
    @Test
    public void test5NodesSequentialEdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes10Edges graph with the above requirement
     */
    @Test
    public void test15Nodes10EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(271, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes80Edges graph with the above requirement
     */
    @Test
    public void test15Nodes80EdgesP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_7_OutTree graph with the above requirement
     */
    @Test
    public void testNodes7OutTreeP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(28, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_8_Random graph with the above requirement
     */
    @Test
    public void TestNodes8RandomP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_9_SeriesParallel graph with the above requirement
     */
    @Test
    public void testNodes9SeriesParallelP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_10_Random graph with the above requirement
     */
    @Test
    public void testNodes10RandomP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_11_OutTree graph with the above requirement
     */
    @Test
    public void TestNodes11OutTreeP() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 2, outputFileName,4));
        assertEquals(350, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    // ----------------------------------TESTING: 5 processes 1 core----------------------------------

    /**
     * Testing sample graph with the above requirement
     */
    @Test
    public void testSample5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing EmptyGraph graph with the above requirement
     */
    @Test
    public void testEmptyGraph5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 1Node graph with the above requirement
     */
    @Test
    public void test1NodeTwo5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes0edges graph with the above requirement
     */
    @Test
    public void test5Nodes0Edges5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(46, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes7Edges graph with the above requirement
     */
    @Test
    public void test5Nodes7Edges5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesMaximalEdges graph with the above requirement
     */
    @Test
    public void test5NodesMaximalEdges5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesSequentialEdges graph with the above requirement
     */
    @Test
    public void test5NodesSequentialEdges5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes10Edges graph with the above requirement
     */
    @Test
    public void test15Nodes10Edges5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(197, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes80Edges graph with the above requirement
     */
    @Test
    public void test15Nodes80Edges5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_7_OutTree graph with the above requirement
     */
    @Test
    public void testNodes7OutTree5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(22, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_8_Random graph with the above requirement
     */
    @Test
    public void TestNodes8Random5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_9_SeriesParallel graph with the above requirement
     */
    @Test
    public void testNodes9SeriesParallel5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_10_Random graph with the above requirement
     */
    @Test
    public void testNodes10Random5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_11_OutTree graph with the above requirement
     */
    @Test
    public void TestNodes11OutTree5Processors() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,1));
        assertEquals(227, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    // ----------------------------------TESTING: 5 processes 4 cores----------------------------------

    /**
     * Testing sample graph with the above requirement
     */
    @Test
    public void testSample5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "sample.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(8, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing EmptyGraph graph with the above requirement
     */
    @Test
    public void testEmptyGraph5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "EmptyGraph.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(0, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 1Node graph with the above requirement
     */
    @Test
    public void test1NodeTwo5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "1Node.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(21, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes0edges graph with the above requirement
     */
    @Test
    public void test5Nodes0Edges5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes0edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(46, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5Nodes7Edges graph with the above requirement
     */
    @Test
    public void test5Nodes7Edges5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5Nodes7Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesMaximalEdges graph with the above requirement
     */
    @Test
    public void test5NodesMaximalEdges5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesMaximalEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 5NodesSequentialEdges graph with the above requirement
     */
    @Test
    public void test5NodesSequentialEdges5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "5NodesSequentialEdges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(154, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes10Edges graph with the above requirement
     */
    @Test
    public void test15Nodes10Edges5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes10Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(197, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing 15Nodes80Edges graph with the above requirement
     */
    @Test
    public void test15Nodes80Edges5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "15Nodes80Edges.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(516, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_7_OutTree graph with the above requirement
     */
    @Test
    public void testNodes7OutTree5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_7_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(22, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_8_Random graph with the above requirement
     */
    @Test
    public void TestNodes8Random5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_8_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(581, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_9_SeriesParallel graph with the above requirement
     */
    @Test
    public void testNodes9SeriesParallel5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_9_SeriesParallel.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(55, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_10_Random graph with the above requirement
     */
    @Test
    public void testNodes10Random5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_10_Random.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(50, validator.getFinishTime());
        deleteOutput(outputFileName);
    }

    /**
     * Testing Nodes_11_OutTree graph with the above requirement
     */
    @Test
    public void TestNodes11OutTree5Processors4Cores() {
        ValidOutputTester validator = new ValidOutputTester();

        String inputFileName = testGraphDir + "Nodes_11_OutTree.dot";
        String outputFileName = inputFileName.replace(".dot", "-output.dot");
        assertTrue(testValidity(validator, inputFileName, 5, outputFileName,4));
        assertEquals(227, validator.getFinishTime());
        deleteOutput(outputFileName);
    }


    /**
     * Utility method for validating whether the graph is valid and what its final time is
     * @param validOutputTester
     * @param graphFileName
     * @param numProcessors
     * @param outputFileName
     * @param cores
     * @return
     */
    private boolean testValidity(ValidOutputTester validOutputTester, String graphFileName, int numProcessors, String outputFileName, int cores) {
        GraphReader graphReader = GraphReader.getInstance();
        try {
            graphReader.loadGraphData(graphFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Only for testing
        TotalFCostCalculator.getInstance().resetGraphReader();

        AStar aStar2 = new AStar(numProcessors,cores);
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
