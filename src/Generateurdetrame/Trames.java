package Generateurdetrame;

import java.util.ArrayList;

import Composant.Trame;

public class Trames {
	private ArrayList<Trame> Trames;
	public Trames() {
		Trames= new ArrayList<Trame>();
	}
	public boolean addTrame(Trame e) {
		return Trames.add(e);
	}
	
	public ArrayList<Trame> getTrames(){
		return Trames;
	}
}
