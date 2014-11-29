//package round_robin_processing;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    // Scheduler attributes
    Double totalTime = 0.0;
    List<Process> completedProcesses = new ArrayList<Process>();

    public class InterruptHandler extends Thread {
        /*
         * Inner class to handle interrupt during process execution.
         */

        @Override
        public void run() {
            /*
             * Method to run interrupt handler code
             */

            System.out.printf("\n\nInterrupt detected.");
            printSummary();
        }
    }

    private void addWaitTime(List<Process> readyQueue, Double waitTime) {
        /*
         * Add wait time to the processes that are not executing.
         */

        for (int i = 1; i < readyQueue.size(); i++) {
            Process p = readyQueue.get(i);
            p.waitTime = p.waitTime + waitTime;
        }
    }

    private void printSummary() {
        /*
         * Print summary of completed process statistics
         */

        System.out.printf("\nTotal program execution time: %f seconds\n", this.totalTime);

        if (this.completedProcesses.size() == 0) {
            System.out.println("No completed processes\n");
        } else {
            System.out.println("Completed processes:");
            for (int i = 0; i < this.completedProcesses.size(); i++) {

                Process completedProcess = this.completedProcesses.get(i);
                double cpuUsage = (completedProcess.burstTime / this.totalTime) * 100.;

                System.out.printf("\nProcess PID = %d:\n", completedProcess.pid);
                System.out.printf("\tTotal Execution Time: %f\n", completedProcess.burstTime);
                System.out.printf("\tWait Time: %f\n", completedProcess.waitTime);
                System.out.printf("\tTurnaround Time: %f\n", completedProcess.turnaroundTime);
                System.out.printf("\tCPU Usage: %f%%\n", cpuUsage);
            }
            System.out.println();
        }
    }

    public void roundRobin(List<Process> readyQueue, Double timeQuantum) {
        /*
         * Execute the scheduler using Round-Robin Scheduling.
         */

        // Add interrupt handler
        Runtime.getRuntime().addShutdownHook(new InterruptHandler());

        // Initialize dispatcher
        Dispatcher dispatcher = new Dispatcher();

        while (readyQueue.size() > 0) {

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

        // Print completed process statistics
        System.out.printf("\n\n*** All processes completed ***\n");
        printSummary();

        // Exit the program
        Runtime.getRuntime().halt(0);
    }
}