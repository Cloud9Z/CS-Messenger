package test.server;

import java.util.List;
import java.util.Set;

import message.Message;
import server.db.DatabaseFunctions;

/**
 * DummyDatabase to test the server.
 * 
 * @author Meng-Jung Lee & Jochen Stuber
 * @version 2017-03-20
 */
public class DummyDatabase implements DatabaseFunctions {
	
	private boolean result;
	private List<Set<String>> historyList;
	private List<Message> history;
	
	public DummyDatabase(boolean result, List<Set<String>> historyList, List<Message> history) {
		this.result = result;
		this.historyList = historyList;
		this.history = history;
	}
	
	public DummyDatabase() {
		this.result = false;
		this.historyList = null;
		this.history = null;
	}
	
	@Override
	public boolean signUp(String userName, String password) {
		return result;
	}

	@Override
	public boolean signIn(String userName, String password) {
		return result;
	}

	@Override
	public void saveMessage(Message message) {
		// Do nothing
	}

	@Override
	public List<Set<String>> getHistoryList(String userName) {
		return historyList;
	}

	@Override
	public List<Message> getHistory(Set<String> users) {
		return history;
	}
	
	@Override
	public void saveUCMapping(String username, String chatid) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @param historyList the historyList to set
	 */
	public void setHistoryList(List<Set<String>> historyList) {
		this.historyList = historyList;
	}

	/**
	 * @param history the history to set
	 */
	public void setHistory(List<Message> history) {
		this.history = history;
	}

}
