package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import javax.json.JsonException;

import message.Message;
import message.ProtocolMessage;

/**
 * This class represents the thread spwawned by the server to service an individual client.
 * 
 * @author Meng-Jung Lee & Jochen Stuber
 * @version 2017-03-20
 */
public class WorkerThread implements Runnable {
	// Reference to the server
	private final Server server;
	// Socket for client communication and the associated input and output stream
	private final Socket socket;
	private PrintWriter out;
	// Protocol for communication with client
	private final ServerProtocol protocol;
	// The user name of the user served by this thread
	private String connectedUser;
	// Indicates whether the current user has been authenticated, i.e. is signed in
	private boolean authenticated;
	// Queue of Messages to be sent to the client this thread is connected to
	private Queue<Message> messageQueue;
	// Queue of offline notifications to be delivered to the client
	private Queue<String> offlineQueue;
	// Queue of notifications about group chats that need to be closed
	private Queue<Set<String>> groupChatsToCloseQueue;
	private LocalDateTime friendListTime;	
	// Indicates whether the helper thread should be stopped
	private boolean stopHelper;
	
	/**
	 * Constructs a new WorkerThread given a server and socket as arguments. It creates
	 * a new protocol for client communication and queues, however without registering these.
	 * 
	 * @param server the server to which the WorkerThread belongs
	 * @param socket the socket to be used for client communication
	 */
	public WorkerThread(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.protocol = new ServerProtocol();
	}
	
