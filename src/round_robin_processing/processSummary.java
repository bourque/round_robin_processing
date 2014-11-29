//package round_robin_processing;

import java.util.List;

public class processSummary {

    public List<Process> completedProcesses;
    public Double totalTime;

    public processSummary(List<Process> compProcs, Double totTime) {
        /*
         * Constructor method.
        */

        completedProcesses = compProcs;
        totalTime = totTime;
    }

    public void printSummary() {
        /*
         * Print summary of completed process statistics
         */

        if (this.totalTime > 0) {
            System.out.printf("\nTotal program execution time: %f seconds\n", this.totalTime);
        }

        if (this.completedProcesses.size() == 0) {
            System.out.println("No completed processes\n");
        } else {
            System.out.println("Completed processes:");
            for (int i = 0; i < this.completedProcesses.size(); i++) {

                Process completedProcess = this.completedProcesses.get(i);

                System.out.printf("\nProcess PID = %d:\n", completedProcess.pid);
                System.out.printf("\tTotal Execution Time: %f\n", completedProcess.burstTime);
                System.out.printf("\tWait Time: %f\n", completedProcess.waitTime);
                System.out.printf("\tTurnaround Time: %f\n", completedProcess.turnaroundTime);

                if (this.totalTime > 0) {
                    double cpuUsage = (completedProcess.burstTime / this.totalTime) * 100.;
                    System.out.printf("\tCPU Usage: %f%%\n", cpuUsage);
                }
            }
            System.out.println();
        }
    }
}
