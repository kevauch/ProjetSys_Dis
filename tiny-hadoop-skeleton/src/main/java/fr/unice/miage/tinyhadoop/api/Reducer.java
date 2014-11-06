package fr.unice.miage.tinyhadoop.api;

import java.util.LinkedList;
import java.util.List;

import fr.unice.miage.tinyhadoop.core.KeyValuePair;

public abstract class Reducer {
	
	private List<KeyValuePair> reduceOuput;
	
	protected Reducer() {
		this.reduceOuput = new LinkedList<KeyValuePair>();
	}
	
	protected void emitReduceOutput(String key, String value) {
		this.reduceOuput.add(new KeyValuePair(key, value));
	}
	
	public List<KeyValuePair> collectReduceOutput() {
		List<KeyValuePair> currentReduceOutput = 
				new LinkedList<KeyValuePair>();
		for (KeyValuePair kvp: this.reduceOuput) {
			currentReduceOutput.add(kvp);
		}	
		this.reduceOuput.clear();
		return currentReduceOutput;
	}

	public abstract void reduce(String key, List<String> values);
	
}