	/**
	 * Listen for client requests, parse them, and handle them appropriately.
	 * Tasks for delivering messages and notifications to the client are handled 
	 * concurrently by inner class HelperThread
	 * 
	 */
	@Override
	public void run() {
		// Try to retrieve input and output streams for socket.
		// The try-with-resources statement closes them automatically in the end.
		try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
			// Once obtained assign streams to member variables
			this.out = out;
			// Read text from client one line at a time. 
			String json;
			while((json = in.readLine()) != null) {
				// In each iteration, check for and handle interrupts
				if(Thread.interrupted()) {
					System.err.println("WorkerThread interrupted. Shutting down...");
					return;
				}
				// Try to decode client message
				ProtocolMessage pm = null;
				try { 
					pm = protocol.decode(json);
				} catch (JsonException je) {
					// If decoding fails, stop listening and close connection
					je.printStackTrace();
					break;
				}
				// Distinguish message type and handle appropriately
				switch(pm.getType()) {
				case "sign-up":
					signUp(pm.getUserName(), pm.getPassword());
					break;
				case "sign-in":
					signIn(pm.getUserName(), pm.getPassword());											
					break;
				case "send-message":					
					sendMessage(pm.getMessage());
					break;
				case "close-group-chat":
					closeGroupChatNotifyOthers(pm.getChat());
					break;
				case "get-history-list":
					getHistoryList(connectedUser);
					break;
				case "get-history":
					getHistory(pm.getChat());
					break;
				}
			}
			// Arriving here means that the client has closed the connection.
			System.err.println(String.format("%s has disconnected from server...", 
					authenticated ? connectedUser : "Unauthenticated user"));
		} catch (IOException ioe) {
			// Catch other exceptions from retrieving data streams or from reading from socket.
			System.err.println("WorkerThread's connection has been interrupted...");
		} catch (IllegalStateException ise) {
			// Thrown when there is an unauthenticated attempt to access functionality
			System.err.println(ise.getMessage());
		} catch (ClassNotFoundException e) {
			// Handle case where DB driver cannot be loaded (should never happen as jar is included in the project)
			System.err.println("Database driver could not be loaded...");
		} catch (SQLException e) {
			// Handle case where a database error occurs
			System.err.println("A database error occured...");
		} catch (Exception e) {
			// Should never get here. Handle unexpected Exception.
			System.err.println("Unexpected server exception occured...");
		} finally {
			if(authenticated) {
				// Stop helper thread
				stopHelper = true;
				// Remove client from online list
				server.removeFromOnlineList(connectedUser);
				// Unregister queues
				unregisterQueues();
			}
			// Close socket
			try {
				this.socket.close();
			} catch (IOException ioe2) {
				System.err.println("Error closing client socket in WorkerThread...");
			}
		}
	}

	/**
	 * Notifies the threads serving the users in the chat that the group chat needs
	 * to be closed.
	 * 
	 * @param chat the group chat that needs to e closed
	 */
	private void closeGroupChatNotifyOthers(Set<String> chat) {
		server.addGroupChatClosingNotification(connectedUser, chat);
	}
	
	/**
	 * Notified the client that a group chat needs to be closed.
	 * 
	 * @param chat the group chat that needs to be closed
	 */
	private void closeGroupChatNotifyClient(Set<String> chat) {
		out.println(protocol.closeGroupChat(chat));
	}

	/**
	 * This method sends the user account information to database to sign up a new user
	 * and returns if sign up is successful to the client in JSON protocol format.
	 *  
	 * @param userName the user name of the account
	 * @param password the password of the account
	 * @throws IOException if an error occurs while writing to the socket
	 * @throws SQLException if a database error occurs
	 * @throws ClassNotFoundException  if the driver cannot be found
	 */	
	private void signUp(String userName, String password) throws IOException, ClassNotFoundException, SQLException{
		boolean signedUp = server.getDatabaseFunctions().signUp(userName, password);
		if(signedUp){
			out.println(protocol.signUp("Account:1"));			
		}
		else{
			out.println(protocol.signUp("Account:-1"));
		}	
	}
	
	/**
	 * This method sends the user's user name and password to database 
	 * to map if sign in information is correct, and returns if log in is successful 
	 * to the client in JSON protocol format.
	 *  
	 * @param userName the user name of the account
	 * @param password the password of the account
	 * @throws IOException if an error occurs while writing to the socket
	 * @@throws SQLException if a database error occurs
	 * @throws ClassNotFoundException  if the driver cannot be found
	 */
	private void signIn(String userName, String password) throws IOException, ClassNotFoundException, SQLException{
		// Handle case where user is already logged in
		if(server.isOnline(userName)) {
			out.println(protocol.signIn("Login:-2"));
			return;
		}
		// Compare credentials with information in the database
		boolean signedIn = server.getDatabaseFunctions().signIn(userName, password);
		if(signedIn){
			authenticated = true;
			connectedUser = userName;
			// Create queues for the authenticated user and register them with the server
			this.messageQueue = new LinkedBlockingQueue<Message>();
			server.registerMessageQueue(userName, messageQueue);
			this.offlineQueue = new LinkedBlockingQueue<String>();
			server.registerOfflineQueue(offlineQueue);
			this.groupChatsToCloseQueue = new LinkedBlockingQueue<Set<String>>(); 
			server.registerChatsToCloseQueue(userName, groupChatsToCloseQueue);
			// Send response to client
			out.println(protocol.signIn("Login:1"));
			// Add to online list and send online list to client
			server.addToOnlineList(userName);
			sendOnlineList();
			// Start HelperThread
			server.submitToThreadPool(new HelperThread());
		}
		else{
			// Inform client that login was not sucessful
			out.println(protocol.signIn("Login:-1"));
		}
		
	}
	
	/**
	 * This method sends messages from a client to other clients 
	 * who are involved in the corresponding chat in JSON protocol format, 
	 * and save the messages and relative information into database for future recall.  
	 *  
	 * @param userName the user name of the account
	 * @param password the password of the account
	 * @throws SQLException if a database error occurs
	 * @throws ClassNotFoundException  if the driver cannot be found
	 * @throws IOException if an error occurs while writing to the socket
	 */
	private void sendMessage(Message message) throws IllegalStateException, ClassNotFoundException, SQLException {
		if(!authenticated) {throw new IllegalStateException("Unauthorised access attempted");}
		// Save message to database
		server.getDatabaseFunctions().saveMessage(message);
		// Add message to queues of recipients
		server.addMessageToOutgoingQueues(message);
		// Loop message back to client
		out.println(protocol.sendMessage(message));
	}
	
	/**
	 * This method obtains the list of histories for the specified user name from the
	 * database and returns it to the client in JSON protocol format.
	 * 
	 * @param userName the user name for which the history list shall be retrieved
	 * @throws SQLException if a database error occurs
	 * @throws ClassNotFoundException  if the driver cannot be found
	 * @throws IOException if an error occurs while writing to the socket
	 */
	private void getHistoryList(String userName) throws IllegalStateException, ClassNotFoundException, SQLException {
		if(!authenticated) {throw new IllegalStateException("Unauthorised access attempted");}
		// Get history list from database and send to client
		List<Set<String>> list = server.getDatabaseFunctions().getHistoryList(userName);
		out.println(protocol.getHistoryList(list));
	}
	
	/**
	 * Obtains a chat history for the specified chat from the database and returns it to
	 * the client in JSON protocol format.
	 * 
	 * @param users the users that are part of the chat for which the history shall be obtained
	 * @throws SQLException if a database error occurs
	 * @throws ClassNotFoundException  if the driver cannot be found
	 * @throws IOException if an error occurs while writing to the socket
	 */
	private void getHistory(Set<String> users) throws IllegalStateException, ClassNotFoundException, SQLException {
		if(!authenticated) {throw new IllegalStateException("Unauthorised access attempted");}
		// Get history from database and deliver it to client
		List<Message> history = server.getDatabaseFunctions().getHistory(users);
		out.println(protocol.getHistory(history));
	}
	
	/**
	 * Informs the connected client that a user has gone offline. 
	 * 
	 * @param userName the user who has gone offline
	 * @throws IOException if an error occurs while writing to the socket
	 */
	private void offlineNotification(String userName) {
		// In the unlikely event that a user logs in quickly after having gone offline and
		// the notification for that has not yet been placed in all queues, check that the
		// user is not notified of herself going offline.
		if(!userName.equals(connectedUser)) {
			out.println(protocol.offlineNotification(userName));
		}
	}
	
	/**
	 * Unregisters all of the thread's queues from the server
	 */
	private void unregisterQueues() {
		server.unregisterMessageQueue(connectedUser);
		server.unregisterGroupChatsToCloseQueue(connectedUser);
		server.unregisterOfflineQueue(offlineQueue);
	}
	
	/**
	 * Send the online list to the client. The implementation assures that no update
	 * of the online list is missed. It is possible that the same version of the online
	 * list is sent multiple times but this is traded off with the performance loss of
	 * synchronisation.
	 */
	private void sendOnlineList(){
		LocalDateTime lastChanged = server.getFriendListTime();
		Set<String> onlineList = server.getOnlineList();
		this.friendListTime = lastChanged;
		out.println(protocol.getOnlineList(onlineList));
	}
	
	/**
	 * Send message to client.
	 * 
	 * @param m the message to be sent
	 */
	private void deliverMessageToConnectedUser(Message m) {
		out.println(protocol.sendMessage(m));
	}
	
	/**
	 * This class checks whether messages or notifications need to be sent to the client
	 * and delivers those concurrently to WorkerThread's servicing of client requests.
	 *
	 * @author Meng-Jung Lee & Jochen Stuber
	 * @version 2017-03-20
	 */
	private class HelperThread implements Runnable {
		/**
		 * Check task queues and service them until empty.
		 */
		@Override
		public void run() {
			// Loop until the WorkerThread is about to shut down
			while(!stopHelper) {
				// In each iteration, check for and handle interrupts
				if(Thread.interrupted()) {
					System.err.println("HelperThread interrupted. Shutting down...");
					return;
				}
				// Check for messages to be delivered to the client and deliver if yes
				if(!messageQueue.isEmpty()) {
					deliverMessageToConnectedUser(messageQueue.remove());
				}
				// Check if the online list is out of date and send updates if yes
				if(friendListTime.isBefore(server.getFriendListTime())) {
					sendOnlineList();
				}
				// Check if offline notifications remain to be delivered
				while(!offlineQueue.isEmpty()) {
					offlineNotification(offlineQueue.remove());
				}
				// Check if group chats have to be closed and inform client if yes
				while(!groupChatsToCloseQueue.isEmpty()) {
					closeGroupChatNotifyClient(groupChatsToCloseQueue.remove());
				}
			}
			System.err.println("HelperThread is shutting down...");
		}
		
	}
	
}
