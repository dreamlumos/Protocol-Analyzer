package protocolAnalyzer;

import java.util.HashMap;
import java.util.List;

public class Ethernet implements DataUnit {
	
	private List<String> frame;
	
	private HashMap<String, String> protocolDict = new HashMap<>();
	
	private StringBuilder macSource = new StringBuilder();
	private StringBuilder macDest = new StringBuilder();
	private String protocol;
	
	private DataUnit packet;
	
	public Ethernet(List<String> octets) {

		frame = octets;
		
		protocolDict.put("0800", "IPV4");
		protocolDict.put("0806", "ARP");
		protocolDict.put("0805", "X.25 niveau 3");
		
		protocol = frame.get(12) + frame.get(13);
		
		for(int i = 0; i<6;i++) {
			macSource.append(frame.get(i+6)).append(":");
			macDest.append(frame.get(i)).append(":");
		}
		
		macDest.deleteCharAt(macDest.lastIndexOf(":"));
		macSource.deleteCharAt(macSource.lastIndexOf(":"));
		
		if (protocol.equals("0800") && Integer.parseInt(""+frame.get(14).charAt(0),16) == 4) {
			packet = new IPV4(frame.subList(14, frame.size()));
		} else {
			System.out.println("Protocol "+protocol+" is not supported.");
		}

	}
	
	@Override
	public String toString() {
		
		StringBuilder ch = new StringBuilder();
		ch.append("\tDestination : ").append(macDest.toString()).append(" \n\tSource : ").append(macSource).append(" \n\tType : ").append(protocolDict.get(protocol.toString())).append(" (0x").append(protocol).append(")\n\n");
		
		if (packet != null) {
			ch.append(packet.toString());
		}
		
		return "Ethernet II, Src: "+macSource.toString()+", Dst: "+macDest+"\n"+ch.toString();
	}
	
	

}
