package gui.dialogues;

import java.awt.event.WindowEvent;

import gui.windows.groupChat;


/**
 * This class contains the groupChatDisconnected dialog. 
 * 
 * In this dialog window the user is notified that the groupChat they were participating in is no
 * longer available (triggered by a participant leaving the chat - either because they disconnected from Messenger or closed the groupChat
 * window). When the user clicks the close button on the dialog, both the dialog and the groupChat connected to the dialog will be closed.
 */
public class groupChatDisconnected extends javax.swing.JDialog {
	
	private groupChat chat;
    private javax.swing.JButton closeWindowButton;
    private javax.swing.JLabel jLabel1;
	
    /**
     * Creates new form groupChatDisconnected
     */
    public groupChatDisconnected(groupChat chat, boolean modal) {
        super(chat, modal);
        this.chat = chat;
        initComponents();
    }

    /**
     * Initialisation of components of the dialog window.
     */
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        closeWindowButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Chat Terminated - Messenger");
        setResizable(false);

        jLabel1.setText("<html>Due to a user disconnecting, \n<br>this group chat is now terminated.</html>");

        closeWindowButton.setText("Close");
        closeWindowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeWindowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(190, 190, 190)
                        .addComponent(closeWindowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(closeWindowButton)
                .addGap(32, 32, 32))
        );

        pack();
        setLocationRelativeTo(chat);
    }
    
    /**
     * When the user clicks the close button on the dialog, both the dialog and the groupChat connected to the dialog will be closed.
     */
    private void closeWindowButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	chat.dispatchEvent(new WindowEvent(chat, WindowEvent.WINDOW_CLOSING));
    	this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
}
