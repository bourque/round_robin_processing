//package round_robin_processing;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    // Scheduler attributes
    Double totalTime = 0.0;
    List<Process> completedProcesses = new ArrayList<Process>();

    private void addWaitTime(List<Process> readyQueue, Double waitTime) {
        /*
         * Add wait time to the processes that are not executing.
         */

        for (int i = 1; i < readyQueue.size(); i++) {
            Process p = readyQueue.get(i);
            p.waitTime = p.waitTime + waitTime;
        }
    }

    public List<Process> getCompletedProcesses() {
        /*
         * Return the list of completed processes.
        */

        return this.completedProcesses;
    }

    public Double getTotalTime() {
        /*
         * Return the total system time.
        */

        return this.totalTime;
    }

    public void roundRobin(List<Process> readyQueue, Double timeQuantum) {
        /*
         * Execute the scheduler using Round-Robin Scheduling.
         */

        // Initialize dispatcher
        Dispatcher dispatcher = new Dispatcher();

        while (!readyQueue.isEmpty()) {

            // Get next process
            Process p = readyQueue.get(0);
            System.out.printf("\n\nLoading process PID = %d\n", p.pid);
            System.out.printf("\tProcess burst time: %f\n", p.burstTime);
            System.out.printf("\tProcess execution time remaining: %f\n", p.executionTime);

            // If the process execution time is greater or equal to one
            // time quantum, then execute the process for one time quantum.
            // If the execution time is less one time quantum, then execute
            // the remainder of the process.
            if (p.executionTime >= timeQuantum) {
                dispatcher.dispatch(p.pid, timeQuantum);
                p.executionTime = p.executionTime - timeQuantum;
                this.totalTime = this.totalTime + timeQuantum;
                addWaitTime(readyQueue, timeQuantum);
                readyQueue.remove(0);
            } else {
                dispatcher.dispatch(p.pid, p.executionTime);
                this.totalTime = this.totalTime + p.executionTime;
                addWaitTime(readyQueue, p.executionTime);
                p.executionTime = 0.0;
                readyQueue.remove(0);
            }

            // If the process still has execution time, the put the process
            // back into the ready queue.
            if (p.executionTime > 0) {
                readyQueue.add(p);
            } else {
                System.out.printf("\tProcess PID = %d has completed!\n", p.pid);
                p.turnaroundTime = this.totalTime;
                this.completedProcesses.add(p);
            }
        }
    }
}