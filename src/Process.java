
public class Process extends Thread {

    private final int arrivalTime, burstTime;
    private final String color;

    private int quantum;
    private int remainingTime;
    private double fcai_factor = 0;

    public Process(String name, String color, int arrivalTime, int burstTime, int priority, int quantum) {
        super(name);
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.setPriority(priority);
    }

    // Getters start here-------------------------------------------------
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
    // Getters end here-------------------------------------------------

    // Setters start here-------------------------------------------------
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setFcai(double fcai_factor) {
        this.fcai_factor = fcai_factor;
    }

    public double getFcai() {
        return fcai_factor;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    // Setters end here-------------------------------------------------
    @Override
    public void run() {
        try {
            Thread.sleep(arrivalTime + burstTime);
        } catch (InterruptedException e) {
            System.err.println("Process interrupted: " + e.getMessage());
        }
    }
}
