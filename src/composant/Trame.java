package composant;

import java.util.ArrayList;

public class Trame {
	
	private static int nombredetrames = 0;
	private int numerotrame;
	private boolean tramevalide;
	private String ligneincompletes = "Erreur : Ligne incomplete en position ";
	private ArrayList<String> octets;
	private int[] offsets;
	
	public Trame() {
		nombredetrames++;
		numerotrame = nombredetrames;
		tramevalide = true;
		octets = new ArrayList<>();
		offsets = new int[1526];
		for(int i = 0; i < 1526; i++) {
			offsets[i] = 0;
		}
	}
	
	public boolean trameValide() {
		return tramevalide;
	}
	
	public void trameInvalide(int position) {
		tramevalide = false;
		ligneincompletes += "" + position + " de la trame " + numerotrame;
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
	
	public String toString() {
		
		System.out.println("\nTrame "+numerotrame+" : ---------------------------");
		
		if(!trameValide()) return ligneincompletes;

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
		
		sb.append("\n");
		
		return sb.toString();
	}
	
	public ArrayList<String> getoctets(){
		return octets;
	}
	//jen ai besoin pr tester
	public void afficheOffsets() {
		for(int i =0;i<5;i++)
			System.out.println(offsets[i]);
	}
	
	public String extBit(String s, int taille) { //extension non signe sur taille bits
		String sb =s;
		
		for (int i=0;i<taille-s.length() ; i++) {
			sb="0"+sb;
		}
		return sb;
	}
	
}
