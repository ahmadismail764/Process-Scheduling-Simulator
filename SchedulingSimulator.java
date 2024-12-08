
import java.util.*;

public class SchedulingSimulator {

    static List<Process> processList = new ArrayList<>();
    int timeQuantum, contextSwitching;

    public void takeInInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of processes: ");
            int numProcesses = scanner.nextInt();
            System.out.print("Enter the Round Robin Time Quantum: ");
            timeQuantum = scanner.nextInt();
            System.out.print("Enter the Context Switching Time: ");
            contextSwitching = scanner.nextInt();
            for (int i = 0; i < numProcesses; i++) {
                System.out.println("\nEnter details for Process " + (i + 1) + ":");
                System.out.print("Process Name: ");
                String name = scanner.next();
                System.out.print("Process Color (Graphical Representation): ");
                String color = scanner.next();
                System.out.print("Process Arrival Time: ");
                int arrivalTime = scanner.nextInt();
                System.out.print("Process Burst Time: ");
                int burstTime = scanner.nextInt();
                System.out.print("Process Priority Number: ");
                int priority = scanner.nextInt();
                System.out.print("Process Quantum: ");
                int quantum = scanner.nextInt();
                Process process = new Process(name, color, arrivalTime, burstTime, priority, quantum);
                processList.add(process);
            }
        }
    }

    public static void main(String[] args) {
        SchedulingSimulator simulator = new SchedulingSimulator();
        simulator.takeInInput();
        System.out.println("\nScheduling Simulation is ready to proceed with input parameters.");
        FCAISchedule instance = new FCAISchedule(processList);
        instance.execution();
        instance.printStats();
    }
}
