import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;

import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * User Control Panel class for Control Panel GUI
 * This class contains a method that creates a GUI window for the User Control Panel
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Some button functionality still needs to be added
 */
public class ControlPanelGUIUserControlPanel extends JFrame implements Runnable, ActionListener, ListSelectionListener, DocumentListener, WindowListener {
    // Back JButton
    private JButton backButton;

    // Logout JButton
    private JButton logoutButton;

    // Search JTextField
    private JTextField search;

    // User JList
    private JList list;

    // User Selection JLabel
    private JLabel userSelectionLabel;
    private String userSelected;

    // Edit User JButton
    private JButton editUserButton;

    // Delete User JButton
    private JButton deleteUserButton;

    // Create User JButton
    private JButton createUserButton;

    // Master Array of Users, used in user search checking
    private String[] usersMasterArray;

    // Variable list used to populate JList when user is searching
    private DefaultListModel userListModel = new DefaultListModel();

    String username;
    String sessionToken;
    HashMap<String, User> userList;
    boolean closeable = true;
    String openedUser;


    /**
     * Method used to create a GUI window for the User Control Panel
     */
    public ControlPanelGUIUserControlPanel(String username, String sessionToken, HashMap<String, User> userList) {
        // Set window title
        super("User Control Panel");
        this.username = username;
        this.sessionToken = sessionToken;
        this.userList = userList;
    }

    /**
     * Method used to create a GUI window for the User Control Panel screen
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation set to Dispose on Close, so when user closes this screen, only this screen closes (keeps Control Panel GUI running)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //make window non resizable
        setResizable(false);

        // Create main content JPanel, with Y axis Box Layout
        JPanel topButtonPanel = newPanel('X');

        // Add strut for formatting
        topButtonPanel.add(Box.createHorizontalStrut(20));


        // Create back JButton
        backButton = newButton("Back", topButtonPanel);

        // Add horizontal glue in between buttons for spacing
        topButtonPanel.add(Box.createHorizontalGlue());


        // Create logout JButton
        logoutButton = newButton("Logout", topButtonPanel);

        // Add strut for formatting
        topButtonPanel.add(Box.createHorizontalStrut(20));

        // Create main content JPanel, with Y axis Box Layout
        JPanel mainPanel = newPanel('X');

        // Create left JPanel, with Y axis Box Layout
        JPanel leftPanel = newPanel('Y');

        // Add strut for formatting
        leftPanel.add(Box.createVerticalStrut(50));

        // Create search JPanel, with X axis Box Layout
        JPanel searchPanel = newPanel('X');

        // Create search user JLabel, inside of search JPanel
        newTitle("Search User", searchPanel);

        // Create search JTextField, inside search JPanel
        search = newTextField(searchPanel);

        // Create search JPanel, inside of left JPanel
        leftPanel.add(searchPanel);

        // Add strut for formatting
        leftPanel.add(Box.createVerticalStrut(20));

        // Populate the master array of users, with user information
        usersMasterArray = populateUserArray(userList);

        // Create user JList which is linked to the User List Model, populated with data from Users Master Array, inside of left JPanel
        list = newList(userListModel, usersMasterArray, leftPanel);

        // Create right JPanel, with Y axis Box Layout
        JPanel rightPanel = newPanel('Y');

        // Add strut for formatting
        rightPanel.add(Box.createVerticalStrut(50));

        // Create user selection JLabel, inside of right JPanel
        userSelectionLabel = newLargeTitle("", rightPanel);
        userSelectionLabel.setText("No User Selected");

        // Create buttons JPanel, with X axis Box Layout
        JPanel buttonsPanel = newPanel('X');

        // Create Edit User button, inside of buttons JPanel
        editUserButton = newButton("     Edit User     ", buttonsPanel);

        // Create Delete User button, inside of buttons JPanel
        deleteUserButton = newButton("     Delete User     ", buttonsPanel);

        // Add buttons JPanel to right JPanel
        rightPanel.add(buttonsPanel);

        // Add strut for formatting
        rightPanel.add(Box.createVerticalStrut(40));

        // Create Create User button, inside of right JPanel
        createUserButton = newButton("Create User", rightPanel);

        // Main JPanel window formatting
        mainPanel.add(Box.createHorizontalStrut(100));
        mainPanel.add(leftPanel);
        mainPanel.add(Box.createHorizontalStrut(50));
        mainPanel.add(rightPanel);
        mainPanel.add(Box.createHorizontalStrut(100));

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(20)); // boundary
        getContentPane().add(topButtonPanel);
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createVerticalStrut(50)); // boundary

        // Add Window Listener, used for when window is closed
        addWindowListener(this);

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * This method creates a new JPanel, on specified X_AXIS layout or Y_AXIS layout
     * @param axis The specified layout axis as a char (X or Y)
     * @return Returns a JPanel
     */
    private JPanel newPanel(char axis) {
        // Create new JPanel
        JPanel panel = new JPanel();

        // Check if 'X' axis has been requested
        if (axis == 'X') {
            // Create X_AXIS BoxLayout
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        }

        // Check if 'Y' axis has been requested
        else if (axis == 'Y') {
            // Create Y_AXIS BoxLayout
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        }

        // Returns a JPanel
        return panel;
    }

