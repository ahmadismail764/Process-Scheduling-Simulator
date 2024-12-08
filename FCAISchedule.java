
import java.util.*;

public class FCAISchedule implements ScheduleTechnique {

    private final int v1, v2;
    private final List<Process> processes;
    private final List<Process> done;

    FCAISchedule(List<Process> processList) {
        this.processes = processList;
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        v1 = processList.get(processList.size() - 1).getArrivalTime();
        v2 = processList.stream().mapToInt(Process::getBurstTime).max().orElse(-1);
        for (Process process : processList) {
            process.setFcai(v1, v2);
        }
        done = new ArrayList<>();
    }

    public static void executeNextProcess(LinkedList<Process> readyQueue, LinkedList<Process> availQueue, int v1, int v2, int currentTime, List<Process> done) {
        Process currentProcess = availQueue.poll();
        for (Process process : availQueue) {
            process.incrementWaitTime();
        }
        currentProcess.decrementRemainingTime();
        currentProcess.updateUsedQuantum();
        currentProcess.setFcai(v1, v2);
        System.out.println("Executing process: " + currentProcess.getName() + " at time " + currentTime + ", remaining burst time: " + currentProcess.getRemainingTime());
        if (currentProcess.getUsedQuant() == currentProcess.getQuantum()) {
            System.out.println("The process " + currentProcess.getName() + " has completed a quantum at time " + currentTime);
            readyQueue.offer(currentProcess);
            availQueue.pop();
            return;
        }
        if (currentProcess.getRemainingTime() > 0) {
            availQueue.addFirst(currentProcess);
        } else {
            System.out.println("Process " + currentProcess.getName() + " finished execution at time " + currentTime);
            currentProcess.setTurnAround(currentTime);
            done.add(currentProcess);
        }
    }

    public static void addProcessSorted(LinkedList<Process> queue, Process newProcess) {
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

    @Override
    public int calculateExecutionTime() {
        LinkedList<Process> readyQueue = new LinkedList<>();
        LinkedList<Process> availQueue = new LinkedList<>();
        int currentTime = 0;
        while (!processes.isEmpty()) {
            // Check for new processes
            for (Process process : this.processes) {
                if (process.getArrivalTime() == currentTime) {
                    addProcessSorted(readyQueue, process);
                    addProcessSorted(availQueue, process);
                    System.out.println("Process " + process.getName() + " activated at time " + currentTime);
                }
            }

            // Execute processes
            if (availQueue.isEmpty()) {
                // To avoid idling
                if (readyQueue.isEmpty()) {
                    System.out.println("CPU idle at time " + currentTime);
                } else {
                    Process newProcess = readyQueue.peek();
                    newProcess.resetUsedQuant();
                    availQueue.addFirst(newProcess);
                }
            } else {
                if (availQueue.getLast() == processes.get(processes.size() - 1)) {
                    for (Process process : readyQueue) {
                        if (!availQueue.contains(process)) {
                            process.resetUsedQuant();
                            availQueue.offer(process);
                        }
                    }
                }

                if (availQueue.size() == 1) {
                    executeNextProcess(readyQueue, availQueue, v1, v2, currentTime, this.done);
                    currentTime++;
                } else {
                    Process process1 = availQueue.poll();
                    Process process2 = availQueue.poll();
                    double calc = process1.getUsedQuant() / process1.getQuantum();
                    if (calc > 0.4 && process1.getFcai() > process2.getFcai()) {
                        availQueue.offer(process1);
                        availQueue.offer(process2);
                    } else {
                        availQueue.offer(process2);
                        availQueue.offer(process1);
                        System.out.println(process2.getName() + " has preempted " + process1.getName() + " at time " + currentTime);
                        process1.updateQuantumHistory(process1.getQuantum() - process1.getUsedQuant());
                        process1.resetUsedQuant();
                    }
                    executeNextProcess(readyQueue, availQueue, v1, v2, currentTime, this.done);
                }
            }
            currentTime++;
        }
        return currentTime;
    }
}
