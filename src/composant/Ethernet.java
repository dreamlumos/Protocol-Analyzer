package composant;

import java.util.HashMap;

public class Ethernet {
	
	private Trame trame;
	private HashMap<String, String> dictionnary = new HashMap<>();
	private StringBuilder macSource = new StringBuilder();
	private StringBuilder macDest = new StringBuilder();
	private StringBuilder protocole = new StringBuilder();
	
	public Ethernet(Trame trame) {
				
		if(!trame.trameValide()) {
			throw new IllegalArgumentException();
		}
		
		this.trame=trame;
		
		dictionnary.put("0800", "IPV4");
		dictionnary.put("0806", "ARP");
		dictionnary.put("0805", "X.25 niveau 3");
		protocole.append(trame.getOctets().get(12)).append(trame.getOctets().get(13));
		
		for(int i = 0; i<6;i++) {
			macSource.append(trame.getOctets().get(i+6)).append(":");
			macDest.append(trame.getOctets().get(i)).append(":");
		}
		
		macDest.deleteCharAt(macDest.lastIndexOf(":"));
		macSource.deleteCharAt(macSource.lastIndexOf(":"));

	}
	
	@Override
	public String toString() {
		StringBuilder ch = new StringBuilder();
		ch.append("\tDestination : ").append(macDest.toString()).append(" \n\tSource : ").append(macSource).append(" \n\tType : ").append(dictionnary.get(protocole.toString())).append(" (0x").append(protocole).append(")\n");
		return "Ethernet II, Src: "+macSource.toString()+" Dst: "+macDest+"\n"+ch.toString();
	}

	
	

}

