
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SchedulingSimulator {

    public static void main(String[] args) {
        List<Process> processList = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File("processes.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\s+"); // Split by whitespace
                if (parts.length != 5) {
                    System.err.println("Invalid input format: " + line);
                    continue;
                }
                String name = parts[0];
                int arrivalTime = Integer.parseInt(parts[1]);
                int burstTime = Integer.parseInt(parts[2]);
                int quantum = Integer.parseInt(parts[3]);
                int priority = Integer.parseInt(parts[4]);
                Process process = new Process(name, arrivalTime, burstTime, quantum, priority);
                processList.add(process);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        }

        int v1 = getLatestArrival(processList), v2 = getMaximumBurstTime(processList);
        factorSetter(processList);

    }

    public static int getLatestArrival(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return -1;
        }
        int latestArrival = processes.get(0).getArrivalTime();
        for (Process process : processes) {
            if (process.getArrivalTime() > latestArrival) {
                latestArrival = process.getArrivalTime(); // Update if the current process has a greater arrival time
            }
        }
        return latestArrival;
    }

    public static int getMaximumBurstTime(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return -1;
        }
        int maxBurst = processes.get(0).getBurstTime();
        for (Process process : processes) {
            if (process.getBurstTime() > maxBurst) {
                maxBurst = process.getBurstTime(); // Update if the current process has a greater burst time
            }
        }
        return maxBurst;
    }

    public static void factorSetter(List<Process> processes) {
        for (Process process : processes) {
            double factor = (10 - process.getPriority()) + (process.getArrivalTime() / 10.0) + (process.getBurstTime() / 10.0);
            process.setFcai(factor);
        }
    }
}
