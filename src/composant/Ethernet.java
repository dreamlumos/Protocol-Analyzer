package composant;

import java.util.HashMap;

public class Ethernet {
	private Trame trame;
	private HashMap<String, String> dictionnary = new HashMap<>();
	private StringBuilder macSource = new StringBuilder();
	private StringBuilder macDest = new StringBuilder();
	private StringBuilder protocole = new StringBuilder();
	public Ethernet(Trame trame) {
		this.trame=trame;
		if(trame.trameValide()) {
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
	}
	@Override
	public String toString() {
		return "Ethernet [trame=" + trame +", macSource=" + macSource + ", macDest="
				+ macDest + ", protocole=" + protocole + "]";
	}
	

}
