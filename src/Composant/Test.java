package Composant;
import java.io.IOException;

import Generateurdetrame.GenTrame;
import Generateurdetrame.Trames;
public class Test {
public static void main (String[]args) {
		
		Trames trames = new Trames();
		try {
		GenTrame.FileToTrames(trames);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Trame tram2 : trames.getTrames()) {
			System.out.println(tram2.toString());
			System.out.println(tram2.affichetrame()+tram2.ethernet()+tram2.iPv4()+tram2.tcp());
		}
	
	}
}
