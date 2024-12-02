
import java.util.List;

public class FCAISchedule implements ScheduleTechnique {

    public static int getLatestArrival(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            return -1;
        }
        int latestArrival = processes.get(0).getArrivalTime();
        for (Process process : processes) {
            if (process.getArrivalTime() > latestArrival) {
                latestArrival = process.getArrivalTime(); // Update if the current process has a greater arrival time
            }
        }
        return latestArrival;
    }

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

    public static void factorSetter(List<Process> processes) {
        for (Process process : processes) {
            double factor = (10 - process.getPriority()) + (process.getArrivalTime() / 10.0) + (process.getBurstTime() / 10.0);
            process.setFcai(factor);
        }
    }

    @Override
    public double calculateExecutionTime(List<Process> processList) {
        int v1 = getLatestArrival(processList), v2 = getMaximumBurstTime(processList);
        factorSetter(processList);
        return 0;
    }
}
