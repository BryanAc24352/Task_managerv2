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

       
        List<Process> processesList = new ArrayList<>(List.of(p1, p2, p3, p4));

        
        FCFS fcfs = new FCFS();

        
        fcfs.addNewProcesses(processesList);

        
        ProcessTable processTable = fcfs.execute();

        
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

        while (!this.clonedProcesses.isEmpty()) { 
            List<Process> arrivedProcesses = getArrivedProcesses(currentTime);

            if (arrivedProcesses.isEmpty()) {
                
                currentTime++;
                continue;
            }

            
            Process firstProcess = arrivedProcesses.get(0);

            
            processTable.addExecutionEvent(firstProcess, currentTime, firstProcess.getProcessNumber(), ProcessState.ARRIVED);

           
            processTable.addExecutionEvent(firstProcess, currentTime, firstProcess.getProcessNumber(), ProcessState.STARTED);

            
            firstProcess.decrementRemainingTime();
            
            int endTime = currentTime + firstProcess.getRemainingTime();
            while(firstProcess.getRemainingTime() != 0) {
                processTable.addExecutionEvent(firstProcess, ++currentTime, firstProcess.getProcessNumber(), ProcessState.RUNNING);
                List<Process> hackProcess = getCurrentProcesses(currentTime);
                firstProcess.decrementRemainingTime();

            }

            processTable.addExecutionEvent(firstProcess, currentTime, firstProcess.getProcessNumber(), ProcessState.COMPLETED);


            currentTime++;


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