# Parallelization

Parallelization is achieved using the Java concurrency library.

A thread pool is used in ScheduleNode during the process of "tree" expanding, e.g. finding the children of a given schedule based on the schedulable task nodes. For each schedulable task node of a schedule a "runnable task" is created with the goal of making a new child schedule with that given node. These tasks are then executed given a thread pool which is the size of the input from the user or 1 by default (therefore sequential by default). Once all these tasks are done their results are added to the children schedule list and the algorithm continues.

This has not created much of an increase in performance due to the algorithm already being quiet optimized and efficient however this would provide better results on very large inputs.

