
import java.util.*;
import java.util.stream.Collectors;

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
            } else if (newProcess.getFcai() == process.getFcai()) {
                // If FCAI values are equal, compare priorities
                if (newProcess.getPriority() > process.getPriority()) {
                    break; // Higher priority (larger value) comes first
                }
            }
            position++;
        }

        // Insert the process at the found position
        queue.add(position, newProcess);
    }

    public void printStats() {
        double avgTurnaround = done.stream().mapToInt(Process::getTurnAround).average().orElse(0);
        double avgWait = done.stream().mapToInt(Process::getWaitTime).average().orElse(0);
        System.out.println(done.stream().map(Process::getName).collect(Collectors.joining(" | ")));
        for (Process process : done) {
            System.out.println("Process " + process.getName() + " arrived at time " + process.getArrivalTime());
            System.out.println(">>> " + process.getName() + " finished at time " + (process.getTurnAround() - process.getArrivalTime()));
            System.out.println(">>> " + process.getName() + " waited for time " + process.getWaitTime() + "\n");
            if (!process.getQuantumHistory().isEmpty()) {
                System.out.println(">>> The Quantuam Update History of " + process.getName() + ":");
                System.out.println(String.join(" -> ", process.getQuantumHistory().stream().map(String::valueOf).toList()));
            }
        }
        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average TurnAround Time: " + avgTurnaround);
    }

    @Override
    public void execution() {
        // Initialize queues and tracking variables
        LinkedList<Process> readyQueue = new LinkedList<>();
        LinkedList<Process> availQueue = new LinkedList<>();
        int currentTime = 0;
        Process inCPU = null;

        // Create a copy of processes to avoid concurrent modification
        List<Process> remainingProcesses = new ArrayList<>(processes);

        while (true) {
            // 1. Activate new processes
            Iterator<Process> processIterator = remainingProcesses.iterator();
            while (processIterator.hasNext()) {
                Process process = processIterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    addProcessSorted(readyQueue, process);
                    addProcessSorted(availQueue, process);
                    System.out.println("Process " + process.getName() + " activated at time " + currentTime);
                    processIterator.remove();
                }
            }

            // 2. Termination check
            if (remainingProcesses.isEmpty() && readyQueue.isEmpty() && availQueue.isEmpty() && inCPU == null) {
                break;
            }

            // 3. Select process for CPU
            if (inCPU == null && !availQueue.isEmpty()) {
                inCPU = availQueue.poll();
                System.out.println("Selected process: " + inCPU.getName());
            }

            // 4. Preemption logic
            if (inCPU != null && availQueue.peek() != null && availQueue.size() > 1) {
                double usageRatio = (double) inCPU.getUsedQuant() / inCPU.getQuantum();
                if (usageRatio > 0.4
                        && (inCPU.getFcai() > availQueue.peek().getFcai()
                        || (inCPU.getFcai() == availQueue.peek().getFcai() && inCPU.getPriority() > availQueue.peek().getPriority()))) {
                    // Preempt the current process
                    inCPU.updateQuantumHistory(inCPU.getQuantum() - inCPU.getUsedQuant());
                    Process preemptedProcess = inCPU;

                    // Select new process
                    inCPU = availQueue.poll();

                    // Reinsert the preempted process
                    readyQueue.remove(preemptedProcess);
                    addProcessSorted(availQueue, preemptedProcess);
                    addProcessSorted(readyQueue, preemptedProcess);

                    System.out.println("Preempted " + preemptedProcess.getName() + ", new process: " + inCPU.getName());
                }
            }
        }

        // 5. Process Execution
        if (inCPU != null) {
            System.out.println("Executing process: " + inCPU.getName()
                    + " at time " + currentTime
                    + ", remaining burst time: " + inCPU.getRemainingTime());

            // Execute the process
            inCPU.decrementRemainingTime();
            inCPU.updateUsedQuantum();
            inCPU.setFcai(v1, v2);

            // 6. Process Completion Checks
            if (inCPU.getUsedQuant() >= inCPU.getQuantum()) {
                // Process completed its quantum
                System.out.println("Process " + inCPU.getName() + " completed quantum at time " + currentTime);
                addProcessSorted(readyQueue, inCPU);
                inCPU = null;
            } else if (inCPU.getRemainingTime() == 0) {
                // Process fully completed
                System.out.println("Process " + inCPU.getName() + " finished execution at time " + currentTime);
                inCPU.setTurnAround(currentTime);
                done.add(inCPU);

                // Remove from available queue
                availQueue.remove(inCPU);
                inCPU = null;
            }
        }

        // 7. Update wait times
        for (Process process : readyQueue) {
            process.incrementWaitTime();
        }
        currentTime++;
    }
}
