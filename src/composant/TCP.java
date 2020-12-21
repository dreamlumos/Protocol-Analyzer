package composant;

import java.util.HashMap;

public class TCP {
	
	private Trame trame;
	private HashMap<String, String> dictionary = new HashMap<>();
	
	private int sourcePort;
	private int destPort;
	private int sequenceNumber;
	private int acknowledgmentNumber;
	private String dataOffset;
	
	private String flagsValues;
	private String flags;
	private String binaryFlags;
	
	private int window;
	private String checksum;
	private int urgent;
	
	private int len;
	private int headerLength;
	
	public TCP(Trame trame) {
		this.trame = trame;
		
		if (trame.trameValide() && (trame.getOctets().get(12) + trame.getOctets().get(13)).equals("0800") ) { //trame complete et ipv4
			dictionary.put("002", "(SYN)");
			dictionary.put("010", "(ACK)");
			dictionary.put("012", "(SYN, ACK)");
			
			String versionip = trame.getOctets().get(14);
			int tailleip = Integer.parseInt("0" + versionip.charAt(0), 16)* Integer.parseInt("0" + versionip.charAt(1), 16);
			int tailleTrame = 14 + Integer.parseInt(trame.getOctets().get(16)+trame.getOctets().get(17),16);
			
			if (Integer.parseInt(trame.getOctets().get(23),16) == 6 && tailleip+14 < tailleTrame ) {
				
				int position = tailleip+14;//position tcp
				
				sourcePort = Integer.parseInt(trame.getOctets().get(position)+trame.getOctets().get(position+1),16);
				destPort = Integer.parseInt(trame.getOctets().get(position+2)+trame.getOctets().get(position+3),16);
				sequenceNumber = bigHexa(trame.getOctets().get(position + 4) + trame.getOctets().get(position + 5)+ trame.getOctets().get(position + 6) + trame.getOctets().get(position + 7));
				acknowledgmentNumber = bigHexa(trame.getOctets().get(position + 8) + trame.getOctets().get(position + 9)+ trame.getOctets().get(position + 10) + trame.getOctets().get(position + 11));
				dataOffset = trame.getOctets().get(position+12);
	
				flagsValues = ""+dataOffset.charAt(1)+trame.getOctets().get(position+13);
				flags = "Flags: 0x"+flagsValues+" "+dictionary.get(flagsValues);
				binaryFlags = trame.extBit(Integer.toBinaryString(Integer.parseInt(flagsValues,16)),12);
				
				window = Integer.parseInt(trame.getOctets().get(position+14)+trame.getOctets().get(position+15),16);
				checksum = trame.getOctets().get(position+16)+trame.getOctets().get(position+17);
				urgent = Integer.parseInt(trame.getOctets().get(position+18)+trame.getOctets().get(position+19),16);
				headerLength = Integer.parseInt(""+dataOffset.charAt(0),16);
				System.out.println((Integer.parseInt("0"+trame.getOctets().get(14).charAt(0),16)*Integer.parseInt("0"+trame.getOctets().get(14).charAt(1),16)));
				System.out.println( headerLength);
				len = trame.getOctets().size()-14-(Integer.parseInt("0"+trame.getOctets().get(14).charAt(0),16)*Integer.parseInt("0"+trame.getOctets().get(14).charAt(1),16))-headerLength*4;
			}
		}
	}
	
