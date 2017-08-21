import java.io.*;
import java.util.Scanner;

public class FCFS {

	static int[] PID;
	static int[] AAT;
	static int[] CBT;

	public static void main(String[] args) {
		PID = new int[1000];// create array to hold PID for each process
		AAT = new int[1000];// create array to hold AAT for each process
		CBT = new int[1000];// create array to hold CBT for each process
		int i = 0;
		String filename = "w1.csv"; // select which workload to process
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
		for (int x = 0; x < PID.length; x++) { // print waiting and turnaround time for each process along with PID
			int a = waitingtime(x);
			int b = turnaroundtime(x);
			System.out.println("PID " + x + ": " + "Waiting time= "+a+" Turnaroundtime= "+ b);
		}
		int averagewt = 0;
		int averagett = 0;
		int noofarrays = PID.length;
		for (int y = 0; y < noofarrays; y++) { // calculate average waiting and turnaround times
			averagewt += waitingtime(y);
			averagett += turnaroundtime(y);
		}
		averagewt = averagewt / noofarrays; // total waiting time divided by the number of processes
		averagett = averagett / noofarrays;// total turnaround time divided by the number of processes
		System.out.println("Average Waiting Time= " + averagewt);
		System.out.println("Average TurnAround Time= " + averagett); // print average waiting and turnaround times
	}

	static int waitingtime(int process) { //calculate waiting time, function takes parameter the process index of which waiting time is demanded
		int time = 0;
		for (int x = 0; x < process; x++) {
			time += CBT[x]; // function adds burst time of each process up to the requested process's excecution
		}
		time -= AAT[process]; // subtract the process's arrival time
		return time;
	}

	static int turnaroundtime(int process) {//calculate waiting time, function takes parameter the process index of which turnaround time is demanded
		int time = 0;
		for (int x = 0; x <= process; x++) {
			time += CBT[x]; // function adds burst time of each process up to the requested process's excecution, including the current process
		}
		time -= AAT[process]; // subtract the process's arrival time
		return time;
	}

}
