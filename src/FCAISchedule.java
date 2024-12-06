
import java.util.*;

public class FCAISchedule implements ScheduleTechnique {

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

    public static void factorSetter(List<Process> processes, int v1, int v2) {
        for (Process process : processes) {
            process.setFcai(v1, v2);
        }
    }

    public static void executeProcess(PriorityQueue<Process> readyQueue, int v1, int v2, int currentTime, List<Process> done) {
        Process currentProcess = readyQueue.poll();
        for (Process process : readyQueue) {
            process.incrementWaitTime();
        }
        currentProcess.decrementRemainingTime();
        currentProcess.updateQuantum();
        currentProcess.setFcai(v1, v2);
        System.out.println("Executing process: " + currentProcess.getName() + " at time " + currentTime + ", remaining burst time: " + currentProcess.getRemainingTime());
        if (currentProcess.getRemainingTime() > 0) {
            readyQueue.offer(currentProcess);
        } else {
            System.out.println("Process " + currentProcess.getName() + " finished execution at time " + currentTime);
            done.add(currentProcess);
        }
    }

    @Override
    public int calculateExecutionTime(List<Process> processList) {
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));
        int v1 = processList.get(processList.size() - 1).getArrivalTime();
        int v2 = getMaximumBurstTime(processList);
        factorSetter(processList, v1, v2);

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparing(Process::getFcai));
        List<Process> done = new ArrayList<>();
        int totalExecutionTime = 0;
        for (int currentTime = 0; currentTime <= v1; currentTime++) {
            // Step 1: Check for new processes
            for (Process process : processList) {
                if (process.getArrivalTime() == currentTime) {
                    readyQueue.offer(process);
                    processList.remove(process);
                    System.out.println("Process " + process.getName() + " activated at time " + currentTime);
                }
            }

            // Step 2: Execute processes
            if (!readyQueue.isEmpty()) {
                if (readyQueue.size() == 1) {
                    executeProcess(readyQueue, v1, v2, currentTime, done);
                    totalExecutionTime++;
                } else {
                    Process process1 = readyQueue.poll();
                    Process process2 = readyQueue.poll();
                    double calc = process1.getNewQuant() / process1.getQuantum();
                    if (calc > 0.4 && process1.getFcai() > process2.getFcai()) {
                        readyQueue.offer(process1);
                        readyQueue.offer(process2);
                    } else {
                        readyQueue.offer(process2);
                        readyQueue.offer(process1);
                    }
                    executeProcess(readyQueue, v1, v2, currentTime, done);
                }
            } else {
                System.out.println("CPU idle at time " + currentTime);
            }
        }
        return totalExecutionTime;
    }
}
