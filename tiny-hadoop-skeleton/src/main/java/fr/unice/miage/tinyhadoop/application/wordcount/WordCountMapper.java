package fr.unice.miage.tinyhadoop.application.wordcount;

import fr.unice.miage.tinyhadoop.api.Mapper;

public class WordCountMapper extends Mapper {

	@Override
	public void map(String key, String value) {
		String word = value.toLowerCase();
		this.emitMapOutput(word.substring(0, word.length()), "1");
	}

}
