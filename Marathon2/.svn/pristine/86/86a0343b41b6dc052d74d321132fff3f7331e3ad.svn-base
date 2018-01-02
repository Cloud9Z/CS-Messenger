package test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * DummyClient to test the server.
 * 
 * @author Meng-Jung Lee & Jochen Stuber
 * @version 2017-03-20
 */
public class DummyClient {
	
	private Socket socket;
	private BufferedReader dis;
	private PrintWriter dos;
	
	
	public DummyClient(String hostName, int port) throws IOException {
		this.socket = new Socket(hostName, port);
		this.dis = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.dos = new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void sendToServer(String message) throws IOException {
		dos.println(message);
	}
	
	public String readFromServer() throws IOException {
		return dis.readLine();
	}
	
	public void disonnect() throws IOException {
		socket.close();
	}
}
