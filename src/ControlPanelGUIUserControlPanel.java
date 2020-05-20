import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User Control Panel class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the User Control Panel
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; some functionality still needs to be added; further refinement required
 */
public class ControlPanelGUIUserControlPanel extends JFrame implements Runnable, ActionListener, ListSelectionListener {
    JTextField search;
    JList list;
    JLabel userSelectionLabel;
    JButton editUserButton;
    JButton deleteUserButton;
    JButton createUserButton;

    /**
     * Method used to create a GUI window for the User Control Panel
     */
    public ControlPanelGUIUserControlPanel() {
        // Set window title
        super("User Control Panel");
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

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

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

        // Create user list, inside of left JPanel
        list = newUserList(leftPanel);

        // Create right JPanel, with Y axis Box Layout
        JPanel rightPanel = newPanel('Y');
        // Formatting
        rightPanel.add(Box.createVerticalStrut(50));

        // Create user selection JLabel, inside of right JPanel
        userSelectionLabel = newLargeTitle(" ", rightPanel);

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
     * This method creates a JList from a populated String array, and adds it to a specified JPanel
     * @param panel The JPanel that the created JLabel is to be added to
     * @return Returns a JList
     */
    private JList newUserList(JPanel panel) {
        // String array with all users
        // NEEDS TO BE CHANGED TO FETCH REAL USER INFORMATION
        String users[] = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6", "User 7", "User 9", "User 10"};

        // Create JList
        JList list = new JList(users);

        // Create JScrollPane
        JScrollPane scroll = new JScrollPane(list);

        // Add JScrollPane to the specified JPanel
        panel.add(scroll);

        // Formatting
        panel.add(Box.createVerticalStrut(50));

        // Listener for user selection
        // When a user is clicked from the user list, it is displayed above the Edit User and Delete User buttons
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the login button has been clicked
        if (buttonClicked == editUserButton) {
            System.out.println("edit user button clicked with " + userSelectionLabel.getText() + " selected");
            //run user (to check if admin), then if admin open Create/Edit User GUI
            //SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel());
        }

        // Checks if the delete user button has been clicked
        else if (buttonClicked == deleteUserButton) {
            System.out.println("delete user button clicked with " + userSelectionLabel.getText() + " selected");
            //run user (to check if admin), then if admin open dialog double checking if user should be deleted or not
            //SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel());
        }

        // Checks if the create user button has been clicked
        else if (buttonClicked == createUserButton) {
            System.out.println("create user button clicked");
            //run user (to check if admin), then if admin open Create/Edit User GUI
            //SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel());
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent selectionEvent) {
        // If an option is clicked in the JList, the user selection label is changed to display the option clicked
        userSelectionLabel.setText(list.getSelectedValue().toString());
    }

    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method which creates a GUI window for the User Control Panel
     * @param args This method takes no arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel());
    }
}
