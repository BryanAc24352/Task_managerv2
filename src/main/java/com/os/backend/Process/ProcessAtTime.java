package com.os.backend.Process;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class ProcessAtTime {
    private final Process process;
    private ProcessState state;

    public ProcessAtTime(Process process, int time, ProcessTable table) {
        this.process = process;
        updateState(time, table);
    }

    public void updateState(int time, ProcessTable table) {
        List<ProcessExecutionEvent> currentTable = table.getProcessesList(time);

        
        if (currentTable.stream().noneMatch(e -> e.getProcessNumber() == process.getProcessNumber())) {
            this.state = ProcessState.PENDING;
            return;
        }

        
        List<ProcessExecutionEvent> processEvents = currentTable.stream()
                .filter(e -> e.getProcessNumber() == process.getProcessNumber())
                .toList();

        
        this.state = processEvents.get(processEvents.size() - 1).getState();
    }

    public String getProcessNumber() {
        return "P" + process.getProcessNumber();
    }

    public Rectangle getState() {
        String color = getColorForState(state);
        return new Rectangle(100, 20, Color.web(color));

    }

    private String getColorForState(ProcessState state) {
        switch (state) {
            case ARRIVED -> {
                return "#add8e6";
            }
            case PENDING -> {
                return "#cccccc";
            }
            case RUNNING -> {
                return "#00ff00";
            }
            case INTERRUPTED -> {
                return "#ff0000";
            }
            case STARTED -> {
                return "#ffdab9";
            }
            case COMPLETED -> {
                return "#008000";
            }
        }
        return "white";
    }
    public int getArrivalTime() {
        return process.getArrivalTime();
    }

    public int getBurstTime() {
        return process.getBurstTime();
    }

    public int getRemainingTime() {
        return process.getRemainingTime();
    }

    public int getTurnaroundTime() {
        return process.getTurnaroundTime();
    }

    public int getWaitingTime() {
        return process.getWaitingTime();
    }

    public int getPriority() {
        if (process instanceof PriorityProcess) {
            return ((PriorityProcess) process).getPriority();
        }
        return -1;
    }

}
