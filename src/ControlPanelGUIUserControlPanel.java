import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * User Control Panel class for Control Panel GUI
 * This class contains a method that creates a GUI window for the User Control Panel
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Some button functionality still needs to be added
 */
public class ControlPanelGUIUserControlPanel extends JFrame implements Runnable, ActionListener, ListSelectionListener, DocumentListener {
    // Search JTextField
    JTextField search;

    // User JList
    JList list;

    // User Selection JLabel
    JLabel userSelectionLabel;

    // Edit User JButton
    JButton editUserButton;

    // Delete User JButton
    JButton deleteUserButton;

    // Create User JButton
    JButton createUserButton;

    // Master Array of Users, used in user search checking
    String[] usersMasterArray;

    // Variable list used to populate JList when user is searching
    DefaultListModel userListModel = new DefaultListModel();

    String username;
    String sessionToken;


    /**
     * Method used to create a GUI window for the User Control Panel
     */
    public ControlPanelGUIUserControlPanel(String username, String sessionToken) {
        // Set window title
        super("User Control Panel");
        this.username = username;
        this.sessionToken = sessionToken;
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

        // Create left JPanel, with Y axis Box Layout
        JPanel leftPanel = newPanel('Y');
        // Formatting
        leftPanel.add(Box.createVerticalStrut(50));

        // Create search JPanel, with X axis Box Layout
        JPanel searchPanel = newPanel('X');

        // Create search user JLabel, inside of search JPanel
        newTitle("Search User", searchPanel);

        // Create search JTextField, inside search JPanel
        search = newTextField(searchPanel);

        // Create search JPanel, inside of left JPanel
        leftPanel.add(searchPanel);
        // Formatting
        leftPanel.add(Box.createVerticalStrut(20));

        // Populate the master array of users, with user information
        usersMasterArray = populateUserArray();

        // Create user JList which is linked to the User List Model, populated with data from Users Master Array, inside of left JPanel
        list = newList(userListModel, usersMasterArray, leftPanel);

        // Create right JPanel, with Y axis Box Layout
        JPanel rightPanel = newPanel('Y');
        // Formatting
        rightPanel.add(Box.createVerticalStrut(50));

        // Create user selection JLabel, inside of right JPanel
        userSelectionLabel = newLargeTitle("", rightPanel);

        // Create buttons JPanel, with X axis Box Layout
        JPanel buttonsPanel = newPanel('X');

        // Create Edit User button, inside of buttons JPanel
        editUserButton = newButton("     Edit User     ", buttonsPanel);

        // Create Delete User button, inside of buttons JPanel
        deleteUserButton = newButton("     Delete User     ", buttonsPanel);

        // Add buttons JPanel to right JPanel
        rightPanel.add(buttonsPanel);
        // Formatting
        rightPanel.add(Box.createVerticalStrut(40));

        // Create Create User button, inside of right JPanel
        createUserButton = newButton("Create User", rightPanel);

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(Box.createHorizontalStrut(100)); // boundary
        getContentPane().add(leftPanel);
        getContentPane().add(Box.createHorizontalStrut(50));
        getContentPane().add(rightPanel);
        getContentPane().add(Box.createHorizontalStrut(100)); // boundary

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
    private String[] populateUserArray() {

        // String array with all users
        // NOTE: NEEDS TO BE CHANGED TO FETCH REAL USER INFORMATION
        String[] array = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6", "User 7", "User 9", "User 10"};

        return array;
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

        // Formatting
        panel.add(Box.createVerticalStrut(50));

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

        // Formatting
        panel.add(Box.createVerticalStrut(50));

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

        String userType = "admin"; // USED FOR TESTING

        // Checks if the login button has been clicked
        if (buttonClicked == editUserButton) {
            System.out.println("edit user button clicked with " + userSelectionLabel.getText() + " selected");
            //run user (to check if admin), then if admin open Create/Edit User GUI

            if (!userType.equals("admin")) {
                JOptionPane.showMessageDialog(this,
                        "You do not have Admin permissions to edit a user.");
            }

            else if (userSelectionLabel.getText().equals("") || userSelectionLabel.getText() == null) {
                // Add message dialog, a user has not been selected from the list
            }

            else {
                SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, sessionToken));
            }
        }

        // Checks if the delete user button has been clicked
        else if (buttonClicked == deleteUserButton) {
            System.out.println("delete user button clicked with " + userSelectionLabel.getText() + " selected");
            //run user (to check if admin), then if admin open dialog double checking if user should be deleted or not

            if (!userType.equals("admin")) {
                JOptionPane.showMessageDialog(this,
                        "You do not have Admin permissions to delete a user.");
            }

            else if (userSelectionLabel.getText().equals("") || userSelectionLabel.getText() == null) {
                // Add message dialog, a user has not been selected from the list
            }

            else {
                SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, sessionToken));
            }
        }

        // Checks if the create user button has been clicked
        else if (buttonClicked == createUserButton) {
            System.out.println("create user button clicked");
            //run user (to check if admin), then if admin open Create/Edit User GUI

            if (userType.equals("admin")) {
                SwingUtilities.invokeLater(new ControlPanelGUICreateEditUser(username, sessionToken));
            }

            else {
                // Display an Error Message Dialog, alerting the user that the entered credentials are incorrect
                JOptionPane.showMessageDialog(this,
                        "You do not have Admin permissions to create a new user.");
            }
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent selectionEvent) {
        // If an option is currently selected in the JList, the user selection label is changed to display the option clicked,
        if (!list.isSelectionEmpty()) {
            // Set the user selection label text to be the value selected in the JList
            userSelectionLabel.setText(list.getSelectedValue().toString());
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
}
