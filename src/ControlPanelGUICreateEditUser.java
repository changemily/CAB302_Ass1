import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static javax.swing.JOptionPane.*;

/**
 * Create/Edit User class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Create/Edit User Screen
 * @author - Nickhil Nischal (GUI), Harry Estreich (buttons & permissions, refreshing)
 * @version - final
 */
public class ControlPanelGUICreateEditUser extends JFrame implements Runnable, ActionListener, WindowListener {
    // Components
    private JTextField usernameField;
    private JTextField password;
    private JButton saveExitButton;
    private JButton exitWithoutSaving;
    private JCheckBox createBillboardsBox;
    private JCheckBox scheduleBillboardsBox;
    private JCheckBox editAllBillboardsBox;
    private JCheckBox editUsersBox;

    // Global variables
    String username;
    String sessionToken;
    User targetUser;
    HashMap<String, User> userList;

    // Global booleans
    boolean adminUser; // true if user has Edit Users
    boolean newUser; // true if creating a user
    boolean selfUser; // true if editing own user
    boolean forcedExit = true; // true if exits without set plan, not when button pressed

    /**
     * First of two constructors for this class, this one is for creating new user, only log in user and list of users are required3
     * @param   username username of logged in user
     * @param   sessionToken sessionToken of logged in user
     * @param   userList HashMap of users
     * @throws  Exception throws exception when creating user if permissions fail
     */
    public ControlPanelGUICreateEditUser(String username, String sessionToken, HashMap<String, User> userList) throws Exception {
        // Set window title
        super("Create User");
        this.username = username;
        this.sessionToken = sessionToken;
        this.adminUser = true;
        this.newUser = true;
        this.targetUser = new User("", "", "");
        this.userList = userList;
    }

    /**
     * Second of two constructors for this class, this one is for editing a user, along with username and sessionToken, the
     * target user, whether it is a adminUser, whether it is on the logged in user, and the list of users are required
     * @param   username username of logged in user
     * @param   sessionToken sessionToken of logged in user
     * @param   targetUser target user of edit
     * @param   adminUser whether user has/requires Edit Users
     * @param   selfUser whether this is a self edit
     * @param   userList HashMap of users
     */
    public ControlPanelGUICreateEditUser(String username, String sessionToken, User targetUser, boolean adminUser, boolean selfUser, HashMap<String, User> userList){
        super("Edit User");
        this.username = username;
        this.sessionToken = sessionToken;
        this.adminUser = adminUser;
        this.targetUser = targetUser;
        this.newUser = false;
        this.selfUser = selfUser;
        this.userList = userList;
    }


