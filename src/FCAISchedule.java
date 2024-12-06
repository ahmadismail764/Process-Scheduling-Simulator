
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
            double fact = (10 - process.getPriority()) + (process.getArrivalTime() / v1) + (process.getBurstTime() / v2);
            int factor = (int) Math.ceil(fact);
            process.setFcai(factor);
        }
    }

    @Override
    public int calculateExecutionTime(List<Process> processList) {
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));
        int v1 = processList.get(processList.size() - 1).getArrivalTime();
        int v2 = getMaximumBurstTime(processList);
        factorSetter(processList, v1, v2);
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparing(Process::getFcai));
        int totalExecutionTime = 0;
        for (int currentTime = 0; currentTime <= v1; currentTime++) {
            Iterator<Process> iterator = new ArrayList<>(processList).iterator(); // Make a copy to avoid ConcurrentModificationException
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getArrivalTime() == currentTime) {
                    readyQueue.offer(process);
                    iterator.remove();
                    System.out.println("Process " + process.getName() + " activated at time " + currentTime);
                }
            }

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                System.out.println("Executing process: " + currentProcess.getName());

                // Decrease burst time for 1 second
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                totalExecutionTime++;
                System.out.println("Process " + currentProcess.getName() + " remaining burst time: " + currentProcess.getRemainingTime());

                // If the process is not finished, re-add it to the queue
                if (currentProcess.getRemainingTime() > 0) {
                    readyQueue.offer(currentProcess);
                } else {
                    System.out.println("Process " + currentProcess.getName() + " finished execution at time " + currentTime);
                }
            }
        }

        return totalExecutionTime;
    }
}
