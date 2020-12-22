package protocolAnalyzer;

import java.util.List;

public class HTTP implements DataUnit {

	private List<String> data;
	
	/* data must be a String of bytes separated by spaces */
	public HTTP(List<String> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
				
		StringBuilder sb = new StringBuilder();
		sb.append("Hypertext Transfer Protocol\n\t");
		
		for (int i = 0; i < data.size(); i++) {
			String oneByte = data.get(i);
			char ch = (char)Integer.parseInt(oneByte, 16);
		    sb.append(ch);
		    
		    if (ch == '\n') {
		    	sb.append("\t");
		    }
		}
		
		return sb.toString();
	}
}
