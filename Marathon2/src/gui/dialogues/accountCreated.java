package gui.dialogues;

import java.awt.Frame;

import gui.MessengerGUI;

/**
 * This class contains the accountCreated dialog window.
 * 
 * This window is displayed after successful creation of a new account in the signUpScreen. 
 * It contains a backToLogIn button that once is clicked, will take the user back to the sign
 * in window (Create account window is setVisible(false)). 
 */
public class accountCreated extends javax.swing.JDialog {
	
    private MessengerGUI gui;
    private Frame parent;
	private javax.swing.JButton backToLogIn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
	
    /**
     * Creates new form accountCreated
     * 
     * @param gui 
     * @param parent window
     * @param modal true
     */
    public accountCreated(MessengerGUI gui, Frame parent, boolean modal) {
        super(parent, modal);
        this.gui = gui;
        this.parent = parent;
        gui.setAccountSuccess(this);
        initComponents();
    }
    
    /**
     * Initialisation of components of the dialog window.
     */
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        backToLogIn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Account created! - Messenger");
        setResizable(false);

        jLabel1.setText("Account created successfully!");

        backToLogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/1489282760_Button_Back.png"))); 
        backToLogIn.setText("Back to log in");
        backToLogIn.setToolTipText("");
        backToLogIn.setBorder(null);
        backToLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backToLogInActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/1489356417_Paper-Plane.png"))); 

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backToLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)))
                .addGap(31, 31, 31)
                .addComponent(backToLogIn)
                .addGap(19, 19, 19))
        );
        pack();
        setLocationRelativeTo(parent);
    }
    /**
     * Action performed handler, where the user goes back to the log in screen after clicking the backToLogIn button
     */
    private void backToLogInActionPerformed(java.awt.event.ActionEvent evt) {
    	gui.getLoginWindow().setVisible(true);
        gui.getSignUpWindow().setVisible(false);
    	this.setVisible(false);;
    }

}
