package protocolAnalyzer;

import java.io.IOException;
import java.util.Scanner;

import tools.TraceFile;
import tools.Traces;

public class Analyzer {

	public static void main (String[] args) {
		
		Traces traces = new Traces();
		String file = "data/input/TestFile.txt";
		//String file = "data/input/TestHTTP.txt";
		
		try {
			
			TraceFile.FileToTraces(traces, file);
			
/*			
			Scanner sc = new Scanner(System.in); 
			String fileName = "data/output/";
			fileName += sc.nextLine();
			sc.close();
*/			
			for (Trace trace : traces.getTraces()) {
				System.out.println(trace.toString());
				//trame.stringToFile(fileName);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
