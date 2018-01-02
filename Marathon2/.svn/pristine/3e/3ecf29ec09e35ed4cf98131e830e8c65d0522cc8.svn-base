package gui;

import java.awt.Frame;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.ImageIcon;

import client.ClientInterface;
import gui.dialogues.accountCreated;
import gui.dialogues.errorMessageLogOut;
import gui.dialogues.nicknameAlreadyTaken;
import gui.dialogues.usernamePassNotCorrect;
import gui.windows.ChatWindow;
import gui.windows.HistoryWindow;
import gui.windows.LogIn;
import gui.windows.groupChat;
import gui.windows.loggedIn;
import gui.windows.selectUserHistory;
import gui.windows.selectUsersGroupChat;
import gui.windows.signUpScreen;
import gui.windows.userChat;
import message.Message;

/**
 * This class manages the GUI of the Messenger application.
 */
public class MessengerGUI implements Observer {
	
	private ClientInterface client;
	private LogIn loginWindow;
	private signUpScreen signUpWindow;
	private loggedIn homeScreen; 
	private selectUserHistory historySelectionScreen;
	private selectUsersGroupChat groupChatSelectionScreen;
	private Set<userChat> userChats;
	private Set<groupChat> groupChats;
	private Set<HistoryWindow> historyWindows;
	private String user;
	private accountCreated accountSuccess;
	private nicknameAlreadyTaken nickUnavailable;
	private usernamePassNotCorrect userPassWrong;
	private errorMessageLogOut errorMessage;
	private boolean loggedOut;
	private Image logo;
	
	/**
	 * Instantiate new GUI.
	 * 
	 * @param client
	 */
	public MessengerGUI(ClientInterface client) {
		this.client = client;
		this.userChats = new HashSet<userChat>();
		this.groupChats = new HashSet<groupChat>();
		this.historyWindows= new HashSet<HistoryWindow>();
		this.logo = new ImageIcon(getClass().getResource("/gui/images/1489279158_Paper-Plane.png")).getImage();
	}
	
	/**
	 * Start GUI.
	 */
	public void startGUI() {
		loginWindow = new LogIn(this);
		loginWindow.setVisible(true);
	}
	
	/**
	 * Update view based on changes in the model.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		String status = arg1.toString();
		// Distinguish status values and handle appropriately
		switch(status) {
		case "Account:1":
			if(accountSuccess == null) {
				accountSuccess = new accountCreated(this, signUpWindow, true);
			}
			accountSuccess.setVisible(true);
			break;
		case "Account:-1":
			if(nickUnavailable == null) {
				nickUnavailable = new nicknameAlreadyTaken(this);
			}
			nickUnavailable.setVisible(true);
			break;
		case "Login:1":
			// Login screen disappears
			// Home screen is created (show empty only list)
			loginWindow.setVisible(false);
			homeScreen = new loggedIn(this);
			homeScreen.setVisible(false);
			break;
		case "Login:-1":
			userPassWrong = new usernamePassNotCorrect(loginWindow, true);
			userPassWrong.setVisible(true);	
			break;
		case "Online:1":
			Set<String> onlineList = client.getOnlineList();
			homeScreen.displayOnlineList(onlineList);
			homeScreen.setVisible(true);
			break;
		case "Chat:1":
			displayMessage(client.getNextMessage());
			break;
		case "Chat:2":
			closeGroupChat(client.nextGroupChatToClose());
			break;
		case "Offline:1":
			handleOfflineNotification(client.nextOfflineNotification());
			break;
		case "History:1":
			showHistorySelectionScreen(client.getHistoryList());
			break;
		case "History:2":
			showNewHistoryWindow(client.getNextHistory());
			break;
		case "Login:-2":
			System.err.println("User is already logged in...");
		case "Connection:1":
			handleClientError();
			break;
		}
		
	}
	
	/**
	 * Handles termination of the client.
	 */
	private void handleClientError() {
		// Do not handle errors after logout
		if(loggedOut) {
			return;
		}
		Frame parent = homeScreen;
		if(homeScreen == null) {
			if(loginWindow.isVisible()) {
				parent = loginWindow;
			} else if(signUpWindow.isVisible()) {
				parent = signUpWindow;
			} else {
				System.err.println("Cannot display error dialogue.");
			}
		}
		errorMessage = new errorMessageLogOut(this, parent, true);
		errorMessage.setVisible(true);
	}
	
	/**
	 * Display next chat history.
	 * 
	 * @param nextHistory
	 */
	private void showNewHistoryWindow(List<Message> nextHistory) {
		HistoryWindow newWindow = new HistoryWindow(this, nextHistory);
		newWindow.setVisible(true);
	}

	/**
	 * Display screen to select chat history.
	 * 
	 * @param historyList
	 */
	private void showHistorySelectionScreen(List<Set<String>> historyList) {
    	if(historySelectionScreen == null) {
    		historySelectionScreen = new selectUserHistory(this, homeScreen, true);
    	}
		historySelectionScreen.displayHistoryList(historyList);
    	historySelectionScreen.setVisible(true);	
	}
	
	/**
	 * Handle offline notification of a user.
	 * 
	 * @param offlineUser
	 */
	private void handleOfflineNotification(String offlineUser) {
		for(groupChat chat : groupChats){
			Set<String> members = chat.getMembers();
			if(members.contains(offlineUser)) {
				chat.handleClosingEvent();
			}
		}
		for(userChat chat: userChats) {
			if(chat.getOtherUser().equals(offlineUser)) {
				chat.handleUserOffline();
			}
		}
	}
	
