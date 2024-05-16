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
            /*to be used in lambda expressions*/
            final int currentTime = time;
            //----------------------------------------------------------------
            //arrived processes
            //get the arrived process list
            List<PriorityProcess> arrivedProcesses = this.clonedProcesses.stream()
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
                PriorityProcess runningProcess = queue.remove();
                PriorityProcess runningProcessToAdd = (PriorityProcess) this.processesList.get(this.processesList.indexOf(runningProcess));

                if (runningProcess != prevPriority) {
                    //called for the first time --> add to table as started
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.STARTED);
                    //if the prev process remaining time != 0, then it was interrupted
                    if (prevPriority != null) {
                        if (prevPriority.getRemainingTime() != 0) {
                            PriorityProcess prevProcessToAdd = (PriorityProcess) this.processesList.get(this.processesList.indexOf(prevPriority));
                            table.addExecutionEvent(prevProcessToAdd, time, prevProcessToAdd.getProcessNumber(), ProcessState.INTERRUPTED);
                        }
                    }
                } else {
                    // add the process as running
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.RUNNING);
                }


                //----------------------------------------------------------------
                //waiting processes
                // add waiting (in queue) non-arrived processes to the table as Ready
                /*for lambda usage*/
                final PriorityProcess p = prevPriority;
                queue.stream().filter(process -> !arrivedProcesses.contains(process) && !process.equals(runningProcess) && !process.equals(p)).
                        forEach(process -> {
                            Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                            table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.READY);
                        });

                //----------------------------------------------------------------
                //update Variables
                //update the remaining time of the current running process
                runningProcess.decrementRemainingTime();

                //set the prev process to the current one
                prevPriority = runningProcess;

                //if the running process's remaining time == 0, then it is completed
                if (runningProcess.getRemainingTime() == 0) {
                    table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                } else {
                    //else reAdd it to the queue
                    queue.add(runningProcess);
                }

            }
            //update time
            time++;
        }
        return table;
    }

    @Override
    public void addNewProcesses(List<Process> newProcesses) {
        super.addNewProcesses(newProcesses);
        cloneProcessList();
    }

    // Helper methods
    private void cloneProcessList() {
        this.clonedProcesses = processesList.stream()
                .map(process -> ((PriorityProcess) process).clone())
                .collect(Collectors.toList());
    }

    /***
     * For testing
     */
    public static void main(String[] args) {
// Create some sample processes
        List<Process> processes = new ArrayList<>();
        processes.add(new PriorityProcess(1, 0, 5, 2));
        processes.add(new PriorityProcess(2, 2, 3, 1));
        processes.add(new PriorityProcess(3, 4, 4, 3));

        // Create an instance of Priority_Pree
        Priority_Pree scheduler = new Priority_Pree();

        // Set the list of processes
        scheduler.addNewProcesses(processes);

        // Execute the scheduler
        ProcessTable table = scheduler.execute();

        // Print the process table
        System.out.println(table);
    }

    @Override
    public String getSchedulerName() {
        return "Prioridad";
    }
}
