package fr.unice.miage.tinyhadoop.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import fr.unice.miage.tinyhadoop.api.Mapper;
import fr.unice.miage.tinyhadoop.api.Reducer;
import fr.unice.miage.tinyhadoop.core.KeyValuePair;

public interface ITaskTracker extends Remote {
	
	void setMapper(Class<? extends Mapper> mapperClass) throws RemoteException;
	
	void setReducer(Class<? extends Reducer> reducerClass) 
			throws RemoteException;

	void doMap(KeyValuePair inputPair) throws RemoteException;

	List<KeyValuePair> collectMappersOutput() throws RemoteException;

	void doReduce(String key, List<String> values) throws RemoteException;

	List<KeyValuePair> collectReducersOutput() throws RemoteException;

}
