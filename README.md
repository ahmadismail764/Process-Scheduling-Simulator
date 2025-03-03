  # CPU Schedulers Simulator

This project simulates various CPU scheduling algorithms as part of **CS341 – Operating Systems 1** at **Cairo University, Faculty of Computers & Artificial Intelligence**.

## Overview

CPU scheduling is a fundamental aspect of operating systems, determining which processes run when multiple processes are eligible. This simulator implements the following schedulers:

- **Non-preemptive Priority Scheduling** (with context switching)
- **Non-preemptive Shortest Job First (SJF)** (with starvation prevention)
- **Shortest Remaining Time First (SRTF)** (with starvation prevention)
- **FCAI Scheduling**: An adaptive algorithm combining priority, arrival time, and remaining burst time.

---
## Dynamic FCAI Factor

The **FCAI Factor** is calculated using **Priority (P)**, **Arrival Time (AT)**, and **Remaining Burst Time (RBT)** as follows:

**FCAI Factor** = (10 - P) + (AT / V₁) + (RBT / V₂)

Where:  

- **V₁** = (Last Arrival Time of All Processes) ÷ 10  

- **V₂** = (Maximum Burst Time of All Processes) ÷ 10  

This ensures that **priority, arrival time, and burst time dynamically influence scheduling decisions**.

- **Quantum Allocation Rules**:

  - Initial quantum is unique to each process.
  - Quantum updates dynamically based on execution:
    - \( Q = Q + 2 \) if a process uses its quantum completely but has remaining work.
    - \( Q = Q + \text{unused quantum} \) if preempted.

- **Execution**:
  - Non-preemptive for the first 40% of quantum.
  - Allows preemption afterward.

---

## Example of FCAI Scheduling

### Input Processes

| Process | Burst Time | Arrival Time | Priority | Initial Quantum |
| ------- | ---------- | ------------ | -------- | --------------- |
| P1      | 17         | 0            | 4        | 4               |
| P2      | 6          | 3            | 9        | 3               |
| P3      | 10         | 4            | 3        | 5               |
| P4      | 4          | 29           | 8        | 2               |

### Execution Timeline

| Time Interval | Process | Action                             |
| ------------- | ------- | ---------------------------------- |
| 0–3           | P1      | Executes for 3 units, burst=14.    |
| 3–6           | P2      | Preempts P1, executes for 3 units. |
| ...           | ...     | (More detailed execution steps)    |

---

## Program Details

### Input

1. **Number of Processes**
2. **Round Robin Time Quantum**
3. **Context Switching**
4. For each process:
   - **Name**
   - **Color** (for graphical representation)
   - **Arrival Time**
   - **Burst Time**
   - **Priority Number**

### Output

For each scheduler:

- **Execution order** of processes
- **Waiting time** for each process
- **Turnaround time** for each process
- **Average waiting time**
- **Average turnaround time**
- **Quantum history updates** (for FCAI Scheduling)
- **Graphical representation** of execution order (bonus)
