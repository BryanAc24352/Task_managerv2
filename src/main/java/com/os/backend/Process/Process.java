package com.os.backend.Process;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javax.management.InvalidAttributeValueException;

public class Process implements Cloneable{
    private int processNumber;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int turnaroundTime;
    private int waitingTime;
    private int Tickets;

    public Process(int processNumber, int arrivalTime, int burstTime) {
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public void setTickets(int Tickets) {
        this.Tickets = Tickets;
    }

    public int getTickets() {
        return Tickets;
    }
    
    

    public Process() {
        this(0, 0, 1);
    }

    

    public int getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(int processNumber) {
        this.processNumber = processNumber;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime= arrivalTime;
    }


    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    public void decrementRemainingTime() {
        if(remainingTime==0){
            throw new RuntimeException(
                    "Process "+ this.processNumber + "has been accessed illegally."
            );
        }
        this.remainingTime--;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processNumber=" + processNumber +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", remainingTime=" + remainingTime +
                ", turnaroundTime=" + turnaroundTime +
                ", waitingTime=" + waitingTime +
                '}';
    }

    @Override
    public Process clone() {
        try {
            Process clone = (Process) super.clone();
            clone.processNumber = this.processNumber;
            clone.arrivalTime = this.arrivalTime;
            clone.burstTime = this.burstTime;
            clone.remainingTime = clone.burstTime;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Process process = (Process) o;
        return processNumber == process.processNumber && arrivalTime == process.arrivalTime && burstTime == process.burstTime;
    }

    public void setState(ProcessState processState) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void decrementRemainingTime(int guaranteedTime) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /*public void setState(ProcessState processState) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/





}
