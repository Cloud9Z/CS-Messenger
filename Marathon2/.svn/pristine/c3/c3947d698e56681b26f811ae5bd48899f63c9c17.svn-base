package client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
 * Protocol used by the client to read and write messages of the JSON-based application
 * protocol.
 *
 * @author Jochen Stuber
 * @version 2017-03-09
 */
public class ClientProtocol {
	
	// Formatter for date and time
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
	
	/**
	 * This method takes in a JsonObject model and returns a String that contains its
	 * String representation.
	 * Precondition: model is not null.
	 * Postcondition: result is not null and not empty, unless an IOException occurs.
	 * 
	 * @param model the model to write out as a String
	 * @return the String representation of the model
	 */
	private static String makeString(JsonObject model) {
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
		System.err.println("Client sends: " + result);
		return result;
	}
	
	/**
	 * Create protocol message for sign-up.
	 * 
	 * @param userName
	 * @param password
	 * @return JSON message
	 */
	public static String signUp(String userName, String password) {
		JsonObject model= Json.createObjectBuilder()
				.add("type", "sign-up")
				.add("userName", userName)
				.add("password", password)
				.build();
		return makeString(model);
		
	}
	
	/**
	 * Create protocol message for sign-in.
	 * 
	 * @param userName
	 * @param password
	 * @return JSON message
	 */
	public static String signIn(String userName, String password) {
		JsonObject model= Json.createObjectBuilder()
				.add("type", "sign-in")
				.add("userName", userName)
				.add("password", password)
				.build();
		return makeString(model);
	}
	
	/**
	 * Create protocol message for sending a message.
	 * 
	 * @param message
	 * @return JSON message
	 */
	public static String sendMessage(Message message) {
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
	 * Create protocol message for gettng the history list.
	 * 
	 * @return JSON message
	 */
	public static String getHistoryList() {
		JsonObject model= Json.createObjectBuilder()
				.add("type", "get-history-list")
				.build();
		return makeString(model);
	}
	
	/**
	 * Create protocol message for closing a group chat.
	 * 
	 * @param chat the chat to be closed
	 * @return JSON message
	 */
	public static String closeGroupChat(Set<String> chat) {
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
	 * Create protocol message for getting a chat history.
	 * 
	 * @param chat chat for which history shall be retrieved
	 * @return JSON message
	 */
	public static String getHistory(Set<String> chat) {
		JsonArrayBuilder chatArray = Json.createArrayBuilder();
		for(String userName : chat) {
			chatArray.add(userName);
		}
		JsonObject model= Json.createObjectBuilder()
				.add("type", "get-history")
				.add("chat", chatArray)
				.build();
		return makeString(model);
	}
	
	/**
	 * Decodes a JSON message into an instance of ProtocolMessage.
	 * 
	 * @param json the JSON to be parsed
	 * @return the instance of ProtocolMessage
	 */
	public static ProtocolMessage decode(String json) {
		System.err.println("Client receives: " + json);
		ProtocolMessage pm = new ProtocolMessage();
		try(JsonReader reader = Json.createReader(new StringReader(json))) {
			JsonObject model = reader.readObject();
			String type = model.getString("type");
			pm.setType(type);
			// Distinguish protocol type and retrieve corresponding data fields
			switch(type) {
			case "sign-up":
			case "sign-in":
				pm.setStatus(model.getString("status"));
				break;
			case "send-message":
				pm.setMessage(textToMessage(model.getString("sender"), model.getJsonArray("chat"), 
						model.getString("timeStamp"), model.getString("text")));
				break;
			case "offline-notification":
				pm.setUserName(model.getString("userName"));
				break;
			case "close-group-chat":
				pm.setChat(arrayToSet(model.getJsonArray("chat")));
				break;
			case "online-list":
				pm.setOnlineList(arrayToSet(model.getJsonArray("list")));
				break;
			case "get-history-list":
				List<Set<String>> list = new ArrayList<Set<String>>();
				for(JsonValue chat : model.getJsonArray("list")) {
					list.add(arrayToSet((JsonArray) chat));
				}
				pm.setHistoryList(list);
				break;
			case "get-history":
				List<Message> hist = new ArrayList<Message>();
				for(JsonValue message : model.getJsonArray("history")) {
					JsonObject m = (JsonObject) message;
					hist.add(textToMessage(m.getString("sender"), m.getJsonArray("chat"),
							m.getString("timeStamp"), m.getString("text")));
				}
				pm.setHistory(hist);
				break;
			default:
				System.err.println("ClientProtocol received unkown message type for decoding.");
			}
		} catch(JsonException je) {
			System.err.println("Error parsing Json.");
			je.printStackTrace();
		}
		return pm;
	}
	
	/**
	 * Create a JSON array into a Set<String>
	 * 
	 * @param array the JSON array to be converted
	 * @return the Set<String>
	 */
	private static Set<String> arrayToSet(JsonArray array) {
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
	private static Message textToMessage(String sender, JsonArray chat, String time, String text) {
		Set<String> theChat = arrayToSet(chat);
		LocalDateTime theTime = LocalDateTime.parse(time, formatter);
		return new Message(sender, theChat, theTime, text);
	}
	
}
