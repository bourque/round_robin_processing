package round_robin_processing;

public class Process {

	String pid;
	double burstTime;
	double executionTime;
	double waitTime;
	double turnaroundTime;

	public Process() {
		/*
		 * Constructor method.
		 */

		pid = "1";
		burstTime = 0.0;
		executionTime = 0.0;
		waitTime = 0.0;
		turnaroundTime = 0.0;
	}
}