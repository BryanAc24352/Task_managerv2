package com.os.backend.Schedule;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Guaranteed extends SchedulingAlgo {

    private final int timeQuantum;
    private List<Process> clonedProcesses;

    public Guaranteed(int timeQuantum) {
        this.timeQuantum = timeQuantum;
        cloneProcessList();
    }

    @Override
    public ProcessTable execute() {
        ProcessTable table = new ProcessTable();
        int time = 0;
        List<Process> queue = new LinkedList<>();

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

                if (runningProcess.getRemainingTime() == 0) {
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.STARTED);
                    runningProcess.setState(ProcessState.RUNNING);
                } else {
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.RUNNING);
                }

                int guaranteedTime = Math.min(runningProcess.getRemainingTime(), timeQuantum);

                
                runningProcess.decrementRemainingTime(guaranteedTime);
                time += guaranteedTime;

                
                if (runningProcess.getRemainingTime() == 0) {
                    runningProcess.setState(ProcessState.COMPLETED);
                    table.addExecutionEvent(runningProcessToAdd, time, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                    queue.remove(0);
                } else {
                    runningProcess.setState(ProcessState.READY);
                    queue.add(runningProcess); 
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

    @Override
    public String getSchedulerName() {
        return "Guaranteed Scheduling";
    }
}

