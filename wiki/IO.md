# IO

We originally used a graph stream library for reading and writing the graphs however through testing we realized that it slowed down the performance of our algorithm as well as take up an unnecessary amount of memory.

Therefore we had decided to create our own I/O.

### Graph reader

A singleton class which is accessed by using its getInstance() method and then calling loadGraphData on a graph file path. It then parses the file into an array of node ids and 4 hashmaps of node and edge weights as well as parent and child relationships. This data is then accessed throughout the application and does not waste any unnecessary memory on methods and variables we don't use.

### Graph writter

Is a class that is instantiated once in the Main and takes in an output file name and the schedule that was produced for a given graph. It then just reads the schedule data and the GraphReader data and outputs a valid dot file with the Start and Processor attributes attached.

