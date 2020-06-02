package guis;

import network.ControlPanelClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * Control Panel GUI class
 * This class contains a Main method and method that creates a GUI window for the Control Panel GUI screen
 * @author - Nickhil Nischal
 * @version - Complete
 */
public class GUIMainMenu extends JFrame implements Runnable, ActionListener {
    private JButton logoutButton;
    private JButton editUsersButton;
    private JButton editBillboardButton;
    private JButton viewBillboardScheduleButton;
    private JButton userDetailsButton;
    String username;
    String sessionToken;

    /**
     * Method used to create a GUI window for the Control Panel Menu
     * @param username The user's username
     * @param sessionToken The user's session token
     */
    public GUIMainMenu(String username, String sessionToken){
        // Set window title
        super("Control Panel Menu");
        // The user's username
        this.username = username;

        // The user's session token
        this.sessionToken = sessionToken;
    }

    /**
     * Method used to create a GUI window for the Control Panel GUI screen
     * @throws ClassNotFoundException Exception thrown by setLookAndFeel: when an application tries to load in a class
     * through its string name but no definition for the class with the specified name could be found.
     * @throws InstantiationException Exception thrown by setLookAndFeel: an exception that indicates the requested
     * look & feel management classes are not present on the user's system.
     * @throws IllegalAccessException Exception thrown by setLookAndFeel: when an application tries to create an
     * instance of a class using the newInstance method in class Class, but the specified class object cannot be instantiated
     * @throws UnsupportedLookAndFeelException Exception thrown by setLookAndFeel: an IllegalAccessException is thrown
     * when an application tries to reflectively create an instance (other than an array), set or get a field, or invoke
     * a method, but the currently executing method does not have access to the definition of the specified class, field,
     * method or constructor.
     */
    private void createGUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Make window non resizable
        setResizable(false);

        // Create logout JButton
        logoutButton = createButton("Logout");

        // Create JPanel that holds logout JButton
        JPanel logoutPanel = createButtonJPanel(logoutButton,0,20, true);

        // Create JLabel with a new Control Panel Menu JLabel
        JPanel label = createLabel("Control Panel Menu");

        // Create edit users JButton
        editUsersButton = createButton("Manage Users");

        // Create edit users JPanel that holds the edit users JButton
        JPanel editUsersPanel = createButtonJPanel(editUsersButton,150,150,false);

        // Create edit billboard JButton
        editBillboardButton = createButton("Manage Billboards");

        // Create edit billboard JPanel that holds the edit billboard JButton
        JPanel editBillboardPanel = createButtonJPanel(editBillboardButton,150,150,false);

        // Create view billboard schedule JButton
        viewBillboardScheduleButton = createButton("View Billboard Schedule");

        // Create view billboard schedule JPanel that holds the view billboard schedule JButton
        JPanel editBillboardSchedulePanel = createButtonJPanel(viewBillboardScheduleButton,150,150,false);

        // Create user details JButton
        userDetailsButton = createButton("My Details");

        // Create edit password JPanel that holds the edit password JButton
        JPanel editPasswordChangePanel = createButtonJPanel(userDetailsButton,150,150,false);

