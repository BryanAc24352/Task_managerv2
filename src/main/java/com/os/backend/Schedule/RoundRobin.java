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
      
            final int currentTime = time;
         
            List<Process> arrivedProcesses = this.clonedProcesses.stream()
                    .filter(process -> process.getArrivalTime() == currentTime)
                    .toList();
     
            this.clonedProcesses.removeAll(arrivedProcesses);

        
            arrivedProcesses.forEach(process -> {
                Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.ARRIVED);
            });

   
            queue.addAll(arrivedProcesses);

            
            if (!queue.isEmpty()) {
                Process runningProcess = queue.get(0);
                Process runningProcessToAdd = this.processesList.get(this.processesList.indexOf(runningProcess));

                if (counter == 0) {
                 
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.STARTED);
                } else {
                   
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.RUNNING);
                }

        
                queue.stream().filter(process -> !arrivedProcesses.contains(process) && !process.equals(runningProcess)).
                        forEach(process -> {
                            Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                            table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.READY);
                        });

     
                counter = (counter + 1) % this.timeQuantum;
     
                runningProcess.decrementRemainingTime();
           
                if (counter == 0) {
             
                    if(runningProcess.getRemainingTime() != 0) {
                        table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.INTERRUPTED);
                    }
         
                    queue.remove(0);
          
                    if (runningProcess.getRemainingTime() > 0) {
                        queue.add(runningProcess);
                    }
                }

  
                if (runningProcess.getRemainingTime() == 0) {
                    if(!queue.isEmpty() && queue.get(0).equals(runningProcess)) {
                        queue.remove(0);
                    }
                    table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                    counter = 0;
                }
            }
    
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


    private void cloneProcessList() {
        this.clonedProcesses = processesList.stream()
                .map(Process::clone)
                .collect(Collectors.toList());
    }

 
    public static void main(String[] args) {

        List<Process> testProcessesList = new ArrayList<>();
        testProcessesList.add(new Process(1, 0, 1));
        testProcessesList.add(new Process(2, 0, 1));
        testProcessesList.add(new Process(3, 0, 1));

      
        RoundRobin roundRobin = new RoundRobin(1);


        roundRobin.addNewProcesses(testProcessesList);
        
        ProcessTable processTable = roundRobin.execute();

 
        System.out.println(processTable);
    }

    @Override
    public String getSchedulerName() {
        return "Round Robin";
    }
}


