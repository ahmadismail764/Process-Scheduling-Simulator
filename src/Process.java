
public class Process extends Thread {

    private final int arrivalTime;
    private final int burstTime;
    private int remainingTime;
    private int quantum;

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        super(name);
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
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

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getQuantum() {
        return quantum;
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
