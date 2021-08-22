Visualisation is achieved using JavaFX.

##### Overall flow of how it runs

When `Main` class first runs the program, it checks whether visualiser flag (`-v`) is present. If it is present, it triggers `Visualiser#main()`. 

`Visualiser#main(String[])` launches the `Application` class which is a JavaFX class that is used to initialize a JavaFX GUI program, and `Visualiser#start()` is run.

Inside `Visualiser#start()`, the fundamental configurations for the application window is set. An FXML file is loaded into a loader, then it is loaded to the GUI application be set as a *scene* of the GUI application. (Here, a scene is essentially like a container which contains JavaFX elements such as VBox and various Panes to even Texts and Buttons.) It then shows the application which would make the GUI application visible to the user. Also as the FXML file is loaded, the controller class which is connected to the FXML file is instantiated, which results controllers handling the background work of the view to be available to the program.

Inside of Visualiser screen, there are multiple components: (please include a screenshot that I sent you alongside it)

- Status - The status of the program, either "READY" or "RUNNING"
- Optimal Time - Optimal time produced from the scheduler, i.e. the final time when no processors gets scheduled a task to do.

- Elapsed Time - How long has the scheduler been running. (Since the time "START" button was pressed.)

- Start Button - Button which starts the scheduler
- Schedule Graph - Graph that shows the schedule of the program. As the program runs, it shows a live scheduling process of schedules that are considered optimal at that specific time by the scheduler, then an optimal schedule when the scheduler finishes.



##### Button Action

When the button is pressed, it runs `Controller#handleStartButton()`. When it runs, it runs following things in order:

- Update application screen accordingly. (change status text and etc...)
- disable start button to prevent it from being pressed while scheduler is running.
- Create a background thread called SchedulingThread which runs the scheduler and also contains information such as a current schedule map and flags to determine whether the scheduler has updated the schedule or whether the scheduler has finished

- start SchedulingThread
- Start polling for a new changes in the scheduler and in the application screen. (such as most current schedule, and elapsed time)

If during polling, an update in the schedule has been detected by a True value in `SchedulingThread#isChanged()`, it goes through following process:

- clear previous data in the schedule graph
- for each processor
  - if a processor is empty in terms of tasks scheduled, place a transparent placeholder schedule to still show the existence of the schedule.
  - Check whether it has any gap between the end time of the previous task and starting time, and if there is, add another transparent placeholder schedule to calibrate the actual task
  - add the actual task
- Plot all the schedules to the graph.

When SchedulingThread has isFinished() flag raised, it undergoes a end of schedule procedure. 

- write the current optimal schedule to the output file.
- update application screen accordingly (change back status text, show optimal time, etc...)

