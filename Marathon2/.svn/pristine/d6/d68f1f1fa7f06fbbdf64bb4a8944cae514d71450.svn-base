package test;
import java.awt.EventQueue;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import client.Client;
import gui.MessengerGUI;
import message.Message;
import server.Server;
import server.db.DatabaseFunctions;
import test.server.DummyDatabase;

/**
 * Application test sing dummy database. 
 * 
 * @version 2017-03-20
 */
public class ChatAppTestDrive {
	
	/**
	 * Application test sing dummy database.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int port = 63000;
		List<Set<String>> historyList = new ArrayList<Set<String>>();
		Set<String> history = new TreeSet<String>(Arrays.asList(new String[]{"achilles", "menelaos", "nestor"}));
		historyList.add(history);
		List<Message> hist = new ArrayList<Message>();
		List<String> l = Arrays.asList(new String[]{"achilles", "menelaos", "nestor"});
		Set<String> chat = new TreeSet<String>(l);
		LocalDateTime t = LocalDateTime.of(2017, 4, 29, 23, 45);
		Message m = new Message("achilles", chat, t, "Off to Troy!");
		hist.add(m);
		t = LocalDateTime.of(2017, 4, 29, 23, 50);
		m = new Message("nestor", chat, t, "Sack Priam's city!");
		hist.add(m);
		DatabaseFunctions db = new DummyDatabase(true, historyList, hist);
		Server server =  new Server(port);
		server.addToOnlineList("gibson");
		server.setDatabaseFunctions(db);
		new Thread(new Runnable() {
			public void run() {
				server.start();
			}
		}).start();
		Client client = new Client();
		client.connect("localhost", port);
		MessengerGUI gui = new MessengerGUI(client);
		client.addObserver(gui);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				gui.startGUI();
			}
		});
		client.listen();
		server.stop();
	}
}
