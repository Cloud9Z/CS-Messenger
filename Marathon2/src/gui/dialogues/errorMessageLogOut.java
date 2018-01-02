package gui.dialogues;

import java.awt.Frame;

import gui.MessengerGUI;

/**
 * This class contains the errorMessageLogOut dialog.
 * 
 * This dialog is displayed every time an error occurs and the user is logged out from Messenger (and if applicable, client is 
 * disconnected).
 */
public class errorMessageLogOut extends javax.swing.JDialog {
	
	private MessengerGUI gui;
	private Frame parent;
    private javax.swing.JButton shutDown;
    private javax.swing.JLabel jLabel1;
	
    /**
     * Creates new form errorMessageLogOut.
     */
    public errorMessageLogOut(MessengerGUI gui, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        this.gui = gui;
        initComponents();
    }

    /**
     * Initialisation of components of the dialog window.
     */
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        shutDown = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Error - Messenger");
        setResizable(false);

        jLabel1.setText("<html>Something went wrong!\n<br>You have now been disconnected.\n</html>");

        shutDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/1489282760_Button_Back.png"))); // NOI18N
        shutDown.setText("OK");
        shutDown.setToolTipText("");
        shutDown.setBorder(null);
        shutDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shutDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(shutDown, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(shutDown)
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(parent);
    }

    private void shutDownActionPerformed(java.awt.event.ActionEvent evt) {
        gui.logOut();
    }

}
