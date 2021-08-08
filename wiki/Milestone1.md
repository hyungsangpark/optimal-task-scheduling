# Milestone 1

The objective of Milestone 1 was to provide a valid schedule for an input of tasks. Visualization and optimality was not expected to be satisfied for this milestone.

## I/O Parsing

* GraphStream used to help read the files.
* Apache Commons CLI used for parsing command line options.

## A* Implementation

* Used for searching schedules with smaller input size.
* Heuristic approach to find a valid schedule for input of tasks.

## DFS Branch and Bound Implementation

* Used for searching schedules with larger input size.
* Depth-First-Search algorithm to find an optimal schedule and using Branch and Bound to save the algorithm from searching the entire tree. 

## Testing

* No proper testing implemented in this stage of the project.
* Planning to use JUnit as the framework to write tests.
