package com.os.backend.Schedule;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessExecutionEvent;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;

import java.util.*;
import java.util.stream.Collectors;

public class SJF_Pree extends SchedulingAlgo {
    private List<Process> clonedProcesses;

    public static void main(String[] args) {
       
        Process p1 = new Process(1, 0, 5);
        Process p2 = new Process(2, 1, 3);
        Process p3 = new Process(3, 2, 2);
        Process p4 = new Process(4, 3, 5);

        
        SJF_Pree sjfPree = new SJF_Pree();
        sjfPree.addNewProcesses(List.of(p1, p2, p3, p4));

        
        ProcessTable processTable = sjfPree.execute();

       
        for (ProcessExecutionEvent event : processTable.getExecutionEvents()) {
            System.out.println(event);
        }
    }

    public SJF_Pree() {
    }

    @Override
    public ProcessTable execute() {
        ProcessTable processTable = new ProcessTable();
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getRemainingTime).thenComparing(Process::getArrivalTime));
        int currentTime = 0;

        while (!clonedProcesses.isEmpty()) { 
            List<Process> arrivedProcesses = getArrivedProcesses(currentTime);

            if (arrivedProcesses.isEmpty()) {
                
                currentTime++;
                continue;
            }

            
            for (Process arrivedProcess : arrivedProcesses) {
                if (!readyQueue.contains(arrivedProcess)) {
                    readyQueue.add(arrivedProcess);
                }
            }

           
            Process runningProcess = readyQueue.poll();


            
            assert runningProcess != null;
            runningProcess.decrementRemainingTime();

           
            processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.ARRIVED);

            
            processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.STARTED);



            int burstTime = runningProcess.getRemainingTime();
            int endTime = 0;

            while(runningProcess.getRemainingTime() != 0){
                arrivedProcesses.clear();
                arrivedProcesses.addAll(getArrivedProcesses(++currentTime));

                
                for (Process arrivedProcess : arrivedProcesses) {
                    if (!readyQueue.contains(arrivedProcess)) {
                        readyQueue.add(arrivedProcess);
                    }
                }

                if (!readyQueue.isEmpty()) {
                    Process nextProcess = readyQueue.peek();
                    if (nextProcess != null && nextProcess != runningProcess) {
                       
                        processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.INTERRUPTED);
                        assert readyQueue.peek() != null;
                        runningProcess = readyQueue.poll();
                        assert runningProcess != null;
                        runningProcess.decrementRemainingTime();
                        processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.ARRIVED);
                        processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.STARTED);
                    } else {
                       
                        processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.RUNNING);
                        runningProcess.decrementRemainingTime();
                    }
                }
            }

            processTable.addExecutionEvent(runningProcess, currentTime, runningProcess.getProcessNumber(), ProcessState.COMPLETED);

         
            readyQueue.remove(runningProcess);
            
            clonedProcesses.remove(runningProcess);
            currentTime++;
        }

        return processTable;
    }

    private List<Process> getArrivedProcesses(int currentTime) {
        List<Process> arrivedProcesses = new ArrayList<>();
        for (Process process : clonedProcesses) {
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
        return "SJF Preemptivo";
    }
}