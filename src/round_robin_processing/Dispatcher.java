//package round_robin_processing;

public class Dispatcher {

    public void dispatch(int pid, Double cpuTime) {
        /*
         * Dispatches the process to the CPU
        */

        execute(pid, convertToMillis(cpuTime));
    }

    private int convertToMillis(Double cpuTime) {
        /*
         * Convert the time quantum from double (in seconds) to integer (in
         * milliseconds)
         */

        Double cpuTimeMilli = cpuTime * 1000;
        int cpuTimeInt = cpuTimeMilli.intValue();
        return cpuTimeInt;
    }

    private void execute(int pid, int cpuTime) {
        /*
         * Suspend the program execution for the timeQuantum amount.
         */

        try {
            System.out.printf("\tExecuting process %d for %d milliseconds\n", pid, cpuTime);
            Thread.sleep(cpuTime);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
}