package composant;

import java.util.List;

import generateurDeTrame.HexTools;

public class IPV4 implements DataUnit {
	
	private List<String> packet;
	
	private String version;
	private int headerLength;
	private String typeOfService;
	private int totalLength;
	private StringBuilder ipSource = new StringBuilder();
	private StringBuilder ipDest = new StringBuilder();
	private String identification;
	private String flags;
	private int flags2;
	private int protocol;
	
	private DataUnit segment;
	
	/* Le constructeur prend en argument une liste d'octets correspondant à un paquet IP. */
	public IPV4(List<String> packet) {
		
		 this.packet = packet;
		 
		 version = packet.get(0);
		 typeOfService = packet.get(1);
		 headerLength = Integer.parseInt("0"+version.charAt(0),16)*Integer.parseInt("0"+version.charAt(1),16);
		 totalLength = Integer.parseInt(packet.get(2)+packet.get(3),16);
		 identification = packet.get(4) + packet.get(5);
		 flags = packet.get(6);
		 flags2 = Integer.parseInt(""+flags.charAt(0));
		 protocol = Integer.parseInt(packet.get(9));
		 
		 ipSource.append(Integer.parseInt(packet.get(12),16)).append(".").append(Integer.parseInt(packet.get(13),16));
		 ipSource.append(".").append(Integer.parseInt(packet.get(14),16)).append(".").append(Integer.parseInt(packet.get(15),16));
		 ipDest.append(Integer.parseInt(packet.get(16),16)).append(".").append(Integer.parseInt(packet.get(17),16));
		 ipDest.append(".").append(Integer.parseInt(packet.get(18),16)).append(".").append(Integer.parseInt(packet.get(19),16));
		 
		 createSegment();
	}
	
	private void createSegment() {
		
		if (headerLength == packet.size()) {
			System.out.println("IP Packet contains no data.");
			return;
		}
		
		if (protocol == 6) {
			segment = new TCP(packet.subList(headerLength, packet.size()));
		} else {
			System.out.println("Protocol "+protocol+" is not supported.");
		}
	}
	
	@Override
	public String toString() {
			
		StringBuilder sb = new StringBuilder();

		sb.append("\t0100 .... = Version: ").append(version.charAt(0)+"\n");//version
		sb.append("\t.... ").append(HexTools.extBit(Integer.toBinaryString(Integer.parseInt("0"+version.charAt(1),16)),4)).append(" = Header Length: ");
		sb.append(headerLength+" bytes ("+Integer.parseInt("0"+version.charAt(1),16)+")\n"); // nombres de lignes 
		sb.append("\tDifferentiated Services Field(TOS): 0x"+typeOfService+"\n");
		sb.append("\tTotal Length: " + totalLength);
		sb.append("\n\tIdentification: 0x"+identification+" ("+Integer.parseInt(identification,16)+")\n");
		sb.append("\tFlags: 0x"+flags+"\n");
		
		if((HexTools.extBit(Integer.toBinaryString(flags2),4).charAt(0)+"").equals("1")) {
			sb.append("\t\t1... .... = Reserved bit: Set");
			
		} else {
			sb.append("\t\t0... .... = Reserved bit: Not set");
		}
		
		if((HexTools.extBit(Integer.toBinaryString(flags2),4).charAt(1)+"").equals("1")) {// a resoudre
			sb.append("\n\t\t.1.. .... = Don't fragment: Set");
		}
		else {
			sb.append("\n\t\t.0.. .... = Don't fragment: Not set");
		}
		
		if((HexTools.extBit(Integer.toBinaryString(flags2),4).charAt(2)+"").equals("1")) {
			sb.append("\n\t\t..1. .... = More fragments: Set");
		} else {
			sb.append("\n\t\t..0. .... = More fragments: Not set");
		}
		
		sb.append("\n\tFragment Offset: "+Integer.parseInt(packet.get(7),16) );
		sb.append("\n\tTime to Live: "+Integer.parseInt(packet.get(8),16) );
		sb.append("\n\tProtocol: ");
		
		if(Integer.parseInt(packet.get(9),16)==6)
			sb.append("TCP ("+Integer.parseInt(packet.get(9),16)+")" );//protocol
		else sb.append("Protocol Not-known");
		
		sb.append("\n\tHeader Checksum: 0x"+packet.get(10)+packet.get(11));
		sb.append("\n\tSource Address: "+ipSource);
		sb.append("\n\tDestination Address: "+ipDest);
		
		StringBuilder option = new StringBuilder();
		if (headerLength > 20) {
			int position = 20;
			option.append("\n\tOptions :");
			int op;
			int tailleOp = 0 ;
			
			while(position < headerLength) {
				op = Integer.parseInt(packet.get(position),16);
				
				if(position+1<headerLength) {
					tailleOp = Integer.parseInt(packet.get(position+1),16);
				}
				
				switch (op) {
					case 0 :
						if (position == headerLength-1) {
							option.append("\n\t\tEnd of Options List \n\t\t\tLength : 1 Bytes");
						}
						else {
							option.append("\n\t\tPadding \n\t\t\tLength : "+(headerLength-position)+" Bytes");
						}
						position = headerLength;
					case 1 :  
						option.append("\n\t\tNo Operation  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 7 :
						option.append("\n\t\tRecord Route (RR)  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 68 :
						option.append("\n\t\tTime Stamps  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 131 :
						option.append("\n\t\tLoose Routing  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 137 :
						option.append("\n\t\tStrict Routing  \n\t\t\tLength : "+tailleOp+" Bytes");
				}
				
				if (position != headerLength) {
					position += tailleOp;	
				}
			}
			
		}
		
		if (segment != null) {
			sb.append("\n"+segment.toString());
		}
		
		return "Internet Protocol Version 4, Src: "+ipSource+", Dst: "+ipDest+"\n"+sb.toString();
	}
}
