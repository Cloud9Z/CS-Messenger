package message;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * This class represents a message.
 *
 * @author Jochen Stuber
 * @version 2017-03-07
 */
public class Message {

	private final String sender; // sender of the message
	private final Set<String> chat; // chat to which the method has been posted
	private final LocalDateTime time; // date and time when message was sent 
	private final String text; // message text
	
	/**
	 * This constructor creates a new instance of type Message.
	 * Precondition: sender, chat, time, and text are non-null and non-empty.
	 * 
	 * @param sender the message's sender
	 * @param chat the chat to which the message was posted
	 * @param time the date and time at which the message was sent
	 * @param text the message text
	 */
	public Message(String sender, Set<String> chat, LocalDateTime time, String text) {
		this.sender = sender;
		this.chat = chat;
		this.time = time;
		this.text = text;
	}
	
	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * @return the chat
	 */
	public Set<String> getChat() {
		return chat;
	}
	
	/**
	 * @return the hour and minute of the time as a String (format "hh:mm")
	 */
	public String getTimeString() {
		return String.format("%02d:%02d", time.getHour(), time.getMinute());
	}
	
	/**
	 * 
	 * @return the time
	 */
	public LocalDateTime getTime() {
		return time;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Constructs a String representation of the message.
	 * 
	 * @returns a String representation of the message
	 */
	@Override
	public String toString() {
		return String.format("%s (%s): %s", sender, getTimeString(), text);
	}
	
	/**
	 * Check whether two messages are equal
	 * 
	 * @param otherMessage
	 * @return true if equal, else false
	 */
	@Override
	public boolean equals(Object otherMessage) {
		Message other = (Message) otherMessage;
		return this.sender.equals(other.getSender()) &&
				this.chat.equals(other.getChat()) &&
				this.time.equals(other.getTime()) &&
				this.text.equals(other.getText());
	}
	
}
