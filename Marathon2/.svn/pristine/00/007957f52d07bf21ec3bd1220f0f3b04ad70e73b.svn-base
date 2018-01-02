package test.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import client.ClientInterface;
import message.Message;

/**
 * Dummy Client to test the GUI.
 *
 * @author violamarku
 * @version 2017-03-20
 */
public class DummyClientForGUI extends Observable implements ClientInterface {
	
	private String accountStatus = "Account:1";
	private String loginStatus = "Login:1";
	private Set<String> onlineList = new TreeSet<String>(Arrays.asList(new String[]{"odysseus","zeus","achilles"}));
	
	@Override
	public void createAccount(String userName, String password) {
		setChanged();
		notifyObservers(accountStatus);
	}

	@Override
	public void login(String userName, String password) {
		setChanged();
		notifyObservers(loginStatus);
		if(loginStatus.equals("Login:1")) {
			setChanged();
			notifyObservers("Online:1");
		}
	}

	@Override
	public Set<String> getOnlineList() {
		return onlineList;
	}

	@Override
	public void sendMessage(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message getNextMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestHistoryList() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Set<String>> getHistoryList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestHistory(Set<String> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Message> getNextHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeGroupChat(Set<String> users) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> nextGroupChatToClose() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String nextOfflineNotification() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logOut() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param accountStatus the accountStatus to set
	 */
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	/**
	 * @param loginStatus the loginStatus to set
	 */
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * @param onlineList the onlineList to set
	 */
	public void setOnlineList(Set<String> onlineList) {
		this.onlineList = onlineList;
	}

}
