package gui.windows;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import gui.MessengerGUI;


/**
 * This class contains the logIn JFrame and associated methods. In this frame, a createNewAccount button is located and 
 * when clicked the signUpScreen will be generated. The logInButton, when clicked, will pass the texts present in nicknameField and passwordField
 * to the client interface method createAccount. Those fields also need to be containing some text in order to enable the logInButton, 
 * which will be disabled until both fields contain some text. 
 */
public class LogIn extends javax.swing.JFrame {
    
	private MessengerGUI gui;
    private javax.swing.JButton createNewAccount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JToggleButton logInButton;
    private javax.swing.JTextField nicknameField;
    private javax.swing.JLabel passNickDontMatch;
    private javax.swing.JPasswordField passwordField;
    
    /**
     * Creates new form NewJFrame
     */
    public LogIn(MessengerGUI gui) {
        this.gui = gui;
        initComponents();
    }

    /**
     * Initialise components.
     */
    private void initComponents() {

        logInButton = new javax.swing.JToggleButton();
        nicknameField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        createNewAccount = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        passNickDontMatch = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Messenger");
        setBackground(new java.awt.Color(255, 255, 255));
        setBounds(new java.awt.Rectangle(600, 50, 0, 0));
        setResizable(false);

        logInButton.setText("Log In");
        logInButton.setEnabled(false);
        logInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInButtonActionPerformed(evt);
            }
        });

        nicknameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nicknameFieldKeyReleased(evt);
            }
        });

        jLabel1.setText("Nickname");

        jLabel2.setText("Password");

        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                passwordFieldKeyReleased(evt);
            }
        });
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(passwordField.getPassword().length == 0 || nicknameField.getText().length() == 0) {
                	return;
                }
                logInButtonActionPerformed(evt);
            }
        });

        createNewAccount.setText("Create New Account");
        createNewAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createNewAccountActionPerformed(evt);
            }
        });

        jLabel3.setText("Click here to create new account:");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/images/1489279158_Paper-Plane.png"))); 

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(passNickDontMatch))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(nicknameField)
                                .addComponent(logInButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(passwordField)
                                .addComponent(createNewAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel4)
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nicknameField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(passNickDontMatch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(createNewAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(184, Short.MAX_VALUE))
        );
        
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {	
            	// If logged out, do not handle event to prevent endless recursive calls
            	if(gui.isLoggedOut()) {
            		return;
            	}
            	gui.logOut();
            }
        });

        pack();
        // Center frame
        this.setLocationRelativeTo(null);
        this.setIconImage(gui.getLogo());
    }
  
    /**
     * The nicknameField and passwordField need to contain some text before enabling the log in button so we check if both contain 
     * some text whenever a key is released (since the user could be also deleting the text after, for example, making a mistake, and 
     * the log in button should not be left enabled in that specific case). 
     */
    private void nicknameFieldKeyReleased(java.awt.event.KeyEvent evt) {
        if (passwordField.getPassword().length == 0 || nicknameField.getText().length() == 0) {
           passNickDontMatch.setText("Fields are left empty.");
           logInButton.setEnabled(false);
       }else{
    	   passNickDontMatch.setText("");
    	   logInButton.setEnabled(true);
    }                                     
    }
    
    /**
     * As done for the nicknameFieldKeyReleased event handler, we check the passwordField for presence of text every time the 
     * user releases a key.
     */
    private void passwordFieldKeyReleased(java.awt.event.KeyEvent evt) {
    if (nicknameField == null || nicknameField.getText().length() == 0 || passwordField.getPassword().length == 0) {
           passNickDontMatch.setText("Fields are left empty.");
           logInButton.setEnabled(false);
       }else{
    	   passNickDontMatch.setText("");
    	   logInButton.setEnabled(true);
    }                                    
    }

    /**
     * When the user clicks on the createNewAccount button, the signUpScreen frame will be generated and the the logIn 
     * frame will be set to visible(false).
     */
    private void createNewAccountActionPerformed(java.awt.event.ActionEvent evt) {
    	signUpScreen signUp = new signUpScreen(gui);
        signUp.setVisible(true);       
        this.setVisible(false);
    }
    
    /**
     * When the logInButton is clicked, the nickname and password fields will be passed to the client login method. 
     * A new homeScreen will be generated and display a welcome message with the user nickname. 
     */
    private void logInButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	String userName = nicknameField.getText();
    	ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
    	// Encrypt password
    	passwordEncryptor.setAlgorithm("SHA-1");
    	passwordEncryptor.setPlainDigest(true);
    	String encryptedPassword = passwordEncryptor.encryptPassword(Arrays.toString(passwordField.getPassword()));
    	gui.setUser(userName);
    	gui.getClient().login(userName, encryptedPassword);
    }

}
