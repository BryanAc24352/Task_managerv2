package com.os.backend.Process;

public class PriorityProcess extends Process implements Cloneable, Comparable<PriorityProcess> {
    private int priority;


    public PriorityProcess(int processNumber, int arrivalTime, int burstTime, int priority) {
        super(processNumber, arrivalTime, burstTime);
        this.priority = priority;
    }

    public PriorityProcess(int priority) {
        super();
        this.priority = priority;
    }

    public PriorityProcess() {
        this(0);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PriorityProcess{" +
                "priority=" + priority +
                ", " +
                super.toString() +
                '}';
    }

    @Override
    public PriorityProcess clone() {
        PriorityProcess clone = (PriorityProcess) super.clone();
        clone.priority = this.priority;
        return clone;
    }

    @Override
    public int compareTo(PriorityProcess o) {
        int priorityComparison = Integer.compare(this.priority, o.priority);
        if (priorityComparison != 0) {
            return priorityComparison; 
        }

        int arrivalTimeComparison = Integer.compare(this.getArrivalTime(), o.getArrivalTime());
        if (arrivalTimeComparison != 0) {
            return arrivalTimeComparison;
        }

        
        return Integer.compare(this.getProcessNumber(), o.getProcessNumber());
    }
}
