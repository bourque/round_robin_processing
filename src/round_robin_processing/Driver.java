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

public class Driver {

    public static void main(String[] args) {

        // Parse agruments
        Object[] arguments = parseArgs(args);
        String processFile = arguments[0].toString();
        Double timeQuantum = Double.parseDouble(arguments[1].toString());

        // Read in process list
        List<String> processList = readProcesses(processFile);

        // Read in each process and place it in ready queue
        List<Process> readyQueue = new ArrayList<Process>();
        for (int i = 0; i < processList.size(); i++) {
            Process process = initProcess(processList, i);
            readyQueue.add(process);
        }

        // Schedule the processes
        Scheduler scheduler = new Scheduler();
        scheduler.roundRobin(readyQueue, timeQuantum);
    }

    private static Process initProcess(List<String> processList, int i) {
        /*
         * Read in the process and assign class attributes.
         */

        // Read in process parameters from file
        String processFile = processList.get(i);
        List<String> attributeList = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(processFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                attributeList.add(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.printf("Error reading in file %s", processFile);
        }

        // Parse attribute list
        String pid = attributeList.get(0);
        double burstTime = Double.parseDouble(attributeList.get(1));

        // Assign attributes to process
        Process process = new Process();
        process.pid = pid;
        process.burstTime = burstTime;
        process.executionTime = burstTime;

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

    private static List<String> readProcesses(String processFile) {
        /*
         * Return a list of processes to execute.
         */

        List<String> processList = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(processFile));
            String line = null;
            while ((line = reader.readLine()) != null) {
                processList.add(line);
            }
            reader.close();
        } catch (Exception e) {
            System.out.printf("Error reading in file", processFile);
        }

        return processList;
    }
}