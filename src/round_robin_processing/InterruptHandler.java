import java.util.List;

public class InterruptHandler extends Thread {
    /*
     * Handles user interrupt during process simulation.
     */

    // Define class attributes
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
         * Method to run interrupt handler code.
         */

        System.out.printf("\n\nUser interrupt detected.\n\n");
        processSummary ps = new processSummary(this.completedProcesses, this.totalTime);
        ps.printSummary();
    }
}