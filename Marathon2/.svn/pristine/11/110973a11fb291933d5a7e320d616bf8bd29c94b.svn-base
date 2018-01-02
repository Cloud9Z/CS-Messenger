package server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains helper functions to fulfill database-related functionality for the server.
 * 
 * @author Ruoyu He
 * @version 08/03/2017
 */
public class SQLSever {

    public static String driver = "org.postgresql.Driver";
    public static String url = "jdbc:postgresql://mod-fund-databases.cs.bham.ac.uk:5432/rxh657";
    public static String user = "rxh657";
    public static String pass = "34cgl8wgt2";
  
    /**
     * Get database connection.
     * 
     * @return a connection
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }
    
    /**
     * Create base tables.
     * 
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void createBaseTables() throws ClassNotFoundException, SQLException {
		Connection conn = SQLSever.getConnection();
		Statement stmt = null;
        try {
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users(User_id varchar(50) PRIMARY KEY, Username varchar(50), Password varchar(50));");
            System.out.println("Users created successfully");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Message(Message_id varchar(50) PRIMARY KEY, from_id varchar(50), chat_id varchar(50), Content TEXT, Datetime timestamp );");
            System.out.println("Message created successfully");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS UsersChatsMapping(UserChatsMapping_id varchar(50) PRIMARY KEY, User_id varchar(50), chat_id varchar(50));");
            System.out.println("UsersChatsMapping created successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    /**
     * get the chat id from a message
     * chat id = intersection part of chat lists from a list of users
     */
    public static String findIntersection(List<Set<String>> chatslist) {
        String findchatid = "";
        Set<String> firstchatlist = chatslist.get(0);
        for (String chatid : firstchatlist) {
            boolean havethisid = true;
            for (Set<String> chatlist : chatslist) {
                    if (!ListHavetthisId(chatid, chatlist)) {
                            havethisid = false;
                            break;
                    }
            }
            if (havethisid) {
                    findchatid = chatid;
                    break;
            }
        }
        return findchatid;
    }
    
    /**
     * Finds chat id based on user name list.
     * 
     * @param users chat members
     * @return chat id
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static String findChatIDByUserList(Set<String> users) throws ClassNotFoundException, SQLException {
    	String oneUser = users.iterator().next();
    	Set<String> chatIDsOfUser = SQLSever.getChatListByUser(oneUser);
    	String result = "";
    	Set<String> compareUserNames;
    	for(String id : chatIDsOfUser) {
    		compareUserNames = getUsersByChat(id);
			if(compareUserNames.equals(users)) {
				result = id;
				break;
			}
    	}
    	return result;
    }

    /**
     * check whether chat id has exisited in the chat set or not
     */
    public static boolean ListHavetthisId(String chatid, Set<String> chatset) {
        boolean haveone = false;
        for (String myid : chatset) {
            if (myid.equals(chatid))
                haveone = true;
        }
        return haveone;
    }
    
    /**
     * Get id for a user name.
     * 
     * @param username
     * @return the id
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static String getIdByUserName(String username) throws ClassNotFoundException, SQLException {
        String userid = "";
        Connection conn = SQLSever.getConnection();
        String sql = "select * from Users where Username='" + username + "'";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                    userid = rs.getString("User_id").toString();
            }
        } catch (SQLException e) {
                e.printStackTrace();
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
        return userid;
    }
    
    /**
     * Get user name for an id.
     * 
     * @param userid
     * @return the user name
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static String getNameByUserId(String userid) throws ClassNotFoundException, SQLException {
        String username = "";
        Connection conn = SQLSever.getConnection();
        String sql = "select * from Users where User_id='" + userid + "'";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                    username = rs.getString("Username").toString();
            }
        } catch (SQLException e) {
                e.printStackTrace();
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
        return username;
    }

    /**
     * get all the chats of a user by its username
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public static Set<String> getChatListByUser(String username) throws ClassNotFoundException, SQLException {
        String userid = getIdByUserName(username);
        Set<String> chatlist = new HashSet<String>();
        Connection conn = SQLSever.getConnection();
        String sql = "select * from UsersChatsMapping where User_id='" + userid
                        + "'";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	String chatid = rs.getString("chat_id").toString();
                    chatlist.add(chatid);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
        return chatlist;
    }

    /**
     * get all usernames of a chat by its chat id
     * @throws SQLException 
     * @throws ClassNotFoundException 
     */
    public static Set<String> getUsersByChat(String chatid) throws ClassNotFoundException, SQLException {

        Set<String> usernamelist = new HashSet<String>();
        Connection conn = SQLSever.getConnection();
        String sql = "select * from UsersChatsMapping where chat_id='" + chatid
                        + "'";
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String userid = rs.getString("User_id").toString();
                String username = getNameByUserId(userid);
                usernamelist.add(username);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        } finally {
        	try {rs.close();} catch(Exception e) { /* ignore */ };
        	try {pstmt.close();} catch(Exception e) { /* ignore */ };
        	try {conn.close();} catch(Exception e) { /* ignore */ };
        }
        return usernamelist;
    }

}
