package project1.IO;

import java.io.IOException;

public class TestNewGraphReader {
    public static void main(String[] args) {
        GraphReader graphReader = GraphReader.getInstance();

        try {
            graphReader.loadGraphData("./src/test/graphs/sample.dot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