    /**
     * This method creates a JLabel, and adds it to a specified JPanel
     * @param title The text of the JLabel
     * @param panel The JPanel that the created JLabel is to be added to
     * @return Returns a JLabel
     */
    private JLabel newTitle(String title, JPanel panel) {
        // Create new JLabel
        JLabel label = new JLabel(title);

        // Add JLabel to specified JPanel
        panel.add(label);

        // Formatting
        panel.add(Box.createHorizontalStrut(10));

        // Returns a JLabel
        return label;
    }

    /**
     * This method populates an array with real user information
     * @return Returns a String array
     */
    private String[] populateUserArray(HashMap<String, User> userList) {

        // String array with all users
        // NOTE: NEEDS TO BE CHANGED TO FETCH REAL USER INFORMATION

        String[] users = userList.keySet().toArray(new String[userList.size()]);
        for(int i = 0; i < users.length; i++){
            for(int j = i + 1; j < users.length; j++){
                if(users[i].compareTo(users[j]) > 0){
                    String temp = users[i];
                    users[i] = users[j];
                    users[j] = temp;
                }
            }
        }

        // Returns a String array of users
        return users;
    }

    /**
     * This method creates a JList from a specified list model, then populates the list model from a populated String array,
     *  then the JList is added to a specified JPanel
     * @param listModel The list model to be written to
     * @param array The array to fetch information from
     * @param panel The JPanel that the created JList is to be added to
     * @return Returns a JList
     */
    private JList newList(DefaultListModel listModel, String[] array, JPanel panel) {

        // Create JList
        JList list = new JList(listModel);

        // For each element in the provided array, add the element to the list model
        for (String a : array) {
            // Add element a to the list model
            listModel.addElement(a);
        }

        // Create JScrollPane
        JScrollPane scroll = new JScrollPane(list);

        // Add JScrollPane to the specified JPanel
        panel.add(scroll);

        // Listener for user selection
        // When a user is clicked from the user list, the selection is displayed above the Edit User and Delete User buttons
        list.addListSelectionListener(this);

        // Returns a JList
        return list;
    }

    /**
     * This method creates a JLabel with large (and bold) text, and adds it to a specified JPanel
     * @param title The text of the JLabel
     * @param panel The JPanel that the created JLabel is to be added to
     * @return Returns a JLabel
     */
    private JLabel newLargeTitle(String title, JPanel panel) {
        // Create new JLabel
        JLabel label = new JLabel(title);

        // Centre the JLabel
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Set large size of the JLabel text, and make it bold
        label.setFont(new Font(null, Font.BOLD, 15));

        // Add the JLabel to the specified JPanel
        panel.add(label);

        // Formatting
        panel.add(Box.createVerticalStrut(5));

        // Returns a JLabel
        return label;
    }

    /**
     * This method creates a JTextField, and adds it to a specified JPanel
     * @param panel The JPanel that the created JTextField is to be added to
     * @return Returns a JTextField
     */
    private JTextField newTextField(JPanel panel) {
        // Create new JTextField
        JTextField textField = new JTextField(10);

        // Add document listener for text field search
        textField.getDocument().addDocumentListener(this);

        // Add the JTextField to the specified JPanel
        panel.add(textField);

        // Returns a JTextField
        return textField;
    }

    /**
     * This method creates a JButton, and adds it to a specified JPanel
     * @param buttonName The text that will be displayed inside the button
     * @param panel The JPanel that the created JButton is to be added to
     * @return Returns a JButton
     */
    private JButton newButton(String buttonName, JPanel panel) {
        //Create JButton
        JButton button = new JButton(buttonName);

        // Centre the JButton
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add the frame as an actionListener
        button.addActionListener(this);

        // Add JButton to specified JPanel
        panel.add(button);

        // Returns a JButton
        return button;
    }

