# Milestone 2

The objective of Milestone 2 was to provide the complete implementation. In order to achieve this, following tasks were completed (in order of importance):

* Create an optimal schedule for small input graphs in reasonable time (say less than at most 30 minutes)
* Have a parallel version of the search that demonstrates speedup in comparison to the sequential version
* Have a meaningful and interesting live visualisation of the search

## Algorithm implementation

* Both A* and DFS algorithms were implemented so that corresponding algorithm can be used based on the inputs. However, we have identified that the A* algorithm is faster in all
cases based on our testing. Therefore, our current implementation only uses A* to produce an optimal schedule.

## Testing

* No proper testing implemented in this stage of the project.
* Planning to use JUnit as the framework to write tests.

For more information, check the following links:
* [Hash Collisions](Hash_Collisions.md)
