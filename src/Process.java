import java.util.*;

public class Process {

    private int arrivalTime = 0, burstTime = 0, effBurstTime = 0, priority = 0, quantum = 0, completionTime = 0;
    private String name = null, color = null;
    private int used_quant, remainingTime, waitTime, fcai_factor, turnAround;
    private final List<Integer> quantum_history;

    Process(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.effBurstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.priority = priority;
        this.waitTime = 0;
        this.used_quant = 0;
        this.turnAround = 0;
        this.quantum_history = new ArrayList<>();
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

    public int getUsedQuant() {
        return used_quant;
    }

    public int getPriority() {
        return priority;

    }

    public int getFcai() {
        return fcai_factor;

    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getTurnAround() {
        return turnAround;
    }

    public List<Integer> getQuantumHistory() {
        return quantum_history;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getEffBurstTime() {
        return this.effBurstTime;
    }

    // Getters end here-------------------------------------------------
    // Setters start here-------------------------------------------------
    public void setFcai(int v1, int v2) {
        fcai_factor = (int) Math.ceil((10 - priority) + (arrivalTime / v1) + (remainingTime / v2));
    }

    public void setRemainingTime(int r) {
        remainingTime = r;
    }

    public void setTurnAround() {
        turnAround = waitTime + burstTime;
    }

    public void setTurnAround(int t) {
        turnAround = t;
    }

    public void setCompletionTime(int t) {
        completionTime = t;
    }

    // Setters end here-------------------------------------------------
    // Updating functions start here------------------------------------ 
    public void updateQuantumHistory(int updated_quantum) {
        quantum_history.add(updated_quantum);
    }

    public void resetUsedQuant() {
        used_quant = 0;
    }

    // In the Process class
    public void decrementRemainingTime() {
        remainingTime = Math.max(0, remainingTime - 1);
    }

    public void updateUsedQuantum() {
        used_quant++;
    }

    public void incrementWaitTime(int time) {
        waitTime+= time;
    }

    public void incrementTurnaround(int time) {
        turnAround+= time;
    }

    public void decEffBurstTime(int time) {
        effBurstTime -= time / 6;
    }

    public void decPriority(int time) {
        priority -= time / 3;
    }

    public void setWaitTime(int i) {this.waitTime = i; }
    // Updating functions end here--------------------------------------
}
