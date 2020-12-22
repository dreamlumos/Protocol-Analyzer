package composant;

import java.util.HashMap;
import java.util.List;

public class TCP implements DataUnit {
	
	private List<String> segment;
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
	private int headerLength; // in bytes
	
	private DataUnit data;
	
	public TCP(List<String> segment) {
		
		this.segment = segment;
		
		dictionary.put("002", "(SYN)");
		dictionary.put("010", "(ACK)");
		dictionary.put("012", "(SYN, ACK)");
		
		sourcePort = Integer.parseInt(segment.get(0)+segment.get(1),16);
		destPort = Integer.parseInt(segment.get(2)+segment.get(3),16);
		sequenceNumber = hexToDec(segment.get(4) + segment.get(5)+ segment.get(6) + segment.get(7));
		acknowledgmentNumber = hexToDec(segment.get(8) + segment.get(9)+ segment.get(10) + segment.get(11));
		dataOffset = segment.get(12);

		flagsValues = ""+dataOffset.charAt(1)+segment.get(13);
		flags = "Flags: 0x"+flagsValues+" "+dictionary.get(flagsValues);
		binaryFlags = Trame.extBit(Integer.toBinaryString(Integer.parseInt(flagsValues,16)),12);
		
		window = Integer.parseInt(segment.get(14)+segment.get(15),16);
		checksum = segment.get(16)+segment.get(17);
		urgent = Integer.parseInt(segment.get(18)+segment.get(19),16);
		headerLength = Integer.parseInt(""+dataOffset.charAt(0),16);
		len = segment.size()-headerLength*4;
		
		if (sourcePort == 80 || destPort == 80) {
			data = new HTTP(segment.subList(headerLength*4, segment.size()));
		} else {
			System.out.println("Application layer protocol unknown.");
		}
	}
	
	/* Transforme un octet en valeur decimale. */
	public int hexToDec(String s) {
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
		sb.append("\n\t\t.... "+s.charAt(4)+"... .... = Congestion Window Reduced (CWR): "+dict.get(Integer.parseInt(""+s.charAt(4),2)));
		sb.append("\n\t\t.... ."+s.charAt(5)+".. .... = ECN-Echo: "+dict.get(Integer.parseInt(""+s.charAt(5),2)));
		sb.append("\n\t\t.... .."+s.charAt(6)+". .... = Urgent: "+dict.get(Integer.parseInt(""+s.charAt(6),2)));
		sb.append("\n\t\t.... ..."+s.charAt(7)+" .... = Acknowledgment: "+dict.get(Integer.parseInt(""+s.charAt(7),2)));
		sb.append("\n\t\t.... .... "+s.charAt(8)+"... = Push: "+dict.get(Integer.parseInt(""+s.charAt(8),2)));
		sb.append("\n\t\t.... .... ."+s.charAt(9)+".. = Reset: "+dict.get(Integer.parseInt(""+s.charAt(9),2)));
		sb.append("\n\t\t.... .... .."+s.charAt(10)+". = Syn: "+dict.get(Integer.parseInt(""+s.charAt(10),2)));
		sb.append("\n\t\t.... .... ..."+s.charAt(11)+" = Fin: "+dict.get(Integer.parseInt(""+s.charAt(11),2)));
		sb.append("\n\t\t[TCP Flags: ");
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
		
		int position = 0;
		StringBuilder option = new StringBuilder();
		
		if (headerLength > 5) {
			
			int tailleOp = (headerLength - 5) * 4;
			option.append("\tOptions: (" + tailleOp + " bytes)");

			while (tailleOp > 0) {
				int kind = Integer.parseInt(segment.get(position + 20));
				int lengthOp = 1;
				if (kind != 0 && kind != 1)
					lengthOp = Integer.parseInt(segment.get(position + 21), 16);

				switch (kind) {
					case 0:
						option.append("\n\t\tTCP Option - End of Option List");
						option.append("\n\t\t\tKind: End of Option List (" + kind + ")");
						if (position != tailleOp - 1)
							option.append("Padding: \n\t\t Length:" + (tailleOp - position - 2) + " Bytes");
						break;
					
					case 1:
						option.append("\n\t\tTCP Option - No-Operation ");
						option.append("\n\t\t\tKind: No-Operation (" + kind + ")");
						break;
						
					case 2:
						option.append("\n\t\tTCP Option - Maximum Segment Size ");
						option.append("\n\t\t\tKind: Maximum Segment Size (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 3:
						option.append("\n\t\tTCP Option - Window Scale ");
						option.append("\n\t\t\tKind: Window Scale (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 4:
						option.append("\n\t\tTCP Option - SACK Permitted ");
						option.append("\n\t\t\tKind: SACK Permitted (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 5:
						option.append("\n\t\t\tKind: SACK (Selective ACK) (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 6:
						option.append("\n\t\tTCP Option - Echo (obsoleted by option 8) ");
						option.append("\n\t\t\tKind: Echo (obsoleted by option 8) (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 7:
						option.append("\n\t\tTCP Option - Echo Reply (obsoleted by option 8) ");
						option.append("\n\t\t\tKind: Echo Reply (obsoleted by option 8) (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
					
					case 8:
						option.append("\n\t\tTCP Option - Time Stamp Option ");
						option.append("\n\t\t\tKind: TSOPT - Time Stamp Option (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 9:
						option.append("\n\t\tTCP Option - Partial Order Connection Permitted");
						option.append("\n\t\t\tKind: Partial Order Connection Permitted (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 10:
						option.append("\n\t\tTCP Option - Partial Order Service Profile ");
						option.append("\n\t\t\tKind: Partial Order Service Profile (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 14:
						option.append("\n\t\tTCP Option - TCP Alternate Checksum Request ");
						option.append("\n\t\t\tKind: TCP Alternate Checksum Request (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
						break;
						
					case 15:
						option.append("\n\t\tTCP Option - TCP Alternate Checksum Data ");
						option.append("\n\t\t\tKind: TCP Alternate Checksum Data (" + kind + ")");
						option.append("\n\t\t\tLength: " + lengthOp);
				}

				tailleOp -= lengthOp;
				position += lengthOp;
			}
		}
		return option.toString();
	}
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("\n\tSource Port: "+sourcePort);
		sb.append("\n\tDestination Port: "+destPort);
		sb.append("\n\t[TCP Segment Len: "+len +"]");
		sb.append("\n\tSequence Number: "+sequenceNumber);
		sb.append("\n\tAcknowledgment Number: "+acknowledgmentNumber);
		sb.append("\n\t"+Trame.extBit(Integer.toBinaryString(headerLength),4)+" .... = Header Length: "+headerLength*4+" Bytes ("+headerLength+")");
		sb.append("\n\t"+flags);
		sb.append(flags(binaryFlags));
		sb.append("\n\tWindow: "+window);
		sb.append("\n\tChecksum: 0x"+checksum);
		sb.append("\n\tUrgent Pointer: "+urgent+"\n");
		sb.append(options()+"\n\n");
	
		if (data != null) {
			sb.append(data.toString());
		}
		
		return "\nTransmission Control Protocol, Src Port: "+sourcePort+", Dst Port: "+destPort+", Seq: "+sequenceNumber+", Len: "+len+sb.toString();
	}
}
