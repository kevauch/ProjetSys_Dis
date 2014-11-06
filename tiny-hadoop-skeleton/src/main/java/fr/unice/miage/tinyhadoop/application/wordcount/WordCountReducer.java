package fr.unice.miage.tinyhadoop.application.wordcount;

import java.util.List;

import fr.unice.miage.tinyhadoop.api.Reducer;

public class WordCountReducer extends Reducer {

	@Override
	public void reduce(String key, List<String> values) {
		int sum = 0;
		for (String value: values) {
			sum += Integer.parseInt(value);
		}
		this.emitReduceOutput(key, String.valueOf(sum));
	}

}
