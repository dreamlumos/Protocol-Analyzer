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
		return "Ethernet II, Src: "+macSource.toString()+" Dst: "+macDest+"\n"+ch.toString();}
		return "";
	}
	
	public String extBit(String s, int taille) { //extension non signé sur taille bits
		String sb =s;
		
		for (int i=0;i<taille-s.length() ; i++) {
			sb="0"+sb;
		}
		return sb;
	}
	
	public String iPv4() {
		if (tramevalide) {
		StringBuilder sb = new StringBuilder();
		String version = octets.get(14);
		StringBuilder ipSource = new StringBuilder();
		StringBuilder ipDest = new StringBuilder();
		ipSource.append(Integer.parseInt(octets.get(25),16)).append(".").append(Integer.parseInt(octets.get(26),16));
		ipSource.append(".").append(Integer.parseInt(octets.get(27),16)).append(".").append(Integer.parseInt(octets.get(28),16));
		ipDest.append(Integer.parseInt(octets.get(29),16)).append(".").append(Integer.parseInt(octets.get(30),16));
		ipDest.append(".").append(Integer.parseInt(octets.get(31),16)).append(".").append(Integer.parseInt(octets.get(32),16));
		
		if((octets.get(12)+octets.get(13)).equals("0800")) {
			sb.append("\t0100 .... = Version: ").append(version.charAt(0)+"\n");//version
			sb.append("\t.... ").append(extBit(Integer.toBinaryString(Integer.parseInt("0"+version.charAt(1),16)),4)).append(" = Header Length: ");//header legth
			int tailleentete = Integer.parseInt("0"+version.charAt(0),16)*Integer.parseInt("0"+version.charAt(1),16);//taille de l'entete
			sb.append(tailleentete+" bytes ("+Integer.parseInt("0"+version.charAt(1),16)+")\n");// nombres de lignes 
			sb.append("\tDifferentiated Services Field(TOS): 0x"+octets.get(15)+"\n");// TOS
			sb.append("\tTotal Length: "+Integer.parseInt(octets.get(16)+octets.get(17),16));//taille sans entete ethernet
			String identification = octets.get(18)+octets.get(19);
			sb.append("\n\tIdentification: 0x"+identification+" ("+Integer.parseInt(identification,16)+")\n");
			String flags = octets.get(20);
			sb.append("\tFlags: 0x"+Integer.parseInt(flags,16));
			int flags2 = Integer.parseInt(""+flags.charAt(0));
			if((extBit(Integer.toBinaryString(flags2),4).charAt(0)+"").equals("1")) {
				sb.append("\n\t\t1... .... = Reserved bit: Set");
				
			}
			else sb.append("\t\t0... .... = Reserved bit: Not set");
			if((extBit(Integer.toBinaryString(flags2),4).charAt(1)+"").equals("1")) {// a resoudre
				sb.append("\n\t\t.1.. .... = Don't fragment: Set");
			}
			else {
				sb.append("\n\t\t.0.. .... = Don't fragment: Not set");
			}
			if((extBit(Integer.toBinaryString(flags2),4).charAt(2)+"").equals("1"))
				sb.append("\n\t\t..1. .... = More fragment: Set");
			else sb.append("\n\t\t..0. .... = More fragment: Not set");
			sb.append("\n\tFragment Offset: "+Integer.parseInt(octets.get(21),16) );//fragment offset
			sb.append("\n\tTime to Live: "+Integer.parseInt(octets.get(22),16) );
			sb.append("\n\tProtocol: ");
			if(Integer.parseInt(octets.get(23),16)==6)
				sb.append("TCP ("+Integer.parseInt(octets.get(23),16)+")" );//protocol
			else sb.append("Protocol Not-known");
			sb.append("\n\tHeader Checksum: 0x"+octets.get(24)+octets.get(25));//checksum
			sb.append("\n\tSource Address: "+ipSource);
			sb.append("\n\tDesttination Address: "+ipDest);
			StringBuilder option= new StringBuilder();
			if (tailleentete > 20) {
				int position=34;
				sb.append("\n\tOptions :");
				int op;
				int tailleOp = 0 ;
				while(position < tailleentete+14) {
					op= Integer.parseInt(octets.get(position),16);
					if(position+1<tailleentete+14)
						tailleOp = Integer.parseInt(octets.get(position+1),16);
					switch (op) {
					case 0 :
						if (position == tailleentete+14-1) {
							sb.append("\n\t\tEnd of Options List \n\t\t\tLength : 1 Bytes");
						}
						else {
							sb.append("\n\t\tPadding \n\t\t\tLength : "+(tailleentete+14-position)+" Bytes");
						}
						
						position = tailleentete+14;
						
					case 1 :  
						option.append("\n\t\tNo Operation  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 7 :
						sb.append("\n\t\tRecord Route (RR)  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 68 :
					option.append("\n\t\tTime Stamps  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 131 :
					option.append("\n\t\tLoose Routing  \n\t\t\tLength : "+tailleOp+" Bytes");
					case 137 :
					option.append("\n\t\tStrict Routing  \n\t\t\tLength : "+tailleOp+" Bytes");
					}
					if (position != tailleentete+14)
						position+=tailleOp;				
				}
				
			}
			return "Internet Protocol Version 4, Src: "+ipSource+" Dst: "+ipDest+"\n"+sb.toString();
			
			
		}
		return "not ipv4";}
		return "";
	}
	
	public String tcp() {
		if (tramevalide && (octets.get(12)+octets.get(13)).equals("0800") ) {//trame complete et ipv4
		HashMap<String, String> dictionnary = new HashMap<>();
		dictionnary.put("002", "(SYN)");
		dictionnary.put("010", "(ACK)");
		dictionnary.put("012", "(SYN, ACK)");
		String versionip = octets.get(14);
		int tailleip = Integer.parseInt("0"+versionip.charAt(0),16)*Integer.parseInt("0"+versionip.charAt(1),16);
		int tailleTrame = 14+Integer.parseInt(octets.get(16)+octets.get(17),16);
		if(Integer.parseInt(octets.get(23),16)==6 && tailleip+14<tailleTrame ) {
			int position = tailleip+14;//position tcp
			StringBuilder sb = new StringBuilder();
			int sourcePort = Integer.parseInt(octets.get(position)+octets.get(position+1),16);
			sb.append("\n\tSource Port: "+sourcePort);
			int destPort = Integer.parseInt(octets.get(position+2)+octets.get(position+3),16);
			sb.append("\n\tDestination Port: "+destPort);
			String dataOffset = octets.get(position+12);
			int len = Integer.parseInt(""+dataOffset.charAt(1),16);
			sb.append("\n\t[TCP Segment Len: "+len +"]");
			int sequenceNumber = Integer.parseUnsignedInt(octets.get(position+4)+octets.get(position+5)+octets.get(position+6)+octets.get(position+7),16);
			sb.append("\n\tSequence Number: "+sequenceNumber);
			int acknowledgmentNumber = Integer.parseInt(octets.get(position+8)+octets.get(position+9)+octets.get(position+10)+octets.get(position+11),16);
			sb.append("\n\tAcknowledgmentNumber: "+acknowledgmentNumber);
			
			int headerLength = Integer.parseInt(""+dataOffset.charAt(0),16);
			sb.append("\n\t"+extBit(Integer.toBinaryString(headerLength),4)+" .... = Header Length:"+headerLength*4+" Bytes ("+headerLength+")");
			String flagsValues = ""+dataOffset.charAt(1)+octets.get(position+13);
			String flags = "Flags: 0x"+flagsValues+" "+dictionnary.get(flagsValues);
			sb.append("\n\t"+flags);
			String binaryFlags= extBit(Integer.toBinaryString(Integer.parseInt(flagsValues,16)),12);
			sb.append(flagsTcp(binaryFlags));
			int window = Integer.parseInt(octets.get(position+14)+octets.get(position+15),16);
			sb.append("\n\tWindow: "+window);
			String checksum = octets.get(position+16)+octets.get(position+17);
			sb.append("\n\tChecksum: 0x"+checksum);
			int Urgent = Integer.parseInt(octets.get(position+18)+octets.get(position+19),16);
			sb.append("\n\tUrgent Pointer: "+Urgent);

			return"\nTransmission Control Protocol, Src Port: "+sourcePort+", Dst Port: "+destPort+", Seq: "+sequenceNumber+", Len: "+len+sb.toString();
		}
		}
		return "";
	}
	public String flagsTcp(String s) {
		HashMap<Integer, String> dictionnary = new HashMap<>();
		dictionnary.put(0, "Not set");
		dictionnary.put(1, "Set");
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t\t"+s.charAt(0)+s.charAt(1)+s.charAt(2)+". .... = Reserved "+dictionnary.get(Integer.parseInt(""+s.charAt(0)+s.charAt(1)+s.charAt(2),2)));
		sb.append("\n\t\t..."+s.charAt(3)+" .... .... = Nonce: "+dictionnary.get(Integer.parseInt(""+s.charAt(3),2)));
		sb.append("\n\t\t.... "+s.charAt(4)+"... .... = Congestion Window Reduced (CMR): "+dictionnary.get(Integer.parseInt(""+s.charAt(4),2)));
		sb.append("\n\t\t.... ."+s.charAt(5)+".. .... = ECN-Echo: "+dictionnary.get(Integer.parseInt(""+s.charAt(5),2)));
		sb.append("\n\t\t.... .."+s.charAt(6)+". .... = Urgent: "+dictionnary.get(Integer.parseInt(""+s.charAt(6),2)));
		sb.append("\n\t\t.... ..."+s.charAt(7)+" .... = Acknowledgment: "+dictionnary.get(Integer.parseInt(""+s.charAt(7),2)));
		sb.append("\n\t\t.... .... "+s.charAt(8)+"... = Push: "+dictionnary.get(Integer.parseInt(""+s.charAt(8),2)));
		sb.append("\n\t\t.... .... ."+s.charAt(9)+".. = Reset: "+dictionnary.get(Integer.parseInt(""+s.charAt(9),2)));
		sb.append("\n\t\t.... .... .."+s.charAt(10)+". = Syn: "+dictionnary.get(Integer.parseInt(""+s.charAt(10),2)));
		sb.append("\n\t\t.... .... ..."+s.charAt(11)+" = Fin: "+dictionnary.get(Integer.parseInt(""+s.charAt(11),2)));
		sb.append("\n\t\t[TCP Flags:");
		if((""+s.charAt(0)+s.charAt(1)+s.charAt(2))=="001") 
			sb.append("R");
		else sb.append(".");	
		if((s.charAt(3))=='1') 
			sb.append("N");
		else sb.append(".");
		if((s.charAt(4))=='1') 
			sb.append("C");
		else sb.append(".");
		if((s.charAt(5))=='1') 
			sb.append("E");
		else sb.append(".");
		if((s.charAt(6))=='1') 
			sb.append("U");
		else sb.append(".");
		if((s.charAt(7))=='1') 
			sb.append("A");
		else sb.append(".");
		if((s.charAt(8))=='1') 
			sb.append("P");
		else sb.append(".");
		if((s.charAt(9))=='1') 
			sb.append("R");
		else sb.append(".");
		if((s.charAt(10))=='1') 
			sb.append("S");
		else sb.append(".");
		if((s.charAt(11))=='1') 
			sb.append("F");
		else sb.append(".");
		return sb.append("]").toString();
	}
}
