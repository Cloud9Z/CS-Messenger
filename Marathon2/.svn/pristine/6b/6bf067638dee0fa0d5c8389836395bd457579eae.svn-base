package client;

import java.awt.EventQueue;
import java.io.IOException;

import gui.MessengerGUI;

/**
 * Main application class on client side, launching client and GUI.
 *
 * @author Jochen Stuber
 * @version 2017-03-20
 */
public class ClientApp {
	
	/**
	 * Launches the client application, accepting the server's hostname and port on the
	 * command line.
	 * 
	 * @param args hostname and port of the server
	 */
	public static void main(String[]args) {
		if(args.length != 2) {
			System.out.println("Usage: java -jar messenger.jar <hostname> <port>");
			System.exit(-1);
		} 
		// Parse port
		int port = -1;
		try {
			port = Integer.parseInt(args[1]);
		} catch(NumberFormatException nfe) {
			System.out.println("Port has to be a positive integer.");
			System.exit(-1);
		}
		// Create client
		String hostName = args[0];
		Client client = new Client();
		try {
			client.connect(hostName, port);
		} catch (IOException e) {
			System.out.println("The client could not connect to the server.");
			System.exit(-1);
		}
		// Create GUI, add as Observer to client, launch GUI.
		MessengerGUI gui = new MessengerGUI(client);
		client.addObserver(gui);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui.startGUI();
				} catch (Exception e) {
					System.out.println("Oops, something went wrong in the GUI. Shutting down...");
					client.logOut();
					System.exit(-1);
				}
			}
		});
		// Make client listen
		try {
			client.listen();
		} catch (Exception e) {
			System.out.println("Oops, something went wrong in the client. Shutting down...");
		}

	}
	
}
