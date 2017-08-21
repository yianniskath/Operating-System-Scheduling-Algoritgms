import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class RoundRobin {

	static ArrayList<Integer> PID = new ArrayList<Integer>();
	static ArrayList<Integer> CBT = new ArrayList<Integer>();
	static ArrayList<Integer> AAT = new ArrayList<Integer>();
	static ArrayList<int[]> RR = new ArrayList<int[]>();
	static ArrayList<Integer> processqueue = new ArrayList<Integer>();

	public static void main(String[] args) {
		String filename = "w2.csv"; // select which workload to process
		File file = new File(filename);
		try {
			Scanner inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String data = inputStream.next();
				String[] values = data.split(",");
				PID.add(Integer.parseInt(values[0]));
				CBT.add(Integer.parseInt(values[1]));
				AAT.add(Integer.parseInt(values[2]));
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		createprocessqueue(40); // this function excecuted the Round Robin algorithm which takes the time quantum as a parameter

		// print list of processes with waiting and average times
		for (int x = 0; x < PID.size(); x++) {
			int a = waitingtime(x);
			int b = turnaroundtime(x);
			System.out.println("PID " + x + ": " + "Waiting time: " + a
					+ " Turnaroundtime: " + b);
		} // calculate average waiting and turnaround time
		int averagewt = 0;
		int averagett = 0;
		int noofarrays = PID.size();
		for (int y = 0; y < noofarrays; y++) {
			averagewt += waitingtime(y);
			averagett += turnaroundtime(y);
		}
		averagewt = averagewt / noofarrays; // total waiting time divided by the number of processes
		averagett = averagett / noofarrays;// total turnaround time divided by the number of processes
		System.out.println("Average Waiting Time: " + averagewt);
		System.out.println("Average TurnAround Time: " + averagett); // print average waiting and turnaround times

	}

	static void createprocessqueue(int quantum) {
		int currentburst = 0;
		// pass processes into RR arraylist
		for (int x = 0; x < PID.size(); x++) {
			int[] rr = new int[6];
			int w1 = PID.get(x);
			int w2 = CBT.get(x);
			int w3 = AAT.get(x);
			rr[0] = w1;//store PID
			rr[1] = w2;//store CBT
			rr[2] = w3;//store AAT
			rr[3] = 0; // these three variables (rr[3],rr[4],rr[5]) will be used later for waiting and turnaround time
			rr[4] = 0;
			rr[5] = 0;
			RR.addAll(Arrays.asList(rr));
		}
		processqueue.add(0); // add process to process queue

		for (int x = 0; x < processqueue.size(); x++) { // loop process queue
			int[] a;
			int b;
			b = processqueue.get(x);
			a = RR.get(b); // access corresponding process
			if (a[1] > quantum) { // if time remaining is greater than quantum time
				a[0] = b; // PID remains the same
				a[1] -= quantum; // CBT reduced by quantum
				a[2] = a[2]; // AAT does not change
				a[3] = currentburst;
				currentburst += quantum; // increase total burst time by quantum
				a[4] = currentburst; // set to current burst
				a[5] += a[3];
				a[5] -= a[4];// calculate waiting time and store in array
				RR.set(b, (a)); // update process's data on RR arraylist
				nextprocess(b, currentburst); // find next processes to be excecuted									
			} else if (a[1] <= quantum && a[1] != 0) { // if time remaining for process is less than or equal to quantum
				a[0] = b; // PID remains the same
				a[3] = currentburst;
				a[5] += a[3];
				currentburst += a[1]; // incrase total burst time with process's remaining time
				a[1] = 0; // set processe's CBT to 0
				a[2] = a[2]; // AAT does not change
				a[4] = currentburst; // set to current burst
				RR.set(b, (a)); // update processe's data on RR arraylist
				nextprocess(b, currentburst); // update processe's data on RR arraylist											
			}
		}
	}

	static void nextprocess(int n, int burst) { // find processes to be excecuted
		int[] a;
		a = RR.get(n); // get last process that was excecuted
		if (checkprocesses() == false) { // check if there are processes that have not been yet added to queue
			for (int x = 0; x < RR.size(); x++) {
				int[] e;
				e = RR.get(x);
				int pid = e[0]; // PID
				int aat = e[2]; // AAT
				if (processqueue.contains(pid) == false) { // find processes that have not yet been added to the queue
					if (aat <= burst) { // check if process has arrived
						processqueue.add(x); // add to queue
					}
				}
			}
		} else { // if all processes have been added to queue execute remaining ones
			for (int x = 0; x < RR.size(); x++) {
				int[] e;
				e = RR.get(x);
				int cbt = e[1]; // CBT
				int aat = e[2]; // AAT
				// System.out.println(c+" "+d+" "+b);
				if (aat <= burst && cbt != 0) { // check if process has not finished
					processqueue.add(x);// add to queue
				}
			}
		}
		if (a[1] != 0) {// if current process did not finish place in queue
			processqueue.add(n);
		}
	}

	// check if all processes have been added to queue, return true if all processes have been added
	static boolean checkprocesses() {
		int y = 0;
		for (int x = 0; x < RR.size(); x++) {
			int[] e;
			e = RR.get(x);
			int pid = e[0]; // PID
			if (processqueue.contains(pid) == true) {
				y++;
				if (y == RR.size()) {
					return true;
				}
			}
		}
		return false;
	}

	static int waitingtime(int n) { //retrieve waiting time
		int[] e;
		e = RR.get(n);
		return e[5];
	}

	static int turnaroundtime(int n) { //retrieve turnaround time
		int[] e;
		e = RR.get(n);
		return e[4];
	}
}