# Testing

For automated testing we have used:

- ValidityAndOptimalityTester
- ValidOutputTester

ValidOutputTester is a class that contains a method isValid which takes in the output and input graph file paths and checks whether the produced schedule is a valid one.

ValidityAndOptimalityTester has 70 test cases which use ValidOutputTester and check that the output is valid as well as check whether the solution is optimal through checking if the finish time of a schedule is an optimal one.

Out of these 42 test cases there are 3 groups:

- 1 processor 1 core
- 2 processor 1 core
- 2 processor 4 cores
- 5 processor 1 core
- 5 processor 4 cores

Which were ran on all the test graphs that are contained in the graphs folder. This made sure that our tests covered the full range functionality that is required from this project.

Aside from the automated testing we have done a lot of manual testing for our code especially for the GUI where it is a lot harder to have automated testing.