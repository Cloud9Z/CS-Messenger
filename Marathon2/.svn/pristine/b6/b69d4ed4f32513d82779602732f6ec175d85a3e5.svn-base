package client;
import java.util.List;
import java.util.Set;

import message.Message;

/**
 * Interface for the Messenger client, defining methods called by the GUI.
 *
 * @author All
 * @version 2017-03-20
 */
public interface ClientInterface {
	
	/**
	 * Tries to create an account.
	 * Precondition: userName and password are not null and not empty.
	 * 
	 * @param userName
	 * @param password
	 */
	public void createAccount(String userName, String password);
	
	/**
	 * Tries to log a user in.
	 * Precondition: userName and password are not null and not empty.
	 * 
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password);
	
	/**
	 * Precondition: The user is logged in and the client has received an online list from the server.
	 * Postcondition: If no user is online, the empty set is returned, else the set of online users is returned.
	 * 
	 * @return the online list
	 */
	public Set<String> getOnlineList();
	
	/**
	 * Sends a message.
	 * 
	 * @param message
	 */
	public void sendMessage(Message message);
	
	/**
	 * Precondition: Client has informed GUI that new message has arrived
	 * 
	 * @return the next message
	 */
	public Message getNextMessage();
	
	/**
	 * Requests the history list.
	 */
	public void requestHistoryList();
	
	/**
	 * Returns the history list.
	 * 
	 * @return
	 */
	public List<Set<String>> getHistoryList();
	
	/**
	 * Requests a history.
	 * 
	 * @param users
	 */
	public void requestHistory(Set<String> users);
	
	/**
	 * Returns the next history to be displayed.
	 * 
	 * @return
	 */
	public List<Message> getNextHistory();
	
	/**
	 * This method informs the client that the group chat containing the specified users
	 * has been closed. The client sends a message to the server so that all other users
	 * in the group chat are also informed and close the group chat.
	 * 
	 * @param users the users that are part of the group chat
	 */
	public void closeGroupChat(Set<String> users);
	
	/**
	 * Returns a set of user names identifying a group that that needs to be closed as a user
	 * has left it. Such notifications are queued in the Server and this method retrieves the
	 * next notification to be processed.
	 * 
	 * @return the 
	 */
	public Set<String> nextGroupChatToClose();
	
	/**
	 * Returns the next offline notification received from the server. An offline notification
	 * refers to the user name of a user who has gone offline. Such notifications are queued in 
	 * the Server and this method retrieves the next notification to be processed.
	 * 
	 * @return the user name of the user who has gone offline
	 */
	public String nextOfflineNotification();
	
	/**
	 * Sends a notification to the server that the user wants to log out.
	 */
	public void logOut();
	
}



