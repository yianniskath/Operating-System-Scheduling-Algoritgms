import java.io.*;
import java.util.Scanner;

public class SJF {

	static int[] PID;
	static int[] AAT;
	static int[] CBT;
	static int[] SJF;
	static int[] SJFPID;
	static int[] SJFCBT;
	static int[] SJFAAT;

	public static void main(String[] args) {
		PID = new int[1000];//create array to hold PID
		AAT = new int[1000];//create array to hold AAT
		CBT = new int[1000];//create array to hold CBT
		SJF = new int[1000];// this array will be used for passing in temporarily burst time values, to determine which has the minimum burst time
		SJFPID = new int[1000];//create array to hold PID after SJF algorithm
		SJFCBT = new int[1000];//create array to hold CBT after SJF algorithm
		SJFAAT = new int[1000];//create array to hold AAT after SJF algorithm
		int i = 0;
		String filename = "w1.csv";// select which workload to process
		File file = new File(filename);
		//pass values from file to arrays
		try {
			Scanner inputStream = new Scanner(file);
			while (inputStream.hasNext()) {
				String data = inputStream.next();
				String[] values = data.split(",");
				PID[i] = Integer.parseInt(values[0]);
				CBT[i] = Integer.parseInt(values[1]);
				AAT[i] = Integer.parseInt(values[2]);
				i++;
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//call function to generate pre-emptive SJF
		createprocessqueue();
		// print list of processes with waiting and average times
		for (int x = 0; x < PID.length; x++) {
			int a = waitingtime(x);
			int b = turnaroundtime(x);
			System.out.println("PID " + x + ": " + "Waiting time: " + a
					+ " Turnaroundtime: " + b);
		}
		// calculate average waiting and turnaround time
		int averagewt = 0;
		int averagett = 0;
		int noofarrays = PID.length;
		for (int y = 0; y < noofarrays; y++) {
			averagewt += waitingtime(y);
			averagett += turnaroundtime(y);
		}
		averagewt = averagewt / noofarrays;// total waiting time divided by the number of processes
		averagett = averagett / noofarrays;// total turnaround time divided by the number of processes
		System.out.println("Average Waiting Time: " + averagewt);
		System.out.println("Average TurnAround Time: " + averagett); // print average waiting and turnaround times

	}
	static void createprocessqueue() {
		//pass first process to the new SJF arrays
		SJFPID[0] = PID[0];
		SJFAAT[0] = AAT[0];
		SJFCBT[0] = CBT[0];
		int i = 0;
		int x = 1;
		int currentCBT = CBT[0];//initialise burst time
		int min = 0; 
		int a = 0;
		delete(0);// remove process with index 0 from PID,CBT,AAT arrays 
		while (SJFPID[999] == 0) {// Find processes with arrival time less than CBT
			for (i = 0; i < PID.length; i++) {//loop PID array
				if (AAT[i] < currentCBT && AAT[i] != 0) {// if process has arrived and its value is not zero
					SJF[i] = CBT[i]; // store CBT of process in new SJF array at the same index
					min = SJF[i]; // store a min for next part of function
				}
			}
			// find process with smallest CBT in SJF array to be executed
			for (int y = 1; y < SJF.length; y++) { // loop array
				if (SJF[y] < min && SJF[y] != 0 || SJF[y] == min) {
					min = SJF[y]; // new min
					a = y; // store index of min burst time
				}
			}
			SJFPID[x] = PID[a]; // add process to the new  PID array using previously stored index
			SJFAAT[x] = AAT[a]; // add process to the new  AAT array using previously stored index
			SJFCBT[x] = CBT[a]; // add process to the new  CBT array using previously stored index
			currentCBT += CBT[a]; // increase CBT
			x++; // increase index of new arrays
			delete(a);// clear PID,CBT, AAT arrays
			deleteSJF(a);// clear SJF array
		}
	}

	static void delete(int i) { //function for clearing PID,AAT,CBT arrays at given index
		PID[i] = 0;
		AAT[i] = 0;
		CBT[i] = 0;
	}

	static void deleteSJF(int i) { //function for clearing SJF array at given index
		SJF[i] = 0;
	}

	static int waitingtime(int process) { //calculate waiting time, function takes parameter the process index of which waiting time is demanded
		int time = 0;
		for (int i = 0; i < process; i++) {
			time += SJFCBT[i];
		}
		time -= SJFAAT[process]; // subtract the process's arrival time
		return time;
	}

	static int turnaroundtime(int process) { //calculate waiting time, function takes parameter the process index of which turnaround time is demanded
		int time = 0;
		for (int i = 0; i <= process; i++) {
			time += SJFCBT[i];
		}
		time -= SJFAAT[process]; // subtract the process's arrival time
		return time;
	}
}
