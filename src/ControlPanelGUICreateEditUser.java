import javax.swing.*;
import java.awt.*;

/**
 * Create/Edit User class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Create/Edit User Screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; some functionality still needs to be added; further refinement required
 */
public class ControlPanelGUICreateEditUser extends JFrame {
    /**
     * Method used to create a GUI window for the Create/Edit User Screen
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public ControlPanelGUICreateEditUser() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Set window title
        super("Create/Edit User");

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add username label, inside of a JPanel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalStrut(50));
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font(null, Font.PLAIN, 15));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(usernameLabel);

        // Add username text field, inside of left JPanel
        JTextField username;
        username = new JTextField(15);
        leftPanel.add(username);
        leftPanel.add(Box.createVerticalStrut(20));

        // Add password label, inside of left JPanel
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font(null, Font.PLAIN, 15));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(passwordLabel);

        // Add password text field, inside of left JPanel
        JTextField password;
        password = new JTextField(15);
        leftPanel.add(password);
        leftPanel.add(Box.createVerticalStrut(40));

        // Add Save and Exit button, inside of left panel
        JButton saveAndExitButton = new JButton("Save and Exit");
        saveAndExitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(saveAndExitButton);
        leftPanel.add(Box.createVerticalStrut(5));

        // Add Exit Without Saving button, inside of left panel
        JButton exitWithoutSavingButton = new JButton("Exit Without Saving");
        exitWithoutSavingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(exitWithoutSavingButton);
        leftPanel.add(Box.createVerticalStrut(50));

        // Creating a right column JPanel for spacing
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(Box.createVerticalStrut(50));

        // Creating another spacing JPanel, with a border
        JPanel spacer = new JPanel();
        spacer.setLayout(new BoxLayout(spacer, BoxLayout.X_AXIS));
        spacer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        spacer.add(Box.createHorizontalStrut(10));

        // Add User Permissions label, inside of a JPanel
        JPanel permissionsPanel = new JPanel();
        permissionsPanel.setLayout(new BoxLayout(permissionsPanel, BoxLayout.Y_AXIS));
        permissionsPanel.add(Box.createVerticalStrut(20));
        JLabel userPermissionsLabel = new JLabel("User Permissions");
        userPermissionsLabel.setFont(new Font(null, Font.PLAIN, 15));
        userPermissionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        permissionsPanel.add(userPermissionsLabel);
        permissionsPanel.add(Box.createVerticalStrut(20));

        // Add Create Billboards checkbox, inside of permissions JPanel
        JCheckBox createBillboardsBox = new JCheckBox("Create Billboards");
        createBillboardsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        permissionsPanel.add(createBillboardsBox);
        permissionsPanel.add(Box.createVerticalStrut(20));

        // Add Schedule Billboards checkbox, inside of permissions JPanel
        JCheckBox scheduleBillboardsBox = new JCheckBox("Schedule Billboards");
        scheduleBillboardsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        permissionsPanel.add(scheduleBillboardsBox);
        permissionsPanel.add(Box.createVerticalStrut(20));

        // Add Edit All Billboards checkbox, inside of permissions JPanel
        JCheckBox editAllBillboardsBox = new JCheckBox("Edit All Billboards");
        editAllBillboardsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        permissionsPanel.add(editAllBillboardsBox);
        permissionsPanel.add(Box.createVerticalStrut(20));

        // Add Edit Users (Admin) checkbox, inside of permissions JPanel
        JCheckBox editUsersBox = new JCheckBox("Edit Users (Admin)");
        editUsersBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        permissionsPanel.add(editUsersBox);

        // Add JPanels inside of each other, with Structs for spacing
        spacer.add(permissionsPanel);
        spacer.add(Box.createHorizontalStrut(10));
        rightPanel.add(spacer);
        permissionsPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(Box.createVerticalStrut(50));

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(Box.createHorizontalStrut(100)); // boundary
        getContentPane().add(leftPanel);
        getContentPane().add(Box.createHorizontalStrut(40));
        getContentPane().add(rightPanel);
        getContentPane().add(Box.createHorizontalStrut(100)); // boundary

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * Main method which creates a GUI window for the Create/Edit User Screen
     * @param args This method takes no arguments
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new ControlPanelGUICreateEditUser();
    }
}
