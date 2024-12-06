
public class Process {

    private final int arrivalTime, burstTime, priority, quantum;
    private final String name, color;
    private int new_quant, remainingTime, waitTime, fcai_factor;

    public Process(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.priority = priority;
        this.waitTime = 0;
        this.new_quant = 0;
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

    public int getNewQuant() {
        return new_quant;
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
    // Getters end here-------------------------------------------------

    // Setters start here-------------------------------------------------
    public void decrementRemainingTime() {
        this.remainingTime--;
    }

    public void setFcai(int v1, int v2) {
        this.fcai_factor = (int) Math.ceil((10 - priority) + (arrivalTime / v1) + (remainingTime / v2));
    }

    public void updateQuantum() {
        this.new_quant++;
    }

    public void incrementWaitTime() {
        this.waitTime++;
    }
    // Setters end here-------------------------------------------------
}
