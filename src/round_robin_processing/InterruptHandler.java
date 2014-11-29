//package round_robin_processing;

import java.util.List;

public class InterruptHandler extends Thread {
    /*
     * Inner class to handle interrupt during process execution.
     */

    List<Process> completedProcesses;
    Double totalTime;

    public InterruptHandler(List<Process> compProcs, Double totTime) {
        /*
         * Constructor method.
        */

        completedProcesses = compProcs;
        totalTime = totTime;
    }

    @Override
    public void run() {
        /*
         * Method to run interrupt handler code
         */

        System.out.printf("\n\nUser interrupt detected.");
        processSummary ps = new processSummary(completedProcesses, totalTime);
        ps.printSummary();
    }
}