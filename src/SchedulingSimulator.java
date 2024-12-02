
import java.io.*;
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
    }
}
