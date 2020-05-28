import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Control Panel GUI class
 * This class contains a Main method and method that creates a GUI window for the Control Panel GUI screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Some button functionality still needs to be added
 */
public class ControlPanelGUI extends JFrame implements Runnable, ActionListener {
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

        // create label
        JPanel label = createLabel("Control Panel Menu");

        // create edit users button
        editUsersButton = createButton("Edit Users");
        //create JPanel that holds button
        JPanel editUsersPanel = createButtonJPanel(editUsersButton);

        // create edit billboard button
        editBillboardButton = createButton("Edit Billboards");
        //create JPanel that holds button
        JPanel editBillboardPanel = createButtonJPanel(editBillboardButton);

        // create edit billboard schedule button
        viewBillboardScheduleButton = createButton("View Billboard Schedule");
        //create JPanel that holds button
        JPanel editBillboardSchedulePanel = createButtonJPanel(viewBillboardScheduleButton);

        // create edit password button
        passwordChangeButton = createButton("Edit Password");
        // create JPanel that holds button
        JPanel editPasswordChangePanel = createButtonJPanel(passwordChangeButton);


        //Add label and buttons to content pane
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
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

        // Center align label
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add JLabel to JPanel
        labelPanel.add(label);

        // Returns a JPanel
        return labelPanel;
    }

    private JPanel createButtonJPanel(JButton button) {
        // Create new JPanel to hold button
        JPanel buttonPanel = new JPanel();

        // Set box layout
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        // Add horizontal strut for formatting
        buttonPanel.add(Box.createHorizontalStrut(150));

        // Add new button to button JPanel
        buttonPanel.add(button);

        // Add horizontal strut for formatting
        buttonPanel.add(Box.createHorizontalStrut(150));

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

        if (buttonClicked==editUsersButton) {
            // Open User Control Panel GUI
            String[] user_input = {"List users", "Admin"};
            ControlPanelClient.Run_Client(user_input);
        }

        else if (buttonClicked==editBillboardButton) {
            //run Billboard Control Panel GUI
            String [] user_input = {"List billboards"};
            //request schedule and run calendar GUI
            ControlPanelClient.Run_Client(user_input);
        }

        else if (buttonClicked == viewBillboardScheduleButton)
        {
            String [] user_input = {"View schedule"};
            //request schedule and run calendar GUI
            ControlPanelClient.Run_Client(user_input);
        }

        else if (buttonClicked == passwordChangeButton)
        {
            String [] user_input = {"List users", "Password"};
            //request schedule and run calendar GUI
            ControlPanelClient.Run_Client(user_input);
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
