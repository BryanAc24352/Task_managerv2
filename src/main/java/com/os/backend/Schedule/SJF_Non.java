package com.os.backend.Schedule;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessExecutionEvent;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SJF_Non extends SchedulingAlgo {
    private List<Process> clonedProcesses;

    public static void main(String[] args) {
        // Create SJF_Non instance
        SJF_Non sjf = new SJF_Non();

        // Create sample processes
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 5));
        processes.add(new Process(2, 2, 8));
        processes.add(new Process(3, 4, 3));

        // Add processes to the scheduler
        sjf.addNewProcesses(processes);

        // Execute the SJF non-preemptive algorithm
        ProcessTable processTable = sjf.execute();

        // Print each event on a new line
        for (ProcessExecutionEvent event : processTable.getExecutionEvents()) {
            System.out.println(event);
        }
    }

    public SJF_Non() {
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

            // Sort arrived processes by burst time (shortest job first)
            arrivedProcesses.sort((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));

            // Get the process with the shortest burst time
            Process shortestProcess = arrivedProcesses.get(0);

            // Add event for process arrival
            processTable.addExecutionEvent(shortestProcess, currentTime, shortestProcess.getProcessNumber(), ProcessState.ARRIVED);

            // Add event for process start
            processTable.addExecutionEvent(shortestProcess, currentTime, shortestProcess.getProcessNumber(), ProcessState.STARTED);

           shortestProcess.decrementRemainingTime();

            while (shortestProcess.getRemainingTime() != 0) {
                processTable.addExecutionEvent(shortestProcess, ++currentTime, shortestProcess.getProcessNumber(), ProcessState.RUNNING);
                shortestProcess.decrementRemainingTime();
            }

            // Add event for process completion
            processTable.addExecutionEvent(shortestProcess, currentTime, shortestProcess.getProcessNumber(), ProcessState.COMPLETED);

            // Update current time
            currentTime++;

            // Remove the executed process from the list
            clonedProcesses.remove(shortestProcess);
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
        return "SJF No Preemptivo";
    }
}
