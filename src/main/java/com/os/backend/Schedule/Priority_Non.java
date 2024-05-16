package com.os.backend.Schedule;

import com.os.backend.Process.*;
import com.os.backend.Process.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Priority_Non extends SchedulingAlgo {
    private List<Process> clonedProcesses;

    public Priority_Non() {
    }

    @Override
    public ProcessTable execute() {

        ProcessTable processTable = new ProcessTable();

        int currentTime = 0;
        while (!clonedProcesses.isEmpty()) { 
            clonedProcesses.sort((p1, p2) -> {
                if (p1.getArrivalTime() != p2.getArrivalTime()) {
                    return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
                } else {
                   
                    if (p1 instanceof PriorityProcess && p2 instanceof PriorityProcess) {
                        return Integer.compare(((PriorityProcess) p1).getPriority(), ((PriorityProcess) p2).getPriority());
                    } else {
                        return 0; 
                    }
                }
            });


            Process process = clonedProcesses.get(0);

            processTable.addExecutionEvent(process, currentTime, process.getProcessNumber(), ProcessState.ARRIVED);

            processTable.addExecutionEvent(process, currentTime, process.getProcessNumber(), ProcessState.STARTED);

            process.decrementRemainingTime();

            while (process.getRemainingTime() != 0) {
                processTable.addExecutionEvent(process, ++currentTime, process.getProcessNumber(), ProcessState.RUNNING);
                process.decrementRemainingTime();
            }

            processTable.addExecutionEvent(process, currentTime, process.getProcessNumber(), ProcessState.COMPLETED);

            currentTime++;

            clonedProcesses.remove(process);
        }


        return processTable;

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
        return "Prioridad No Preemptivo";
    }
}
