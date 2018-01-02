package gui.dialogues;

import java.awt.Frame;

// import static gui.Gui.frame;
// import static gui.Gui.frame2;
// import static gui.Gui.frame3;
// import static gui.Gui.frame4;

import java.awt.event.WindowEvent;

import gui.MessengerGUI;

/**
 * This class contains the AreYouSure dialog window.
 * 
 * In this dialog window the user is prompted to confirm if they want to log out. If the click the 
 * logOutYesButton, the current GUI will be terminated and client will be disconnected.
 */
public class AreYouSure extends javax.swing.JDialog {
	
	private MessengerGUI gui;
	private Frame parent;
    private javax.swing.JButton cancelLogOut;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logOutYesButton;
	
    /**
     * Creates new form AreYouSure.
     * 
     * @param gui 
     * @param parent window
     * @param modal true
     */
    public AreYouSure(MessengerGUI gui, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        this.gui = gui;
        initComponents();
    }

    /**
     * Initialisation of components of the dialog window.
     */
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jLabel1 = new javax.swing.JLabel();
        logOutYesButton = new javax.swing.JButton();
        cancelLogOut = new javax.swing.JButton();

        jScrollPane1.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Log out - Messenger");
        setResizable(false);

        jLabel1.setText("Are you sure you want to log out?");

        logOutYesButton.setText("Yes");
  
        logOutYesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutYesButtonActionPerformed(evt);
            }
        });

        cancelLogOut.setText("Cancel");
        cancelLogOut.setAlignmentX(35.0F);
        cancelLogOut.setAlignmentY(23.0F);
        
        cancelLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelLogOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(cancelLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(logOutYesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelLogOut)
                    .addComponent(logOutYesButton))
                .addGap(29, 29, 29))
        );

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * If the logOutYesButton is clicked, the dialog window will be closed and the logOut method will be called.
     */
    private void logOutYesButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	gui.logOut();
    }

    /**
     * If the user clicks cancelLogOut button, the dialog window will be closed. 
     */
    private void cancelLogOutActionPerformed(java.awt.event.ActionEvent evt) {
    	this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
