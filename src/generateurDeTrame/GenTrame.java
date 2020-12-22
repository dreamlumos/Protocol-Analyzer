package generateurDeTrame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import composant.Trame;

public class GenTrame {
	
	public static void FileToTrames(Trames trames, String file) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		String line2;
		Trame tram = null;
		String[] lignecourante;
		String[] lignesuivante = null;
		int positionOffset = 0;
		int nbrOctets = 0; //nombres d'octets pour chaque ligne
		
		while (line != null) {
			
			lignecourante = line.split("[ ]+");
			
			if(!(lignecourante.length != 0) || !isOffset(lignecourante[0], "")) {
				line = br.readLine();
			} else {
				
				//Premiere ligne de la trame (le offset est egal a zero)
				if((Integer.parseInt(lignecourante[0], 16)) == 0) {
					if(tram != null) {
						if(!trames.getTrames().contains(tram)) {
							trames.addTrame(tram);
						}
					}
					tram = new Trame();
				}
				
				if(!tram.trameValide()) {
					line = br.readLine();
				} else {
					tram.addOffset(Integer.parseInt(lignecourante[0], 16), positionOffset);
					positionOffset++;
					do{
						line2 = br.readLine();
						if(line2!=null) {
							lignesuivante = line2.split("[ ]+");
						}
						else break;
					} while(lignesuivante.length==0||!isOffset(lignesuivante[0], ""));//trouver le prochain offset valide ou null
				
					if(line2!=null && lignesuivante.length!=0){
						
						lignesuivante = line2.split("[ ]+");//pour compiler a la sortie du do while
						
						if((Integer.parseInt(lignesuivante[0], 16))==0 && positionOffset-1!=0) {//cas de la derniere ligne de chaque trame
							/* taille tram - dernieroffset */
							nbrOctets = 14 + Integer.parseInt(tram.getOctets().get(16),16) + Integer.parseInt(tram.getOctets().get(17),16)-Integer.parseInt(lignecourante[0], 16);
							test(nbrOctets, tram, lignecourante, positionOffset);
							positionOffset=0;
							line=line2;
						} else {
							if(isOffset(lignesuivante[0], lignecourante[0])) {//verifier si offset plus grand ou bien ignorer
								nbrOctets= Integer.parseInt(lignesuivante[0], 16) - Integer.parseInt(lignecourante[0], 16);
								if (!test(nbrOctets, tram, lignecourante, positionOffset)) {
									positionOffset = 0;
								}
								line=line2;
							} else {
								if(Integer.parseInt(lignesuivante[0], 16)==0)tram.trameInvalide(positionOffset);
								positionOffset--;
							}
						}

					} else { //cas derniere trame
						/* taille tram - dernieroffset */
						if(tram.getOctets().size()>17) {
							nbrOctets = 14 + Integer.parseInt(tram.getOctets().get(16)+tram.getOctets().get(17),16)-Integer.parseInt(lignecourante[0], 16);
							if (!test(nbrOctets, tram, lignecourante, positionOffset)) {
								positionOffset = 0;
							}

						} else {
							tram.trameInvalide(positionOffset);
							positionOffset=0;
						}
						trames.addTrame(tram);
						tram = null;
						line = br.readLine();
					}
				}
			}
		} //endwhile

		
		for(Trame tram2 : trames.getTrames()) {
			tram2.createFrame();
		}
		
		br.close();
	}
	
	private static boolean test(int nbOctets, Trame tram, String[] lignecourante, int positionOffset) {
		try{
			for(int i=1; i <= nbOctets; i++ ) {
				if(isOctet(lignecourante[i])) {
					tram.addOctets(lignecourante[i]);
				}	
			}
		} catch(IndexOutOfBoundsException e) {
			tram.trameInvalide(positionOffset);
			return false;
		}
		return true;
	}
	
	/* Teste si c'est un octet en hexa */
	public static boolean isOctet(String s) { 
		if(s.length()==2) {
			char lettre1= s.charAt(0);
			char lettre2= s.charAt(1);
			boolean l1 = ((lettre1 >='0' && lettre1 <= '9')||(lettre1 >= 'a'&&lettre1 <= 'f')||(lettre1 >='A'&&lettre1<='F')); 
			boolean l2 = ((lettre2 >='0' && lettre2 <= '9')||(lettre2 >= 'a'&&lettre2 <= 'f')||(lettre2 >='A'&&lettre2<='F')); 
			return  l1&&l2;
		}
		return false;
	}
	
	/* Verifie si offset est correct, convention de string vide pour la premiere ligne de la trame */
	private static boolean isOffset(String offset, String offsetavant) {
		if (offset=="") return false;
		try {
			Integer.parseInt(offset,16);
		}
		catch(NumberFormatException e) {
			return false;
		}
		if(offsetavant!="")
			return Integer.parseInt(offset,16)>Integer.parseInt(offsetavant,16);
		return true;
	}
}
