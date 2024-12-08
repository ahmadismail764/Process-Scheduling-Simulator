
import java.util.*;

public class FCAISchedule implements ScheduleTechnique {

    private final int v1, v2;
    private final List<Process> processes;
    private final List<Process> done;

    FCAISchedule(List<Process> processList) {
        processes = processList;
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        v1 = processList.get(processList.size() - 1).getArrivalTime();
        v2 = processList.stream().mapToInt(Process::getBurstTime).max().orElse(-1);
        for (Process process : processList) {
            process.setFcai(v1, v2);
        }
        done = new ArrayList<>();
    }

    public void addProcessSorted(LinkedList<Process> queue, Process newProcess) {
        // If the queue is empty, simply add the process
        if (queue.isEmpty()) {
            queue.add(newProcess);
            return;
        }

        // Find the correct position for the new process
        int position = 0;
        for (Process process : queue) {
            if (newProcess.getFcai() < process.getFcai()) { // Lower FCAI has higher priority
                break; // Insert at the current position
            }
            position++;
        }

        // Insert the process at the found position
        queue.add(position, newProcess);
    }

    public void printStats() {
        double avgTurnaround = done.stream().mapToInt(Process::getTurnAround).average().orElse(0);
        double avgWait = done.stream().mapToInt(Process::getWaitTime).average().orElse(0);
        for (Process process : done) {
            System.out.println("Process " + process.getName() + " arrived at time " + process.getArrivalTime());
            System.out.println(">>> " + process.getName() + " finished at time " + (process.getTurnAround() - process.getArrivalTime()));
            System.out.println(">>> " + process.getName() + " waited for time " + process.getWaitTime() + "\n");
            System.out.println(">>> The Quantuam Update History of " + process.getName() + ":");
            System.out.println(String.join(" -> ", process.getQuantumHistory().stream().map(String::valueOf).toList()));
        }
        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average TurnAround Time: " + avgTurnaround);
    }

    @Override
    public void execution() {
        LinkedList<Process> readyQueue = new LinkedList<>();
        LinkedList<Process> availQueue = new LinkedList<>();
        int currentTime = 0, cycleCounter = 0;
        Process inCPU = null;
        while (!processes.isEmpty()) {
            // Check for new processes
            for (Process process : processes) {
                if (process.getArrivalTime() == currentTime) {
                    addProcessSorted(readyQueue, process);
                    addProcessSorted(availQueue, process);
                    System.out.println("Process " + process.getName() + " activated at time " + currentTime);
                }
            }
            if (cycleCounter == processes.size()) {
                for (Process process : readyQueue) {
                    if (!availQueue.contains(process)) {
                        process.resetUsedQuant();
                        availQueue.offer(process);
                    }
                }
                cycleCounter = 0;
            }
            if (availQueue.isEmpty()) {
                if (readyQueue.isEmpty() && inCPU == null) {
                    System.out.println("CPU idle at time " + currentTime);
                    continue;
                } else {
                    for (Process process : readyQueue) {
                        addProcessSorted(availQueue, process);
                    }
                }
            }

            if (inCPU == null) {
                inCPU = availQueue.poll();
            } else {
                double calc = inCPU.getUsedQuant() / inCPU.getQuantum();
                if (calc > 0.4 && inCPU.getFcai() > availQueue.peek().getFcai()) {
                    inCPU.updateQuantumHistory(inCPU.getQuantum() - inCPU.getUsedQuant());
                    Process temp = inCPU;
                    inCPU = availQueue.poll();
                    readyQueue.pop();
                    addProcessSorted(availQueue, temp);
                    addProcessSorted(readyQueue, temp);

                }
            }
            //------------------------------------------------------------------
            readyQueue.pop();
            for (Process process : readyQueue) {
                process.incrementWaitTime();
            }
            inCPU.decrementRemainingTime();
            inCPU.updateUsedQuantum();
            inCPU.setFcai(v1, v2);
            System.out.println("Executing process: " + inCPU.getName() + " at time " + currentTime + ", remaining burst time: " + inCPU.getRemainingTime());
            if (inCPU.getUsedQuant() == inCPU.getQuantum()) {
                System.out.println("The process " + inCPU.getName() + " has completed a quantum at time " + currentTime);
                readyQueue.offer(inCPU);
                inCPU = null;
                cycleCounter++;
            } else if (inCPU.getRemainingTime() == 0) {
                System.out.println("Process " + inCPU.getName() + " finished execution at time " + currentTime);
                inCPU.setTurnAround(currentTime);
                done.add(inCPU);
                inCPU = null;
                processes.remove(inCPU);
            }
            currentTime++;
        }
    }

}
