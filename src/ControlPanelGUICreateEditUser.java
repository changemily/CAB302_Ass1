import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Create/Edit User class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Create/Edit User Screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Some functionality still needs to be added
 */
public class ControlPanelGUICreateEditUser extends JFrame implements Runnable, ActionListener, WindowListener {
    private JTextField usernameField;
    private JTextField password;
    private JButton saveExitButton;
    private JButton exitWithoutSaving;
    private JCheckBox createBillboardsBox;
    private JCheckBox scheduleBillboardsBox;
    private JCheckBox editAllBillboardsBox;
    private JCheckBox editUsersBox;
    String username;
    String sessionToken;
    boolean adminUser;
    boolean newUser;
    User targetUser;
    HashMap<String, User> userList;
    boolean forcedExit = true;

    /**
     * Method used to create a GUI window for the Create/Edit User Screen
     */
    public ControlPanelGUICreateEditUser(String username, String sessionToken, boolean newUser, HashMap<String, User> userList) throws Exception {
        // Set window title
        super("Create/Edit User");
        this.username = username;
        this.sessionToken = sessionToken;
        this.adminUser = true;
        this.newUser = newUser;
        this.targetUser = new User("", "", "");
        this.userList = userList;
    }

    public ControlPanelGUICreateEditUser(String username, String sessionToken, User targetUser, boolean adminUser, HashMap<String, User> userList){
        super("Create/Edit User");
        this.username = username;
        this.sessionToken = sessionToken;
        this.adminUser = adminUser;
        this.targetUser = targetUser;
        this.newUser = false;
        this.userList = userList;
    }


    /**
     * Method used to create a GUI window for the Create/Edit User screen
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

        // Create a left column JPanel for formatting
        JPanel leftPanel = newFormatPanel();

        // Create username JLabel, add to left JPanel
        newLabel("Username", leftPanel);

        // Create username JTextField, add to left JPanel
        usernameField = newTextField(leftPanel);
        if(newUser) {
            usernameField.setEditable(true);
        }
        else{
            usernameField.setEditable(false);
            usernameField.setText(targetUser.Username);
        }

        // Create password JLabel, add to left JPanel
        newLabel("Password", leftPanel);

        // Create password JTextField, add to left JPanel
        password = newTextField(leftPanel);
        password.setText(targetUser.Password);
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
        if(targetUser.Permissions.contains("Create Billboards")){
            createBillboardsBox.setSelected(true);
        }

        // Create Schedule Billboards JCheckBox, add to permissions JPanel
        scheduleBillboardsBox = newCheckBox("Schedule Billboards", permissionsPanel);
        if(targetUser.Permissions.contains("Schedule Billboards")){
            scheduleBillboardsBox.setSelected(true);
        }

        // Create Edit All Billboards JCheckBox, add to permissions JPanel
        editAllBillboardsBox = newCheckBox("Edit All Billboards", permissionsPanel);
        if(targetUser.Permissions.contains("Edit All Billboards")){
            editAllBillboardsBox.setSelected(true);
        }

        // Create Edit Users (Admin) JCheckBox, add to permissions JPanel
        editUsersBox = newCheckBox("Edit Users (Admin)", permissionsPanel);
        if(targetUser.Permissions.contains("Edit Users")){
            editUsersBox.setSelected(true);
        }

        // Add permissions JPanel to spacer JPanel, with Structs for spacing
        spacer.add(permissionsPanel);
        spacer.add(Box.createHorizontalStrut(10));

        // Add spacer JPanel to right JPanel, with Structs for spacing
        rightPanel.add(spacer);
        rightPanel.add(Box.createVerticalStrut(50));

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        if(adminUser) {
            getContentPane().add(Box.createHorizontalStrut(100)); // boundary
        }
        else{
            getContentPane().add(Box.createHorizontalStrut(40)); // boundary
        }
        getContentPane().add(leftPanel);
        getContentPane().add(Box.createHorizontalStrut(40));
        if(adminUser) {
            getContentPane().add(rightPanel);
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
     * @return Returns a JPanel
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
     * @param labelName The text that will appear in the JLabel
     * @param panel The JPanel that the created JButton is to be added to
     * @return Returns a JLabel
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
        panel.add(Box.createVerticalStrut(20));

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
     * @param name The text that will appear next to the JCheckBox
     * @param panel  The JPanel that the created JCheckBox is to be added to
     * @return Returns a JCheckBox
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
     * @param labelName The text that will appear in the JLabel
     * @return Returns a JPanel
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the Save and Exit button has been clicked
        if (buttonClicked == saveExitButton) {
            System.out.println("save and exit clicked");
            System.out.println(usernameField.getText());
            System.out.println(password.getText());
            System.out.println(createBillboardsBox.isSelected());
            System.out.println(scheduleBillboardsBox.isSelected());
            System.out.println(editAllBillboardsBox.isSelected());
            System.out.println(editUsersBox.isSelected());
            //run save info, and close window

            if (newUser) {
                String newUsername = usernameField.getText();
                String newPassword = null;
                try {
                    newPassword = userManager.hashPassword(password.getText());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String createBillboard;
                String scheduleBillboard;
                String editBillboard;
                String editUsers;
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

                String[] user_inputs = {"Create User", newUsername, newPassword, createBillboard, scheduleBillboard, editBillboard, editUsers};
                ControlPanelClient.Run_Client(user_inputs);
                Frame[] allFrames = Frame.getFrames();
                for (Frame fr : allFrames) {
                    if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                        fr.dispose();
                        if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                            fr.dispose();
                        }
                        dispose();
                    }
                }
                forcedExit = false;
                dispose();

                //run Billboard Control Panel GUI
                String[] user_input = {"List users", "Admin"};
                //request schedule and run calendar GUI
                ControlPanelClient.Run_Client(user_input);
            } else {
                String newUsername = usernameField.getText();
                String newPassword = password.getText();
                String createBillboard;
                String scheduleBillboard;
                String editBillboard;
                String editUsers;
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

                String[] user_inputs = {"Edit User", newUsername, newPassword, createBillboard, scheduleBillboard, editBillboard, editUsers};
                ControlPanelClient.Run_Client(user_inputs);
                Frame[] allFrames = Frame.getFrames();
                if(adminUser) {
                    for (Frame fr : allFrames) {
                        if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                            fr.dispose();
                            if ((fr.getClass().getName().equals("ControlPanelGUIUserControlPanel"))) {
                                fr.dispose();
                            }
                            dispose();
                        }
                    }
                    dispose();

                    //run Billboard Control Panel GUI
                    String[] user_input = {"List users", "Admin"};
                    //request schedule and run calendar GUI
                    ControlPanelClient.Run_Client(user_input);
                    forcedExit = false;
                }
                else{
                    dispose();
                    SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
                }

            }
        }
        // Checks if the Exit Without Saving button has been clicked
        else if (buttonClicked == exitWithoutSaving) {
            if(adminUser){
                dispose();
            }
            else{
                dispose();
                SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
            }
            System.out.println("exit without saving clicked");
            //run close window

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
        if(!adminUser){
            // When this window is being closed, a new Control Panel GUI is opened (simulates going back to previous screen)
            SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
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
}


