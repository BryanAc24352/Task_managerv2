package com.os.backend.Schedule;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessExecutionEvent;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FCFS extends SchedulingAlgo {

    private List<Process> clonedProcesses;

    public static void main(String[] args) {
        // Create some sample processes
        Process p1 = new Process(1, 0, 5);
        Process p2 = new Process(2, 1, 3);
        Process p3 = new Process(3, 2, 2);
        Process p4 = new Process(4, 3, 5);

        // Add the processes to a list
        List<Process> processesList = new ArrayList<>(List.of(p1, p2, p3, p4));

        // Create an instance of your scheduling algorithm
        FCFS fcfs = new FCFS();

        // Set the list of processes for the algorithm to execute
        fcfs.addNewProcesses(processesList);

        // Execute the algorithm
        ProcessTable processTable = fcfs.execute();

        // Output the execution events
        for (ProcessExecutionEvent event : processTable.getExecutionEvents()) {
            System.out.println(event);
        }
    }

    public FCFS() {
    }

    @Override
    public ProcessTable execute() {
        ProcessTable processTable = new ProcessTable();
        int currentTime = 0;

        while (!this.clonedProcesses.isEmpty()) { // Continue until all processes are executed
            // Get the processes that have arrived by the current time
            List<Process> arrivedProcesses = getArrivedProcesses(currentTime);

            if (arrivedProcesses.isEmpty()) {
                // If no processes have arrived, increment current time
                currentTime++;
                continue;
            }

            // Get the process that arrived first (FCFS)
            Process firstProcess = arrivedProcesses.get(0);

            // Add event for process arrival
            processTable.addExecutionEvent(firstProcess, currentTime, firstProcess.getProcessNumber(), ProcessState.ARRIVED);

            // Add event for process start
            processTable.addExecutionEvent(firstProcess, currentTime, firstProcess.getProcessNumber(), ProcessState.STARTED);

            // Decrement cloned Process remaining time
            firstProcess.decrementRemainingTime();
            // Simulate process execution
            int endTime = currentTime + firstProcess.getRemainingTime();
            while(firstProcess.getRemainingTime() != 0) {
                processTable.addExecutionEvent(firstProcess, ++currentTime, firstProcess.getProcessNumber(), ProcessState.RUNNING);
                List<Process> hackProcess = getCurrentProcesses(currentTime);
                firstProcess.decrementRemainingTime();
//                if (!hackProcess.isEmpty()) {
//                    processTable.addExecutionEvent(hackProcess.get(i), i, hackProcess.get(i).getProcessNumber(), ProcessState.ARRIVED);
//                }
            }

            // Add event for process completion
            processTable.addExecutionEvent(firstProcess, currentTime, firstProcess.getProcessNumber(), ProcessState.COMPLETED);

//          // Update current time
            currentTime++;

            // Remove the executed process from the list
            clonedProcesses.remove(firstProcess);
        }

        return processTable;
    }

    private List<Process> getArrivedProcesses(int currentTime) {
        List<Process> arrivedProcesses = new ArrayList<>();
        for (Process process : this.clonedProcesses) {
            if (process.getArrivalTime() <= currentTime) {
                arrivedProcesses.add(process);
            }
        }
        return arrivedProcesses;
    }

    private List<Process> getCurrentProcesses(int currentTime) {
        List<Process> arrivedProcesses = new ArrayList<>();
        for (Process process : this.clonedProcesses) {
            if (process.getArrivalTime() == currentTime) {
                arrivedProcesses.add(process);
            }
        }
        return arrivedProcesses;
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

    @Override
    public String getSchedulerName() {
        return "First Come First Serve";
    }
}