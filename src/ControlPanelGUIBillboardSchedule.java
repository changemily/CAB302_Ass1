import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Billboard Schedule class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Billboard Schedule Screen
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; some functionality still needs to be added; further refinement required
 */
public class ControlPanelGUIBillboardSchedule extends JFrame implements Runnable {
    MultiMap billboard_schedule;
    String username;
    String sessionToken;
    final int days_in_week = 7;
    /**
     * Method used to create a GUI window for the Billboard Schedule Screen
     */
    public ControlPanelGUIBillboardSchedule(String username, String sessionToken, MultiMap schedule) {
        // Set window title
        super("Billboard Schedule");

        billboard_schedule = schedule;
        this.username = username;
        this.sessionToken = sessionToken;
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

        // Default close operation set to Dispose on Close, so when user closes this screen, only this screen closes (keeps Control Panel GUI running)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        //for each billboard in billboard_schedule
        for (Object billboard_name : billboard_schedule.keySet() ) {

            //create array list to store viewings of billboard
            ArrayList<ScheduleInfo> viewings = billboard_schedule.get(billboard_name);

            //for every viewing of billboard
            for ( ScheduleInfo viewing : viewings ) {

                //store scheduled date time of billboard in local var
                LocalDateTime scheduled_date_time = viewing.startTimeScheduled;

                //store current date time in local var
                LocalDateTime current_date_time = LocalDateTime.now();

                //round scheduled date time up in days
                LocalDateTime ceiling_scheduled_date_time = viewing.startTimeScheduled.truncatedTo(DAYS).plusDays(1);

                //round current date time up in days
                LocalDateTime ceiling_current_date_time = LocalDateTime.now().truncatedTo(DAYS).plusDays(1);

                //calculate days in between current time and scheduled viewing
                long time_diff = DAYS.between(ceiling_current_date_time, ceiling_scheduled_date_time);

                System.out.println("time diff of "+ billboard_name +"= "+time_diff);

                //get current day of the week
                DayOfWeek current_day = current_date_time.getDayOfWeek();

                System.out.println("scheduled day" + scheduled_date_time.getDayOfWeek());
                System.out.println("scheduled day int: " + scheduled_date_time.getDayOfWeek().getValue());
                System.out.println("current day: "+current_day);

                //convert day to int value
                int int_current_day = current_day.getValue();

                //calculate time till end of the week (Sunday)
                int time_end_wk = days_in_week - int_current_day;

                System.out.println("end of week: "+time_end_wk);
                //if viewing is in the current week
                if (time_diff <= time_end_wk)
                {
                    //get day of the week from LocalDateTime
                    DayOfWeek billboard_day = scheduled_date_time.getDayOfWeek();

                    //get int value of day
                    int day_int = billboard_day.getValue();

                    //get hour displayed from LocalDateTime
                    int billboard_hour = scheduled_date_time.getHour();

                    //retrieve string from cell
                    String temp_cell_string = data[billboard_hour][day_int];

                    //add billboard name and creator to string in cell
                    temp_cell_string += billboard_name + " by " + viewing.billboardCreator + "\n";

                    //replace all instances of null with empty string
                    String cell_string = temp_cell_string.replaceAll("null", "");

                    //populate cell with new string
                    data[billboard_hour][day_int] = cell_string; // Change this line to "BillboardName by User"
                }
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
        String[] days = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

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

}
