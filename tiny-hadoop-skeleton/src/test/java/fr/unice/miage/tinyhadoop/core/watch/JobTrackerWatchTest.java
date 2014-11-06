package fr.unice.miage.tinyhadoop.core.watch;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import fr.unice.miage.tinyhadoop.application.wordcount.WordCountMapper;
import fr.unice.miage.tinyhadoop.application.wordcount.WordCountReducer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit test for directory watching functionality.
 * A directory is watched and files are added in this directory.
 * The Word Count application is triggered each time.
 */
@RunWith(JUnitParamsRunner.class)
public class JobTrackerWatchTest {

	private static final int NB_TASK_TRACKERS_TEST = 2;
	private static final int NB_TASK_TRACKERS = 5;
	private static final String TEST_INPUT_FILENAME = 
			"/test-input-wordcount.txt";
	private static final String INPUT_FILENAME = "/input-wordcount.txt";
	private static final String TEST_OUTPUT_FILENAME_BASE = 
			"output/watched-output/test-output";
	private static final String OUTPUT_FILENAME_BASE = 
			"output/watched-output/output";
	private static final String WATCHED_DIRECTORY = 
			"watched-input";
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
	public void testJobTrackerWatch(int nbTaskTrackers, 
			String watchedDirectory, String fileToCopy,
			String outputFilenameBase, int testIndex, boolean testInput) 
					throws NumberFormatException, IOException, 
					NotBoundException, InterruptedException {
		System.out.println("testJobTrackerWatch: watchedDirectory=" 
				+ watchedDirectory + ", outputFilenameBase=" + 
				outputFilenameBase + ", testInput=" + testInput);

		IJobTracker remoteJobTracker = (IJobTracker) Naming.lookup(
				"jobtracker");
		ITaskTracker[] taskTrackers = new ITaskTracker[nbTaskTrackers];
		for (int i = 0 ; i < nbTaskTrackers ; i++) {
			taskTrackers[i] = (ITaskTracker) Naming.lookup("taskTracker" + i);
		}
		remoteJobTracker.setTaskTrackers(taskTrackers);
		remoteJobTracker.watchDirectory(watchedDirectory, outputFilenameBase, 
				DefaultInputFormat.class);

		Thread.sleep(1000);

		Files.copy(JobTrackerWatchTest.class.getResourceAsStream(fileToCopy),
				Paths.get(watchedDirectory+fileToCopy), 
				StandardCopyOption.REPLACE_EXISTING);

		Thread.sleep(15000);
		
		remoteJobTracker.stopWatching();

		String line;
		String[] words;
		BufferedReader reader = new BufferedReader(
				new FileReader(outputFilenameBase + testIndex + ".txt"));

		if (testInput) {
			while ((line = reader.readLine()) != null) {
				words = line.split(" ");
				assertEquals(Integer.parseInt(words[1]), OCCURENCES);
			}
		}
		else {
			int occurrence;
			while ((line = reader.readLine()) != null) {
				words = line.split(" ");
				assertEquals(words[0].endsWith(","), false);
				assertEquals(words[0].endsWith("."), false);
				assertEquals(words[0].endsWith(";"), false);

				occurrence = Integer.parseInt(words[1]);
				assertEquals(Math.min(occurrence, 1), 1);
				assertEquals(Math.max(occurrence, 64), 64);
			}
		}
		reader.close();
	}

	/**
	 * Parameters for the JobTrackerWatch method.
	 */
	@SuppressWarnings("unused")
	private Object[] parametersForTestJobTrackerWatch() {
		return new Object[][] {
				{NB_TASK_TRACKERS_TEST, WATCHED_DIRECTORY, TEST_INPUT_FILENAME, 
					TEST_OUTPUT_FILENAME_BASE, 0, true},
				{NB_TASK_TRACKERS, WATCHED_DIRECTORY, INPUT_FILENAME, 
					OUTPUT_FILENAME_BASE, 0, false}
		};
	}

	@After
	public void tearDown() throws RemoteException {
		try {
			UnicastRemoteObject.unexportObject(this.jobTracker, true);
			File testFile = new File(WATCHED_DIRECTORY + TEST_INPUT_FILENAME);
			if (testFile.exists()) testFile.delete();
			File file = new File(WATCHED_DIRECTORY + INPUT_FILENAME);
			if (file.exists()) file.delete();
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
