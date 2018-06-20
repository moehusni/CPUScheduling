import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class Scheduler {
	private List<Process> readyQueue = new CircularLinkedList<Process>();
	private Queue<Process> queue = new LinkedList<Process>();
	private Queue<Process> finishedQueue = new LinkedList<Process>();
	private Scanner input = null;
	private int size;
	private int CS;
	private int counter = 0;
	private boolean flag;
	private Map<Integer, LinkedList<Process>> multiMap = new HashMap<Integer, LinkedList<Process>>();

	Scheduler(Scanner input, String type) {

		this.input = input;

	}

	public void createData() {
		String line = null;
		while (input.hasNextLine()) {
			line = input.nextLine();
			String[] data = line.trim().split("\\s");// trims the leading space
			int id = Integer.parseInt(data[0]);
			int arrivalTime = Integer.parseInt(data[1]);
			int cpuBurst = Integer.parseInt(data[2]);

			Process process = new Process(id, arrivalTime, cpuBurst);
			queue.add(process);
		}

		// grouping process depending on the arrival time.
		size = queue.size();
		while (!queue.isEmpty()) {// gets the lead arrival time
			Process temp = queue.remove();
			LinkedList<Process> tempList = new LinkedList<Process>();
			tempList.add(temp);
			int tempArrive = temp.getArrivalTime();

			// gets all process with similar time
			while (!queue.isEmpty() && tempArrive == queue.peek().getArrivalTime()) {
				Process remove = queue.remove();

				// linkedList making for table.
				tempList.add(remove);
			}

			// sorting linkedList.

			if (flag) {
				sort(tempList);

			}
			multiMap.put(tempArrive, tempList);
		}

	}

	private void sort(LinkedList<Process> tempList) {
		Collections.sort(tempList, new Comparator<Process>() {
			@Override
			public int compare(Process p1, Process p2) {

				if (p1.getCpuBurst() > p2.getCpuBurst())
					return 1;
				else {
					return -1;
				}
			}
		});

	}

	public void SRTF() {
		flag = true;
		createData();
		printAlgorithmType("srtf");
		checkNewArrival();
		while (!readyQueue.isEmpty()) {
			Process process = readyQueue.remove(0);
			process.setResponse(counter);
			while (process.getCpuBurst() > 0) {
				printMsgRunning(process);
				process.setCpuBurst();
				counter++;
				if (checkNewArrival()) {
					readyQueue.add(process);
					findShortest();
					process = readyQueue.remove(0);
					CS += .015;

				}

				updateWaitTime();
			}
			printMsgFinished(process);
			findShortest();
			process.SetTurnAround(counter);
			finishedQueue.add(process);
			CS += .015;
		}
		printMsgDone(counter);
		printAvgWaitTime();
	}

	public void FCFS() {
		createData();
		printAlgorithmType("fcfs");
		checkNewArrival();

		while (!readyQueue.isEmpty()) {
			Process process = readyQueue.remove(0);
			process.setResponse(counter);
			for (int i = 0; i < process.getCpuBurst(); i++) {
				printMsgRunning(process);
				checkNewArrival();
				counter++;
				updateWaitTime();
			}
			printMsgFinished(process);
			process.SetTurnAround(counter);
			finishedQueue.add(process);
			CS += 0.015; // context switch
		}
		printMsgDone(counter);
		printAvgWaitTime();
	}

	public void RR(String q) throws IOException {
		createData();
		printAlgorithmType("rr");
		int quintum ;
		if(q == null) {
			 System.out.println("how many quintum: ");
			InputStream input =  System.in;
			quintum = input.read();
			
		}else {
		quintum = Integer.parseInt(q);
		}
		int timer = 0;
		checkNewArrival();
		while (!readyQueue.isEmpty()) {
			Process process = readyQueue.remove(0);
			process.setResponse(counter);
			while (process.getCpuBurst() > 0) {
				process.setCpuBurst();
				printMsgRunning(process);
				counter++;
				timer++;
				checkNewArrival();
				if (timer == quintum && process.getCpuBurst()!= 0) {
					process = swap(process);
					CS += 0.015;
					timer = 0;
				}
				
				
				updateWaitTime();
			}
			
			printMsgFinished(process);
			counter++;
			process.SetTurnAround(counter);
			
			timer = 0;
			finishedQueue.add(process);
			CS += 0.015;
		}
		counter--;
		printMsgDone(counter);
		printAvgWaitTime();
	}

	private void printAvgWaitTime() {
		float averageWaitTime = 0;
		float turnAround = 0;
		float response = 0;
		float CPU = 0;
		 System.out.println("============================================================");
		while (!finishedQueue.isEmpty()) {
			Process wait = finishedQueue.remove();
			CPU = CS + (float) counter;
			CPU = counter / CPU;
			CPU *= 100;
			averageWaitTime += wait.getWaitTime();
			response += wait.getResponse();
			turnAround += wait.getTurnAround();
		}
		 System.out.print("Average CPU usage:      " + "   " + CPU + "%\n" + "Average waiting time:   " + "   "
				+ averageWaitTime / (float) size + "\n" + "Average response time:  " + "   " + response / (float) size
				+ "\n" + "Average turnaround time:" + "   " + turnAround / (float) size + "\n");
		 System.out.println("============================================================\n");

	}

	private void printAlgorithmType(String string) {
		switch (string) {
		case "srtf":
			 System.out.println("Scheduling algorithm: Shorest Remaining Time First \r\n"
					+ "============================================================\n");
			break;
		case "rr":
			 System.out.println("Scheduling algorithm: Round Robin \r\n"
					+ "============================================================\n");
			break;
		default:
		case "fcfs":
			 System.out.println("Scheduling algorithm: First Come First Serve\r\n"
					+ "============================================================\n");

		}
	}

	private void printMsgDone(int counter) {
		String digit = " ";
		if (counter > 9)
			digit = "  ";
		 System.out.println("<System time" + digit+ counter + "> All processes finished......");
	}

	private void printMsgFinished(Process process) {
		String digit = "   ";
		if (counter > 9)
			digit = "  ";
		 System.out.println(

				"<System time" + digit + counter + "> " + "process" + " " + process.getId() + " finished....");
	}

	private void printMsgRunning(Process process) {
		String digit = "   ";
		if (counter > 9)
			digit = "  ";
		 System.out.println(
				"<System time" + digit + counter + "> " + "process" + " " + process.getId() + " is running");
	}

	private void updateWaitTime() {
		if (!readyQueue.isEmpty()) {
			for (int i = 0; i < readyQueue.size(); i++) {
				Process temp = readyQueue.remove(0);
				temp.setWaitTime(temp.getWaitTime() + 1);
				readyQueue.add(temp);
			}
		}
	}

	private Process swap(Process process) {
		readyQueue.add(process);
		process = readyQueue.remove(0);
		CS += 1;
		return process;
	}

	private Boolean checkNewArrival() {
		if (multiMap.containsKey(counter)) {
			LinkedList<Process> first = multiMap.remove(counter);// removes arrival time zero.
			while (!first.isEmpty()) {
				readyQueue.add(first.remove());
			}

			return true;
		}
		return false;
	}

	public void findShortest() {
		LinkedList<Process> tempList = new LinkedList<Process>();
		while (!readyQueue.isEmpty()) {
			tempList.add(readyQueue.remove(0));

		}

		Collections.sort(tempList, new Comparator<Process>() {
			@Override
			public int compare(Process p1, Process p2) {

				if (p1.getCpuBurst() > p2.getCpuBurst())
					return 1;
				else {
					return -1;
				}
			}
		});
		while (!tempList.isEmpty()) {
			readyQueue.add(tempList.remove(0));
		}
	}

}
