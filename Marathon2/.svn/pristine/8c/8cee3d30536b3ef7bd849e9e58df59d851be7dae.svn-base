package test.client;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import client.ClientProtocol;
import message.Message;
import message.ProtocolMessage;

/**
 * This test case contains tests for class ClientProtocolTest.
 * 
 * @author Jochen Stuber
 * @version 2017-03-09
 */
public class ClientProtocolTest {

	@Test
	public void testSignIn() {
		String userName = "odysseus";
		String password = "telemachus";
		String expected = "{\"type\":\"sign-in\",\"userName\":\"odysseus\",\"password\":\"telemachus\"}";
		String actual = ClientProtocol.signIn(userName, password);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testSignUp() {
		String userName = "odysseus";
		String password = "telemachus";
		String expected = "{\"type\":\"sign-up\",\"userName\":\"odysseus\",\"password\":\"telemachus\"}";
		String actual = ClientProtocol.signUp(userName, password);
		assertEquals(expected, actual);
	}
	
	@Test 
	public void testSendMessage() {
		List<String> l = Arrays.asList(new String[]{"achilles", "menelaos", "nestor"});
		Set<String> chat = new TreeSet<String>(l);
		LocalDateTime t = LocalDateTime.of(2017, 4, 29, 23, 45);
		Message message = new Message("achilles", chat, t, "Off to Troy!");
		String expected = "{\"type\":\"send-message\",\"sender\":\"achilles\",\"chat\":[\"achilles\",\"menelaos\",\"nestor\"],\"timeStamp\":\"2017-04-29T23:45\",\"text\":\"Off to Troy!\"}";
		String actual = ClientProtocol.sendMessage(message);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCloseGroupChat() {
		List<String> l = Arrays.asList(new String[]{"achilles", "menelaos", "nestor"});
		Set<String> chat = new TreeSet<String>(l);
		String expected = "{\"type\":\"close-group-chat\",\"chat\":[\"achilles\",\"menelaos\",\"nestor\"]}";
		String actual = ClientProtocol.closeGroupChat(chat);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGetHistoryList() {
		String expected = "{\"type\":\"get-history-list\"}";
		String actual = ClientProtocol.getHistoryList();
		assertEquals(expected, actual);
	}
	
	@Test 
	public void testGetHistory() {
		List<String> l = Arrays.asList(new String[]{"achilles", "patroklos"});
		Set<String> chat = new TreeSet<String>(l);
		String expected = "{\"type\":\"get-history\",\"chat\":[\"achilles\",\"patroklos\"]}";
		String actual = ClientProtocol.getHistory(chat);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeSignIn() {
		String json = "{\"type\":\"sign-in\",\"status\":\"Login:1\"}";
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("sign-in");
		expected.setStatus("Login:1");
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeSignUp() {
		String json = "{\"type\":\"sign-up\",\"status\":\"Account:1\"}";
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("sign-up");
		expected.setStatus("Account:1");
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeSendMessage() {
		String json = "{\"type\":\"send-message\",\"sender\":\"achilles\",\"chat\":[\"achilles\",\"menelaos\",\"nestor\"],\"timeStamp\":\"2017-04-29T23:45\",\"text\":\"Off to Troy!\"}";
		List<String> l = Arrays.asList(new String[]{"achilles", "menelaos", "nestor"});
		Set<String> chat = new TreeSet<String>(l);
		LocalDateTime t = LocalDateTime.of(2017, 4, 29, 23, 45);
		Message m = new Message("achilles", chat, t, "Off to Troy!");
		
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("send-message");
		expected.setMessage(m);
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeCloseGroupChat() {
		String json = "{\"type\":\"close-group-chat\",\"chat\":[\"achilles\",\"menelaos\",\"nestor\"]}";
		Set<String> chat = new TreeSet<String>(Arrays.asList(new String[]{"achilles","menelaos","nestor"}));

		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("close-group-chat");
		expected.setChat(chat);
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeGetHistoryList() {
		String json = "{\"type\":\"get-history-list\",\"list\":[[\"achilles\",\"helen\"],[\"nestor\",\"helen\",\"muse\"]]}";
		List<Set<String>> hist = new ArrayList<Set<String>>();
		Set<String> chat = new TreeSet<String>(Arrays.asList(new String[]{"achilles","helen"}));
		hist.add(chat);
		chat = new TreeSet<String>(Arrays.asList(new String[]{"nestor","helen","muse"}));
		hist.add(chat);
		
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("get-history-list");
		expected.setHistoryList(hist);
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test 
	public void testDecodeGetHistory() {
		String json = "{\"type\":\"get-history\",\"history\":[{\"sender\":\"achilles\",\"chat\":[\"achilles\",\"menelaos\",\"nestor\"],\"timeStamp\":\"2017-04-29T23:45\",\"text\":\"Off to Troy!\"},{\"sender\":\"nestor\",\"chat\":[\"achilles\",\"menelaos\",\"nestor\"],\"timeStamp\":\"2017-04-29T23:50\",\"text\":\"Sack Priam's city!\"}]}";
		List<Message> hist = new ArrayList<Message>();
		List<String> l = Arrays.asList(new String[]{"achilles", "menelaos", "nestor"});
		Set<String> chat = new TreeSet<String>(l);
		LocalDateTime t = LocalDateTime.of(2017, 4, 29, 23, 45);
		Message m = new Message("achilles", chat, t, "Off to Troy!");
		hist.add(m);
		t = LocalDateTime.of(2017, 4, 29, 23, 50);
		m = new Message("nestor", chat, t, "Sack Priam's city!");
		hist.add(m);
		
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("get-history");
		expected.setHistory(hist);
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeGetOnlineList1() {
		String json = "{\"type\":\"online-list\",\"list\":[]}";
		Set<String> users = new TreeSet<String>();
		
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("online-list");
		expected.setOnlineList(users);
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeGetOnlineList2() {
		String json = "{\"type\":\"online-list\",\"list\":[\"achilles\",\"helen\",\"priam\"]}";
		Set<String> users = new TreeSet<String>(Arrays.asList(new String[]{"achilles","helen","priam"}));
		
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("online-list");
		expected.setOnlineList(users);
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testDecodeOfflineNotification() {
		String json = "{\"type\":\"offline-notification\",\"userName\":\"hektor\"}";
		
		ProtocolMessage expected = new ProtocolMessage();
		expected.setType("offline-notification");
		expected.setUserName("hektor");
		
		ProtocolMessage actual = ClientProtocol.decode(json);
		assertEquals(expected, actual);
	}

}
