package com.os.backend.Schedule;

import com.os.backend.Process.PriorityProcess;
import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Priority_Pree extends SchedulingAlgo {

    List<PriorityProcess> clonedProcesses;

    public Priority_Pree() {
        cloneProcessList();
    }

    @Override
    public ProcessTable execute() {
        ProcessTable table = new ProcessTable();
        PriorityQueue<PriorityProcess> queue = new PriorityQueue<>();
        int time = 0;
        PriorityProcess prevPriority = null;
        while (!clonedProcesses.isEmpty() || !queue.isEmpty()) {
            
            final int currentTime = time;
        
            List<PriorityProcess> arrivedProcesses = this.clonedProcesses.stream()
                    .filter(process -> process.getArrivalTime() == currentTime)
                    .toList();
        
            this.clonedProcesses.removeAll(arrivedProcesses);

       
            arrivedProcesses.forEach(process -> {
                Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.ARRIVED);
            });

  
            queue.addAll(arrivedProcesses);

        
            if (!queue.isEmpty()) {
                PriorityProcess runningProcess = queue.remove();
                PriorityProcess runningProcessToAdd = (PriorityProcess) this.processesList.get(this.processesList.indexOf(runningProcess));

                if (runningProcess != prevPriority) {
                  
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.STARTED);
                  
                    if (prevPriority != null) {
                        if (prevPriority.getRemainingTime() != 0) {
                            PriorityProcess prevProcessToAdd = (PriorityProcess) this.processesList.get(this.processesList.indexOf(prevPriority));
                            table.addExecutionEvent(prevProcessToAdd, time, prevProcessToAdd.getProcessNumber(), ProcessState.INTERRUPTED);
                        }
                    }
                } else {
    
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.RUNNING);
                }


         
                final PriorityProcess p = prevPriority;
                queue.stream().filter(process -> !arrivedProcesses.contains(process) && !process.equals(runningProcess) && !process.equals(p)).
                        forEach(process -> {
                            Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                            table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.READY);
                        });

       
                runningProcess.decrementRemainingTime();

       
                prevPriority = runningProcess;

             
                if (runningProcess.getRemainingTime() == 0) {
                    table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                } else {
         
                    queue.add(runningProcess);
                }

            }
     
            time++;
        }
        return table;
    }

    @Override
    public void addNewProcesses(List<Process> newProcesses) {
        super.addNewProcesses(newProcesses);
        cloneProcessList();
    }

  
    private void cloneProcessList() {
        this.clonedProcesses = processesList.stream()
                .map(process -> ((PriorityProcess) process).clone())
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {

        List<Process> processes = new ArrayList<>();
        processes.add(new PriorityProcess(1, 0, 5, 2));
        processes.add(new PriorityProcess(2, 2, 3, 1));
        processes.add(new PriorityProcess(3, 4, 4, 3));


        Priority_Pree scheduler = new Priority_Pree();

       
        scheduler.addNewProcesses(processes);


        ProcessTable table = scheduler.execute();

    
        System.out.println(table);
    }

    @Override
    public String getSchedulerName() {
        return "Prioridad";
    }
}
