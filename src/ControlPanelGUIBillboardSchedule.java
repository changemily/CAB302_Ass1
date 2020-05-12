import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Billboard Schedule class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Billboard Schedule Screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; some functionality still needs to be added; further refinement required
 */
public class ControlPanelGUIBillboardSchedule extends JFrame {
    /**
     * Method used to create a GUI window for the Billboard Schedule Screen
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public ControlPanelGUIBillboardSchedule() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Set window title
        super("Billboard Schedule");

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add Billboards this Week label, inside of a JPanel
        JPanel title = new JPanel();
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        JLabel billboardsThisWeekLabel = new JLabel("Billboards this Week");
        billboardsThisWeekLabel.setFont(new Font(null, Font.PLAIN, 15));
        billboardsThisWeekLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.add(billboardsThisWeekLabel);

        // Create Billboards Table view, inside of a JPanel
        JPanel table = new JPanel();
        table.setLayout(new BoxLayout(table, BoxLayout.X_AXIS));
        table.add(Box.createHorizontalStrut(100));

        // Create string array for days of the week, used in table column headers
        // First element of array left blank for time column
        String[] days = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        // Create string array for times in a day, used in leftmost column
        String[] times = new String[24];

        // Variables used in for loop
        int hour = 12;
        String[] AMPM = {"AM", "PM"};
        int AMPMCount = 0;

        // For loop which populates the times array with 12-hour times, 1-hour intervals between each
        for (int i = 0; i < 24; i++, hour++) {
            times[i] = hour + ":00" + AMPM[AMPMCount];

            if(hour == 12) {
                hour -= 12;
            }

            if (i == 11) {
                AMPMCount = 1;
            }
        }

        // Create string array for each cell in table
        String[][] data = new String[24][8];

        // Populate first column of table with times
        int i = 0;
        for (String time : times) {
            data[i][0] = time;
            i += 1;
        }

        // Create and populate This Week JTable
        JTable thisWeek = new JTable(data, days);
        //thisWeek.setBounds(30,40,200,300); //keep this line for formatting the JTable
        JScrollPane scroll = new JScrollPane(thisWeek); // Scroll pane
        table.add(scroll);
        table.add(Box.createHorizontalStrut(100));

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(50)); // boundary
        getContentPane().add(title);
        getContentPane().add(Box.createVerticalStrut(40));
        getContentPane().add(table);
        getContentPane().add(Box.createVerticalStrut(50)); // boundary

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * Main method which creates a GUI window for the Billboard Schedule Screen
     * @param args This method takes no arguments
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        new ControlPanelGUIBillboardSchedule();
    }
}
