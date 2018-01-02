package gui.windows;

import java.util.Set;
import java.util.TreeSet;

// import static gui.Gui.frame6;
// import static gui.Gui.frame7;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import gui.MessengerGUI;

/**
 * This class contains the dialog frame for selecting the online users desired in order to create a new group chat. 
 * It displays an online list of users, so that the user logged in can select the ones he/she wants and further 
 * click on the create group chat button, which will display the new group chat window created. 
 */

public class selectUsersGroupChat extends javax.swing.JDialog {
    
	private MessengerGUI gui;
    private DefaultListModel<String> onlineListModel;
    private javax.swing.JButton cancelGroupChatMaker;
    private javax.swing.JButton createGroupChatButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> onlineUsersGroupChat;
    
    /**
     * Creates new form selectUsersGroupChat
     */
    public selectUsersGroupChat(MessengerGUI gui, java.awt.Frame parent, boolean modal, DefaultListModel<String> onlineListModel) {
        super(parent, modal);
        this.gui = gui;
        this.onlineListModel= onlineListModel; 
        initComponents();
    }
    
    /**
     * Initialise components.
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        onlineUsersGroupChat = new JList<String>(onlineListModel);
        onlineUsersGroupChat.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // Multiple interval is selected in order to allow the user to select multiple online users for the group chat.
        jLabel1 = new javax.swing.JLabel();
        createGroupChatButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cancelGroupChatMaker = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Chat Select - Messenger");
        setResizable(false);
        
        jScrollPane1.setViewportView(onlineUsersGroupChat);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); 
        jLabel1.setText("Select users for group chat:");

        createGroupChatButton.setText("Create group chat");
        createGroupChatButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGroupChatButtonActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/1489279223_magnifier_glass.png"))); 

        cancelGroupChatMaker.setText("Cancel");
        cancelGroupChatMaker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelGroupChatMakerActionPerformed(evt);
            }
        });
        
        //Layout of the dialog frame
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelGroupChatMaker, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createGroupChatButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(40, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelGroupChatMaker)
                    .addComponent(createGroupChatButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
        // Center window
        this.setLocationRelativeTo(null);
        this.setIconImage(gui.getLogo());
    }
    
    /**
     * Create new group chat.
     * 
     * @param evt
     */
    private void createGroupChatButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	int[] indices = onlineUsersGroupChat.getSelectedIndices();
    	if(indices.length < 2) {
    		JOptionPane.showMessageDialog(this, "Please select at least two users to create a group chat.");
    		return;
    	}
    	this.setVisible(false); 
    	Set<String> members = new TreeSet<String>(onlineUsersGroupChat.getSelectedValuesList());
    	members.add(gui.getUser());
        groupChat newGroupChat = new groupChat(gui, members);
        newGroupChat.setVisible(true);
    }
    
    /**
     * Cancel.
     * 
     * @param evt
     */
    private void cancelGroupChatMakerActionPerformed(java.awt.event.ActionEvent evt) {
    	 this.setVisible(false);
    }

}
