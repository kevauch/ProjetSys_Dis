package fr.unice.miage.tinyhadoop.core.inputformat;

import fr.unice.miage.tinyhadoop.api.InputFormat;
import fr.unice.miage.tinyhadoop.core.KeyValuePair;

public class DefaultInputFormat extends InputFormat {

	public DefaultInputFormat(String inputFilename) {
		super(inputFilename);
	}

	@Override
	public KeyValuePair readRecord() {
		return null;
	}

}
