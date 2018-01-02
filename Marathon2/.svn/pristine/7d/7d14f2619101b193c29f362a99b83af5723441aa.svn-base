package server.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import message.Message;

/**
 * This class specified the interface used by the server to access database-related functionality.
 *
 * @author All
 * @version 2017-03-07
 */
public interface DatabaseFunctions {
	
    /**
     * 1. Check in DB whether user already exists: return false if the user already exists 
     * 2. If the user does not yet exist, create a new record in the user table for the new user
     * 
     * @param userName
     * @param password
     * @return true if sucessful, else false
     * @throws ClassNotFoundException
     * @throws SQLException
     */ 
    public boolean signUp(String userName, String password) throws ClassNotFoundException, SQLException;
    
    /**
     * Checks in the DB whether there is a record with this username and password.
     * If yes, return true; else, return false 
     * @param userName
     * @param password
     * @return true if sucessful, else false
     * @throws ClassNotFoundException
     * @throws SQLException
     */ 
    public boolean signIn(String userName, String password) throws ClassNotFoundException, SQLException;
     
    /**
     * Get the list of users from the message.
     * Check if a chat exists already containing these users. 
     * If not, create a new chat.
     * Create in the mapping table between chats and users, a record for each user that maps it to the chat. 
     * In every case, save message in message table, linking it to the chat ID. 
     * 
     * @param message
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void saveMessage(Message message) throws ClassNotFoundException, SQLException;
    
    /**
     * Get the chat IDs of the chats that the user is part of.
     * For each of those chat IDs, get the list of all users that are part of the chat.
     * Return the list of the list of users for each chat .
     * 
     * @param userName
     * @return the history list
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Set<String>> getHistoryList(String userName) throws ClassNotFoundException, SQLException;
    
    /**
     * Get the chat ID of the chat that contains all the users in the user list .
     * From the message table, get all the messages for that chat ID.
     * 
     * @param users
     * @return the chat history
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Message> getHistory(Set<String> users) throws ClassNotFoundException, SQLException;
     
    /**
     * Saves a UserChatsMapping in the database. Only used internally, not called by server.
     * 
     * @param username
     * @param chatid
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void saveUCMapping(String username, String chatid) throws ClassNotFoundException, SQLException;
	
}
