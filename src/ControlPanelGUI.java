import javax.swing.*;
import java.awt.*;

/**
 * Control Panel GUI class
 * This class contains a Main method and method that creates a GUI window for the Control Panel Menu
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; button functionality still needs to be added; further refinement required
 */
public class ControlPanelGUI extends JFrame {
    /**
     * Method used to create a GUI window for the Control Panel Menu
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public ControlPanelGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Set window title
        super("Control Panel Menu");

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add layout
        //getContentPane().setLayout(new BorderLayout());

        // Add label
        //JLabel label = new JLabel("Control Panel Menu");
        //getContentPane().add(label, BorderLayout.NORTH);
        JPanel label = new JPanel();
        label.setLayout(new BoxLayout(label, BoxLayout.X_AXIS));
        label.add(Box.createHorizontalGlue());
        label.add(new JLabel("Control Panel Menu"));
        label.add(Box.createHorizontalGlue());

        // Add edit users button
        //JButton editUsersButton = new JButton("Edit Users");
        //getContentPane().add(editUsersButton, BorderLayout.CENTER);
        JPanel editUsersButton = new JPanel();
        editUsersButton.setLayout(new BoxLayout(editUsersButton, BoxLayout.X_AXIS));
        editUsersButton.add(Box.createHorizontalGlue());
        JButton editUsersButton2 = new JButton("Edit Users");
        editUsersButton.add(editUsersButton2);
        //editUsersButton2.setPreferredSize(new Dimension(200,0));
        //editUsersButton2.setSize(200,200);
        editUsersButton.add(Box.createHorizontalGlue());

        // Add edit billboard button
        //JButton editBillboardButton = new JButton("Edit Billboard");
        //getContentPane().add(editBillboardButton, BorderLayout.CENTER);
        JPanel editBillboardButton = new JPanel();
        editBillboardButton.setLayout(new BoxLayout(editBillboardButton, BoxLayout.X_AXIS));
        editBillboardButton.add(Box.createHorizontalGlue());
        editBillboardButton.add(new JButton("Edit Billboard"));
        editBillboardButton.add(Box.createHorizontalGlue());

        // Add edit billboard schedule button
        //JButton editBillboardScheduleButton = new JButton("Edit Billboard Schedule");
        //getContentPane().add(editBillboardScheduleButton, BorderLayout.CENTER);
        JPanel editBillboardScheduleButton = new JPanel();
        editBillboardScheduleButton.setLayout(new BoxLayout(editBillboardScheduleButton, BoxLayout.X_AXIS));
        editBillboardScheduleButton.add(Box.createHorizontalGlue());
        editBillboardScheduleButton.add(new JButton("Edit Billboard Schedule"));
        editBillboardScheduleButton.add(Box.createHorizontalGlue());

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(30));
        getContentPane().add(label);
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(editUsersButton);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editBillboardButton);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editBillboardScheduleButton);
        getContentPane().add(Box.createVerticalStrut(30));

        // Display window
        setPreferredSize(new Dimension(600,400));
        //setPreferredSize(new Dimension((Toolkit.getDefaultToolkit().getScreenSize().width), (Toolkit.getDefaultToolkit().getScreenSize().height)));
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        //setState(Frame.NORMAL);
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * Main method which creates a GUI window for the Control Panel Menu
     * @param args This method takes no arguments
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new ControlPanelGUI();
    }
}
