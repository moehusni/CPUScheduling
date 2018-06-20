import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Assignment2 {

	public static void main(String[] args) throws IOException {
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
		int method;
		System.out.println("MENU FOR RUN-LENGTH ENCODE");
		System.out.println("1.Round Roubin");
		System.out.println("2.Shortest Remaining Time");
		System.out.println("3.First Come First Servce");
		System.out.println("4.Encode with zero and wrap-around");
		System.out.println("ENTER YOUR CHOICE:");
		method = Integer.parseInt(obj.readLine());
		String type = null;
		String quintum = null;
		if (args.length == 1 || args.length == 2) {
			type = args[0];
			if (args.length == 2) {
				quintum = args[1];
			}
			FileInputStream fstream = new FileInputStream("Assignment2.txt");
			Scanner input = new Scanner(new InputStreamReader(fstream));
			Scheduler scheduler = new Scheduler(input, type);

			switch (method) {
			case 1:
				scheduler.RR(quintum);
				break;
			case 2:
				scheduler.SRTF();
				break;
			default:
				scheduler.FCFS();
			}

		}
	}
}
