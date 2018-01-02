package client;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Queue;
import java.util.Set;

import message.Message;
import message.ProtocolMessage;

/**
 * This class represents the client of the Messenger application.
 *
 * @author Yunxiao Zhuang
 * @version 2017-03-20
 */
public class Client extends Observable implements ClientInterface {
	
	private Socket s = null;
	private PrintWriter dos = null;
	private BufferedReader dis = null;
	private Set<String> onlineList;
	private List<Set<String>> historyList;
	private Queue<Set<String>> groupChatsToBeClosed = new LinkedList<Set<String>>();
	private Queue<String> nextOfflineNotification = new LinkedList<String>();
	private Queue<Message> getNextMessage = new LinkedList<Message>();
	private Queue<List<Message>> getHistory = new LinkedList<List<Message>>();
	private boolean continueListening = true;
	
	/**
	 * This method is used to connect to server
	 * @param hostName IP address
	 * @param port number of port
	 * @throws IOException
	 */

	public void connect(String hostName, int port) throws IOException {
		s = new Socket(hostName, port);
		//output stream
		dos = new PrintWriter(s.getOutputStream(), true);
		//input stream
		dis = new BufferedReader(new InputStreamReader(s.getInputStream()));
		System.err.println("Client has connected...");
	}
	
	/**
	 * This method is used to disconnect
	 */
	public void disconnect() {
		try {
			// Close the socket, input, and output stream
			if(!s.isClosed()) {
				System.err.println("Closing client socket...");
				s.close();
				dis.close();
				dos.close();
			}
		} catch (IOException e) {
			System.err.println("Error closing client socket...");
		}
	}
	
	/**
	 * The listen method is used to listen the instructions from server.
	 * Using decode method and get the type from it. 
	 * Using observer method to notify the different instructions to GUI.
	 */
	public void listen() {
		String json, type;
		//protocol message is the instructions' type that listen from server
		ProtocolMessage pm;
		try {
			while(continueListening && ((json = dis.readLine()) != null)) {
				pm = ClientProtocol.decode(json);
				type = pm.getType();
				//Using switch(type) to distinguish different instructions
				switch(type) {
				case "sign-in":
				case "sign-up":
					notifyGUI(pm.getStatus());
					break;
				case "send-message":
					getNextMessage.add(pm.getMessage());
					notifyGUI("Chat:1");
					break;
				case "online-list":
					onlineList = pm.getOnlineList();
					notifyGUI("Online:1");
					break;
				case "close-group-chat":
					// Adding pm.getChat() to a Queue<Set<String>> groupChatsToBeClosed
					groupChatsToBeClosed.add(pm.getChat());
					notifyGUI("Chat:2");
					break;
				case "offline-notification":
					// Adding pm.getUserName() to Queue<String> offlieNotifications
					nextOfflineNotification.add(pm.getUserName());
					notifyGUI("Offline:1");
					break;
				case "get-history-list":
					// get the historyList
					historyList = pm.getHistoryList();
					notifyGUI("History:1");
					break;
				case "get-history":
                    //Adding pm.getHistory() to the queue of List<Message>
					getHistory.add(pm.getHistory());
					notifyGUI("History:2");
					break;
				}
			}
		} catch (IOException e) {
			System.err.println("Client connection has been interrupted...");
		} finally {
			disconnect();
			System.err.println("Client is shutting down...");
			notifyGUI("Connection:1");
		}
		
	}
	
	
	/**
	 * Notifies GUI on event-dispatch thread
	 * 
	 * @param status the status to notify the GUI
	 */
	private void notifyGUI(String status) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setChanged();
				notifyObservers(status);
			}
		});
	}
	
	/**
	 * Precondition: userName and password are not null and not empty.
	 * 
	 * @param userName
	 * @param password
	 */
	@Override
	public void createAccount(String userName, String password) {
		dos.println(ClientProtocol.signUp(userName, password));
	};
	
	/**
	 * Precondition: userName and password are not null and not empty.
	 * 
	 * @param userName
	 * @param password
	 */
	@Override
	public void login(String userName, String password) {
		dos.println(ClientProtocol.signIn(userName, password));
	};
	
	
	/**
	 * This method is used to send message to server
	 * @param message
	 */
	@Override
	public void sendMessage(Message message) {
		dos.println(ClientProtocol.sendMessage(message));

	};
	

	/**
	 * Precondition: The user is logged in and the client has received an online list from the server.
	 * Postcondition: If no user is online, the empty set is returned, else the set of online users is returned.
	 * 
	 * @return
	 */
	@Override
	public Set<String> getOnlineList() {
		return onlineList;
	}
	
	/**
	 * request the historyList from sever
	 */
	@Override
	public void requestHistoryList() {
		dos.println(ClientProtocol.getHistoryList());
	}
	
	/**
	 * 
	 * @return the content of that field variable.
	 */
	@Override
	public List<Set<String>> getHistoryList() {
		return historyList;
	};

	/**
	 * 
	 * @param users request history with these users 
	 */
	@Override
	public void requestHistory(Set<String> users) {
		dos.println(ClientProtocol.getHistory(users));
	};

	@Override
	public List<Message> getNextHistory() {
		return getHistory.poll();
	}
	
	/**
	 * Precondition: Client has informed GUI that new message has arrived
	 * Postcondition: Returns the next message
	 * 
	 * @return
	 */
	@Override
	public Message getNextMessage() {
		// return next message from queue
		return getNextMessage.poll();
	};

	/**
	 * Using method poll() to return the next element from the queue.
	 */
	@Override
	public Set<String> nextGroupChatToClose() {
		return groupChatsToBeClosed.poll();
	}

	/**
	 * return the next element from the queue.
	 */
	@Override
	public String nextOfflineNotification() {
		return nextOfflineNotification.poll();
	}

	/**
	 * output the chat should be closed to server which has these users
	 */
	@Override
	public void closeGroupChat(Set<String> users) {
		dos.println(ClientProtocol.closeGroupChat(users));
		
	}
	
	/**
	 * log out the user's account
	 */
	@Override
	public void logOut() {
		continueListening = false;
		disconnect();
	}
	
}
