import java.util.*;

public class PriorityBased implements SchedTechnique {

    int avgWT = 0, avgTAT = 0;

    @Override
    public void execute(List<Process> processes, int contextSwitch) {
        int n = processes.size();
        List<Process> running = new ArrayList<>();
        int i = 0, time = 0, finish_count = 0;
        Process inCPU = null;

        while (finish_count < n) {
            while (i < processes.size() && processes.get(i).getArrivalTime() <= time) {
                running.add(processes.get(i));
                i++;
            }
            if (running.isEmpty()) {
                time++;
                continue;
            }

            Process leastPriorityProcess = Collections.min(running, Comparator.comparingInt(Process::getPriority));
            if (inCPU == null) {
                time += contextSwitch;
                for (Process process : running) {
                    if (process != inCPU) {
                        process.incrementWaitTime(contextSwitch);
                    }
                    process.incrementTurnaround(contextSwitch);
                }
                inCPU = leastPriorityProcess;
                time++;
                continue;
            }
            if (leastPriorityProcess.getPriority() < inCPU.getPriority()) {
                time += contextSwitch;
                for (Process process : running) {
                    if (process != inCPU) {
                        process.incrementWaitTime(contextSwitch);
                    }
                    process.incrementTurnaround(contextSwitch);
                }
                inCPU = leastPriorityProcess;
            }

            inCPU.setRemainingTime(inCPU.getRemainingTime() - 1);

            Iterator<Process> iterator = running.iterator(); // Use Iterator for safe removal
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getRemainingTime() == 0) {
                    // Print process required info
                    System.out.printf("Process: %s\n", process.getName());
                    System.out.printf("WT: %d\n", process.getWaitTime());
                    System.out.printf("TAT: %d\n", process.getTurnAround());
                    System.out.println("------------------------------");
                    avgWT += process.getWaitTime();
                    avgTAT += process.getTurnAround();
                    finish_count++;

                    // Remove the completed process from the running list
                    iterator.remove();
                }
                // to solve starvation problem
                if (process.getBurstTime() > process.getRemainingTime() && process != inCPU) {
                    process.decPriority(time);
                }
                if (process != inCPU) {
                    process.incrementWaitTime(1);
                }
                process.incrementTurnaround(1);

                // If no processes are currently in the CPU, just increment time
                if (inCPU != null && inCPU.getRemainingTime() == 0) {
                    inCPU = null;  // Set CPU to idle when the current process finishes
                }
            }
            time++;
        }
        System.out.printf("Average Waiting Time: %d \n", avgWT / finish_count);
        System.out.printf("Average Turnaround Time: %d", avgTAT / finish_count);
    }
}
