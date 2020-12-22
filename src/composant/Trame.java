package composant;

import java.util.ArrayList;

public class Trame {
	
	private static int nbTrames = 0;
	private int numeroTrame;
	private boolean trameValide;
	private String ligneIncompletes = "Erreur : Ligne incomplete en position ";
	private ArrayList<String> octets;
	private int[] offsets;
	private DataUnit frame;
	
	public Trame() {
		nbTrames++;
		numeroTrame = nbTrames;
		trameValide = true;
		octets = new ArrayList<>();
		offsets = new int[1526]; // En pratique, il est rare qu'un datagramme IP fasse plus de 1500 octets. +26 pour Ethernet
		for(int i = 0; i < 1526; i++) {
			offsets[i] = 0;
		}
		
	}
	
	public void createFrame() {
		if (trameValide)
			frame = new Ethernet(octets);
	}
	
	public boolean trameValide() {
		return trameValide;
	}
	
	public void trameInvalide(int position) {
		trameValide = false;
		ligneIncompletes += "" + position + " de la trame " + numeroTrame;
	}
	
	public boolean addOctets(String s) {
		return octets.add(s);
	}
	
	public boolean addOffset(int offset, int position) {
		if(position<1000&&offsets[position]==0) {
			offsets[position]=offset;
			return true;
		}
		return false;	
	}
	
	public ArrayList<String> getOctets(){
		return octets;
	}
	
	//jen ai besoin pr tester
	public void afficheOffsets() {
		for(int i = 0; i < 5; i++)
			System.out.println(offsets[i]);
	}
	
	/* Extension non signe sur taille bits */
	public static String extBit(String s, int taille) {
		String sb = s;
		
		for (int i = 0; i < taille-s.length(); i++) {
			sb = "0"+sb;
		}
		return sb;
	}
	
	public String toString() {
		
		System.out.println("\nTrame "+numeroTrame+" : ---------------------------");
		
		if(!trameValide()) return ligneIncompletes;

		int positionoctets = 0;
		int positionoffsets = 1;
		int i = 0;
		int j = offsets[positionoffsets];
		StringBuilder sb = new StringBuilder();
		boolean notlast = true;
		
		while(i < j) {
			sb.append(octets.get(positionoctets)+" ");
			positionoctets++;
			
			if(i+1 == j && notlast){
				sb.append("\n");
				positionoffsets++;
				if(positionoffsets < offsets.length)
					j = offsets[positionoffsets];
					if(j == 0) {
						j = octets.size();
						notlast = false;
					}
			}
			i++;
		}
		
		sb.append("\n\n");
		sb.append(frame.toString());
				
		return sb.toString();
	}
}
