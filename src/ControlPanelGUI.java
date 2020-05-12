import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Control Panel GUI class
 * This class contains a Main method and method that creates a GUI window for the Control Panel Menu
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; button functionality still needs to be added; further refinement required
 */
public class ControlPanelGUI extends JFrame implements Runnable, ActionListener {
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
    }

    private void createGUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // create label
        JPanel label = createLabel("Control Panel Menu");

        // create edit users button
        JPanel editUsersButton = createButton("Edit Users");

        // create edit billboard button
        JPanel editBillboardButton = createButton("Edit Billboard");

        // create edit billboard schedule button
        JPanel editBillboardScheduleButton = createButton("Edit Billboard Schedule");

        //Add label and buttons to content pane
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

        // Format window
        setPreferredSize(new Dimension(600,400));
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    private JPanel createLabel(String labelText) {
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.add(Box.createHorizontalGlue());
        JLabel label = new JLabel(labelText);
        labelPanel.add(label);
        labelPanel.add(Box.createHorizontalGlue());
        return labelPanel;
    }

    private JPanel createButton(String buttonLabel) {
        //create new JPanel to hold button
        JPanel buttonPanel = new JPanel();
        //set box layout
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        //add new button to button Panel
        JButton button = new JButton(buttonLabel);
        buttonPanel.add(button);
        buttonPanel.add(Box.createHorizontalGlue());
        return buttonPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IOException, IllegalAccessException {
        SwingUtilities.invokeLater(new ControlPanelGUI());
    }
}
