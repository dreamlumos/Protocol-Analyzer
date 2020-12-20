package generateurDeTrame;

import java.util.ArrayList;

import composant.Trame;

public class Trames {
	
	private ArrayList<Trame> trames;
	
	public Trames() {
		trames = new ArrayList<>();
	}
	
	public boolean addTrame(Trame t) {
		return trames.add(t);
	}
	
	public ArrayList<Trame> getTrames(){
		return trames;
	}
}
