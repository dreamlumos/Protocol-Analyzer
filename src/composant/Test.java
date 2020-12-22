package composant;

import java.io.IOException;
import java.util.Scanner;

import generateurDeTrame.GenTrame;
import generateurDeTrame.Trames;
public class Test {

	public static void main (String[]args) {
		
		Trames trames = new Trames();
		String file = "data/input/TestFile.txt";
		//String file = "data/input/TestHTTP.txt";
		
		try {
			
			GenTrame.FileToTrames(trames, file);
			
			Scanner sc = new Scanner(System.in); 
			String fileName = "data/output/";
			fileName += sc.nextLine();
			sc.close();
			
			for (Trame trame : trames.getTrames()) {
				System.out.println(trame.toString());
				trame.stringToFile(fileName);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
