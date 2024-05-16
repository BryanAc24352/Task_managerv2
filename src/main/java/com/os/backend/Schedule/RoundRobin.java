package com.os.backend.Schedule;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RoundRobin extends SchedulingAlgo {
    private final int timeQuantum;
    private List<Process> clonedProcesses;

    public RoundRobin(int timeQuantum) {
        this.timeQuantum = timeQuantum;
        cloneProcessList();
    }

    @Override
    public ProcessTable execute() {
        ProcessTable table = new ProcessTable();
        int time = 0;
        List<Process> queue = new LinkedList<>();
        int counter = 0;

        while (!queue.isEmpty() || !this.clonedProcesses.isEmpty()) {
            /*to be used in lambda expressions*/
            final int currentTime = time;
            //----------------------------------------------------------------
            //arrived processes
            //get the arrived process list
            List<Process> arrivedProcesses = this.clonedProcesses.stream()
                    .filter(process -> process.getArrivalTime() == currentTime)
                    .toList();
            //remove them from the clonedList
            this.clonedProcesses.removeAll(arrivedProcesses);

            //add arrivedProcesses to the table and mark them as arrived state
            arrivedProcesses.forEach(process -> {
                Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.ARRIVED);
            });

            //add the arrived process to the queue
            queue.addAll(arrivedProcesses);

            //----------------------------------------------------------------
            // the running process
            if (!queue.isEmpty()) {
                Process runningProcess = queue.get(0);
                Process runningProcessToAdd = this.processesList.get(this.processesList.indexOf(runningProcess));

                if (counter == 0) {
                    //called for the first time --> add to table as started
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.STARTED);
                } else {
                    // add the process as running
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.RUNNING);
                }

                //----------------------------------------------------------------
                //waiting processes
                // add waiting (in queue) non-arrived processes to the table as Ready
                queue.stream().filter(process -> !arrivedProcesses.contains(process) && !process.equals(runningProcess)).
                        forEach(process -> {
                            Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                            table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.READY);
                        });

                //----------------------------------------------------------------
                //update Variables

                //update counter
                counter = (counter + 1) % this.timeQuantum;
                //update the remaining time of the current running process
                runningProcess.decrementRemainingTime();
                //if counter becomes zero then it is the time for the next process in the queue
                if (counter == 0) {
                    // add the current running process as interrupt to the table in the next time
                    if(runningProcess.getRemainingTime() != 0) {
                        table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.INTERRUPTED);
                    }
                    // remove the current running process from the queue
                    queue.remove(0);
                    //if the remaining != 0, then add the process to the end of the queue
                    if (runningProcess.getRemainingTime() > 0) {
                        queue.add(runningProcess);
                    }
                }

                // if the remaining time of the current running process becomes zero,
                // then it's done, and remove it from the queue
                // and add it to the table as completed
                if (runningProcess.getRemainingTime() == 0) {
                    if(!queue.isEmpty() && queue.get(0).equals(runningProcess)) {
                        queue.remove(0);
                    }
                    table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                    counter = 0;
                }
            }
            //update time
            time++;
        }

        return table;
    }

    public int getTimeQuantum() {
        return timeQuantum;
    }

    @Override
    public void addNewProcesses(List<Process> newProcesses) {
        super.addNewProcesses(newProcesses);
        cloneProcessList();
    }

    // Helper methods
    private void cloneProcessList() {
        this.clonedProcesses = processesList.stream()
                .map(Process::clone)
                .collect(Collectors.toList());
    }

    /**
     * For testing
     */
    public static void main(String[] args) {
        // Create a list of processes for testing
        List<Process> testProcessesList = new ArrayList<>();
        testProcessesList.add(new Process(1, 0, 1));
        testProcessesList.add(new Process(2, 0, 1));
        testProcessesList.add(new Process(3, 0, 1));

        // Create a RoundRobin scheduling algorithm with a time quantum of 2
        RoundRobin roundRobin = new RoundRobin(1);

        // Set the processes list
        roundRobin.addNewProcesses(testProcessesList);

        // Execute the scheduling algorithm
        ProcessTable processTable = roundRobin.execute();

        // Print the process table to observe the execution events
        System.out.println(processTable);
    }

    @Override
    public String getSchedulerName() {
        return "Round Robin";
    }
}


