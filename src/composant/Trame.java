package composant;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Trame {
	
	private static int nbTrames = 0;
	private int numeroTrame;
	private boolean trameValide;
	private String ligneIncomplete = "Erreur : Ligne incomplete en position ";
	private ArrayList<String> octets;
	private int[] offsets;
	private DataUnit frame;
	
	public Trame() {
		nbTrames++;
		numeroTrame = nbTrames;
		trameValide = true;
		octets = new ArrayList<>();
		offsets = new int[1526]; // En pratique, il est rare qu'un datagramme IP fasse plus de 1500 octets. +26 pour Ethernet
		for(int i = 0; i < 1526; i++) {
			offsets[i] = 0;
		}
		
	}
	
	public void createFrame() {
		if (trameValide)
			frame = new Ethernet(octets);
	}
	
	public boolean trameValide() {
		return trameValide;
	}
	
	public void trameInvalide(int position) {
		trameValide = false;
		ligneIncomplete += "" + position + " de la trame " + numeroTrame + "\n\n";
	}
	
	public boolean addOctet(String s) {
		return octets.add(s);
	}
	
	public boolean addOffset(int offset, int position) {
		if(position<1000 && offsets[position]==0) {
			offsets[position]=offset;
			return true;
		}
		return false;	
	}
	
	public ArrayList<String> getOctets(){
		return octets;
	}
	
	public String toString() {
		
		StringBuilder sb = new StringBuilder();

		sb.append("Trame "+numeroTrame+" : ---------------------------\n");
		
		if(!trameValide()) return sb.append(ligneIncomplete).toString();

		int positionOctets = 0;
		int positionOffsets = 1;
		int i = 0;
		int j = offsets[positionOffsets];
		
		boolean notLast = true;
		
		while(i < j) {
			sb.append(octets.get(positionOctets)+" ");
			positionOctets++;
			
			if(i+1 == j && notLast){
				sb.append("\n");
				positionOffsets++;
				if(positionOffsets < offsets.length)
					j = offsets[positionOffsets];
					if(j == 0) {
						j = octets.size();
						notLast = false;
					}
			}
			i++;
		}
		
		sb.append("\n\n");
		sb.append(frame.toString());
		sb.append("\n\n");
				
		return sb.toString();
	}
	
	public void stringToFile(String fileName) throws IOException {
		
		File output = new File(fileName);
		output.createNewFile();
		
		BufferedWriter writer = null;
		String str = toString();

		try {
			writer = new BufferedWriter(new FileWriter(output, true));
			writer.append(str);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
		    writer.close();
		}
	}
}
