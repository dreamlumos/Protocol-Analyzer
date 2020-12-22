package protocolAnalyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Trace {
	
	private static int nbTraces = 0;
	private int numeroTrace;
	private boolean traceValide;
	private String ligneIncomplete = "Erreur : Ligne incomplete en position ";
	private ArrayList<String> octets;
	private int[] offsets;
	private DataUnit frame;
	
	public Trace() {
		nbTraces++;
		numeroTrace = nbTraces;
		traceValide = true;
		octets = new ArrayList<>();
		offsets = new int[1526]; // En pratique, il est rare qu'un datagramme IP fasse plus de 1500 octets. +26 pour Ethernet
		for(int i = 0; i < 1526; i++) {
			offsets[i] = 0;
		}
		
	}
	
	public void createFrame() {
		if (traceValide)
			frame = new Ethernet(octets);
	}
	
	public boolean traceValide() {
		return traceValide;
	}
	
	public void traceInvalide(int position) {
		traceValide = false;
		ligneIncomplete += "" + position + " de la trace " + numeroTrace + "\n\n";
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

		sb.append("Trace "+numeroTrace+" : ---------------------------\n");
		
		if(!traceValide()) return sb.append(ligneIncomplete).toString();

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
	
	public static void  stringToFileGraph(BufferedWriter fileName ,String str) throws IOException {
		
	    BufferedWriter writer=null;
		try {
			writer = new BufferedWriter(fileName);
			writer.append(str);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
		    writer.close();
		}
	}
}
