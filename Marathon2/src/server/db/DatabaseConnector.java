package server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import message.Message;

/**
 * Implements the DatabaseFunctions interface used by the server to access database.
 * 
 * @author Ruoyu He
 * @version 2017-03-20
 */
public class DatabaseConnector implements DatabaseFunctions {
	
	/** sign up
	 * Check in DB whether user already exists: return false if the user already exists
	 * If the user does not yet exist, create a new record in the user table for the new user
	 * @throws SQLException if a database error occurs
	 * @throws ClassNotFoundException if the driver class cannot be loaded
	 */
    @Override
    public boolean signUp(String userName, String password) throws ClassNotFoundException, SQLException {
        Connection conn = SQLSever.getConnection();
        String sql = "insert into Users(User_id,Username,Password) values(?,?,?)";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        //System creates an user_id
        String userid = "USERID"
                        + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                        + Math.abs(new Random().nextInt(1000));
        try {
	        rs = conn.prepareStatement(
	                        "select * from Users where Username='" + userName + "'")
	                        .executeQuery();// check whether the userName has already existed in the DB
	        if (!rs.next()) {
	                pstmt = (PreparedStatement) conn.prepareStatement(sql);
	                pstmt.setString(1, userid);
	                pstmt.setString(2, userName);
	                pstmt.setString(3, password);
	                pstmt.executeUpdate();
	                pstmt.close();
	                conn.close();
	        } else {
	        	return false;
	        }
        } catch (SQLException e) {
                e.printStackTrace();
                return false;
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
        return true;
    }

	/** Sign in
	 * Checks in the DB whether there is a record with this username and password
	 * If yes, return true; else, return false
	 * throws SQLException if a database error occurs
	 * @throws ClassNotFoundException if the driver class cannot be loaded
	 */
    @Override
    public boolean signIn(String userName, String password) throws ClassNotFoundException, SQLException {
        Connection conn = SQLSever.getConnection();
        String sql = "select * from Users where Username='" + userName
                        + "' and Password='" + password + "'";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                    System.err.println("Database: Password or ID is incorrect...");
                    return false;
            } else {
                    System.err.println("Database: Sign in successful...");
                    return true;
            }
        } catch (SQLException e) {
                e.printStackTrace();
                return false;
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
    }

    /** Get the list of users from the message
    * Check if a chat exists already containing these users
    * If not, create a new chat in the chat table
    * Create in the mapping table between chats and users, a record for each user that maps it to the chat
    * In every case, save message in message table, linking it to the chat ID
    * throws SQLException if a database error occurs
	* @throws ClassNotFoundException if the driver class cannot be loaded 
    */
    @Override
    public void saveMessage(Message message) throws ClassNotFoundException, SQLException {
        Connection conn = SQLSever.getConnection();
        String msgsql = "insert into Message(Message_id,from_id,chat_id,Content,Datetime) values(?,?,?,?,?)";
        PreparedStatement msgpstmt = null;
        //System creates a message id
        String messageid = "MSG"
                        + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//

        Set<String> chat = message.getChat();//get the chat which message belongs to
        
        String chatid = SQLSever.findChatIDByUserList(chat);// chat_id = chat containing exactly those users
        if(chatid.equals("")) {
	    	//System creates an user_id
	        chatid = "CHATID"
	                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
	                + Math.abs(new Random().nextInt(1000));
	        for(String user : message.getChat()) {
	        	saveUCMapping(user, chatid);
	        }
        }
        try {
            msgpstmt = (PreparedStatement) conn.prepareStatement(msgsql);
            msgpstmt.setString(1, messageid);
            msgpstmt.setString(2, message.getSender());
            msgpstmt.setString(3, chatid);
            msgpstmt.setString(4, message.getText());
            msgpstmt.setTimestamp(5, Timestamp.valueOf(message.getTime()));
            msgpstmt.executeUpdate();
            msgpstmt.close();
            conn.close();
        } catch (SQLException e) {
                e.printStackTrace();
        } finally {
        	try {msgpstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
    }

    /** Get the chat IDs of the chats that the user is part of
     * For each of those chat IDs, get the list of all users that are part of the chat
     * Return the list of the list of users for each chat
     * throws SQLException if a database error occurs
	 * @throws ClassNotFoundException if the driver class cannot be loaded
     */
    @Override
    public List<Set<String>> getHistoryList(String userName) throws ClassNotFoundException, SQLException {
      	// 首先要找到当前用户打开的所有chat
        Set<String> chatset = SQLSever.getChatListByUser(userName);
        // 再根据chat找到其下面的所有用户
        List<Set<String>> list = new ArrayList<Set<String>>();
        for (String chatid : chatset) {
                Set<String> users = SQLSever.getUsersByChat(chatid);
                list.add(users);
        }
        return list;
    }

    /**
     * Get the chat ID of the chat that contains all the users in the user list
     * From the message table, get all the messages for that chat ID
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    @Override
    public List<Message> getHistory(Set<String> users) throws ClassNotFoundException, SQLException {
    	String chatid = SQLSever.findChatIDByUserList(users);
        List<Message> msglist = new ArrayList<Message>();
        if(chatid.equals("")) {
        	System.err.println("Database could not find chat ID for chat: " + users.toString());
        	return msglist;
        }
        Connection conn = SQLSever.getConnection();
        String sql = "select * from Message where chat_id='" + chatid + "'";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String from_id = rs.getString("from_id").toString();
                String Content = rs.getString("Content").toString();
                //long time = rs.getTimestamp("Datetime").getTime();
                LocalDateTime Timestamp = rs.getTimestamp(5).toLocalDateTime();
                Message msg = new Message(from_id, users, Timestamp, Content);
                msglist.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
        return msglist;
    }

    /**
     * the method is to store user id and chat id in a map 
     * throws SQLException if a database error occurs
	 * @throws ClassNotFoundException if the driver class cannot be loaded 
     */
    @Override
    public void saveUCMapping(String username, String chatid) throws ClassNotFoundException, SQLException {
        Connection conn = SQLSever.getConnection();
        String sql = "insert into UsersChatsMapping(UserChatsMapping_id,User_id,chat_id) values(?,?,?)";
        PreparedStatement pstmt = null;
        String userChatsMapping_id = "UCP"
                        + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                        + Math.abs(new Random().nextInt(1000));
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, userChatsMapping_id);
            pstmt.setString(2, SQLSever.getIdByUserName(username));
            pstmt.setString(3, chatid);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
    }
}
