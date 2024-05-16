package com.os.backend.Schedule;
import java.util.ArrayList;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessTable;

import java.util.List;

public abstract class SchedulingAlgo {
    protected List<Process> processesList;

    public SchedulingAlgo() {
        processesList = new ArrayList<Process>();
    }

    public void addNewProcesses(List<Process> newProcesses) {
        processesList.addAll(newProcesses);
    }

    public abstract ProcessTable execute();

    public List<Process> getProcessesList() {
        return processesList;
    }

    //TODO: must be called in added live processes
    public void setProcessesList(List<Process> processesList) {
        this.processesList = processesList;
    }

    public abstract String getSchedulerName();
}
