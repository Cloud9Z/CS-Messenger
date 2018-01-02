package test.gui;

import java.awt.EventQueue;

import gui.MessengerGUI;

/**
 * Test application for the GUI.
 * 
 * @author violamarku
 * @version 2017-03-20
 */
public class GUITestLoginFail {
	
	public static void main(String[] args) {
		DummyClientForGUI client = new DummyClientForGUI();
		client.setLoginStatus("Login:-1");
		MessengerGUI gui = new MessengerGUI(client);
		client.addObserver(gui);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				gui.startGUI();
			}
		});
		
	}
	
}