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
   
        SJF_Non sjf = new SJF_Non();


        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 5));
        processes.add(new Process(2, 2, 8));
        processes.add(new Process(3, 4, 3));

        
        sjf.addNewProcesses(processes);

       
        ProcessTable processTable = sjf.execute();

        
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

        while (!this.clonedProcesses.isEmpty()) { 
            List<Process> arrivedProcesses = getArrivedProcesses(currentTime);

            if (arrivedProcesses.isEmpty()) {
                
                currentTime++;
                continue;
            }

            
            arrivedProcesses.sort((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));

           
            Process shortestProcess = arrivedProcesses.get(0);

            
            processTable.addExecutionEvent(shortestProcess, currentTime, shortestProcess.getProcessNumber(), ProcessState.ARRIVED);

         
            processTable.addExecutionEvent(shortestProcess, currentTime, shortestProcess.getProcessNumber(), ProcessState.STARTED);

           shortestProcess.decrementRemainingTime();

            while (shortestProcess.getRemainingTime() != 0) {
                processTable.addExecutionEvent(shortestProcess, ++currentTime, shortestProcess.getProcessNumber(), ProcessState.RUNNING);
                shortestProcess.decrementRemainingTime();
            }

         
            processTable.addExecutionEvent(shortestProcess, currentTime, shortestProcess.getProcessNumber(), ProcessState.COMPLETED);

            
            currentTime++;

           
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
