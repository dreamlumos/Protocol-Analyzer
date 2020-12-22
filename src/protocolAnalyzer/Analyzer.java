package protocolAnalyzer;

import java.io.IOException;
import java.util.Scanner;

import tools.TraceFile;
import tools.Traces;

public class Analyzer {

	public static void main (String[] args) {
		
		Traces traces = new Traces();
		
		/* Pour tester, décommentez une des deux lignes suivantes */
		//String input = "data/input/TestFile.txt";
		String input = "data/input/TestHTTP.txt";
		
		try {
			
			Scanner sc = new Scanner(System.in); 
			TraceFile.FileToTraces(traces, input);
			
			String fileName = "data/output/";
			System.out.println("Veuillez entrer le nom du fichier de sortie : \n");
			fileName += sc.nextLine();
			sc.close();
		
			for (Trace trace : traces.getTraces()) {
				System.out.println(trace.toString());
				trace.stringToFile(fileName);
			}
			
			System.out.println("Le résultat de l'analyse se situe dans "+fileName);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
