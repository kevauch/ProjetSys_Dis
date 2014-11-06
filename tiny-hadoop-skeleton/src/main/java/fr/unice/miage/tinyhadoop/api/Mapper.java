package fr.unice.miage.tinyhadoop.api;

import java.util.LinkedList;
import java.util.List;

import fr.unice.miage.tinyhadoop.core.KeyValuePair;

public abstract class Mapper {
	
	private List<KeyValuePair> mapOutput;
	
	protected Mapper() {
		this.mapOutput = new LinkedList<KeyValuePair>();
	}
	
	protected void emitMapOutput(String key, String value) {
		this.mapOutput.add(new KeyValuePair(key, value));
	}
	
	public List<KeyValuePair> collectMapOutput() {
		List<KeyValuePair> currentMapOutput = new LinkedList<KeyValuePair>();
		for (KeyValuePair kvp : this.mapOutput) {
			currentMapOutput.add(kvp);
		}
		this.mapOutput.clear();
		return currentMapOutput;
	}

	public abstract void map(String key, String value); 
	
}
