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

    JButton editUsersButton;
    JButton editBillboardButton;
    JButton editBillboardScheduleButton;

    public ControlPanelGUI(){
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
        editUsersButton = createButton("Edit Users");
        //create JPanel that holds button
        JPanel editUsersPanel = createButtonJPanel(editUsersButton);

        // create edit billboard button
        editBillboardButton = createButton("Edit Billboards");
        //create JPanel that holds button
        JPanel editBillboardPanel = createButtonJPanel(editBillboardButton);

        // create edit billboard schedule button
        editBillboardScheduleButton = createButton("View Billboard Schedule");
        //create JPanel that holds button
        JPanel editBillboardSchedulePanel = createButtonJPanel(editBillboardScheduleButton);

        //Add label and buttons to content pane
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(30));
        getContentPane().add(label);
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(editUsersPanel);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editBillboardPanel);
        getContentPane().add(Box.createVerticalStrut(25));
        getContentPane().add(editBillboardSchedulePanel);
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

    private JPanel createButtonJPanel(JButton button) {
        //create new JPanel to hold button
        JPanel buttonPanel = new JPanel();
        //set box layout
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        //add new button to button Panel
        buttonPanel.add(button);
        buttonPanel.add(Box.createHorizontalGlue());
        return buttonPanel;
    }

    private JButton createButton(String buttonLabel) {
        //Create a JButton object and store it in a local var
        JButton button = new JButton(buttonLabel);

        //Add the frame as an actionListener
        button.addActionListener(this);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        if (buttonClicked==editUsersButton) {
            //run User Control Panel GUI
            SwingUtilities.invokeLater(new ControlPanelGUIUserControlPanel());
        }

        else if (buttonClicked==editBillboardButton) {
            //run Billboard Control Panel GUI
            SwingUtilities.invokeLater(new ControlPanelGUIBillboardControlPanel());
        }

        else if (buttonClicked == editBillboardScheduleButton)
        {
            String [] user_input = {"View schedule"};
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        SwingUtilities.invokeLater(new ControlPanelGUI());
    }
}
