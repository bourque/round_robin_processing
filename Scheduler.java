import java.util.*;

public class Scheduler {

    double totalTime = 0.0;
    List<Process> completedProcesses = new ArrayList<Process>();

    public void roundRobin(List<Process> readyQueue, Double timeQuantum) {
        /*
         * Execute the scheduler using Round-Robin Scheduling.
        */

        while (readyQueue.size() > 0) {

            // Get next process
            Process p = readyQueue.get(0);
            System.out.printf("\n\nLoading process PID = %s\n", p.pid);
            System.out.printf("\tProcess burst time: %f\n", p.burstTime);
            System.out.printf("\tProcess execution time remaining: %f\n", p.executionTime);

            // If the process execution time is greater or equal to one
            // time quantum, then execute the process for one time quantum.  
            // If the execution time is less one time quantum, then execute
            // the remainder of the process.
            if (p.executionTime >= timeQuantum) {
                sleep(convertToMillis(timeQuantum), p.pid);
                p.executionTime = p.executionTime - timeQuantum;
                this.totalTime = this.totalTime + timeQuantum;
                readyQueue.remove(0);
            } else {
                sleep(convertToMillis(p.executionTime), p.pid);
                this.totalTime = this.totalTime + p.executionTime;
                p.executionTime = 0;
                readyQueue.remove(0);
            }

            // If the process still has execution time, the put the process
            // back into the ready queue.
            if (p.executionTime > 0) {
                readyQueue.add(p);
            } else {
                System.out.printf("\tProcess PID = %s has completed!\n", p.pid);
                p.turnaroundTime = this.totalTime;
                this.completedProcesses.add(p);
            }
        }

        // Print completed process statistics
        printSummary();
    }


    private void sleep(int sleepTime, String pid) {
        /*
         * Suspend the program execution for the timeQuantum amount.
        */

        try {
            System.out.printf("\tExecuting process %s for %d milliseconds\n", pid, sleepTime);
            Thread.sleep(sleepTime);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }


    private int convertToMillis(Double timeQuantum) {

        Double timeQuantumMilli = timeQuantum * 1000;
        int sleepTime = timeQuantumMilli.intValue();
        return sleepTime;
    }


    private void printSummary() {
        /*
         * Print summary of completed process statistics
        */

        System.out.printf("\n\n***All processes completed***\n");
        System.out.printf("\nTotal program execution time: %f seconds\n", this.totalTime);
        for (int i=0; i<this.completedProcesses.size(); i++) {

            Process completedProcess = this.completedProcesses.get(i);
            double cpuUsage = (completedProcess.burstTime / this.totalTime) * 100.;

            System.out.printf("\nProcess PID = %s:\n", completedProcess.pid);
            System.out.printf("\tTotal Execution Time: %f\n", completedProcess.burstTime);
            System.out.printf("\tWait Time: %f\n", completedProcess.waitTime);
            System.out.printf("\tTurnaround Time: %f\n", completedProcess.turnaroundTime);
            System.out.printf("\tCPU Usage: %f%%\n", cpuUsage);
        }
    }
}