package composant;

import java.util.ArrayList;

public class IPV4 {
	private Trame trame;
	private ArrayList<String> octets;
	private StringBuilder sb = new StringBuilder();
	private String version ;
	private StringBuilder ipSource = new StringBuilder();
	private StringBuilder ipDest = new StringBuilder();
	private int tailleentete;
	private String identification;
	private String flags;
	private int flags2;
	 public IPV4(Trame trame) {
		 this.trame=trame;
		 if(trame.trameValide()){
		 octets=trame.getOctets();
		 version=octets.get(14);
		 ipSource.append(Integer.parseInt(octets.get(25),16)).append(".").append(Integer.parseInt(octets.get(26),16));
		 ipSource.append(".").append(Integer.parseInt(octets.get(27),16)).append(".").append(Integer.parseInt(octets.get(28),16));
		 ipDest.append(Integer.parseInt(octets.get(29),16)).append(".").append(Integer.parseInt(octets.get(30),16));
		 ipDest.append(".").append(Integer.parseInt(octets.get(31),16)).append(".").append(Integer.parseInt(octets.get(32),16));
		 if((octets.get(12)+octets.get(13)).equals("0800")) {
		 tailleentete = Integer.parseInt("0"+version.charAt(0),16)*Integer.parseInt("0"+version.charAt(1),16);
		 identification = octets.get(18)+octets.get(19);
		 flags = octets.get(20);
		 flags2 = Integer.parseInt(""+flags.charAt(0));
		 }
	 }
}
		public String extBit(String s, int taille) { //extension non signé sur taille bits
			String sb =s;
			
			for (int i=0;i<taille-s.length() ; i++) {
				sb="0"+sb;
			}
			return sb;
		}
		public String toString() {
			if (trame.trameValide()) {
			if((octets.get(12)+octets.get(13)).equals("0800")) {
				sb.append("\t0100 .... = Version: ").append(version.charAt(0)+"\n");//version
				sb.append("\t.... ").append(extBit(Integer.toBinaryString(Integer.parseInt("0"+version.charAt(1),16)),4)).append(" = Header Length: ");//header legth
				//taille de l'entete
				sb.append(tailleentete+" bytes ("+Integer.parseInt("0"+version.charAt(1),16)+")\n");// nombres de lignes 
				sb.append("\tDifferentiated Services Field(TOS): 0x"+octets.get(15)+"\n");// TOS
				sb.append("\tTotal Length: "+Integer.parseInt(octets.get(16)+octets.get(17),16));//taille sans entete ethernet
				sb.append("\n\tIdentification: 0x"+identification+" ("+Integer.parseInt(identification,16)+")\n");
				sb.append("\tFlags: 0x"+Integer.parseInt(flags,16));
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
			return "not ipv4";
			}
			return "";
		}
}
