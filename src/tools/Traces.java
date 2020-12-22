package tools;

import java.util.ArrayList;

import protocolAnalyzer.Trace;

public class Traces {
	
	private ArrayList<Trace> traces;
	
	public Traces() {
		traces = new ArrayList<>();
	}
	
	public boolean addTrace(Trace t) {
		return traces.add(t);
	}
	
	public ArrayList<Trace> getTraces(){
		return traces;
	}
}
