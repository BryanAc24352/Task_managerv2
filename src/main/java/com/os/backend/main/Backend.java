package com.os.backend.main;

import com.os.backend.Process.Process;
import com.os.backend.Process.ProcessTable;
import com.os.backend.Schedule.SchedulingAlgo;
import com.os.frontend.scheduling_window.observers.Observer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

public class Backend {

    private SchedulingAlgo scheduler;
    private SystemScheduler system;
    private List<Process> processList;
    private ProcessTable table;
    private int time;
    private Timeline timeline;

    public Backend(){
        this.system = new SystemScheduler(this);
        time = 0;
    }

    public void setAlgo(SchedulingAlgo scheduler){
        this.scheduler = scheduler;
        
    }

    public void startSchedule(){
  
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event -> {
                            system.setCurrentRunningProcess(system.getCurrentProcess(this.getTable(), time));
                            system.setProcessesAtTime(system.getCurrentProcessesTable());
                            system.notifyObservers();
                            time++;
                        }
                ),
                new KeyFrame(Duration.seconds(1)) 
        );
        timeline.setCycleCount(Animation.INDEFINITE); 
        timeline.play();


    }

    public void updateProcessesList(List<Process> newProcesses){
        this.processList = newProcesses;
        
        scheduler.addNewProcesses(this.processList);
        this.table = scheduler.execute();
    }
    
    public void attach(Observer observer){
        system.attach(observer);
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public ProcessTable getTable() {
        return table;
    }

    public SchedulingAlgo getScheduler() {
        return scheduler;
    }

    public void addProcess(Process process) {
        scheduler.addNewProcesses(Collections.singletonList(process));
        this.processList.add(process);
        this.table = scheduler.execute();
    }

    public void pauseSystem() {

        timeline.stop();
    }

    public void continueSystem() {
        startSchedule();
    }
}
