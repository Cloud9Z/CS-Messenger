package test.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * DummyServer to test the client.
 *
 * @author Yunxiao Zhuang
 * @version 2017-03-20
 */
public class DummyServer {

	private static PrintWriter dos = null;
	private static ServerSocket ss = null;
	
	public static void send(String json) {
		dos.println(json);
	}
	
	public static void start(int port) {
		try {
			ss = new ServerSocket(port);
			Socket s = ss.accept();	
			dos = new PrintWriter(s.getOutputStream(), true);
		} catch (BindException e) {
			System.exit(0);
		} catch (IOException e) {
		}
	}
	
	public static void stop() {
		dos.close();
		try {
			ss.close();
		} catch (IOException e) {
		}
	}
	
}
