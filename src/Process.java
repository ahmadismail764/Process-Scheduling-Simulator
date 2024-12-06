
public class Process {

    private final int arrivalTime, burstTime, priority;
    private final String name, color;
    private int quantum, remainingTime, waitTime, fcai_factor;

    public Process(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.priority = priority;
        this.waitTime = 0;
    }

    // Getters start here-------------------------------------------------
    public final String getName() {
        return name;
    }

    public final String getColor() {
        return color;
    }

    public final int getArrivalTime() {
        return arrivalTime;
    }

    public final int getBurstTime() {
        return burstTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public int getQuantum() {
        return quantum;
    }

    public int getPriority() {
        return priority;
    }
    // Getters end here-------------------------------------------------

    // Setters start here-------------------------------------------------
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setFcai(int fcai_factor) {
        this.fcai_factor = fcai_factor;
    }

    public double getFcai() {
        return fcai_factor;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
    // Setters end here-------------------------------------------------
}
