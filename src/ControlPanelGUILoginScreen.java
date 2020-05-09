import javax.swing.*;
import java.awt.*;

/**
 * Login Screen class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Login Screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; button functionality still needs to be added; further refinement required
 */
public class ControlPanelGUILoginScreen extends JFrame {
    /**
     * Method used to create a GUI window for the Login Screen
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public ControlPanelGUILoginScreen() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Set window title
        super("Billboard Control Panel Login Screen");

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add title label, inside of a JPanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(new JLabel("Welcome to the Billboard Control Panel"));

        // Add subtitle label, inside of a JPanel
        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setLayout(new BoxLayout(subtitlePanel, BoxLayout.X_AXIS));
        subtitlePanel.add(new JLabel("Please enter login details to begin"));

        // Add username label, inside of a JPanel
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));
        usernamePanel.add(Box.createHorizontalStrut(100));
        usernamePanel.add(new JLabel("Username     "));

        // Add username text field, inside of a JPanel
        JTextField username;
        username = new JTextField(15);
        usernamePanel.add(username);
        usernamePanel.add(Box.createHorizontalStrut(100));

        // Add password label, inside of a JPanel
        JPanel labelPassword = new JPanel();
        labelPassword.setLayout(new BoxLayout(labelPassword, BoxLayout.X_AXIS));
        labelPassword.add(Box.createHorizontalStrut(100));
        labelPassword.add(new JLabel("Password      "));

        // Add password text field, inside of a JPanel
        JTextField password;
        password = new JTextField(15);
        labelPassword.add(password);
        labelPassword.add(Box.createHorizontalStrut(100));

        // Add login button, inside of a JPanel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
        JButton loginButton2 = new JButton("Login");
        loginPanel.add(loginButton2);

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(titlePanel);
        getContentPane().add(Box.createVerticalStrut(40));
        getContentPane().add(subtitlePanel);
        getContentPane().add(Box.createVerticalStrut(20));
        getContentPane().add(usernamePanel);
        getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(labelPassword);
        getContentPane().add(Box.createVerticalStrut(20));
        getContentPane().add(loginPanel);
        getContentPane().add(Box.createVerticalStrut(50));

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * Main method which creates a GUI window for the Login Screen
     * @param args This method takes no arguments
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new ControlPanelGUILoginScreen();
    }
}
