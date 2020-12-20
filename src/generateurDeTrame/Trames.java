package generateurDeTrame;

import java.util.ArrayList;

import composant.Trame;

public class Trames {
	private ArrayList<Trame> Trames;
	public Trames() {
		Trames= new ArrayList<>();
	}
	public boolean addTrame(Trame e) {
		return Trames.add(e);
	}
	
	public ArrayList<Trame> getTrames(){
		return Trames;
	}
}
