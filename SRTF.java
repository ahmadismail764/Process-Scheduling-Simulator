import java.util.*;

public class SRTF implements SchedTechnique {

    int totalWT = 0, totalTAT = 0;

    @Override
    public void execute(List<Process> processes, int contextSwitch) {
        List<Process> running = new ArrayList<>();
        int i = 0, time = 0;
        int finishCount = 0;
        Process inCPU = null;

        while (finishCount < processes.size()) {
            // Add newly arrived processes to the running list
            while (i < processes.size() && processes.get(i).getArrivalTime() <= time) {
                running.add(processes.get(i));
                i++;
            }

            // If no processes are ready, increment time and continue
            if (running.isEmpty()) {
                time++;
                continue;
            }

            // Get the process with the shortest remaining time
            Process SRTP = Collections.min(running, Comparator.comparingInt(Process::getRemainingTime));
            Process maxWaitProcess = Collections.max(running, Comparator.comparingInt(Process::getWaitTime));

            // Calculate median waiting time
            int maxWaitTime = maxWaitProcess.getWaitTime();
            int minWaitTime = Collections.min(running, Comparator.comparingInt(Process::getWaitTime)).getWaitTime();
            int median = maxWaitTime - minWaitTime;

            // Starvation handling
            if (inCPU == null || SRTP.getRemainingTime() < inCPU.getRemainingTime()) {
                time += contextSwitch;
                for (Process process : running) {
                    if (process != inCPU) {
                        process.incrementWaitTime(contextSwitch);
                    }
                    process.incrementTurnaround(contextSwitch);
                }
                if (maxWaitProcess.getWaitTime() > median + 3) {
                    inCPU = maxWaitProcess;
                } else {
                    inCPU = SRTP;
                }
            }

            // Execute the selected process for one unit of time
            inCPU.setRemainingTime(inCPU.getRemainingTime() - 1);
            time++; // Increment time after executing

            // Check if the process has finished execution
            if (inCPU.getRemainingTime() == 0) {
                inCPU.setTurnAround(time - inCPU.getArrivalTime());
                inCPU.setWaitTime(inCPU.getTurnAround() - inCPU.getBurstTime());

                // Validate metrics to prevent negative or overflowed values
                if (inCPU.getWaitTime() < 0) {
                    inCPU.setWaitTime(0);
                }

                // Print process metrics
                System.out.printf("Process: %s\n", inCPU.getName());
                System.out.printf("WT: %d\n", inCPU.getWaitTime());
                System.out.printf("TAT: %d\n", inCPU.getTurnAround());
                System.out.println("------------------------------");

                totalWT += inCPU.getWaitTime();
                totalTAT += inCPU.getTurnAround();
                finishCount++;
                running.remove(inCPU); // Safely remove from running list
                inCPU = null; // Reset CPU
            }

            // Increment waiting and turnaround times for all other processes
            for (Process process : running) {
                if (process != inCPU) {
                    process.incrementWaitTime(1);
                }
                process.incrementTurnaround(1);
            }
        }

        // Print overall statistics
        System.out.printf("Average Waiting Time: %.2f \n", (double) totalWT / processes.size());
        System.out.printf("Average Turnaround Time: %.2f \n", (double) totalTAT / processes.size());
    }
}
