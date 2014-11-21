import java.util.*;

public class Scheduler {

    public void roundRobin(List<Process> readyQueue, Double timeQuantum) {
        /*
         * Execute the scheduler using Round-Robin Scheduling.
        */

        while (readyQueue.size() > 0) {

            // Get next process
            Process p = readyQueue.get(0);
            System.out.printf("\n\nLoading process PID = %s\n", p.pid);
            System.out.printf("\tProcess execution time: %f\n", p.executionTime);
            System.out.printf("\tProcess burst time: %f\n", p.burstTime);

            // If the process execution time is greater or equal to one
            // time quantum, then execute the process for one time quantum.  
            // If the execution time is less one time quantum, then execute
            // the remainder of the process.
            if (p.executionTime >= timeQuantum) {
                System.out.printf("\tExecuting process PID = %s\n", p.pid);
                sleep(convertToMillis(timeQuantum));
                p.executionTime = p.executionTime - timeQuantum;
                readyQueue.remove(0);
            } else {
                System.out.printf("\tExecuting process PID = %s\n", p.pid);
                sleep(convertToMillis(p.executionTime));
                p.executionTime = 0;
                readyQueue.remove(0);
            }

            // If the process still has execution time, the put the process
            // back into the ready queue.
            if (p.executionTime > 0) {
                readyQueue.add(p);
            } else {
                System.out.printf("\tProcess PID = %s has completed!\n", p.pid);
            }
        }
    }


    private void sleep(int sleepTime) {
        /*
         * Suspend the program execution for the timeQuantum amount.
        */

        try {
            System.out.printf("\tSleeping for %d\n", sleepTime);
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
}