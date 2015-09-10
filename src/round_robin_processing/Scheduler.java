import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Scheduler {
    /*
     * Provides methods to schedule process and keep track of system times.
    */

    // Define class attributes
    Double totalTime = 0.0;
    List<Process> completedProcesses = new ArrayList<Process>();
    Double timeQuantum = 1.0;


    public Scheduler(Double tq) {
        /*
         * Constructor method.
        */

        timeQuantum = tq;
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


    public List<Process> getCompletedProcesses() {
        /*
         * Return the list of completed processes.
        */

        return this.completedProcesses;
    }


    private int getNextArrival(List<Process> processList) {
        /*
         * Return the index of the process with the earliest arrival time.
        */

        int index = 0;
        Double earliest = processList.get(0).arrivalTime;

        for (int i=1; i<processList.size(); i++) {
            Double arrivalTime = processList.get(i).arrivalTime;
            if (arrivalTime < earliest) {
                index = i;
                earliest = arrivalTime;
            }
        }

        return index;
    }


    public Double getTotalTime() {
        /*
         * Return the total system time.
        */

        return this.totalTime;
    }


    public void roundRobin(List<Process> processList) {
        /*
         * Execute the scheduler using Round-Robin Scheduling.
         */

        // Initialize dispatcher
        Dispatcher dispatcher = new Dispatcher();

        // Continue only when there are processes still to be executed
        while (!processList.isEmpty()) {

            // Move next process into ready queue
            List<Process> readyQueue = new ArrayList<Process>();
            int nextArrival = getNextArrival(processList);
            Process nextProcess = processList.get(nextArrival);
            readyQueue.add(nextProcess);
            processList.remove(nextArrival);

            // Starve the CPU until the next process arrives
            Double starveTime = readyQueue.get(0).arrivalTime - this.totalTime;
            dispatcher.starve(starveTime);
            this.totalTime = this.totalTime + starveTime;
            System.out.printf("Process PID = %d has arrived", readyQueue.get(0).pid);

            // Schedule the processes in the ready queue
            while (!readyQueue.isEmpty()) {

                // Get the next process
                Process p = readyQueue.get(0);
                System.out.printf("\n\n\tLoading process PID = %d\n", p.pid);
                System.out.printf("\tProcess burst time: %f\n", p.burstTime);
                System.out.printf("\tProcess execution time remaining: %f\n", p.executionTime);
                System.out.printf("\tSystem time: %f\n", this.totalTime);

                // If the process execution time is greater or equal to one
                // time quantum, then execute the process for one time quantum.
                if (p.executionTime >= this.timeQuantum) {
                    dispatcher.dispatch(p.pid, this.timeQuantum);
                    p.executionTime = p.executionTime - this.timeQuantum;
                    this.totalTime = this.totalTime + this.timeQuantum;
                    addWaitTime(readyQueue, this.timeQuantum);
                    readyQueue.remove(0);
                    System.out.printf("\tSystem time: %f\n", this.totalTime);

                // If the execution time is less one time quantum, then execute
                // the remainder of the process.
                } else {
                    dispatcher.dispatch(p.pid, p.executionTime);
                    this.totalTime = this.totalTime + p.executionTime;
                    addWaitTime(readyQueue, p.executionTime);
                    p.executionTime = 0.0;
                    readyQueue.remove(0);
                    System.out.printf("\tSystem time: %f\n", this.totalTime);
                }

                // Check to see if any new processes have arrived. If so, move them into the ready queue
                for (Iterator<Process> iterator = processList.iterator(); iterator.hasNext();) {
                    Process newProc = iterator.next();
                    if (newProc.arrivalTime <= this.totalTime) {
                        System.out.printf("Process PID = %d has arrived\n", newProc.pid);
                        readyQueue.add(newProc);
                        iterator.remove();
                    }
                }

                // check to see if the current process still has execution time remaining.
                // If it does, then put the process back into the ready queue.
                if (p.executionTime > 0) {
                    readyQueue.add(p);
                } else {
                    System.out.printf("Process PID = %d has completed!\n", p.pid);
                    p.turnaroundTime = this.totalTime;
                    this.completedProcesses.add(p);
                }
            }

            // Ready Queue is empty, but there are still processes yet to arrive
            // so reschedule remaining processes
            roundRobin(processList);
        }
    }
}