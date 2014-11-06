package fr.unice.miage.tinyhadoop.core;

import java.io.Serializable;

public class KeyValuePair implements Serializable {

	private static final long serialVersionUID = 1L;
	public final String key;
	public final String value;
	
	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
