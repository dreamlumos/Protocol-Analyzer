package composant;

import java.util.ArrayList;
import java.util.HashMap;
public class Trame {
	private static int nombredetrames=0;
	private int numerotrame;
	private boolean tramevalide ;
	private String ligneincompletes="erreur : ligne incomplète en position : ";
	private ArrayList<String> octets;
	private int[]offsets;
	public Trame() {
		nombredetrames++;
		numerotrame=nombredetrames;
		tramevalide=true;
		octets = new ArrayList<>();
		offsets= new int[1526];
		for(int i = 0;i <1526;i++) {
			offsets[i] = 0;
		}
	}
	public boolean trameValide() {
		return tramevalide;
	}
	public void trameInvalide(int position) {
		tramevalide=false;
		ligneincompletes+=""+position+" de la trame "+ numerotrame;
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
		System.out.println("\ntrame "+numerotrame+" : ---------------------------");
		if(!trameValide())return ligneincompletes;
		else {
			int positionoctets = 0;
			int positionoffsets = 1;
			int i = 0;
			int j = offsets[positionoffsets];
			StringBuilder br = new StringBuilder();
			boolean notlast = true;
			while(i < j ) {
				br.append(octets.get(positionoctets)+" ");
				positionoctets++;
				
				if(i+1==j&& notlast){
					br.append("\n");
					positionoffsets++;
					if(positionoffsets<offsets.length)
						j=offsets[positionoffsets];
						if(j==0) {
							j=octets.size();
							notlast=false;
						}
				}
				i++;
			}
			
			return br.toString();
		}
		
	}
	public ArrayList<String> getoctets(){
		return octets;
	}
	//jen ai besoin pr tester
	public void afficheOffsets() {
		for(int i =0;i<5;i++)
			System.out.println(offsets[i]);
	}
	public String affichetrame() {
		String ligne ="-------------------------------\n";
		if(tramevalide)
		return ligne+"frame "+ numerotrame+": "+(14+Integer.parseInt(octets.get(16)+octets.get(17),16))+" bytes captured\n";
		return ligne+"frame "+ numerotrame+" non-valide : \n\t"+ligneincompletes+"\n";
	}
	public String ethernet() {
		if (tramevalide) {
		HashMap<String, String> dictionnary = new HashMap<>();
		dictionnary.put("0800", "IPV4");
		dictionnary.put("0806", "ARP");
		dictionnary.put("0805", "X.25 niveau 3");

		StringBuilder macSource = new StringBuilder();
		StringBuilder macDest = new StringBuilder();
		StringBuilder protocole = new StringBuilder();
		protocole.append(octets.get(12)).append(octets.get(13));
		for(int i = 0; i<6;i++) {
			macSource.append(octets.get(i+6)).append(":");
			macDest.append(octets.get(i)).append(":");
		}
		
		macDest.deleteCharAt(macDest.lastIndexOf(":"));
		macSource.deleteCharAt(macSource.lastIndexOf(":"));
		StringBuilder ch = new StringBuilder();
		ch.append("\tDestination : ").append(macDest.toString()).append(" \n\tSource : ").append(macSource).append(" \n\tType : ").append(dictionnary.get(protocole.toString())).append(" (0x").append(protocole).append(")\n");
		return "Ethernet II, Src: "+macSource.toString()+" Dst: "+macDest+"\n"+ch.toString();
		}
		return "";
	}
	
	public String extBit(String s, int taille) { //extension non signé sur taille bits
		String sb =s;
		
		for (int i=0;i<taille-s.length() ; i++) {
			sb="0"+sb;
		}
		return sb;
	}
	
}
