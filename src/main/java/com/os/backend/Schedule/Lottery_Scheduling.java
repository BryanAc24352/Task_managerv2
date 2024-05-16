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

            final int currentTime = time;

            List<Process> arrivedProcesses = this.clonedProcesses.stream()
                    .filter(process -> process.getArrivalTime() <= currentTime)
                    .toList();

            this.clonedProcesses.removeAll(arrivedProcesses);

     
            arrivedProcesses.forEach(process -> {
                Process toAdd = this.processesList.get(this.processesList.indexOf(process));
                table.addExecutionEvent(toAdd, currentTime, toAdd.getProcessNumber(), ProcessState.ARRIVED);
            });


            queue.addAll(arrivedProcesses);


            if (!queue.isEmpty()) {
                int selectedTicket = random.nextInt(totalTickets) + 1;
                Process runningProcess = queue.stream()
                        .filter(process -> process.getTickets() >= selectedTicket)
                        .findFirst()
                        .orElse(queue.get(0));

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


                counter = (counter + 1) % this.totalTickets;
              
                runningProcess.decrementRemainingTime();
                

                if (runningProcess.getRemainingTime() == 0) {
                    queue.remove(runningProcess);
                    table.addExecutionEvent(runningProcessToAdd, time + 1, runningProcessToAdd.getProcessNumber(), ProcessState.COMPLETED);
                    counter = 0;
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
                .map(Process::clone)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        
        List<Process> testProcessesList = new ArrayList<>();
       

        int totalTickets = testProcessesList.stream().mapToInt(Process::getTickets).sum();


        Lottery_Scheduling lotteryScheduling = new Lottery_Scheduling(totalTickets);


        lotteryScheduling.addNewProcesses(testProcessesList);

     
        ProcessTable processTable = lotteryScheduling.execute();

  
        System.out.println(processTable);
    }

    @Override
    public String getSchedulerName() {
        return "Planificador por sorteo ";
    }
}
