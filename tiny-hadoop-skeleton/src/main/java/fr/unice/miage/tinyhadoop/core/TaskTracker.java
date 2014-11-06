package fr.unice.miage.tinyhadoop.core;

import fr.unice.miage.tinyhadoop.api.Mapper;
import fr.unice.miage.tinyhadoop.api.Reducer;
import fr.unice.miage.tinyhadoop.interfaces.ITaskTracker;

import java.util.List;

public class TaskTracker implements ITaskTracker {

	@Override
	public void setMapper(Class<? extends Mapper> mapperClass) {
		
	}
	
	@Override
	public void setReducer(Class<? extends Reducer> reducerClass) {
		
	}

	@Override
	public void doMap(final KeyValuePair inputPair) {
		
	}

	@Override
	public List<KeyValuePair> collectMappersOutput() {
		return null;
	}

	@Override
	public void doReduce(String key, List<String> values) {
		
	}
	
	@Override
	public List<KeyValuePair> collectReducersOutput() {
		return null;
	}

}
