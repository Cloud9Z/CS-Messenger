package server;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import message.Message;
import message.ProtocolMessage;

/**
 * This class is used by the server to read and write messages of the JSON-based application
 * protocol.
 *
 * @author Jochen Stuber
 * @version 2017-03-09
 */
public class ServerProtocol {
	
	// Formatter to format LocalDateTime
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
	
	/**
	 * This method takes in a JsonObject model and returns a String that contains its
	 * String representation.
	 * Precondition: model is not null.
	 * Postcondition: result is not null and not empty, unless an IOException occurs.
	 * 
	 * @param model the model to write out as a String
	 * @return the String representation of the model
	 */
	private String makeString(JsonObject model) {
		// Initialise result
		String result = "";
		// Write out the JsonObject to a String
		try(StringWriter strWriter = new StringWriter();
				JsonWriter jWriter = Json.createWriter(strWriter)) {
				jWriter.writeObject(model);
				result = strWriter.toString();
		// In case of error message, print error message and return the empty String
		} catch (IOException e) {
				System.err.println("Error while closing StringWriter.");
				e.printStackTrace();
		}
		System.err.println("Server sends: " + result);
		return result;
	}
	
	/**
	 * Create JSON message for sign-in.
	 * 
	 * @param status
	 * @return JSON message
	 */
	public String signIn(String status) {
		JsonObject model= Json.createObjectBuilder()
				.add("type", "sign-in")
				.add("status", status)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for sign-up.
	 * 
	 * @param status
	 * @return JSON message
	 */
	public String signUp(String status) {
		JsonObject model= Json.createObjectBuilder()
				.add("type", "sign-up")
				.add("status", status)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for sending a message.
	 * 
	 * @param message
	 * @return JSON message
	 */
	public String sendMessage(Message message) {
		JsonArrayBuilder chat = Json.createArrayBuilder();
		for(String userName : message.getChat()) {
			chat.add(userName);
		}
		JsonObject model= Json.createObjectBuilder()
				.add("type", "send-message")
				.add("sender", message.getSender())
				.add("chat", chat)
				.add("timeStamp", message.getTime().format(formatter))
				.add("text", message.getText())
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for getting the online list.
	 * 
	 * @param list
	 * @return JSON message
	 */
	public String getOnlineList(Set<String> list) {
		JsonArrayBuilder listArray = Json.createArrayBuilder();
		for(String userName : list) {
			listArray.add(userName);
		}
		JsonObject model= Json.createObjectBuilder()
				.add("type", "online-list")
				.add("list", listArray)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for closing a group chat.
	 * 
	 * @param chat
	 * @return JSON message
	 */
	public String closeGroupChat(Set<String> chat) {
		JsonArrayBuilder theChat = Json.createArrayBuilder();
		for(String userName : chat) {
			theChat.add(userName);
		}
		JsonObject model= Json.createObjectBuilder()
				.add("type", "close-group-chat")
				.add("chat", theChat)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for sending an offline notification.
	 * 
	 * @param userName
	 * @return JSON message
	 */
	public String offlineNotification(String userName) {
		JsonObject model= Json.createObjectBuilder()
				.add("type", "offline-notification")
				.add("userName", userName)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for getting the history list.
	 * 
	 * @param list
	 * @return JSON message
	 */
	public String getHistoryList(List<Set<String>> list) {
		JsonArrayBuilder listArray = Json.createArrayBuilder();
		for(Set<String> chat : list) {
			JsonArrayBuilder chatArray = Json.createArrayBuilder();
			for(String userName: chat) {
				chatArray.add(userName);
			}
			listArray.add(chatArray);
		}
		JsonObject model= Json.createObjectBuilder()
				.add("type", "get-history-list")
				.add("list", listArray)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create JSON message for getting a chat history.
	 * 
	 * @param hist
	 * @return JSON message
	 */
	public String getHistory(List<Message> hist) {
		JsonArrayBuilder histArray = Json.createArrayBuilder();
		for(Message message : hist) {
			JsonArrayBuilder chat = Json.createArrayBuilder();
			for(String userName : message.getChat()) {
				chat.add(userName);
			}
			JsonObject m = Json.createObjectBuilder()
					.add("sender", message.getSender())
					.add("chat", chat)
					.add("timeStamp", message.getTime().format(formatter))
					.add("text", message.getText())
					.build();
			histArray.add(m);
		}
		JsonObject model= Json.createObjectBuilder()
				.add("type", "get-history")
				.add("history", histArray)
				.build();
		return makeString(model);
	}
	
	/**
	 * Decode JSON into an instance of ProtocolMessage.
	 * 
	 * @param json the JSON message to parse
	 * @return the instance of ProtocolMessage
	 * @throws JsonException if the input is not well-formed JSON
	 */
	public ProtocolMessage decode(String json) throws JsonException {
		System.err.println("Server receives: " + json);
		ProtocolMessage pm = new ProtocolMessage();
		try(JsonReader reader = Json.createReader(new StringReader(json))) {
			JsonObject model = reader.readObject();
			String type = model.getString("type");
			pm.setType(type);
			// Distinguish message type and retrieve appropriate fields
			switch(type) {
			case "sign-up":
			case "sign-in":
				pm.setUserName(model.getString("userName"));
				pm.setPassword(model.getString("password"));
				break;
			case "send-message":
				pm.setMessage(textToMessage(model.getString("sender"), model.getJsonArray("chat"), 
						model.getString("timeStamp"), model.getString("text")));
				break;
			case "close-group-chat":
				pm.setChat(arrayToSet(model.getJsonArray("chat")));
				break;
			case "get-history-list":
				// Nothing to do here
				break;
			case "get-history":
				Set<String> chat = arrayToSet(model.getJsonArray("chat"));
				pm.setChat(chat);
				break;
			default:
				throw new JsonException("Received invalid message type from client.");
			}
		} catch(JsonException je) {
			throw new JsonException("Received invalid JSON from client.", je);
		}
		return pm;
	}
	
	/**
	 * Create a Set<String> from a JSON array.
	 * 
	 * @param array the JSON array to be converted
	 * @return the Set<String>
	 */
	private Set<String> arrayToSet(JsonArray array) {
		Set<String> set = new TreeSet<String>();
		for(JsonValue value : array) {
			JsonString valueString = (JsonString) value;
			set.add(valueString.getString());
		}
		return set;
	}
	
	/**
	 * Create Message instance from JSON representation.
	 * 
	 * @param sender
	 * @param chat
	 * @param time
	 * @param text
	 * @return the Message instance
	 */
	private Message textToMessage(String sender, JsonArray chat, String time, String text) {
		Set<String> theChat = arrayToSet(chat);
		LocalDateTime theTime = LocalDateTime.parse(time, formatter);
		return new Message(sender, theChat, theTime, text);
	}
	
}