    /**
     * Method used to create a GUI window for the Create/Edit User screen
     * @throws  ClassNotFoundException Exception handling
     * @throws  UnsupportedLookAndFeelException Exception handling
     * @throws  InstantiationException Exception handling
     * @throws  IllegalAccessException Exception handling
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        //make window non resizable
        setResizable(false);

        // Create a left column JPanel for formatting
        JPanel leftPanel = newFormatPanel();

        // Create username JLabel, add to left JPanel
        newLabel("Username", leftPanel);

        // Create username JTextField, add to left JPanel
        usernameField = newTextField(leftPanel);


        // Create password JLabel, add to left JPanel
        newLabel("Password", leftPanel);

        // Create password JTextField, add to left JPanel
        password = newTextField(leftPanel);

        // If new user set username to be editable, and set password to empty
        if(newUser) {
            usernameField.setEditable(true);
            password.setText("");
            password.setEditable(true);
        }
        else{ // if not, set username to be not editable, and set password to be a change password 'button"
            usernameField.setEditable(false);
            usernameField.setText(targetUser.username);
            password.setText("Change Password");
            password.setEditable(false);
        }

        // if password is clicked on, change password turns into a changeable password text label
        password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(password.getText().equals("Change Password") && !newUser){
                    password.setText("");
                    password.setEditable(true);
                }
            }

            // When focus lost, if still empty, set back to change password, otherwise stay the same
            @Override
            public void focusLost(FocusEvent e) {
                if(password.getText().equals("") && !newUser){
                    password.setText("Change Password");
                    password.setEditable(false);
                }
            }
        });

        leftPanel.add(Box.createVerticalStrut(20)); // Formatting

        // Create Save and Exit JButton, add to left JPanel
        saveExitButton = newButton("Save and Exit", leftPanel);

        // Create Exit Without Saving JButton, add to left JPanel
        exitWithoutSaving = newButton("Exit Without Saving", leftPanel);
        leftPanel.add(Box.createVerticalStrut(45)); // Formatting

        // Create a right column JPanel for formatting
        JPanel rightPanel = newFormatPanel();

        // Create a JPanel for spacing and border
        JPanel spacer = new JPanel();
        spacer.setLayout(new BoxLayout(spacer, BoxLayout.X_AXIS));
        spacer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        spacer.add(Box.createHorizontalStrut(10)); // Formatting

        // Create User Permissions JLabel
        JPanel permissionsPanel = newSubPanel("User Permissions");

        // Create Create Billboards JCheckBox, add to permissions JPanel
        createBillboardsBox = newCheckBox("Create Billboards", permissionsPanel);
        if(targetUser.permissions.contains("Create Billboards")){
            createBillboardsBox.setSelected(true);
        }

        // Create Schedule Billboards JCheckBox, add to permissions JPanel
        scheduleBillboardsBox = newCheckBox("Schedule Billboards", permissionsPanel);
        if(targetUser.permissions.contains("Schedule Billboards")){
            scheduleBillboardsBox.setSelected(true);
        }

        // Create Edit All Billboards JCheckBox, add to permissions JPanel
        editAllBillboardsBox = newCheckBox("Edit All Billboards", permissionsPanel);
        if(targetUser.permissions.contains("Edit All Billboards")){
            editAllBillboardsBox.setSelected(true);
        }

        // Create Edit Users (Admin) JCheckBox, add to permissions JPanel
        editUsersBox = newCheckBox("Edit Users (Admin)", permissionsPanel);
        if(targetUser.permissions.contains("Edit Users")){
            editUsersBox.setSelected(true);
        }

        // Add permissions JPanel to spacer JPanel, with Structs for spacing
        spacer.add(permissionsPanel);
        spacer.add(Box.createHorizontalStrut(10));

        // Add spacer JPanel to right JPanel, with Structs for spacing
        rightPanel.add(spacer);
        rightPanel.add(Box.createVerticalStrut(50));

        // Window formatting, if adminUser is true, add permissions section, otherwise just username/password, it is resized due to this aswell
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        if(adminUser) { // resizing
            getContentPane().add(Box.createHorizontalStrut(100)); // boundary
        }
        else{
            getContentPane().add(Box.createHorizontalStrut(40)); // boundary
        }
        getContentPane().add(leftPanel);
        getContentPane().add(Box.createHorizontalStrut(40));
        if(adminUser) {
            getContentPane().add(rightPanel); // add permissions
            getContentPane().add(Box.createHorizontalStrut(100)); // boundary
        }

        // Add Window Listener, used for when window is closed
        addWindowListener(this);

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * This method creates a JPanel, used for formatting
     * @return  Returns a JPanel
     */
    private JPanel newFormatPanel() {
        // Create JPanel
        JPanel panel = new JPanel();

        // Format JPanel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(50));

        // Returns a JPanel
        return panel;
    }

    /**
     * This method creates a JLabel, and adds it to a specified JPanel
     * @param   labelName The text that will appear in the JLabel
     * @param   panel The JPanel that the created JButton is to be added to
     * @return  Returns a JLabel
     */
    private JLabel newLabel(String labelName, JPanel panel) {
        // Create JLabel
        JLabel label = new JLabel(labelName);

        // Format JLabel
        label.setFont(new Font(null, Font.PLAIN, 15));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add JLabel to specified JPanel
        panel.add(label);

        // Returns a JLabel
        return label;
    }

    /**
     * This method creates a JTextField, and adds it to a specified JPanel
     * @param   panel The JPanel that the created JTextField is to be added to
     * @return  Returns a JTextField
     */
    private JTextField newTextField(JPanel panel) {
        // Create JTextField
        JTextField textField;

        // Format JTextField
        textField = new JTextField(15);

        // Add JTextField to the specified JPanel
        panel.add(textField);

        // Format specified JPanel
        panel.add(Box.createVerticalStrut(20));

        // Returns a JTextField
        return textField;

    }

    /**
     * This method creates a JButton, and adds it to a specified JPanel
     * @param   buttonName The text that will be displayed inside the button
     * @param   panel The JPanel that the created JButton is to be added to
     * @return  Returns a JButton
     */
    private JButton newButton(String buttonName, JPanel panel) {
        //Create JButton
        JButton button = new JButton(buttonName);

        // Format JButton
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add the JButton as an actionListener
        button.addActionListener(this);

        // Add JButton to specified JPanel
        panel.add(button);

        // Format
        panel.add(Box.createVerticalStrut(5));

        // Returns a JButton
        return button;
    }

