package round_robin_processing;

/*
Process Simulation Using Round-Robin Scheduling
COSC 519

Authors:
    Matthew Bourque
    Ravi Chandra

Purpose:
    This program simulates a the creation, loading, scheduling, dispatching, and execution of 
    processes using the round-robin scheduling algorithm.

Classes:
    The program contains the following classes:

        Driver - The main driver of the program. This class is executed in order to perform the simulation.
        Process - Contains process attributes.
        Scheduler - Performs the scheduling of processes using the round-robin scheduling algorithm.
        Dispatcher - Assigns a process to a CPU and executes the process.
        InterruptHandler - Contains an algorithm to handle user interrupts during process simulation.
        ProcessSummary - Prints a summary of completed processes showing process statistics.

Usage:
    The program is executed with two command line arguments:

        >>> java <numProcs> <timeQuantum>

    <numProcs> - The number of processes to create and simulate
    <timeQuantum> - The quantum time used during scheduling
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Driver {
    /*
     * The main driver class.  Executes the program.
    */

    // Define class attributes
    public static List<Integer> usedPIDs = new ArrayList<Integer>();
    public static Integer minPID = 0;
    public static Integer maxPID = 99999;
    public static Double minArrival = 0.0;
    public static Double maxArrival = 10.0;
    public static Double minBurst = 5.0;
    public static Double maxBurst = 10.0;


    private static Double assignArrivalTime() {
        /*
         * Return a random float to be used as a process arrival time
        */

        Random r = new Random();
        Double arrivalTime = minArrival + (maxArrival - minArrival) * r.nextDouble();

        return arrivalTime;
    }


    private static Double assignBurstTime() {
        /*
         * Return a random float to be used as a process burst time.
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
        boolean pidFlag = true;
        Integer pid = 0;

        // Only return a pid that hasn't already been used
        while (pidFlag) {
            pid = r.nextInt(maxPID - minPID) + minPID;

            if (!usedPIDs.contains(pid)) {
                usedPIDs.add(pid);
                pidFlag = false;
            }
        }

        return pid;
    }


    private static List<Process> getTestProcessList() {
        /*
         * Return a list of processes to test with.
        */

        Process p1 = new Process();
        p1.pid = 1;
        p1.arrivalTime = 3.0;
        p1.burstTime = 5.0;
        p1.executionTime = p1.burstTime;
        Process p2 = new Process();
        p2.pid = 2;
        p2.arrivalTime = 12.0;
        p2.burstTime = 5.0;
        p2.executionTime = p2.burstTime;
        Process p3 = new Process();
        p3.pid = 3;
        p3.arrivalTime = 15.0;
        p3.burstTime = 5.0;
        p3.executionTime = p3.burstTime;
        List<Process> testProcessList = new ArrayList<Process>();
        testProcessList.add(p1);
        testProcessList.add(p2);
        testProcessList.add(p3);

        return testProcessList;
    }


    private static Process initProcess() {
        /*
         * Create new process and assign its atributes.
         */

        Process process = new Process();
        process.pid = assignPID();
        process.arrivalTime = assignArrivalTime();
        process.burstTime = assignBurstTime();
        process.executionTime = process.burstTime;

        return process;
    }


    private static void printProcessesToExecute(List<Process> processList) {
        /*
         * Prints out the processes to be executed.
        */

        System.out.println("\n\nProcesses generated:");
        for (int i=0; i<processList.size(); i++) {
            Process p = processList.get(i);
            System.out.printf("\nPID = %d\n", p.pid);
            System.out.printf("Burst Time: %f\n", p.burstTime);
            System.out.printf("Arrival Time: %f\n", p.arrivalTime);
        }
        System.out.println();
    }


    private static void printUsage() {
        /*
         * Print out information on how to use the program.
        */

        System.out.println("\nUsage: java Driver <numProc> <timeQuantum>\n");
        System.out.println("\t<numProc> (int) - The number of processes");
        System.out.println("\t<timeQuantum> (float) - The time quantum\n");
    }


    public static void main(String[] args) {

        // Proceed with simulation of user provided appropriate command line arguments
        if (args.length == 2) {

            // Parse agruments
            Integer numProcesses = 0;
            Double timeQuantum = 0.0;
            try {
                numProcesses = Integer.parseInt(args[0].toString());
                timeQuantum = Double.parseDouble(args[1].toString());
            } catch (Exception e) {
                printUsage();
                System.exit(0);
            }

            // Initialize each process and place it in a list
            List<Process> processList = new ArrayList<Process>();
            for (int i = 0; i < numProcesses; i++) {
                Process process = initProcess();
                processList.add(process);
            }

            // Make test process list for testing purposes
            List<Process> testProcessList = getTestProcessList();

            // Print out process list
            printProcessesToExecute(processList);

            // Initialize the scheduler
            Scheduler scheduler = new Scheduler(timeQuantum);

            // Add interrupt handler in case of user interrupt
            Runtime.getRuntime().addShutdownHook(
                new InterruptHandler(scheduler.getCompletedProcesses(), scheduler.getTotalTime()));

            // Schedule the processes
            scheduler.roundRobin(processList);

            // Print summary of completed results
            System.out.printf("\n\n*** All processes completed ***\n");
            processSummary ps = new processSummary(scheduler.getCompletedProcesses(), scheduler.getTotalTime());
            ps.printSummary();

            // Exit the program
            Runtime.getRuntime().halt(0);

        // If the user failed to supply appropriate command line arguments, then print the program usage
        } else {
            printUsage();
        }
    }
}