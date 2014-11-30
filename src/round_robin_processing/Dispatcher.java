//package round_robin_processing;

public class Dispatcher {
    /*
     * Provides methods to dispatch a process to a CPU.
    */


    private int convertToMillis(Double cpuTime) {
        /*
         * Convert the time quantum from double (in seconds) to integer (in milliseconds)
         */

        Double cpuTimeMilli = cpuTime * 1000;
        int cpuTimeInt = cpuTimeMilli.intValue();
        return cpuTimeInt;
    }


    public void dispatch(int pid, Double cpuTime) {
        /*
         * Dispatches the process to the CPU
        */

        execute(pid, convertToMillis(cpuTime));
    }


    private void execute(int pid, int cpuTime) {
        /*
         * Execute the process for the timeQuantum amount.
         */

        try {
            System.out.printf("\tExecuting process %d for %d milliseconds\n", pid, cpuTime);
            Thread.sleep(cpuTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }


    public void starve(Double cpuTime) {
        /*
         * Starve the CPU for the given amount of time.
        */

        int cpuTimeMilli = convertToMillis(cpuTime);

        try {
            System.out.println("Waiting for next process to arrive...\n");
            Thread.sleep(cpuTimeMilli);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}