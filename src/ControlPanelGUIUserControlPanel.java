import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * User Control Panel class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the User Control Panel
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; some functionality still needs to be added; further refinement required
 */
public class ControlPanelGUIUserControlPanel extends JFrame {
    /**
     * Method used to create a GUI window for the User Control Panel
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public ControlPanelGUIUserControlPanel() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Set window title
        super("User Control Panel");

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add search label, inside of a JPanel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.add(new JLabel("Search User"));
        searchPanel.add(Box.createHorizontalStrut(10));

        // Add search text field, inside search JPanel
        JTextField search;
        search = new JTextField(10);
        searchPanel.add(search);

        // Add search panel, inside of a JPanel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.add(Box.createVerticalStrut(50));
        userPanel.add(searchPanel);
        userPanel.add(Box.createVerticalStrut(20));

        // Add user list, inside of user JPanel
        String users[] = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6", "User 7", "User 9", "User 10"};
        JList list = new JList(users);
        userPanel.add(list);
        userPanel.add(Box.createVerticalStrut(50));

        // Add user selection, inside of a JPanel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(Box.createVerticalStrut(50));
        JLabel userSelectionLabel = new JLabel("");
        userSelectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userSelectionLabel.setFont(new Font(null, Font.BOLD, 15));
        buttonsPanel.add(userSelectionLabel);
        buttonsPanel.add(Box.createVerticalStrut(5));

        // Listener for user selection
        // When a user is clicked from the user list, it is displayed above the Edit User and Delete User buttons
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                userSelectionLabel.setText(list.getSelectedValue().toString());
            }
        });

        // Add Edit User button, inside of a JPanel
        JPanel buttons2Panel = new JPanel();
        buttons2Panel.setLayout(new BoxLayout(buttons2Panel, BoxLayout.X_AXIS));
        JButton editUserButton = new JButton("     Edit User     ");
        buttons2Panel.add(editUserButton);

        // Add Delete User button, inside of buttons2 JPanel
        JButton deleteUserButton = new JButton("     Delete User     ");
        buttons2Panel.add(deleteUserButton);
        buttonsPanel.add(buttons2Panel);
        buttonsPanel.add(Box.createVerticalStrut(40));

        // Add Create User button, inside of buttons panel
        JButton createUserButton = new JButton("Create User");
        createUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.add(createUserButton);
        buttonsPanel.add(Box.createVerticalStrut(50));

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(Box.createHorizontalStrut(100)); // boundary
        getContentPane().add(userPanel);
        getContentPane().add(Box.createHorizontalStrut(50));
        getContentPane().add(buttonsPanel);
        getContentPane().add(Box.createHorizontalStrut(100)); // boundary

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * Main method which creates a GUI window for the User Control Panel
     * @param args This method takes no arguments
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new ControlPanelGUIUserControlPanel();
    }
}