	/**
	 * Close group chat.
	 * 
	 * @param nextGroupChatToClose
	 */
	private void closeGroupChat(Set<String> nextGroupChatToClose) {
		for(groupChat chat : groupChats) {
			if(chat.getMembers().equals(nextGroupChatToClose)) {
				chat.handleClosingEvent();
			}
		}
		
	}
	
	/**
	 * Display a new message.
	 * 
	 * @param message
	 */
	private void displayMessage(Message message) {
		boolean chatIsOpen = false;
		Set<String> chat = message.getChat();
		for(ChatWindow window : (chat.size() == 2 ? userChats : groupChats)) {
			Set<String> members = window.getMembers();
			if(members.equals(chat)) {
				window.displayMessage(message);
				chatIsOpen = true;
			}
		}
		// Create new window, if none exists yet
		if(!chatIsOpen) {
			if(chat.size() == 2) {
				String otherUser = "";
				for(String userName : chat) {
					if(!userName.equals(user)) {
						otherUser = userName;
					}
				}
				userChat newUserChat = new userChat(this, otherUser);
				newUserChat.displayMessage(message);
				newUserChat.setVisible(true);
			} else {
				groupChat newGroupChat = new groupChat(this, chat);
				newGroupChat.displayMessage(message);
				newGroupChat.setVisible(true);
			}
		}
	}
	
	/**
	 * Shut down GUI and log out client.
	 */
	public void logOut() {
		System.err.println("GUI is shutting down...");
		// Indicate that GUI is shutting down so that frames can react appropriately
		loggedOut = true;
		for(Frame frame : Frame.getFrames()) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		// Client needs to log out after frames have been closed so that chat closing notifications are dispatched
		client.logOut();
	}
	
	/**
	 * Register new user chat.
	 * 
	 * @param chat
	 */
	public void registerUserChat(userChat chat) {
		userChats.add(chat);
	}
	
	/**
	 * Register new group chat.
	 * 
	 * @param chat
	 */
	public void registerGroupChat(groupChat chat) {
		groupChats.add(chat);
	}
	
	/**
	 * Register new history window.
	 * 
	 * @param window
	 */
	public void registerHistoryWindow(HistoryWindow window) {
		historyWindows.add(window);
	}
	
	/**
	 * Deregister user chat.
	 * 
	 * @param chat
	 */
	public void deregisterUserChat(userChat chat) {
		userChats.remove(chat);
	}
	
	/**
	 * Deregister group chat.
	 * 
	 * @param chat
	 */
	public void deregisterGroupChat(groupChat chat) {
		groupChats.remove(chat);
	}
	
	/**
	 * Deregister history window.
	 * 
	 * @param window
	 */
	public void deregisterHistoryWindow (HistoryWindow window) {
		historyWindows.remove(window);
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the client
	 */
	public ClientInterface getClient() {
		return client;
	}

	/**
	 * @param historySelectionScreen the historySelectionScreen to set
	 */
	public void setHistorySelectionScreen(selectUserHistory historySelectionScreen) {
		this.historySelectionScreen = historySelectionScreen;
	}

	/**
	 * @return the signUpWindow
	 */
	public signUpScreen getSignUpWindow() {
		return signUpWindow;
	}

	/**
	 * @param signUpWindow the signUpWindow to set
	 */
	public void setSignUpWindow(signUpScreen signUpWindow) {
		this.signUpWindow = signUpWindow;
	}

	/**
	 * @return the homeScreen
	 */
	public loggedIn getHomeScreen() {
		return homeScreen;
	}

	/**
	 * @param homeScreen the homeScreen to set
	 */
	public void setHomeScreen(loggedIn homeScreen) {
		this.homeScreen = homeScreen;
	}

	/**
	 * @return the loginWindow
	 */
	public LogIn getLoginWindow() {
		return loginWindow;
	}

	/**
	 * @param loginWindow the loginWindow to set
	 */
	public void setLoginWindow(LogIn loginWindow) {
		this.loginWindow = loginWindow;
	}

	/**
	 * @return the accountSuccess
	 */
	public accountCreated getAccountSuccess() {
		return accountSuccess;
	}

	/**
	 * @param accountSuccess the accountSuccess to set
	 */
	public void setAccountSuccess(accountCreated accountSuccess) {
		this.accountSuccess = accountSuccess;
	}

	/**
	 * @return the groupChatSelectionScreen
	 */
	public selectUsersGroupChat getGroupChatSelectionScreen() {
		return groupChatSelectionScreen;
	}

	/**
	 * @param groupChatSelectionScreen the groupChatSelectionScreen to set
	 */
	public void setGroupChatSelectionScreen(selectUsersGroupChat groupChatSelectionScreen) {
		this.groupChatSelectionScreen = groupChatSelectionScreen;
	}

	/**
	 * @return the historySelectionScreen
	 */
	public selectUserHistory getHistorySelectionScreen() {
		return historySelectionScreen;
	}

	/**
	 * @return the loggedOut
	 */
	public boolean isLoggedOut() {
		return loggedOut;
	}

	/**
	 * @param loggedOut the loggedOut to set
	 */
	public void setLoggedOut(boolean loggedOut) {
		this.loggedOut = loggedOut;
	}

	/**
	 * @return the logo
	 */
	public Image getLogo() {
		return logo;
	}
	
}
