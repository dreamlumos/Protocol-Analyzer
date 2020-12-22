package tools;

public class HexTools {

	/* Transforme une chaîne d'octets en valeur decimale. */
	public static long longHexToDec(String s) {
		
		long result = 0;
		int cpt = 0;
		for (int i = s.length()-1; i >= 0; i--) {
			result += (Integer.parseInt("" + s.charAt(i), 16) * Math.pow(16, cpt));
			cpt++;
		}
		return result;
	}
	
	/* Extension non signe sur taille bits. */
	public static String extBit(String s, int taille) {
		String sb = s;
		
		for (int i = 0; i < taille-s.length(); i++) {
			sb = "0"+sb;
		}
		return sb;
	}
	
}
