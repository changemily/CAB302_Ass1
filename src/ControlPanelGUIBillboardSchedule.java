import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Billboard Schedule class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Billboard Schedule Screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; some functionality still needs to be added; further refinement required
 */
public class ControlPanelGUIBillboardSchedule extends JFrame implements Runnable, ActionListener {
    /**
     * Method used to create a GUI window for the Billboard Schedule Screen
     */
    public ControlPanelGUIBillboardSchedule() {
        // Set window title
        super("Billboard Schedule");
    }

    /**
     * Method used to create a GUI window for the Billboard Schedule screen
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create and add the Billboards this Week label, inside of a JPanel
        JPanel title = addLabel();

        // Create and populate table, inside of a JPanel
        JPanel table = addTable();

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(50)); // boundary
        getContentPane().add(title);
        getContentPane().add(Box.createVerticalStrut(40));
        getContentPane().add(table);
        getContentPane().add(Box.createVerticalStrut(50)); // boundary

        // Display window
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * This method creates the Billboards this Week label, inside of a JPanel
     * @return Returns a JPanel
     */
    private JPanel addLabel() {
        // Create JPanel
        JPanel title = new JPanel();

        // Set layout of JPanel
        title.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));

        // Create JLabel
        JLabel billboardsThisWeekLabel = new JLabel("Billboards this Week");

        // Change text size of JLabel
        billboardsThisWeekLabel.setFont(new Font(null, Font.PLAIN, 15));

        // Centre JLabel
        billboardsThisWeekLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add JLabel to JPanel
        title.add(billboardsThisWeekLabel);

        // Return JPanel
        return title;
    }

    /**
     * This method populates an array with times in a day
     * @return Returns a string array
     */
    private String[] tableTimes() {
        // Create string array for times in a day, used in leftmost column
        String[] times = new String[24];

        // Variables used in below for loop
        int hour = 12;
        String[] AMPM = {"AM", "PM"};
        int AMPMCount = 0;

        // For loop which populates the times array with 12-hour times, 1-hour intervals between each
        for (int i = 0; i < 24; i++, hour++) {
            times[i] = hour + ":00" + AMPM[AMPMCount];

            // If 12:00 has been reached, the counter will reset to maintain 12 hour time
            if(hour == 12) {
                hour -= 12;
            }

            // If counter reaches 11:00AM, AMPMCount changes to 1 to enable PM for 12 hour time
            if (i == 11) {
                AMPMCount = 1;
            }
        }

        // Returns an array of 24 times (in a 12 hour time format)
        return times;
    }

    /**
     * This method populates the table with real billboard data
     * @return Returns a two-dimensional string array
     */
    private String[][] tableData() {
        // Create string array for each cell in table
        String[][] data = new String[24][8];

        // Populate array with billboard data
        //data[1][1] = "Billboard 1" + " by " + "User 1"; // Test, need to populate with real billboard data
        // For loop which iterates through each day
        for(int day = 1; day < 8; day++) {
            // For loop which iterates through each hour in the day
            for(int hour = 0; hour < 24; hour++) {
                // Populate each element in the array (this translates to each cell in the table)
                data[hour][day] = day + " by " + hour; // Change this line to "BillboardName by User"
            }
        }

        // Returns a two-dimensional array for use in the JTable
        return data;
    }

    /**
     * This method creates a Billboards Table view, inside of a JPanel
     * @return Returns a JPanel
     */
    private JPanel addTable() {
        // Create Billboards Table view, inside of a JPanel
        JPanel table = new JPanel();
        table.setLayout(new BoxLayout(table, BoxLayout.X_AXIS));
        table.add(Box.createHorizontalStrut(100));

        // Create string array for days of the week, used in table column headers
        // First element of array left blank for time column
        String[] days = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        // Populate string array with times in a day (24 hours)
        String[] times = tableTimes();

        // Populate string array with billboard data
        String[][] data = tableData();

        // Populate first column of table with times
        int i = 0;
        for (String time : times) {
            data[i][0] = time;
            i += 1;
        }

        // Create and populate This Week JTable
        JTable thisWeek = new JTable(data, days);

        // Makes cells uneditable by user
        thisWeek.setEnabled(false);

        // Formats the JTable
        //thisWeek.setBounds(30,40,200,300); //keep this line

        // Makes columns fixed, columns cannot be rearranged by user
        thisWeek.getTableHeader().setReorderingAllowed(false);

        // Set preferred widths for columns
        //thisWeek.getColumnModel().getColumn(5).setPreferredWidth(days[5].length()); // Keep this line

        // Makes columns fixed, column widths cannot be changed by user
        // For loop iterates through each day
        for (int j = 0; j <= days.length - 1; j++) {
            thisWeek.getColumnModel().getColumn(j).setResizable(false);
        }

        // Add JScrollPane
        JScrollPane scroll = new JScrollPane(thisWeek);
        scroll.setPreferredSize(new Dimension(1000, 450));

        // Add scroll bar to table JPanel and makes this week JTable visible
        table.add(scroll);

        // Formatting
        table.add(Box.createHorizontalStrut(100));

        // Returns the JTable
        return table;
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
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Main method which creates a GUI window for the Billboard Schedule screen
     * @param args This method takes no arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ControlPanelGUIBillboardSchedule());
    }
}
