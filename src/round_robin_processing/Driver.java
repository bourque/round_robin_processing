//package round_robin_processing;

/* Matthew Bourque
 * Ravi Chandra
 * COSC 519
 * Process Modeling Project
 *
 * This program is the main driver for the round-robin process scheduling
 * project.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Driver {

    public List<Integer> usedPIDs = new ArrayList<Integer>();
    public static Integer minPID = 0;
    public static Integer maxPID = 99999;
    public static Double minBurst = 5.0;
    public static Double maxBurst = 10.0;

    private static Double assignBurstTime() {
        /*
         * Return a random float between 5 and 10 to be used as a process
         * burst time.
        */

        Random r = new Random();
        Double burstTime = minBurst + (maxBurst - minBurst) * r.nextDouble();

        return burstTime;
    }

    private static int assignPID() {
        /*
         * Return a random 0-5 digit integer to be used as a process ID.
        */

        Random r = new Random();
        Integer pid = r.nextInt(maxPID - minPID) + minPID;

        return pid;
    }

    private static Process initProcess() {
        /*
         * Read in the process and assign class attributes.
         */

        Process process = new Process();
        process.pid = assignPID();
        process.burstTime = assignBurstTime();
        process.executionTime = process.burstTime;

        return process;
    }

    private static Object[] parseArgs(String[] args) {
        /*
         * Parse command line arguments. Return arguments in a list of objects.
         */

        Object[] arguments = new Object[2];
        try {
            arguments[0] = args[0];
            arguments[1] = args[1];
        } catch (Exception e) {
            System.out.println("Missing or invalid command line argument.");
            System.exit(0);
        }

        return arguments;
    }

    public static void main(String[] args) {

        // Parse agruments
        Object[] arguments = parseArgs(args);
        Integer numProcesses = Integer.parseInt(arguments[0].toString());
        Double timeQuantum = Double.parseDouble(arguments[1].toString());

        // Read in each process and place it in ready queue
        List<Process> readyQueue = new ArrayList<Process>();
        for (int i = 0; i < numProcesses; i++) {
            Process process = initProcess();
            readyQueue.add(process);
        }

        // Schedule the processes
        Scheduler scheduler = new Scheduler();
        scheduler.roundRobin(readyQueue, timeQuantum);
    }

}