package gui.dialogues;

import java.awt.Frame;

/**
 * This class contains the usernamePassNotCorrect dialog window.
 * This dialog window is triggered when the combination of nickname and password input 
 * by the user is not matching in the database (for example, because of a typo). The user can then close the dialog window and 
 * try again.
 */
public class usernamePassNotCorrect extends javax.swing.JDialog {
	
	private Frame parent;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
	
    /**
     * Creates new form usernamePassNotCorrect
     */
    public usernamePassNotCorrect(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        initComponents();
    }
    
    /**
     * Initialisation of components of the dialog window.
     */
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Incorrect Combination - Messenger");

        jLabel1.setText("<html> Password/Nickname combination  <br>is incorrect. </html>");

        jButton1.setText("Try Again");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(31, 31, 31))
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton1)
                .addGap(22, 22, 22))
        );

        pack();
        setLocationRelativeTo(parent);
    }
    
    /**
     * Handle button event.
     * 
     * @param evt
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    	setVisible(false);
    }

}
