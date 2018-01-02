package gui.windows;

import java.awt.Frame;

// import static gui.Gui.frame8;
// import static gui.Gui.frame9;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;

import gui.MessengerGUI;


/**
 * This class contains the dialog frame that displays the all the available user histories in a scroll pane where the user
 * can select which history to retrieve and confirm by clicking the getHistory button. 
 */
public class selectUserHistory extends javax.swing.JDialog {
	
	private MessengerGUI gui;
	private DefaultListModel<String> listModel;
    private javax.swing.JButton cancelHistory;
    private javax.swing.JButton getHistoryButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> userHistoriesList;
	
    /**
     * Creates new form selectUserHistory
     */
    public selectUserHistory(MessengerGUI gui, Frame parent, boolean modal) {
    	super(parent, modal);
    	this.gui = gui;
    	gui.setHistorySelectionScreen(this);
        super.setLocationRelativeTo(parent);
        initComponents();
    }

    /**
     * Initialise components.
     */
    private void initComponents() {
    	listModel = new DefaultListModel<String>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userHistoriesList = new javax.swing.JList<String>(listModel);
        getHistoryButton = new javax.swing.JButton();
        cancelHistory = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Available Histories - Messenger");
        setResizable(false);

        jLabel1.setText("Select which user history you want to retrieve:");

        jScrollPane1.setViewportView(userHistoriesList);

        getHistoryButton.setText("Get history");
        getHistoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	getHistoryButtonActionPerformed(evt);
            }
        });

        cancelHistory.setText("Cancel");        
        cancelHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	cancelHistoryActionPerformed(evt);
            }
        });
        
        
        // Layout of components
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelHistory)
                .addGap(18, 18, 18)
                .addComponent(getHistoryButton)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelHistory)
                    .addComponent(getHistoryButton))
                .addGap(20, 20, 20))
        );

        pack();
        // Center window
        this.setLocationRelativeTo(null);
        this.setIconImage(gui.getLogo());
    }
    
    /**
     * Cancel. 
     */
	private void cancelHistoryActionPerformed(java.awt.event.ActionEvent evt) {
		this.setVisible(false);
	}
    
    /**
     * When the user clicks the getHistoryButton, a new frame will be generated and registered with the gui, while 
     * the dialog frame will be set to visible(false).
     */
    private void getHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	String selected = userHistoriesList.getSelectedValue(); 
    	if(selected == null) {return;}
    	String[] members = selected.split(", "); 
    	Set<String> memberSet = new TreeSet<String>(Arrays.asList(members));
    	this.setVisible(false);
    	gui.getClient().requestHistory(memberSet);
    }
    
    /**
     * Method to display the available histories list in the panel.
     */
    public void displayHistoryList(List<Set<String>> list) {
    	listModel.removeAllElements();
    	for(Set<String> chat : list) {
    		String formatted = "";
    		Iterator<String> iter = chat.iterator();
    		for(int i = 0; i < chat.size()-1; i++) {
    			formatted += iter.next() + ", ";
    		}
    		formatted += iter.next();
    		listModel.addElement(formatted);
    	}
    }
}
