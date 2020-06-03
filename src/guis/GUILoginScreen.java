package guis;

import network.ControlPanelClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login Screen class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Login Screen
 * @author - Nickhil Nischal (GUI) Jarod Evans (Implementation)
 * @version - Final
 *
 * NOTES: User credentials check to be added
 */
public class GUILoginScreen extends JFrame implements Runnable, ActionListener {
    private JButton loginButton;
    private JTextField username;
    private JTextField password;

    /**
     * Method used to create a GUI window for the Login screen
     */
    public GUILoginScreen() {
        // Set window title
        super("Billboard Control Panel Login Screen");
    }

    /**
     * Method used to create a GUI window for the Login screen
     *
     * @throws ClassNotFoundException          Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException          Exception handling
     * @throws IllegalAccessException          Exception handling
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Make window non resizable
        setResizable(false);

        // Create title label, inside of a JPanel
        JPanel titlePanel = newLabel("Welcome to the Billboard Control Panel", true, true);
        titlePanel.add(Box.createHorizontalStrut(100));

        // Create subtitle label, inside of a JPanel
        JPanel subtitlePanel = newLabel("Please enter login details to begin", true, false);
        subtitlePanel.add(Box.createHorizontalStrut(100));

        // Create username label, inside of a JPanel
        JPanel usernamePanel = newLabel("Username     ", false, false);

        // Create username text field, and add to the username JPanel
        username = newTextField(usernamePanel);

        // Create password label, inside of a JPanel
        JPanel passwordPanel = newLabel("Password      ", false, false);

        // Create password text field, and add to the password JPanel
        password = newTextField(passwordPanel);

        // Create JPanel for the login button
        JPanel loginPanel = newButtonPanel();

        // Create login button, inside of login JPanel
        loginButton = newButton("Login", loginPanel);

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(titlePanel);
        getContentPane().add(Box.createVerticalStrut(40));
        getContentPane().add(subtitlePanel);
        getContentPane().add(Box.createVerticalStrut(20));
        getContentPane().add(usernamePanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(passwordPanel);
        getContentPane().add(Box.createVerticalStrut(20));
        getContentPane().add(loginPanel);
        getContentPane().add(Box.createVerticalStrut(50));

        // Display window
        setLocation(new Point(100, 100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * This method creates a JLabel, inside of a JPanel
     *
     * @param labelText    The text of the JLabel
     * @param large        Boolean for large text
     * @param largeAndBold Boolean for large and bold text
     * @return Returns a JPanel
     */
    private JPanel newLabel(String labelText, boolean large, boolean largeAndBold) {
        // Create JPanel
        JPanel labelPanel = new JPanel();

        // Format JPanel
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.add(Box.createHorizontalStrut(100));

        // Create new JLabel
        JLabel label = new JLabel(labelText);

        // If large text size has been requested
        if (large) {
            // Change text size of JLabel
            label.setFont(new Font(null, Font.PLAIN, 12));
        }

        // If large text size and bold has been requested
        if (largeAndBold) {
            // Change text size of JLabel, and make it bold
            label.setFont(new Font(null, Font.BOLD, 14));
        }

        // Add the JLabel to the created JPanel
        labelPanel.add(label);


        // Returns a JPanel with a JLabel inside
        return labelPanel;
    }

    /**
     * This method creates a JTextField, and adds it to a specified JPanel
     *
     * @param panel The JPanel that the created JTextField is to be added to
     * @return Returns a JTextField
     */
    private JTextField newTextField(JPanel panel) {
        // Create JTextField
        JTextField textField;

        // Format JTextField
        textField = new JTextField(15);

        // Add JTextField to the specified JPanel
        panel.add(textField);

        // Format specified JPanel
        panel.add(Box.createHorizontalStrut(100));

        // Returns a JText field
        return textField;
    }

    /**
     * This method creates a JPanel which can hold a button
     *
     * @return Returns a JPanel
     */
    private JPanel newButtonPanel() {
        // Create JPanel
        JPanel buttonPanel = new JPanel();

        // Format JPanel
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        // Returns a JPanel
        return buttonPanel;
    }

    /**
     * This method creates a JButton, and adds it to a specified JPanel
     *
     * @param buttonName The text that will be displayed inside the button
     * @param panel      The JPanel that the created JButton is to be added to
     * @return Returns a JButton
     */
    private JButton newButton(String buttonName, JPanel panel) {
        JButton button = new JButton(buttonName);

        //Add the frame as an actionListener
        button.addActionListener(this);

        // Add JButton to specified JPanel
        panel.add(button);

        // Returns a JButton
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Username and Password variables from JTextFields
        String usernameText = username.getText();
        String passwordText = password.getText();

        String userName = usernameText;
        String password = passwordText;

        // Checks if the login button has been clicked
        if (buttonClicked == loginButton) {
            //Run control panel with the login request
            String[] user_inputs = {"Login request", userName, password};
            ControlPanelClient.runClient(user_inputs);
            dispose();
        }
    }


    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

}
