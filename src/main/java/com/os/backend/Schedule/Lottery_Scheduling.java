package com.os.backend.Schedule;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessState;
import com.os.backend.Process.ProcessTable;
import com.os.backend.Schedule.SchedulingAlgo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Lottery_Scheduling extends SchedulingAlgo {
    private final int totalTickets;
    private List<Process> clonedProcesses;
    private final Random random;

    public Lottery_Scheduling(int totalTickets) {
        this.totalTickets = totalTickets;
        this.random = new Random();
        cloneProcessList();
    }

    @Override
    public ProcessTable execute() {
        ProcessTable table = new ProcessTable();
        int time = 0;
        List<Process> queue = new LinkedList<>();
        int counter = 0;

        while (!queue.isEmpty() || !this.clonedProcesses.isEmpty()) {
            /*to be used in lambda expressions*/
            final int currentTime = time;
            //----------------------------------------------------------------
            //arrived processes
            //get the arrived process list
            List<Process> arrivedProcesses = this.clonedProcesses.stream()
                    .filter(process -> process.getArrivalTime() <= currentTime)
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
                int selectedTicket = random.nextInt(totalTickets) + 1;
                Process runningProcess = queue.stream()
                        .filter(process -> process.getTickets() >= selectedTicket)
                        .findFirst()
                        .orElse(queue.get(0));

                Process runningProcessToAdd = this.processesList.get(this.processesList.indexOf(runningProcess));

                if (counter == 0) {
                    //called for the first time --> add to table as started
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.STARTED);
                } else {
                    // add the process as running
                    table.addExecutionEvent(runningProcessToAdd, currentTime, runningProcessToAdd.getProcessNumber(), ProcessState.RUNNING);
                }

                //----------------------------------------------------------------
                //waiting processes
                // add waiting (in queue) non-arrived processes to the table as Ready
                queue.stream().filter(process -> !arrivedProcesses.contains(process) && !process.equals(runningProcess)).
                        forEach(process -> {
                            Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                            table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.READY);
                        });

                //----------------------------------------------------------------
                //update Variables

                //update counter
                counter = (counter + 1) % this.totalTickets;
                //update the remaining time of the current running process
                runningProcess.decrementRemainingTime();
                
                // if the remaining time of the current running process becomes zero,
                // then it's done, and remove it from the queue
                // and add it to the table as completed
                if (runningProcess.getRemainingTime() == 0) {
                    queue.remove(runningProcess);
                    table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                    counter = 0;
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
                .map(Process::clone)
                .collect(Collectors.toList());
    }

    /**
     * For testing
     */
    public static void main(String[] args) {
        // Create a list of processes for testing
        List<Process> testProcessesList = new ArrayList<>();
       

        // Total tickets in the system (can be adjusted based on total tickets of all processes)
        int totalTickets = testProcessesList.stream().mapToInt(Process::getTickets).sum();

        // Create a LotteryScheduling scheduling algorithm with total tickets
        Lottery_Scheduling lotteryScheduling = new Lottery_Scheduling(totalTickets);

        // Set the processes list
        lotteryScheduling.addNewProcesses(testProcessesList);

        // Execute the scheduling algorithm
        ProcessTable processTable = lotteryScheduling.execute();

        // Print the process table to observe the execution events
        System.out.println(processTable);
    }

    @Override
    public String getSchedulerName() {
        return "Planificador por sorteo ";
    }
}
