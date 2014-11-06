package fr.unice.miage.tinyhadoop.application.wordcount;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import fr.unice.miage.tinyhadoop.core.JobTracker;
import fr.unice.miage.tinyhadoop.core.TaskTracker;
import fr.unice.miage.tinyhadoop.core.inputformat.DefaultInputFormat;
import fr.unice.miage.tinyhadoop.interfaces.IJobTracker;
import fr.unice.miage.tinyhadoop.interfaces.ITaskTracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit test for Word Count application.
 * The same line is written ten times in the input file.
 * Checks if all words are read ten times.
 */
@RunWith(JUnitParamsRunner.class)
public class WordCountApplicationTest {

	private static final int NB_TASK_TRACKERS_TEST = 2;
	private static final int NB_TASK_TRACKERS = 5;
	private static final String TEST_INPUT_FILENAME = 
			"/test-input-wordcount.txt";
	private static final String INPUT_FILENAME = "/input-wordcount.txt";
	private static final String TEST_OUTPUT_FILENAME = 
			"output/test-output-wordcount.txt";
	private static final String OUTPUT_FILENAME = 
			"output/output-wordcount.txt";
	private static final int OCCURENCES = 10;	

	private Registry registry;
	private JobTracker jobTracker;

	@Before
	public void setUp() throws RemoteException {
		try {
			this.registry = LocateRegistry.createRegistry(1099);
		}
		catch (RemoteException e) {
			this.registry = LocateRegistry.getRegistry(1099);
		}
		this.jobTracker = new JobTracker();
		this.jobTracker.setMapper(WordCountMapper.class);
		this.jobTracker.setReducer(WordCountReducer.class);	
		this.registry.rebind("jobtracker", jobTracker);
		for (int i = 0 ; i < NB_TASK_TRACKERS ; i++) {
			this.registry.rebind("taskTracker" + i, new TaskTracker());
		}
	}

	@Test
	@Parameters
	public void testWordCount(int nbTaskTrackers, String inputFilename, 
			String outputFilename, boolean testInput) 
					throws NumberFormatException, IOException, 
					NotBoundException {
		System.out.println("testWordCount: inputFilename=" + inputFilename
				+ ", outputFilename=" + outputFilename + ", testInput=" 
				+ testInput);

		IJobTracker remoteJobTracker = (IJobTracker) Naming.lookup(
				"jobtracker");
		ITaskTracker[] taskTrackers = new ITaskTracker[nbTaskTrackers];
		for (int i = 0 ; i < nbTaskTrackers ; i++) {
			taskTrackers[i] = (ITaskTracker) Naming.lookup("taskTracker" + i);
		}
		remoteJobTracker.setTaskTrackers(taskTrackers);
		remoteJobTracker.execute(inputFilename, outputFilename, 
				DefaultInputFormat.class);

		String line;
		String[] words;
		boolean empty = true;
		BufferedReader reader = new BufferedReader(
				new FileReader(outputFilename));
		
		if (testInput) {
			while ((line = reader.readLine()) != null) {
				empty = false;
				words = line.split(" ");
				assertEquals(Integer.parseInt(words[1]), OCCURENCES);
			}
		}
		else {
			int occurrence;
			while ((line = reader.readLine()) != null) {
				empty = false;
				words = line.split(" ");
				assertEquals(words[0].endsWith(","), false);
				assertEquals(words[0].endsWith("."), false);
				assertEquals(words[0].endsWith(";"), false);
				
				occurrence = Integer.parseInt(words[1]);
				assertEquals(Math.min(occurrence, 1), 1);
				assertEquals(Math.max(occurrence, 64), 64);
			}			
		}
		assertEquals(empty, false);
		
		reader.close();
	}

	/**
	 * Parameters for the testWordCount method.
	 */
	@SuppressWarnings("unused")
	private Object[] parametersForTestWordCount() {
		return new Object[][] {
				{NB_TASK_TRACKERS_TEST, TEST_INPUT_FILENAME, 
					TEST_OUTPUT_FILENAME, true},
				{NB_TASK_TRACKERS, INPUT_FILENAME, OUTPUT_FILENAME, 
					false}
		};
	}

	@After
	public void tearDown() throws RemoteException {
		try {
			UnicastRemoteObject.unexportObject(this.jobTracker, true);
		}
		catch(RemoteException e) {
			System.err.println("Object already unexported");
		}
		finally {
			this.registry = null;
			this.jobTracker = null;
		}
	}

}
