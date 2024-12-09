
import java.util.*;

public class SJF implements SchedTechnique {

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
            // get shortest job
            Process SJP = Collections.min(running, Comparator.comparingInt(Process::getEffBurstTime));

            if (inCPU == null || SJP.getEffBurstTime() < inCPU.getEffBurstTime()) {
                inCPU = SJP;
            }
            inCPU.setRemainingTime(inCPU.getRemainingTime() - 1);
            if (inCPU.getRemainingTime() == 0) {
                inCPU = null;
            }

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

                    // Remove the completed process from the running list
                    iterator.remove();
                }
                // this is to solve the starvation problem
                if (process.getBurstTime() > process.getRemainingTime() && process != inCPU) {
                    process.decEffBurstTime(time);
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