    /**
     * This method creates a JCheckBox, and adds it to a specified JPanel
     * @param   name The text that will appear next to the JCheckBox
     * @param   panel  The JPanel that the created JCheckBox is to be added to
     * @return  Returns a JCheckBox
     */
    private JCheckBox newCheckBox(String name, JPanel panel) {
        // Create JCheckBox
        JCheckBox box = new JCheckBox(name);

        // Format JCheckBox
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Add the JCheckBox as an actionListener
        box.addActionListener(this);

        // Add JCheckBox to specified JPanel
        panel.add(box);

        // Format
        panel.add(Box.createVerticalStrut(20));

        // Returns a JCheckBox
        return box;
    }

    /**
     * This method creates a JLabel, inside of a JPanel
     * @param   labelName The text that will appear in the JLabel
     * @return  Returns a JPanel
     */
    private JPanel newSubPanel(String labelName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(20));
        JLabel label = new JLabel(labelName);
        label.setFont(new Font(null, Font.PLAIN, 15));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));

        // Returns a JPanel
        return panel;
    }

    /**
     * Method for when a button is pressed
     * @param  actionEvent click on button
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the Save and Exit button has been clicked
        if (buttonClicked == saveExitButton) {
            saveAndExit(); // runs save and exit method
        }
        // Checks if the Exit Without Saving button has been clicked
        else if (buttonClicked == exitWithoutSaving) {
            // if in full edit screen, close and go back to control panel
            if(adminUser){
                // Check if there is any userControlPanel already open, if so just dispose, otherwise reopen user control panel
                int frameCount = 0;
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))){
                        if(fr.isVisible()){
                            frameCount += 1;
                        }
                    }
                }
                dispose();
                if(frameCount == 0){
                    SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel(username, sessionToken, userList));
                }
            }
            else{ // If only changing password, restart control panel gui
                dispose();
                SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
            }
            System.out.println("exit without saving clicked");
            //run close window

        }
        else if (buttonClicked == editUsersBox){
            // check that if user with edit users tries to remove edit user from themselves, it stops them
            if(targetUser.permissions.contains("Edit Users") && selfUser){
                editUsersBox.setSelected(true);
                // Display error pop up
                JOptionPane.showMessageDialog(this,
                        "User can't remove 'Edit Users' from themselves");
            }
        }
    }

    /**
     * Create GUI
     */
    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        }



    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Window Closing event, ask if user wants to save changes, exit without saving, or cancel closing (Yes, No, Cancel)
     * @param   e window closing event
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int a = showConfirmDialog(null, "Would you like to save changes to the database?");
        if(a == YES_OPTION) // same as save and exit
        {
            saveAndExit();
        }
        else if(a == NO_OPTION) // same as exit without saving
        {
            if(adminUser){
                int frameCount = 0;
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))){
                        if(fr.isVisible()){
                            frameCount += 1;
                        }
                    }
                }
                dispose();
                if(frameCount == 0){
                    SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel(username, sessionToken, userList));
                }
            }
            else{
                dispose();
                SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * Save User and Exit back to control panel
     */
    public void saveAndExit(){
        // Print information
        System.out.println("save and exit clicked");
        System.out.println(usernameField.getText());
        System.out.println(createBillboardsBox.isSelected());
        System.out.println(scheduleBillboardsBox.isSelected());
        System.out.println(editAllBillboardsBox.isSelected());
        System.out.println(editUsersBox.isSelected());

        //run save info, and close window
        if(!usernameField.getText().equals("")) { // check that username has been typed in, else error
            if (newUser) { // check is new user
                if(!userList.containsKey(usernameField.getText())){ // Check if username already exists, else error
                    if (!password.getText().equals("")) { // check that password has been typed in, else error
                        String newUsername = usernameField.getText(); // get new username
                        String newPassword = null;
                        // get password and hash
                        try {
                            newPassword = UserManager.hashPassword(password.getText());
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        // initialise permission variables
                        String createBillboard;
                        String scheduleBillboard;
                        String editBillboard;
                        String editUsers;

                        // set permission variables
                        if (createBillboardsBox.isSelected()) {
                            createBillboard = "1";
                        } else {
                            createBillboard = "0";
                        }
                        if (scheduleBillboardsBox.isSelected()) {
                            scheduleBillboard = "1";
                        } else {
                            scheduleBillboard = "0";
                        }
                        if (editAllBillboardsBox.isSelected()) {
                            editBillboard = "1";
                        } else {
                            editBillboard = "0";
                        }
                        if (editUsersBox.isSelected()) {
                            editUsers = "1";
                        } else {
                            editUsers = "0";
                        }

                        // set user inputs and run create user in control panel
                        String[] user_inputs = {"Create User", newUsername, newPassword, createBillboard, scheduleBillboard, editBillboard, editUsers};
                        ControlPanelClient.runClient(user_inputs);

                        // Close any control panels, before refresh
                        Frame[] allFrames = Frame.getFrames();
                        for (Frame fr : allFrames) {
                            if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                                fr.dispose();
                            }
                        }
                        forcedExit = false; // set exit to safe
                        dispose(); // close panel

                        // Refresh user control panel
                        //run Billboard Control Panel GUI
                        String[] user_input = {"List users", "Admin", ControlPanelClient.sessionToken};
                        //request schedule and run calendar GUI
                        ControlPanelClient.runClient(user_input);
                    } else { // error, missing password
                        JOptionPane.showMessageDialog(this,
                                "Missing password");
                    }
                }
                else{ // error, username already exists
                    JOptionPane.showMessageDialog(this,
                            "User with this username already exists");
                }
            } else {
                // If editing user, check if password has changed
                if(!password.getText().equals("Change Password")) { // password change
                    String newUsername = usernameField.getText(); // get username
                    String newPassword = null;
                    // get password and hash
                    try {
                        newPassword = UserManager.hashPassword(password.getText());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    // initialise permission variables
                    String createBillboard;
                    String scheduleBillboard;
                    String editBillboard;
                    String editUsers;

                    // get permission variables
                    if (createBillboardsBox.isSelected()) {
                        createBillboard = "1";
                    } else {
                        createBillboard = "0";
                    }
                    if (scheduleBillboardsBox.isSelected()) {
                        scheduleBillboard = "1";
                    } else {
                        scheduleBillboard = "0";
                    }
                    if (editAllBillboardsBox.isSelected()) {
                        editBillboard = "1";
                    } else {
                        editBillboard = "0";
                    }
                    if (editUsersBox.isSelected()) {
                        editUsers = "1";
                    } else {
                        editUsers = "0";
                    }

                    // Edit user on the database
                    String[] user_inputs = {"Edit User", newUsername, newPassword, createBillboard, scheduleBillboard, editBillboard, editUsers};
                    ControlPanelClient.runClient(user_inputs);

                    // Close any control panels for refresh
                    Frame[] allFrames = Frame.getFrames();
                    if (adminUser) { // if full screen editor
                        for (Frame fr : allFrames) {
                            if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                                fr.dispose();
                            }
                        }
                        dispose();

                        //run Billboard Control Panel GUI to refresh
                        String[] user_input = {"List users", "Admin", ControlPanelClient.sessionToken};
                        ControlPanelClient.runClient(user_input);
                        forcedExit = false; // set exit to safe
                    } else { // only password screen, go back to gui home
                        dispose();
                        SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
                    }
                }
                else{
                    String newUsername = usernameField.getText(); // get username

                    // initialise permission variables
                    String createBillboard;
                    String scheduleBillboard;
                    String editBillboard;
                    String editUsers;

                    // get permission variables
                    if (createBillboardsBox.isSelected()) {
                        createBillboard = "1";
                    } else {
                        createBillboard = "0";
                    }
                    if (scheduleBillboardsBox.isSelected()) {
                        scheduleBillboard = "1";
                    } else {
                        scheduleBillboard = "0";
                    }
                    if (editAllBillboardsBox.isSelected()) {
                        editBillboard = "1";
                    } else {
                        editBillboard = "0";
                    }
                    if (editUsersBox.isSelected()) {
                        editUsers = "1";
                    } else {
                        editUsers = "0";
                    }

                    // Update user, except for password change
                    String[] user_inputs = {"Edit User Keep Password", newUsername, createBillboard, scheduleBillboard, editBillboard, editUsers};
                    ControlPanelClient.runClient(user_inputs);

                    // Close any user control panel
                    Frame[] allFrames = Frame.getFrames();
                    if (adminUser) { // if full screen editor
                        for (Frame fr : allFrames) {
                            if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                                fr.dispose();
                            }
                        }
                        dispose();

                        //run Billboard Control Panel GUI
                        String[] user_input = {"List users", "Admin", ControlPanelClient.sessionToken};
                        //request schedule and run calendar GUI
                        ControlPanelClient.runClient(user_input);
                        forcedExit = false; // set exit to safe
                    } else { // only password screen, go back to gui home
                        dispose();
                        SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
                    }
                }
            }
        }
        else{ // error, Missing Username
            JOptionPane.showMessageDialog(this,
                    "Missing username");
        }


    }
}


