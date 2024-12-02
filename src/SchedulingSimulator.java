
import java.util.*;

public class SchedulingSimulator {

    public static void main(String[] args) {
        int n;
        // Input number of processes
        try (Scanner scanner = new Scanner(System.in)) {
            // Input number of processes
            System.out.print("Enter the number of processes: ");
            n = scanner.nextInt();
            // Create a list to store processes
            // List<Process> processes = new ArrayList<>();
        }

        // Input process details
        for (int i = 0; i < n; i++) {
            // System.out.println("Enter details for Process " + (i + 1) + ":");
            // System.out.print("Name: ");
            // String name = scanner.next();
            // System.out.print("Color: ");
            // String color = scanner.next();
            // System.out.print("Arrival Time: ");
            // int arrivalTime = scanner.nextInt();
            // System.out.print("Burst Time: ");
            // int burstTime = scanner.nextInt();
            // System.out.print("Priority: ");
            // int priority = scanner.nextInt();

            // processes.add(new Process(name, color, arrivalTime, burstTime, priority));
        }

        // Input Round Robin Quantum
        // System.out.print("Enter Round Robin Time Quantum: ");
        // int rrQuantum = scanner.nextInt();
        // // Input context switching time
        // System.out.print("Enter Context Switching Time: ");
        // int contextSwitch = scanner.nextInt();
        // Execute scheduling algorithms
        // System.out.println("\n--- Non-Preemptive Priority Scheduling ---\n");
        // priorityScheduling(processes, contextSwitch);
        // System.out.println("\n--- Non-Preemptive Shortest Job First (SJF) ---\n");
        // sjfScheduling(processes, contextSwitch);
        // System.out.println("\n--- Shortest Remaining Time First (SRTF) ---\n");
        // srtfScheduling(processes, contextSwitch);
        // System.out.println("\n--- FCAI Scheduling ---\n");
        // fcaiScheduling(processes, rrQuantum, contextSwitch);
    }
}
