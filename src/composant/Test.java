package composant;

import java.io.IOException;

import generateurDeTrame.GenTrame;
import generateurDeTrame.Trames;
public class Test {

	public static void main (String[]args) {
		
		Trames trames = new Trames();
		
		try {
			GenTrame.FileToTrames(trames);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Trame tram2 : trames.getTrames()) {
			
			try {
				System.out.println(tram2.toString());
				//System.out.println(tram2.affichetrame()+tram2.ethernet()+tram2.iPv4()+tram2.tcp());
				
				System.out.println(new Ethernet(tram2).toString());
				System.out.println(new TCP(tram2).toString());
				
			} catch (IllegalArgumentException e) {
				System.out.println("Le format de la trame est incorrect");
			}
		}
	
	}
}
