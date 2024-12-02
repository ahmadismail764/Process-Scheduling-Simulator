
public class Process extends Thread {

    private final int arrivalTime, burstTime;
    private int quantum;
    private int remainingTime;
    private double fcai_factor = 0;

    public Process(String name, int arrivalTime, int burstTime, int quantum, int priority) {
        super(name);
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.setPriority(priority);
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

    @Override
    public void run() {
        try {
            Thread.sleep(arrivalTime + burstTime);
        } catch (InterruptedException e) {
            System.err.println("Process interrupted: " + e.getMessage());
        }
    }
}
