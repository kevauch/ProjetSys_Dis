package fr.unice.miage.tinyhadoop.core;

import fr.unice.miage.tinyhadoop.api.InputFormat;
import fr.unice.miage.tinyhadoop.api.Mapper;
import fr.unice.miage.tinyhadoop.api.Reducer;
import fr.unice.miage.tinyhadoop.interfaces.IJobTracker;
import fr.unice.miage.tinyhadoop.interfaces.ITaskTracker;

public class JobTracker implements IJobTracker {
	
	@Override
	public void setMapper(Class<? extends Mapper> mapperClass) {
		
	}
	
	@Override
	public void setReducer(Class<? extends Reducer> reducerClass){
		
	}
	
	@Override
	public void setTaskTrackers(ITaskTracker ... taskTrackers) {
		
	}

	@Override
	public void execute(String inputFilename, String outputFilename) {
		
	}

	@Override
	public void execute(String inputFilename, String outputFilename, 
			Class<? extends InputFormat> inputFormatClass) {
		
	}

	@Override
	public synchronized void watchDirectory(final String directoryName,
			final String outputFilenameBase,
			final Class<? extends InputFormat> inputFormatClass) {
		
	}

	@Override
	public void stopWatching() {
		
	}

}