	/* Transforme un octet en valeur decimale. */
	public int bigHexa(String s) {
		int result = 0;
		int cpt = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			result += (Integer.parseInt("" + s.charAt(cpt), 16) * Math.pow(16, i));
			cpt++;
		}
		return result;
	}
	
	public String flags(String s) {
		
		if (s == null) return "";
		
		HashMap<Integer, String> dict = new HashMap<>();
		dict.put(0, "Not set");
		dict.put(1, "Set");
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t\t"+s.charAt(0)+s.charAt(1)+s.charAt(2)+". .... = Reserved "+dict.get(Integer.parseInt(""+s.charAt(0)+s.charAt(1)+s.charAt(2),2)));
		sb.append("\n\t\t..."+s.charAt(3)+" .... .... = Nonce: "+dict.get(Integer.parseInt(""+s.charAt(3),2)));
		sb.append("\n\t\t.... "+s.charAt(4)+"... .... = Congestion Window Reduced (CMR): "+dict.get(Integer.parseInt(""+s.charAt(4),2)));
		sb.append("\n\t\t.... ."+s.charAt(5)+".. .... = ECN-Echo: "+dict.get(Integer.parseInt(""+s.charAt(5),2)));
		sb.append("\n\t\t.... .."+s.charAt(6)+". .... = Urgent: "+dict.get(Integer.parseInt(""+s.charAt(6),2)));
		sb.append("\n\t\t.... ..."+s.charAt(7)+" .... = Acknowledgment: "+dict.get(Integer.parseInt(""+s.charAt(7),2)));
		sb.append("\n\t\t.... .... "+s.charAt(8)+"... = Push: "+dict.get(Integer.parseInt(""+s.charAt(8),2)));
		sb.append("\n\t\t.... .... ."+s.charAt(9)+".. = Reset: "+dict.get(Integer.parseInt(""+s.charAt(9),2)));
		sb.append("\n\t\t.... .... .."+s.charAt(10)+". = Syn: "+dict.get(Integer.parseInt(""+s.charAt(10),2)));
		sb.append("\n\t\t.... .... ..."+s.charAt(11)+" = Fin: "+dict.get(Integer.parseInt(""+s.charAt(11),2)));
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
	
	public String options() {
		
		String versionip = trame.getOctets().get(14);
		int tailleip = Integer.parseInt("0" + versionip.charAt(0), 16)* Integer.parseInt("0" + versionip.charAt(1), 16);
		int position = tailleip+14;
		StringBuilder option = new StringBuilder();
		option.append("");
		
		if (headerLength > 5) {
			int tailleOp = (headerLength - 5) * 4;
			while (tailleOp > 0) {
				int kind = Integer.parseInt(trame.getOctets().get(position + 20));
				int lengthOp = 1;
				if (kind != 0 && kind != 1 && kind != 11 && kind != 12 && kind != 13)
					lengthOp = Integer.parseInt(trame.getOctets().get(position + 21), 16);

				switch (kind) {
				case 0 :
					option.append("TCP Option - End of Option List");
					option.append("\n\t  Kind : End of Option List (" + kind + ")");
					if (position != tailleOp - 1)
						option.append(
								"Padding : \n\t\t Length :" + (tailleOp - position - 2) + " Bytes");
				
				case 1 :
					option.append("TCP Option - No-Operation ");
					option.append("\n\t Kind : No-Operation (" + kind + ")");
				
				case 2 :
					option.append("TCP Option - Maximum Segment Size ");
					option.append("\n \t Kind : Maximum Segment Size (" + kind + ")");
					option.append("\n \t Length :" + lengthOp);
				
				case 3 :
					option.append("TCP Option - WSOPT - Window Scale ");
				
					option.append("\n \t Kind : Window Scale (" + kind + ")");
					option.append("\n \t Length :" + lengthOp);
				
				case 4 :
					option.append("TCP Option - SACK Permitted ");
					option.append("\n \t Kind : SACK Permitted (" + kind + ")");
					option.append("\n \t Length :" + lengthOp);
				
				case 5 :
					option.append("Kind : SACK (Selective ACK) (" + kind + ")");
					option.append("\n \t Length :" + lengthOp);
				
				case 6 :
					option.append(", Echo (obsoleted by option 8)");
					option.append("TCP Option - Echo (obsoleted by option 8) ");
					option.append("\n\t\t\tKind : Echo (obsoleted by option 8) (" + kind + ")");
					option.append("Length :" + lengthOp);
				
				case 7 :
					option.append(", Echo Reply (obsoleted by option 8)");
					option.append("TCP Option - Echo Reply (obsoleted by option 8) ");
					option.append("Kind : Echo Reply (obsoleted by option 8) (" + kind + ")");
					option.append("Length :" + lengthOp);
				case 8 :
					option.append(", TSOPT - Time Stamp Option");
					option.append("TCP Option - TSOPT - Time Stamp Option ");
					option.append("Kind : TSOPT - Time Stamp Option (" + kind + ")");
					option.append("Length :" + lengthOp);
	
				case 9 :
					option.append("TCP Option - Partial Order Connection Permitted \n");
					option.append("Kind : Partial Order Connection Permitted (" + kind + ")");
					option.append("Length :" + lengthOp);
				
				case 10 :
					option.append(", Partial Order Service Profile");
					option.append("TCP Option - Partial Order Service Profile ");
					option.append("Kind : Partial Order Service Profile (" + kind + ")\n\t\t");
					option.append("Length :" + lengthOp);
				case 11 :
					option.append(", CC");
					option.append("TCP Option - CC ");
					option.append("Kind : CC (" + kind + ")");
			
				case 12 :
					option.append(", CC.NEW");
					option.append("TCP Option - CC.NEW ");
					option.append("Kind : CC.NEW (" + kind + ")");
				
				case 13 :
					option.append("TCP Option - CC.ECHO ");
					option.append("Kind : CC.ECHO (" + kind + ")");
				case 14 :
					option.append("TCP Option - TCP Alternate Checksum Request ");
					option.append("Kind : TCP Alternate Checksum Request (" + kind + ")");
					option.append("Length :" + lengthOp);
				case 15 :
					option.append(", TCP Alternate Checksum Data");
					option.append("TCP Option - TCP Alternate Checksum Data ");
					option.append("Kind : TCP Alternate Checksum Data (" + kind + ")");
					option.append("Length :" + lengthOp);
				
				}

				tailleOp -= lengthOp;
				position += lengthOp;

			}
			
			tailleOp = (headerLength - 5) * 4;
			option.append("Options: (" + tailleOp + ") bytes" + option.toString());
		}
		
		return option.toString();
	}
	
	@Override
	public String toString() {
		
		if (!trame.trameValide() || !(trame.getOctets().get(12) + trame.getOctets().get(13)).equals("0800")) {
			return "TCP : Trame invalide";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\n\tSource Port: "+sourcePort);
		sb.append("\n\tDestination Port: "+destPort);
		sb.append("\n\t[TCP Segment Len: "+len +"]");
		sb.append("\n\tSequence Number: "+sequenceNumber);
		sb.append("\n\tAcknowledgment Number: "+acknowledgmentNumber);
		sb.append("\n\t"+trame.extBit(Integer.toBinaryString(headerLength),4)+" .... = Header Length:"+headerLength*4+" Bytes ("+headerLength+")");
		sb.append("\n\t"+flags);
		sb.append(flags(binaryFlags));
		sb.append("\n\tWindow: "+window);
		sb.append("\n\tChecksum: 0x"+checksum);
		sb.append("\n\tUrgent Pointer: "+urgent);
	
		return "\nTransmission Control Protocol, Src Port: "+sourcePort+", Dst Port: "+destPort+", Seq: "+sequenceNumber+", Len: "+len+sb.toString()+options();
	}
}
