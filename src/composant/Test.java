package composant;

import java.io.IOException;

import generateurDeTrame.GenTrame;
import generateurDeTrame.Trames;
public class Test {

	public static void main (String[]args) {
		
		Trames trames = new Trames();
		String file = "data/TestFile.txt";
		//String file = "data/TestHTTP.txt";
		
		try {
			GenTrame.FileToTrames(trames, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Trame tram2 : trames.getTrames()) {
			
			try {
				System.out.println(tram2.toString());						
			} catch (IllegalArgumentException e) {
				System.out.println("Le format de la trame est incorrect");
			}
		}
	
	}
}