        // Add all JLabel and JPanels to content pane
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(20)); // Border
        getContentPane().add(logoutPanel);
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(label);
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(editUsersPanel);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editBillboardPanel);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editBillboardSchedulePanel);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editPasswordChangePanel);
        getContentPane().add(Box.createVerticalStrut(50)); // Border

        // Format window
        pack(); // Pack all contents of the GUI
        setLocationRelativeTo(null); // Centers window
        setVisible(true); // Make GUI visible
    }

    /**
     * This method creates a JLabel, inside of a JPanel
     * @param labelText The text of the JLabel
     * @return Returns a JPanel
     */
    private JPanel createLabel(String labelText) {
        // Create new JPanel
        JPanel labelPanel = new JPanel();

        // Set box layout
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));

        // Create new JLabel
        JLabel label = new JLabel(labelText);

        // Change text size of JLabel
        label.setFont(new Font(null, Font.BOLD, 15));

        // Center align label
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add JLabel to JPanel
        labelPanel.add(label);

        // Returns a JPanel
        return labelPanel;
    }

    /**
     * Creates a JButton, inside of a JPanel
     * @param button The text displayed o the JButton
     * @param strut1 The horizontal strut size to add before the JPanel
     * @param strut2 The horizontal strut size to add after the JPanel
     * @param horizontalGlue The boolean to specify whether horizontal glue is to be added or not
     * @return Returns a JPanel
     */
    private JPanel createButtonJPanel(JButton button, int strut1, int strut2, boolean horizontalGlue) {
        // Create new JPanel to hold button
        JPanel buttonPanel = new JPanel();

        // Set box layout on X Axis
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        // Add horizontal strut for formatting
        buttonPanel.add(Box.createHorizontalStrut(strut1));

        // If horizontal glue has been requested, add horizontal glue
        if (horizontalGlue) {
            // Add horizontal glue for right alignment of JPanel
            buttonPanel.add(Box.createHorizontalGlue());
        }

        // Add new button to button JPanel
        buttonPanel.add(button);

        // Add horizontal strut for formatting
        buttonPanel.add(Box.createHorizontalStrut(strut2));

        // Centre align the JPanel
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Returns a JPanel
        return buttonPanel;
    }

    /**
     * This method creates a JButton, given text to display inside of it
     * @param buttonLabel The text to display inside the JButton
     * @return Returns a JButton
     */
    private JButton createButton(String buttonLabel) {
        // Create a JButton object and store it in a local variable
        JButton button = new JButton(buttonLabel);

        // Center align button
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add the frame as an actionListener
        button.addActionListener(this);

        // Returns a JButton
        return button;
    }


    /**
     * Listens to button presses and performs the appropriate action/s
     * @param actionEvent JButton pressed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // If logout JButton is clicked
        if (buttonClicked == logoutButton) {
            // Remove users session token and proceed to the login screen
            String[] user_input = {"Logout request", ControlPanelClient.sessionToken};
            ControlPanelClient.runClient(user_input);
            dispose();
        }

        // If edit users JButton is clicked
        else if (buttonClicked == editUsersButton) {
            // Open User Control Panel GUI
            String[] user_input = {"List users", "Admin", ControlPanelClient.sessionToken};
            ControlPanelClient.runClient(user_input);
        }

        // If edit billboard JButton is clicked
        else if (buttonClicked == editBillboardButton) {
            // Run Billboard Control Panel GUI
            String [] user_input = {"List billboards", ControlPanelClient.sessionToken};
            // Request schedule and run calendar GUI
            ControlPanelClient.runClient(user_input);
            dispose();
        }

        // If view billboard schedule JButton is clicked
        else if (buttonClicked == viewBillboardScheduleButton) {
            String [] userInput = {"View schedule", ControlPanelClient.sessionToken};
            //request schedule and run calendar GUI
            ControlPanelClient.runClient(userInput);
        }

        // If user details JButton is clicked
        else if (buttonClicked == userDetailsButton) {
            String [] user_input = {"List users", "Password", ControlPanelClient.sessionToken};
            //request schedule and run calendar GUI
            ControlPanelClient.runClient(user_input);

            // Close the GUI screen
            dispose();
        }
    }

    /**
     * Populates and runs the GUI
     */
    @Override
    public void run() {
        // Try to run the GUI
        try {
            createGUI();
        }
        // Catches an exception and displays an appropriate error message dialog
        catch (ClassNotFoundException e) {
            e.printStackTrace();

            // Show message dialog (popup)
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        // Catches an exception and displays an appropriate error message dialog
        catch (InstantiationException e) {
            e.printStackTrace();

            // Show message dialog (popup)
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        // Catches an exception and displays an appropriate error message dialog
        catch (IllegalAccessException e) {
            e.printStackTrace();

            // Show message dialog (popup)
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        // Catches an exception and displays an appropriate error message dialog
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();

            // Show message dialog (popup)
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