    /**
     * This method checks the user's search within a given array, and updates the search results accordingly in the JList live
     * Used for the Document Listener (insertUpdate, removeUpdate, and changedUpdate)
     * @param model The model list to be changed, translates to the JList
     * @param array The master array to be checked against
     */
    private void listSearch(DefaultListModel model, String[] array) {
        // Converts any input by the user to lowercase characters (making the check case insensitive)
        String textLower = search.getText().toLowerCase();

        // Iterates through each element in the provided master array
        for (String a : array) {
            //  Checks if string entered in JTextField is not present in the master array (lowercase for case insensitivity)
            if (!a.toLowerCase().contains(textLower)) {
                // If list model already contains the element a
                if (model.contains(a)) {
                    // If element a is the same as the current user selection JLabel, clear the JLabel
                    if (userSelectionLabel.getText().equals(a)) {
                        // Clear any selection made by the user in the JList
                        list.clearSelection();

                        // Clear the user selection label
                        userSelectionLabel.setText("");
                    }

                    // Remove the element a from the model
                    model.removeElement(a);
                }
            }
            // The string entered is present in the master array
            else {
                // If list model does not already contain the element a
                if (!model.contains(a)) {
                    // Add the the element a to the model
                    model.addElement(a);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the back button has been clicked
        if (buttonClicked == backButton) {
            // Closes current GUI screen
            dispose();
            closeable = false;

            // Open new Control Panel GUI screen
            SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
        }

        // Checks if the logout button has been clicked
        else if (buttonClicked == logoutButton) {
            // Closes current GUI screen
            dispose();
            closeable = false;

            // Open new Login screen
            SwingUtilities.invokeLater(new ControlPanelGUILoginScreen());
        }

        // Checks if the login button has been clicked
        else if (buttonClicked == editUserButton) {
            boolean selfEdit = false;
            String usernameSelected  = userSelectionLabel.getText();
            if(usernameSelected.equals(username)){
                selfEdit = true;
            }
            if(usernameSelected.equals("No User Selected"))
            {
                // Display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a user in the list to edit");
            }
            else{
                // Retrieve the user associated with the name
                try {
                    int frameCount = 0;
                    Frame[] allFrames = Frame.getFrames();
                    for(Frame fr : allFrames){
                        if((fr.getClass().getName().equals("ControlPanelGUICreateEditUser"))){
                            if(fr.isVisible()){
                                frameCount += 1;
                            }
                        }
                    }

                    User intendedUser = UserList.getUserInformation(userList, userSelectionLabel.getText());
                    System.out.println("edit user: "+ usernameSelected);
                    if(!closeable) {
                        if(frameCount > 0) {
                            int a = showConfirmDialog(null, "This will close your current edit user screen and you will lose any changes");
                            if (a == YES_OPTION) {
                                allFrames = Frame.getFrames();
                                for (Frame fr : allFrames) {
                                    if ((fr.getClass().getName().equals("ControlPanelGUICreateEditUser"))) {
                                        fr.dispose();
                                    }
                                }
                                SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, "1234", intendedUser, true, selfEdit, userList));
                            }
                        }
                        else{
                            SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, "1234", intendedUser, true, selfEdit, userList));
                        }
                    }
                    else{
                        SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, "1234", intendedUser, true, selfEdit, userList));
                    }
                    openedUser = usernameSelected;
                    closeable =  false;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "You must select a user in the list to edit");
                }
            }
        }

        // Checks if the delete user button has been clicked
        else if (buttonClicked == deleteUserButton) {
            System.out.println("delete user button clicked with " + userSelectionLabel.getText() + " selected");
            if (userSelectionLabel.getText().equals("No User Selected")) {
                JOptionPane.showMessageDialog(this,
                        "You have not selected a user");
            }
            else if(userSelectionLabel.getText().equals(openedUser)){
                JOptionPane.showMessageDialog(this,
                        "You can't delete a user that you are currently editing");
            }
            else {
                try {
                    User baseUser = UserList.getUserInformation(userList, "AdminUser");
                    User intendedUser = UserList.getUserInformation(userList, userSelectionLabel.getText());
                    UserManager deleteUser = new UserManager(baseUser, intendedUser);
                    if(deleteUser.deleteUser()){
                        String[] user_inputs = {"Delete User", intendedUser.Username};
                        ControlPanelClient.Run_Client(user_inputs);
                        user_inputs = new String[]{"List users", "Admin"};
                        ControlPanelClient.Run_Client(user_inputs);
                        closeable = false;
                        dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(this,
                                "You can't delete yourself!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Checks if the create user button has been clicked
        else if (buttonClicked == createUserButton) {
            System.out.println("create user button clicked");
            try {
                int frameCount = 0;
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("ControlPanelGUICreateEditUser"))){
                        if(fr.isVisible()){
                            frameCount += 1;
                        }
                    }
                }
                if(!closeable) {
                    if(frameCount > 0){
                        int a = showConfirmDialog(null, "This will close your current edit user screen and you will lose any changes");
                        if(a == YES_OPTION) {
                            allFrames = Frame.getFrames();
                            for(Frame fr : allFrames){
                                if((fr.getClass().getName().equals("ControlPanelGUICreateEditUser"))){
                                    fr.dispose();
                                }
                            }
                            SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser("admin", "1234", true, userList));
                        }
                    }
                    else{
                        SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser("admin", "1234", true, userList));
                    }
                }
                else{
                    SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser("admin", "1234", true, userList));
                }
                closeable = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent selectionEvent) {
        // If an option is currently selected in the JList, the user selection label is changed to display the option clicked,
        if (!list.isSelectionEmpty()) {
            // Set the user selection label text to be the value selected in the JList
            userSelectionLabel.setText(list.getSelectedValue().toString());
            userSelected = list.getSelectedValue().toString();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        // Execute search
        listSearch(userListModel, usersMasterArray);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Execute search
        listSearch(userListModel, usersMasterArray);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Execute search
        listSearch(userListModel, usersMasterArray);
    }

    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // When this window is being closed, a new Control Panel GUI is opened (simulates going back to previous screen)
        if(closeable) {
            SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
        }
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
}
