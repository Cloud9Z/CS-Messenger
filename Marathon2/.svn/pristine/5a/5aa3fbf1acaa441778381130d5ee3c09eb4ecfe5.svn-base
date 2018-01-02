package message;

import java.util.List;
import java.util.Set;

/**
 * This class represents a protocol message of the application protocol.
 * 
 * @author Jochen Stuber
 * @version 2017-03-10
 */
public class ProtocolMessage {
	
	private String type;
	private String status;
	private String userName;
	private String password;
	private Message message;
	private Set<String> chat;
	private Set<String> onlineList;
	private List<Set<String>> historyList;
	private List<Message> history;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}
	/**
	 * @return the chat
	 */
	public Set<String> getChat() {
		return chat;
	}
	/**
	 * @param chat the chat to set
	 */
	public void setChat(Set<String> chat) {
		this.chat = chat;
	}
	/**
	 * @return the onlineList
	 */
	public Set<String> getOnlineList() {
		return onlineList;
	}
	/**
	 * @param onlineList the onlineList to set
	 */
	public void setOnlineList(Set<String> onlineList) {
		this.onlineList = onlineList;
	}
	/**
	 * @return the historyList
	 */
	public List<Set<String>> getHistoryList() {
		return historyList;
	}
	/**
	 * @param historyList the historyList to set
	 */
	public void setHistoryList(List<Set<String>> historyList) {
		this.historyList = historyList;
	}
	/**
	 * @return the history
	 */
	public List<Message> getHistory() {
		return history;
	}
	/**
	 * @param history the history to set
	 */
	public void setHistory(List<Message> history) {
		this.history = history;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Check if two ProtocolMessages are equal.
	 * @param otherMessage
	 * @return true if equal, else false
	 */
	@Override
	public boolean equals(Object otherMessage) {
		ProtocolMessage other = (ProtocolMessage) otherMessage;
		boolean result = true;
		
		// Compare all fields, check for null values
		if(result && (this.type == null || other.getType() == null)) {
		    result = this.type == other.getType();
		} else if (result) {
		    result = this.type.equals(other.getType());
		}  
		
		if(result && (this.status == null || other.getStatus() == null)) {
		    result = this.status == other.getStatus();
		} else if (result) {
		    result = this.status.equals(other.getStatus());
		}    

		if(result && (this.userName == null || other.getUserName() == null)) {
		    result = this.userName == other.getUserName();
		} else if (result) {
		    result = this.userName.equals(other.getUserName());
		}

		if(result && (this.password == null || other.getPassword() == null)) {
		    result = this.password == other.getPassword();
		} else if (result) {
		    result = this.password.equals(other.getPassword());
		}

		if(result && (this.message == null || other.getMessage() == null)) {
		    result = this.message == other.getMessage();
		} else if (result) {
		    result = this.message.equals(other.getMessage());
		}
		 
		if(result && (this.chat == null || other.getChat() == null)) {
		    result = this.chat == other.getChat();
		} else if (result) {
		    result = this.chat.equals(other.getChat());
		}

		if(result && (this.onlineList == null || other.getOnlineList() == null)) {
		    result = this.onlineList == other.getOnlineList();
		} else if (result) {
		    result = this.onlineList.equals(other.getOnlineList());
		}

		if(result && (this.historyList == null || other.getHistoryList() == null)) {
		    result = this.historyList == other.getHistoryList();
		} else if (result) {
		    result = this.historyList.equals(other.getHistoryList());
		}

		if(result && (this.history == null || other.getHistory() == null)) {
		    result = this.history == other.getHistory();
		} else if (result) {
		    result = this.history.equals(other.getHistory());
		}
		 
		return result;
	}
	
	
}
