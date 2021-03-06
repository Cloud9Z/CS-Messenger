package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import message.Message;
import server.db.DatabaseConnector;
import server.db.DatabaseFunctions;

/**
 * This class represents the server of the Messenger application.
 *
 * @author Meng-Jung Lee & Jochen Stuber
 * @version 2017-03-20
 */
public class Server {
	
	private final ServerSocket serverSocket; // server-side socket
	private final ExecutorService pool; // thread pool
	private final Set<String> onlineList; // list of online users
	// Mapping of user names to outgoing message queues for inter-thread communication
	private final Map<String, Queue<Message>> messageQueues; 
	// Set of offline notification queues for inter-thread communication
	private final Set<Queue<String>> offlineQueues;
	// Mapping of user names to offline notification queues for inter-thread communication
	private final Map<String, Queue<Set<String>>> groupChatsToCloseQueues;
	// Object for database-related functionality
	private DatabaseFunctions databaseFunctions = new DatabaseConnector();
	private LocalDateTime friendListTime;
	
	/**
	 * Constructs a new instance of Server, initialising its data structures.
	 * 
	 * @param port the port on which the Server shall listen
	 * @throws IOException if the ServerSocket cannot listen on the specified port
	 */
	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.pool = Executors.newCachedThreadPool(); 
		this.onlineList = ConcurrentHashMap.newKeySet();
		this.messageQueues = new ConcurrentHashMap<String, Queue<Message>>();
		this.offlineQueues = ConcurrentHashMap.newKeySet();
		this.groupChatsToCloseQueues = new ConcurrentHashMap<String, Queue<Set<String>>>();
	}
	
	/**
	 * Starts the server so that it listens for incoming connections.
	 */
	public void start() {
		try	{
			// Accept client connections and start a new thread for each client
			System.err.println("Server is now listening...");
			while(true) {
				Socket s = serverSocket.accept();	
				Runnable r = new WorkerThread(this, s);
				// Submit thread to thread pool
				pool.submit(r);
			}
		} catch (IOException ioe) {
			// In case of exception thrown from "accept", log error
			System.err.println("Server is no longer listening...");;
		} finally {
			// Finally, always stop the server
			stop();
		}
	}
	
	/**
	 * Stops the server by shutting down the thread pool and closing the server socket.
	 */
	public void stop() {
		// As the method may be called when the pool has already shut down, check whether
		// resources are already shut down before shutting them down.
		if(!serverSocket.isClosed()) {
			try {
				// Close server socket
				System.err.println("Closing server socket...");
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!pool.isShutdown()){
			// Shut down thread pool
			System.err.println("Server is shutting down thread pool...");
			pool.shutdownNow();
		}
	}
	
	/**
	 * Get the online list
	 * 
	 * @return the online list
	 */
	public Set<String> getOnlineList(){
		return onlineList;
	}
	
	/**
	 * Adds a user to the online list.
	 * 
	 * @param userName the user name of the user to be added to the online list
	 */
	public void addToOnlineList(String userName) {
		onlineList.add(userName);
		friendListTime = LocalDateTime.now(); 
		System.err.println("Online list changed at " + friendListTime + ": " + onlineList);
	}
	
	/**
	 * Removes a user from the online list and adds the offline notification to each
	 * thread's offline notification queue.
	 * 
	 * @param userName the user name of the user to be removes from the online list
	 */
	public void removeFromOnlineList(String userName) {
		onlineList.remove(userName);
		friendListTime = LocalDateTime.now(); 
		addOfflineNotification(userName);
		System.err.println("Online list changed at " + friendListTime + ": " + onlineList);
	}
	
	/**
	 * Get the time at which the online list was last modified.
	 * 
	 * @return the time at which the online list was last modified
	 */
	public LocalDateTime getFriendListTime(){
		return friendListTime;
	}
	
	/**
	 * Checks whether a user name is contained in the online list.
	 * 
	 * @param userName the user name for which the online status shall be checked
	 */
	public boolean isOnline(String userName) {
		return onlineList.contains(userName);
	}
	
	/**
	 * This method is called by WorkerThreads to register their own message queue with
	 * the server. Each thread has a message queue that contains all messages to be delivered
	 * by the thread to the client it is connected with. Each queue is registered with the
	 * server so that other threads can place messages into it for delivery. This is a 
	 * mechanism of information sharing between the threads.
	 * 
	 * @param userName the userName of the user to whom messages in the queue will be sent
	 * @param messageQueue the queue of messages that should be delivered to the user
	 */
	public void registerMessageQueue(String userName, Queue<Message> messageQueue) {
		// Place the queue in the Map of user names to message queues
		this.messageQueues.put(userName, messageQueue);
	}
	
	/**
	 * This method is called by threads to register their offline queue with the server. 
	 * Once registered, other threads can insert offline notifications into the thread's
	 * queue so that the thread can inform the client when users have gone offline.
	 * 
	 * @param offlineQueue the thread's offlineQueue to register with the server
	 */
	public void registerOfflineQueue(Queue<String> offlineQueue) {
		this.offlineQueues.add(offlineQueue);
	}
	
	/**
	 * This method is called by WorkerThreads to register their own chat closing queue with
	 * the server. Each thread has a queue that contains all group chats to be closed by 
	 * to the client it is connected with. Each queue is registered with the
	 * server so that other threads can place notifications into it for delivery. This is a 
	 * mechanism of information sharing between the threads.
	 * 
	 * @param userName the userName of the user to whom notifications in the queue will be sent
	 * @param messageQueue the queue of notifications for group chats to be closed
	 */
	public void registerChatsToCloseQueue(String userName, Queue<Set<String>> queue) {
		// Place the queue in the Map of user names to queues
		this.groupChatsToCloseQueues.put(userName, queue);
	}
	
	/**
	 * This method is used by threads to place messages in the outgoing queues of their
	 * recipients. For each member of the chat that the message was sent in (except the sender)
	 * the message is placed in the corresponding outgoing message queue.
	 * 
	 * @param message the message to be placed in its recipients' outoing message queue
	 */
	public void addMessageToOutgoingQueues(Message message) {
		Queue<Message> outGoingQueue;
		// Get the sender of the message
		String sender = message.getSender();
		// Loop over the members of the chat that the message was posted in
		for(String userName : message.getChat()) {
			// Messages should not be delivered to their own sender
			if(!userName.equals(sender)) {
				// Retrieve message queue for the recipient
				outGoingQueue = messageQueues.get(userName);
				// In case of error, print to std.err, else add message to recipient's queue
				if(outGoingQueue == null) {
					System.err.println("Could not find a message queue for " + userName);
				} else {
					outGoingQueue.add(message);
				}
			}
		}
	}
	
	/**
	 * This method is used by threads to place messages in the outgoing queues of their
	 * recipients. For each member of the chat that the message was sent in (except the sender)
	 * the message is placed in the corresponding outgoing message queue.
	 * 
	 * @param userWhoQuit the user who has left the group chat
	 * @param chat the group chat that needs to be closed
	 */
	public void addGroupChatClosingNotification(String userWhoQuit, Set<String> chat) {
		Queue<Set<String>> outGoingQueue;
		// Loop over the members of the chat that needs to be closed
		for(String userName : chat) {
			// Notifications should not be delivered to the person who has left the chat
			if(!userName.equals(userWhoQuit)) {
				// Retrieve queue for the current user
				outGoingQueue = groupChatsToCloseQueues.get(userName);
				// In case of error, print to std.err, else add message to recipient's queue
				if(outGoingQueue == null) {
					System.err.println("Could not find a message queue for " + userName);
				} else {
					outGoingQueue.add(chat);
				}
			}
		}
	}
	
	/**
	 * After a user has gone offline, this method places a notification about that in the
	 * offline notification queue of each thread, so that the threads can notify the users
	 * that they are serving appropriately.
	 * 
	 * @param userName the user of whose going offline other threads shall be notified
	 */
	public void addOfflineNotification(String userName) {
		for(Queue<String> queue: offlineQueues) {
			queue.add(userName);
		}
	}
	
	/**
	 * This method submits a Runnable object to the thread pool. It is called
	 * by WorkerThread to submit HelperThread.
	 * 
	 * @param r the Runnable to be submitted to the thread pool
	 */
	public void submitToThreadPool(Runnable r) {
		if(!pool.isShutdown()) {
			pool.submit(r);
		}
	}
	
	/**
	 * Unregisters a queue from the server when a user goes offline.
	 * 
	 * @param userName the user whose queue shall be unregistered
	 */
	public void unregisterMessageQueue(String userName) {
		messageQueues.remove(userName);
	}
	
	/**
	 * Unregisters a queue from the server when a user goes offline.
	 * 
	 * @param userName the user whose queue shall be unregistered
	 */
	public void unregisterOfflineQueue(Queue<String> queue) {
		offlineQueues.remove(queue);
	}
	
	/**
	 * Unregisters a queue from the server when a user goes offline.
	 * 
	 * @param userName the user whose queue shall be unregistered
	 */
	public void unregisterGroupChatsToCloseQueue(String userName) {
		groupChatsToCloseQueues.remove(userName);
	}

	/**
	 * @return the databaseFunctions
	 */
	public DatabaseFunctions getDatabaseFunctions() {
		return databaseFunctions;
	}

	/**
	 * @param databaseFunctions the databaseFunctions to set
	 */
	public void setDatabaseFunctions(DatabaseFunctions databaseFunctions) {
		this.databaseFunctions = databaseFunctions;
	}
	
	/**
	 * This main method accepts a port on the command line and instantiates a server listening
	 * on the specified port.
	 * 
	 * @param args the port at which the server should listen
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: java -jar server.jar <port>");
			System.exit(-1);
		} 
		// Parse port
		int port = -1;
		try {
			port = Integer.parseInt(args[0]);
		} catch(NumberFormatException nfe) {
			System.out.println("Port has to be a positive integer.");
			System.exit(-1);
		}
		// Create and start server
		Server server = null;
		try {
			server = new Server(port);
			server.start();
		} catch (Exception e) {
			System.out.println("Oops, something went wrong. Shutting down...");
		} finally {
			// Once done, stop the server in any case
			if (server != null) {
				server.stop();
			}
		}
		
	}

}
