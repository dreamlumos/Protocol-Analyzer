package composant;

public class HTTP {

	private String http;
	
	/* http must be a String of bytes separated by spaces */
	public HTTP(String http) {
		this.http = http;
		// check if HTTP?
	}
	
	@Override
	public String toString() {
				
		StringBuilder sb = new StringBuilder();
		sb.append("Hypertext Transfer Protocol\n\t");
		
		for (int i = 0; i < http.length(); i += 3) {
			String oneByte = http.substring(i, i+2);
			char ch = (char)Integer.parseInt(oneByte, 16);
		    sb.append(ch);
		    
		    if (ch == '\n') {
		    	sb.append("\t");
		    }
		}
		
		return sb.toString();
	}
}
