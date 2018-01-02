package gui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;

import gui.MessengerGUI;
import message.Message;


/**
 * This class represents a user chat.
 *
 * @author violamarku
 */
public class userChat extends javax.swing.JFrame implements ChatWindow {
	
	private MessengerGUI gui;
	private Set<String> members;
	private String otherUser;
	private userChat thisUserChat;
	private boolean closed;
    private javax.swing.JTextArea chatPanel;
    private javax.swing.JTextArea chatTextArea;
    private javax.swing.JLabel chatWithLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton sendMessage;
	
    /**
     * Creates new form userChat
     */
    public userChat(MessengerGUI gui, String username) {
        this.gui = gui;
        this.otherUser = username;
        members = new TreeSet<String>();
        members.add(gui.getUser());
        members.add(username);
        gui.registerUserChat(this);
    	initComponents();
        chatWithLabel.setText("Chat with "+ username);
        this.setTitle("Chat with "+ username + " - Messenger");
        thisUserChat = this;
    }
    
    /**
     * Initialise components.
     */
    private void initComponents() {
    	
        jScrollPane1 = new javax.swing.JScrollPane();
        chatTextArea = new javax.swing.JTextArea();
        sendMessage = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatPanel = new javax.swing.JTextArea();
        chatWithLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);
        // setType(java.awt.Window.Type.POPUP);
        chatTextArea.setColumns(20);
        chatTextArea.setRows(5);
        InputMap input = chatTextArea.getInputMap();
        KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
        KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
        input.put(shiftEnter, "insert-break");  
        input.put(enter, "text-submit");
        ActionMap actions = chatTextArea.getActionMap();
        actions.put("text-submit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	sendMessageActionPerformed();
            }
        });
        jScrollPane1.setViewportView(chatTextArea);

        sendMessage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/1489283472_telegram.png"))); // NOI18N
        sendMessage.setBorder(null);
        sendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageActionPerformed();
            }
        });

        chatPanel.setEditable(false);
        chatPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        DefaultCaret caret = (DefaultCaret) chatPanel.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jScrollPane2.setViewportView(chatPanel);

        chatWithLabel.setText("Chat");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chatWithLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sendMessage))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(chatWithLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sendMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	gui.deregisterUserChat(thisUserChat);
            }
        });

        pack();
        // Center frame
        this.setLocationRelativeTo(null);
        this.setIconImage(gui.getLogo());
    }
    
    /**
     * Send message.
     */
    private void sendMessageActionPerformed() {//GEN-FIRST:event_sendMessageActionPerformed
        if(!closed) {
        	// Get the message from the text field
        	String text = chatTextArea.getText();
        	if(text.equals("")) {return;}
        	chatTextArea.setText("");
        	chatTextArea.requestFocus();
        	Message message = new Message(gui.getUser(), members, LocalDateTime.now(), text);
        	gui.getClient().sendMessage(message);
        }
    }
    
    /**
     * Display messsage.
     */
    public void displayMessage(Message message) {
    	chatPanel.append(message.toString() + "\n");
    }
    
	/**
	 * @return the members
	 */
	public Set<String> getMembers() {
		return members;
	}
	
	@Override
	public void close() {
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Handle the user going offline.
	 */
	public void handleUserOffline() {
		chatPanel.append(otherUser + " has gone offline.This chat is now closed...\n");
		sendMessage.setEnabled(false);
		closed = true;
	}
	
	/**
	 * @return the other chat participant
	 */
	public String getOtherUser() {
		return otherUser;
	}
}
