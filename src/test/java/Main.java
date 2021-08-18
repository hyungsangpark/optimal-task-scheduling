import java.io.File;

public class Main {
    public static void main(String[] args) {
        ValidOutputTester solutionValidator = new ValidOutputTester();

        File[] files = new File("./src/test/graphs/").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String input = file.getPath();
                String output = file.getPath()
                        .replace("graphs", "actualOutputs")
                        .replace(".dot", "-output.dot");
                solutionValidator.isValid(input,output, 2);
            }
        }
    }
}
