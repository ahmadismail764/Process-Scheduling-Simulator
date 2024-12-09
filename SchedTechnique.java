
import java.util.List;

public interface SchedTechnique {

    public void execute(List<Process> processes, int contextSwitch);
}
