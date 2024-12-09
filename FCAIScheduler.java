
import java.util.*;
import static java.util.Comparator.*;
import java.util.stream.*;

public class FCAIScheduler {

    public static int executeProcess(Process process, int v1, int v2) {
        process.decrementRemainingTime();
        if (process.getRemainingTime() == 0) {
            return 1;
        }
        process.updateUsedQuantum();
        process.setFcai(v1, v2);
        if (process.getUsedQuant() == process.getQuantum()) {
            return 2;
        }
        return 0;
    }

    public static int compareFcais(Process a, Process b) {
        if (a.getFcai() != b.getFcai()) {
            return Double.compare(a.getFcai(), b.getFcai()); // Compare FCAIs
        }
        return Integer.compare(a.getPriority(), b.getPriority()); // Compare priorities if FCAIs are equal
    }

    public static void fcaiScheduling(List<Process> processList, int contextSwitch) {
        // Adjust input data
        int n = processList.size(), done_count = 0;
        processList.sort(comparingInt(Process::getArrivalTime));
        int v1 = processList.get(processList.size() - 1).getArrivalTime();
        int v2 = processList.stream().mapToInt(Process::getBurstTime).max().orElse(-1);
        for (Process process : processList) {
            process.setFcai(v1, v2);
        }

        // Prepare tracking variables
        List<Process> done = new ArrayList<>();
        Process inCPU = null;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(FCAIScheduler::compareFcais);
        int time = 0;
        while (done_count < n) {
            if (inCPU == null) {
                Iterator<Process> iterator = processList.iterator();
                while (iterator.hasNext()) {
                    Process process = iterator.next();
                    if (process.getArrivalTime() <= time) {
                        readyQueue.offer(process);
                        iterator.remove(); // Safely remove the element
                    }
                }
                if (!readyQueue.isEmpty()) {
                    inCPU = readyQueue.poll();
                    System.out.println("Process " + inCPU.getName() + " activated at time " + time);
                } else {
                    System.out.println("CPU is idle at time " + time++);
                    continue;
                }
            }
            // Execute the current process 
            for (Process process : readyQueue) {
                process.incrementWaitTime();
            }

            switch (executeProcess(inCPU, v1, v2)) {
                case 0 -> {
                    if (!readyQueue.isEmpty()) { // Check if you need to do a swap
                        double calc = (double) inCPU.getUsedQuant() / inCPU.getQuantum();
                        if (calc > 0.4) {
                            Process next = readyQueue.peek();
                            if (inCPU.getFcai() > next.getFcai()
                                    || (inCPU.getFcai() == next.getFcai() && inCPU.getPriority() > next.getPriority())) {
                                inCPU.updateQuantumHistory(inCPU.getQuantum() - inCPU.getUsedQuant());
                                inCPU.resetUsedQuant();
                                readyQueue.poll();
                                readyQueue.offer(inCPU);
                                inCPU = next;
                                System.out.println("Process " + inCPU.getName() + " preempted " + readyQueue.peek().getName() + " at time " + time);
                                time += contextSwitch;
                            }
                        }
                    }
                }
                case 1 -> {
                    System.out.println("Process " + inCPU.getName() + " finished at time " + time);
                    inCPU.setCompletionTime(time);
                    done_count++;
                    done.add(inCPU);
                    inCPU = null;
                }
                case 2 -> {
                    System.out.println("Process " + inCPU.getName() + " has finished a quantum at time " + time);
                    readyQueue.offer(inCPU);
                    inCPU = null;
                }
            }
            time++;
        }

        // Finally, print stats
        System.out.println("\n\nExecution finished, here are the stats: \n");
        double avgTurnaround = done.stream().mapToInt(Process::getTurnAround).average().orElse(0);
        double avgWait = done.stream().mapToInt(Process::getWaitTime).average().orElse(0);
        System.out.println(done.stream().map(Process::getName).collect(Collectors.joining(" | ")));
        for (Process process : done) {
            System.out.println("Process " + process.getName() + " arrived at time " + process.getArrivalTime());
            System.out.println(">>> " + process.getName() + " finished at time " + process.getCompletionTime());
            System.out.println(">>> " + process.getName() + " waited for time " + process.getWaitTime() + "\n");
            if (!process.getQuantumHistory().isEmpty()) {
                System.out.println(">>> The Quantuam Update History of " + process.getName() + ":");
                System.out.println(String.join(" -> ", process.getQuantumHistory().stream().map(String::valueOf).toList()));
            }
        }
        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average TurnAround Time: " + avgTurnaround);

    }
}
