# SOFTENG 306 Project 1: Eurasian Federation - 10

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
