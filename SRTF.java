
import java.util.*;

public class SRTF implements SchedTechnique {

    int avgWT = 0, avgTAT = 0;

    @Override
    public void execute(List<Process> processes, int contextSwitch) {
        List<Process> running = new ArrayList<>();
        int i = 0, time = 0;
        int finish_count = 0;
        Process inCPU = null;

        while (finish_count < processes.size()) {
            // continuously checking if new processes arrive until all processes have arrived
            while (i < processes.size() && processes.get(i).getArrivalTime() <= time) {
                running.add(processes.get(i));
                i++;
            }
            // if still no processes arrived just increment time
            if (running.isEmpty()) {
                time++;
                continue;
            }
            // get stortest remaining time process
            Process SRTP = Collections.min(running, Comparator.comparingInt(Process::getEffBurstTime));
            int maxWaitTime = Collections.max(running, Comparator.comparingInt(Process::getWaitTime)).getWaitTime();
            int minWaitTime = Collections.min(running, Comparator.comparingInt(Process::getWaitTime)).getWaitTime();
            int median = maxWaitTime - minWaitTime;
            Process maxWaitProcess = Collections.max(running, Comparator.comparingInt(Process::getWaitTime));

            // starvation => if the max waiting process it waiting above median by 3 it is executed
            if (inCPU == null || SRTP.getRemainingTime() < inCPU.getRemainingTime()) {
                if (maxWaitProcess.getWaitTime() > median + 3) {
                    inCPU = maxWaitProcess;
                } else {
                    inCPU = SRTP;
                }
            }

            // decrementing remaining time for the executing process
            inCPU.setRemainingTime(inCPU.getRemainingTime() - 1);

            Iterator<Process> iterator = running.iterator(); // Using Iterator for safe removal
            while (iterator.hasNext()) {
                // removing finished processes from the running queue
                Process process = iterator.next();
                if (process.getRemainingTime() == 0) {
                    // Printing terminated process required info
                    System.out.printf("Process: %s\n", process.getName());
                    System.out.printf("WT: %d\n", process.getWaitTime());
                    System.out.printf("TAT: %d\n", process.getTurnAround());
                    System.out.println("------------------------------");
                    avgWT += process.getWaitTime();
                    avgTAT += process.getTurnAround();
                    finish_count++;
                    iterator.remove();
                }
                if (process != inCPU) {
                    process.incrementWaitTime();
                }
                process.incrementTurnaround();
            }
            time++;
        }
        // print overall statistics
        System.out.printf("Average Waiting Time: %d \n", avgWT / finish_count);
        System.out.printf("Average Turnaround Time: %d", avgTAT / finish_count);
    }
}
