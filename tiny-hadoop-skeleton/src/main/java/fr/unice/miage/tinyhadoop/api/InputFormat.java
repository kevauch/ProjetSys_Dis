package fr.unice.miage.tinyhadoop.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fr.unice.miage.tinyhadoop.core.KeyValuePair;

public abstract class InputFormat {

	protected InputStream inputStream;

	protected InputFormat(String inputFilename) {
		this.inputStream = InputFormat.class.getResourceAsStream(
				inputFilename);
		if (this.inputStream ==  null) {
			try {
				this.inputStream = new FileInputStream(inputFilename);
			} 
			catch (FileNotFoundException e) {
				System.err.println("getRessource failed, as well as creating "
						+ "the FileInputStream, wrong input filename for: " 
						+ inputFilename);
			}
		}
	}

	public abstract KeyValuePair readRecord();

}
