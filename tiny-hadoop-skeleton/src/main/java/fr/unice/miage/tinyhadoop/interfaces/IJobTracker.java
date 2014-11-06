package fr.unice.miage.tinyhadoop.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import fr.unice.miage.tinyhadoop.api.InputFormat;
import fr.unice.miage.tinyhadoop.api.Mapper;
import fr.unice.miage.tinyhadoop.api.Reducer;

public interface IJobTracker extends Remote {
	
	void setMapper(Class<? extends Mapper> mapperClass) throws RemoteException;
	
	void setReducer(Class<? extends Reducer> reducerClass) 
			throws RemoteException;
	
	void setTaskTrackers(ITaskTracker ... taskTrackers) 
			throws RemoteException;

	void execute(String inputFilename, String outputFilename) 
			throws RemoteException;
	
	void execute(String inputFilename, String outputFilename,
			Class<? extends InputFormat> inputFormatClass) 
					throws RemoteException;

	void watchDirectory(String directoryName, final String outputFilenameBase,
			final Class<? extends InputFormat> inputFormatClass) 
					throws RemoteException;
	
	void stopWatching() throws RemoteException;
}
