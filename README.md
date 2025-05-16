# 🚦 CPU Schedulers Simulator 🚦

Simulate and visualize classic and adaptive CPU scheduling algorithms! Developed for **CS341 – Operating Systems 1** at **Cairo University, Faculty of Computers & Artificial Intelligence**.

---

## 🧠 Overview

CPU scheduling is a core aspect of operating systems, deciding which process runs when multiple are ready. This simulator features:

- 🏅 **Non-preemptive Priority Scheduling** (with context switching)
- ⏳ **Non-preemptive Shortest Job First (SJF)** (with starvation prevention)
- 🕒 **Shortest Remaining Time First (SRTF)** (with starvation prevention)
- 🧮 **FCAI Scheduling**: An adaptive algorithm combining priority, arrival time, and remaining burst time

---

## 🔬 FCAI Scheduling: Dynamic Factor

The **FCAI Factor** is calculated as:

$$
\text{FCAI Factor} = (10 - P) + \frac{AT}{V_1} + \frac{RBT}{V_2}
$$

Where:

- $P$: Priority
- $AT$: Arrival Time
- $RBT$: Remaining Burst Time
- $V_1 = \frac{\text{Last Arrival Time of All Processes}}{10}$
- $V_2 = \frac{\text{Maximum Burst Time of All Processes}}{10}$

This ensures that priority, arrival time, and burst time all dynamically influence scheduling decisions.

### ⏲️ Quantum Allocation Rules

- Each process has a unique initial quantum.
- Quantum updates dynamically:
  - $Q = Q + 2$ if a process uses its quantum completely but still has work left.
  - $Q = Q + \text{unused quantum}$ if preempted.
- Execution is non-preemptive for the first **40%** of quantum, then allows preemption.

---

## 📝 Example: FCAI Scheduling

### Input Processes

| Process | Burst Time | Arrival Time | Priority | Initial Quantum |
| ------- | ---------- | ------------ | -------- | --------------- |
| P1      | 17         | 0            | 4        | 4               |
| P2      | 6          | 3            | 9        | 3               |
| P3      | 10         | 4            | 3        | 5               |
| P4      | 4          | 29           | 8        | 2               |

### ⏱️ Execution Timeline (Sample)

| Time Interval | Process | Action                             |
| ------------- | ------- | ---------------------------------- |
| 0–3           | P1      | Executes for 3 units, burst=14     |
| 3–6           | P2      | Preempts P1, executes for 3 units  |
| ...           | ...     | (Further steps continue)           |

---

## 🛠️ Program Usage

### Input

1. **Number of Processes**
2. **Round Robin Time Quantum**
3. **Context Switching Time**
4. For each process:
   - Name
   - Color (for graphical output)
   - Arrival Time
   - Burst Time
   - Priority Number

### Output

For each scheduler, the program displays:

- 🟢 Execution order of processes
- ⏱️ Waiting time for each process
- 🔄 Turnaround time for each process
- 📊 Average waiting time
- 📈 Average turnaround time
- 🧮 Quantum history updates (FCAI Scheduling)
- 🖼️ (Bonus) Graphical representation of execution order

---

## 🚀 How to Build and Run

1. **Compile all Java files from the `src` directory into the `bin` directory:**

   ```powershell
   javac -d bin src/*.java
   ```

2. **Run the program from the project root (using the compiled classes in `bin`):**

   ```powershell
   java -cp bin Main
   ```

3. **Input:**
   - Follow the prompts for manual input, or
   - Ensure `input.txt` is present in the project root for file-based input.

---

## 💡 Notes

- All source code files (`.java`) are now in the `src/` directory.
- All compiled files (`.class`) are generated in the `bin/` directory.
- All input/output is via the console.
- Algorithms are implemented for educational purposes and may be extended.

---

> **Developed for CS341 – Operating Systems 1, Cairo University**
