import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * Control Panel GUI class
 * This class contains a Main method and method that creates a GUI window for the Control Panel GUI screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Some button functionality still needs to be added
 */
public class ControlPanelGUI extends JFrame implements Runnable, ActionListener {
    private JButton logoutButton;
    private JButton editUsersButton;
    private JButton editBillboardButton;
    private JButton viewBillboardScheduleButton;
    private JButton passwordChangeButton;
    String username;
    String sessionToken;

    /**
     * Method used to create a GUI window for the Control Panel Menu
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public ControlPanelGUI(String username, String sessionToken){
        // Set window title
        super("Control Panel Menu");
        this.username = username;
        this.sessionToken = sessionToken;
    }

    private void createGUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //make window non resizable
        setResizable(false);

        // Create logout JButton
        logoutButton = createButton("Logout");
        // Create JPanel that holds logout JButton
        JPanel logoutPanel = createButtonJPanel(logoutButton,0,20, true);

        // Create JLabel
        JPanel label = createLabel("Control Panel Menu");

        // Create edit users JButton
        editUsersButton = createButton("Manage Users");
        //create JPanel that holds button
        JPanel editUsersPanel = createButtonJPanel(editUsersButton,150,150,false);

        // create edit billboard button
        editBillboardButton = createButton("Manage Billboards");
        //create JPanel that holds button
        JPanel editBillboardPanel = createButtonJPanel(editBillboardButton,150,150,false);

        // create edit billboard schedule button
        viewBillboardScheduleButton = createButton("View Billboard Schedule");
        //create JPanel that holds button
        JPanel editBillboardSchedulePanel = createButtonJPanel(viewBillboardScheduleButton,150,150,false);

        // create edit password button
        passwordChangeButton = createButton("Edit Password");
        // create JPanel that holds button
        JPanel editPasswordChangePanel = createButtonJPanel(passwordChangeButton,150,150,false);


        //Add label and buttons to content pane
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(20));
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
        getContentPane().add(Box.createVerticalStrut(50));

        // Format window
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

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

    private JPanel createButtonJPanel(JButton button, int strut1, int strut2, boolean horizontalGlue) {
        // Create new JPanel to hold button
        JPanel buttonPanel = new JPanel();

        // Set box layout
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


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // If logout JButton is clicked
        if (buttonClicked == logoutButton) {
            // Remove users session token and proceed to the login screen
            String[] user_input = {"Logout request", ControlPanelClient.sessionToken};
            ControlPanelClient.Run_Client(user_input);
            // Close the GUI screen
            dispose();
        }

        // If edit users JButton is clicked
        else if (buttonClicked == editUsersButton) {
            // Open User Control Panel GUI
            String[] user_input = {"List users", "Admin", ControlPanelClient.sessionToken};
            ControlPanelClient.Run_Client(user_input);
        }

        // If edit billboard JButton is clicked
        else if (buttonClicked == editBillboardButton) {
            // Run Billboard Control Panel GUI
            String [] user_input = {"List billboards", ControlPanelClient.sessionToken};
            // Request schedule and run calendar GUI
            ControlPanelClient.Run_Client(user_input);

            // Close the GUI screen
            dispose();
        }

        // If view billboard schedule JButton is clicked
        else if (buttonClicked == viewBillboardScheduleButton) {
            String [] userInput = {"View schedule", ControlPanelClient.sessionToken};
            //request schedule and run calendar GUI
            ControlPanelClient.Run_Client(userInput);
        }

        // If password change JButton is clicked
        else if (buttonClicked == passwordChangeButton) {
            String [] user_input = {"List users", "Password", ControlPanelClient.sessionToken};
            //request schedule and run calendar GUI
            ControlPanelClient.Run_Client(user_input);

            // Close the GUI screen
            dispose();
        }

    }

    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException e) {
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
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
