//package round_robin_processing;

public class Process {

    Integer pid;
    Double arrivalTime;
    Double burstTime;
    Double executionTime;
    Double waitTime;
    Double turnaroundTime;

    public Process() {
        /*
         * Constructor method.
         */

        pid = 1;
        arrivalTime = 0.0;
        burstTime = 0.0;
        executionTime = 0.0;
        waitTime = 0.0;
        turnaroundTime = 0.0;
    }
}