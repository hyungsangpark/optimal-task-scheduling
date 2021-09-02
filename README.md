# SOFTENG 306 Project 1: Eurasian Federation - 10

## Background
This project is about using artificial intelligence and parallel processing power to solve a difficult scheduling problem, which was requested by Big-As Parallel Computing Centre. The main objectives of this project are speed and high quality software. More details about the project can be found in our [wiki](wiki/Home.md).

## Group Members
- Danil Linkov
- Dave Shin
- Daniel Lee
- Marcus Li
- Hyung Park

## Getting Started

### Dependencies

Dependencies used in this project are as follows: 

- [Maven](https://maven.apache.org/)
- [Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [JUnit](https://junit.org/junit4/)
- [JavaFX](https://openjfx.io/)
- [GraphStream](https://graphstream-project.org/) (for test purposes)

### Installing

In order to run the program, it is highly recommended going to the release section and download jar executables according to the OS being used.

Though, detailed guides on how to manually install are listed below.
Follow though the following guide after downloading or cloning the source files.

Given the source files, Maven will be used to package the source files into executable jar files. Due to issues with JavaFX, please package executables in the specific OS that the installed executable will be run.

To check whether maven is installed on your computer, run:

`mvn -version`

Once it's installed, on the root directory of the source folder, (folder which contains src folder and pom.xml) run:

`mvn clean install`

On the root directory, an executable scheduler.jar should've been created.

### Running instructions

Each release with names starting with "Milestone" will include a runnable jar file which can be invoked from the command line with the following parameters.

*\*If your jar file is not named scheduler.jar due to running it on a different OS other than linux, please rename it to scheduler.jar to run the command below, otherwise rename the "scheduler.jar" in the command below to the name of the jar file accordingly.* 

```
java −jar scheduler.jar INPUT.dot P [OPTION]
```

- `INPUT.dot` a task graph with integer weights in dot format
- `P` number of processors to schedule the INPUT graph on

#### Optional:
- `−p N` use `N` cores for execution in parallel (default is sequential)
- `−v` visualise the search
- `−o OUPUT` output file is named `OUTPUT` (default is INPUT−output.dot)

## Acknowledgements
* [Elisa's test cases](https://github.com/Milk-Yan/optimal-task-scheduling/tree/master/src/test)
* [A* Tree Search](https://www.javatpoint.com/ai-informed-search-algorithms)
* [Research on Java Parallelisation](https://www.baeldung.com/java-when-to-use-parallel-stream)
* [Fork/Join Framework Java Example](https://howtodoinjava.com/java7/forkjoin-framework-tutorial-forkjoinpool-example/)
* [Package java.util.concurrent](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/package-summary.html)
* [ArrayList vs LinkedList memory allocation](https://stackoverflow.com/questions/11564352/arraylist-vs-linkedlist-from-memory-allocation-perspective)
* [Graph Visualisor](https://dreampuf.github.io/GraphvizOnline/#digraph%20%22example%22%20%7B%0A%20%20%20%20a%20%20%20%20%20%20%20%20%20%20%20%5BWeight%3D2%5D%3B%0A%20%20%20%20b%20%20%20%20%20%20%20%20%20%20%20%5BWeight%3D2%5D%3B%0A%20%20%20%20a%20-%3E%20b%20%20%20%20%20%20%5BWeight%3D2%5D%3B%0A%20%20%20%20c%20%20%20%20%20%20%20%20%20%20%20%5BWeight%3D3%5D%3B%0A%20%20%20%20a%20-%3E%20c%20%20%20%20%20%20%5BWeight%3D1%5D%3B%0A%20%20%20%20d%20%20%20%20%20%20%20%20%20%20%20%5BWeight%3D2%5D%3B%0A%20%20%20%20b%20-%3E%20d%20%20%20%20%20%20%5BWeight%3D2%5D%3B%0A%20%20%20%20c%20-%3E%20d%20%20%20%20%20%20%5BWeight%3D1%5D%3B%0A%7D)
