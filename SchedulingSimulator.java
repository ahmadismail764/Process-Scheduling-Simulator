
import java.io.*;
import java.util.*;

public class SchedulingSimulator {

    static List<Process> processList = new ArrayList<>();
    static int timeQuantum, contextSwitch;

    public void takeInInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of processes: ");
            int numProcesses = scanner.nextInt();
            System.out.print("Enter the Round Robin Time Quantum: ");
            timeQuantum = scanner.nextInt();
            System.out.print("Enter the Context Switching Time: ");
            contextSwitch = scanner.nextInt();
            for (int i = 0; i < numProcesses; i++) {
                System.out.println("\nEnter details for Process " + (i + 1) + ":");
                System.out.print("Process Name: ");
                String name = scanner.next();
                System.out.print("Process Burst Time: ");
                int burstTime = scanner.nextInt();
                System.out.print("Process Arrival Time: ");
                int arrivalTime = scanner.nextInt();

                System.out.print("Process Priority Number: ");
                int priority = scanner.nextInt();
                System.out.print("Process Quantum: ");
                int quantum = scanner.nextInt();
                System.out.print("Process Color (Graphical Representation): ");
                String color = scanner.next();
                Process process = new Process(name, color, arrivalTime, burstTime, priority, quantum);
                processList.add(process);
            }
        }
    }

    public void fileInput(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            // Read the number of processes, quantum, and context switching time
            int numProcesses = scanner.nextInt();
            timeQuantum = scanner.nextInt();
            contextSwitch = scanner.nextInt();

            // Read process details from the file
            for (int i = 0; i < numProcesses; i++) {
                String name = scanner.next();
                int burstTime = scanner.nextInt();
                int arrivalTime = scanner.nextInt();
                int priority = scanner.nextInt();
                int quantum = scanner.nextInt();
                String color = scanner.next();

                // Create and add the process to the process list
                Process process = new Process(name, color, arrivalTime, burstTime, priority, quantum);
                processList.add(process);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        SchedulingSimulator simulator = new SchedulingSimulator();
        simulator.fileInput("processes.txt");
        System.out.println("\nScheduling Simulation is ready to proceed with input parameters.");
        FCAIScheduler.fcaiScheduling(processList, contextSwitch);
    }
}
