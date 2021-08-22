# SOFTENG 306 Project 1: Eurasian Federation - 10

## Background
This project is about using artificial intelligence and parallel processing power to solve a difficult scheduling problem, which was requested by Big-As Parallel Computing Centre. The main objectives of this project are speed and high quality software. More details about the project can be found in our [wiki](wiki/Home.md).

## Group Members
- Danil Linkov
- Dave Shin
- Daniel Lee
- Marcus Li
- Hyung Park


## Running instructions

Each release with names starting with "Milestone" will include a runnable jar file which can be invoked from the command line with the following parameters.

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
* [Package java.util.concurrent](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/package-summary.html)
