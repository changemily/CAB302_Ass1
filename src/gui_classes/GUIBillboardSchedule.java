package gui_classes;

import network_classes.ControlPanelClient;
import schedule_classes.MultiMap;
import schedule_classes.ScheduleInfo;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * billboard_classes.Billboard Schedule class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the billboard_classes.Billboard Schedule Screen
 * @author - Nickhil Nischal (GUI, Buttons), Harry Estreich (Buttons, Permissions), Emily Chang (adding schedule data to table)
 * @version - Final
 *
 *
 */
public class GUIBillboardSchedule extends JFrame implements Runnable, ActionListener, WindowListener {

    // billboard_classes.Billboard MultiMap
    private MultiMap billboardSchedule;

    // Number of days in each week
    private final int DAYS_IN_WEEK = 7;

    // Boolean for controlling whether a new instance of Control Panel GUI is opened when window is closed
    private boolean forcedClose = false;

    // current user's username
    String username;

    // current user's sessions token
    String sessionToken;

    private JButton backButton;
    private JButton logoutButton;

    /**
     * Method used to create a GUI window for the billboard_classes.Billboard Schedule Screen
     * @param username current username of user logged in
     * @param sessionToken The sessionToken associated with the user
     * @param schedule current billboard schedule
     */
    public GUIBillboardSchedule(String username, String sessionToken, MultiMap schedule) {
        // Set window title
        super("billboard_classes.Billboard Schedule");

        // set internal variables to given values
        this.billboardSchedule = schedule;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    /**
     * Method used to create a GUI window for the billboard_classes.Billboard Schedule screen
     * @throws ClassNotFoundException Thrown by setLookAndFeel - when an application tries to load in a class through its string name but no definition
     * for the class with the specified name could be found.
     * @throws UnsupportedLookAndFeelException Thrown by setLookAndFeel - An exception that indicates the requested look & feel management classes
     * are not present on the user's system.
     * @throws InstantiationException Thrown by setLookAndFeel - when an application tries to create an instance of a class using the newInstance method in
     * class Class, but the specified class object cannot be instantiated
     * @throws IllegalAccessException Thrown by setLookAndFeel - An IllegalAccessException is thrown when an application tries to reflectively
     * create an instance (other than an array), set or get a field, or invoke a method, but the currently executing
     * method does not have access to the definition of the specified class, field, method or constructor.
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation set to Dispose on Close, so when user closes this screen, only this screen closes (keeps Control Panel GUI running)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //make window non resizable
        setResizable(false);

        // Create main content JPanel, with X axis Box Layout
        JPanel buttonPanel = newPanel('X');

        // Add strut for formatting
        buttonPanel.add(Box.createHorizontalStrut(20));

        // Create back JButton
        backButton = newButton("Back", buttonPanel);

        // Add horizontal glue in between buttons for spacing
        buttonPanel.add(Box.createHorizontalGlue());

        // Create logout JButton
        logoutButton = newButton("Logout", buttonPanel);

        // Add strut for formatting
        buttonPanel.add(Box.createHorizontalStrut(20));

        // Create and add the Billboards this Week label, inside of a JPanel
        JPanel title = addLabel();

        // Create and populate table, inside of a JPanel
        JPanel table = addTable();

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(20)); // boundary
        getContentPane().add(buttonPanel);
        getContentPane().add(Box.createVerticalStrut(50));
        getContentPane().add(title);
        getContentPane().add(Box.createVerticalStrut(40));
        getContentPane().add(table);
        getContentPane().add(Box.createVerticalStrut(50)); // boundary

        // Add Window Listener, used for when window is closed
        addWindowListener(this);

        // Display window
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * This method creates a new JPanel, on specified X_AXIS layout or Y_AXIS layout
     * @param axis The specified layout axis as a char (X or Y)
     * @return Returns a JPanel
     */
    private JPanel newPanel(char axis) {
        // Create new JPanel
        JPanel panel = new JPanel();

        // Check if 'X' axis has been requested
        if (axis == 'X') {
            // Create X_AXIS BoxLayout
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        }

        // Check if 'Y' axis has been requested
        else if (axis == 'Y') {
            // Create Y_AXIS BoxLayout
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        }

        // Returns a JPanel
        return panel;
    }

    /**
     * This method creates a JButton, and adds it to a specified JPanel
     * @param buttonName The text that will be displayed inside the button
     * @param panel The JPanel that the created JButton is to be added to
     * @return Returns a JButton
     */
    private JButton newButton(String buttonName, JPanel panel) {
        // Create JButton
        JButton button = new JButton(buttonName);

        // Centre the JButton
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add the frame as an actionListener
        button.addActionListener(this);

        // Add JButton to specified JPanel
        panel.add(button);

        // Returns a JButton
        return button;
    }

    /**
     * This method creates the Billboards this Week label, inside of a JPanel
     * @return Returns a JPanel
     */
    private JPanel addLabel() {
        // Create JPanel
        JPanel title = newPanel('Y');

        // Create JLabel
        JLabel billboardsThisWeekLabel = new JLabel("Billboards this Week");

        // Change text size of JLabel
        billboardsThisWeekLabel.setFont(new Font(null, Font.BOLD, 15));

        // Centre JLabel
        billboardsThisWeekLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add JLabel to JPanel
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
     * This method populates the table with schedule data from the database
     * @return Returns a two-dimensional string array
     */
    private String[][] tableData() {
        // Create string array for each cell in table
        String[][] data = new String[24][8];

        // For each billboard in billboardSchedule
        for (Object billboardName : billboardSchedule.keySet() ) {

            // Create array list to store viewings of billboard
            ArrayList<ScheduleInfo> viewings = billboardSchedule.get(billboardName);

            // For every viewing of billboard
            for ( ScheduleInfo viewing : viewings ) {

                // Store scheduled date time of billboard in local var
                LocalDateTime scheduledDateTime = viewing.startTimeScheduled;

                // Store current date time in local var
                LocalDateTime currentDateTime = LocalDateTime.now();

                // Round scheduled date time up in days
                LocalDateTime ceilingScheduledDateTime = viewing.startTimeScheduled.truncatedTo(DAYS).plusDays(1);

                // Round current date time up in days
                LocalDateTime ceilingCurrentDateTime = LocalDateTime.now().truncatedTo(DAYS).plusDays(1);

                // Calculate days in between current time and scheduled viewing
                long timeDiff = DAYS.between(ceilingCurrentDateTime, ceilingScheduledDateTime);

                // Get current day of the week
                DayOfWeek currentDay = currentDateTime.getDayOfWeek();

                // Convert day to int value
                int intCurrentDay = currentDay.getValue();

                // Calculate time till end of the week (Sunday)
                int timeEndWk = DAYS_IN_WEEK - intCurrentDay;

                // If viewing is in the current week
                if (timeDiff <= timeEndWk)
                {
                    // Get day of the week from scheduledDateTime
                    DayOfWeek billboardDay = scheduledDateTime.getDayOfWeek();

                    // Get int value of day
                    int dayInt = billboardDay.getValue();

                    // Get hour displayed from LocalDateTime
                    int billboardHour = scheduledDateTime.getHour();

                    // Retrieve string from cell
                    String tempCellString = data[billboardHour][dayInt];

                    //define date formatter displayed in cell
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                    //convert scheduledDateTime into formatted string
                    String startTime = scheduledDateTime.format(formatter);

                    //convert duration into string
                    String durationUnformatted = viewing.duration.toString();

                    //remove letters from duration format to leave integer value
                    durationUnformatted = durationUnformatted.replaceAll("PT","");
                    String duration = durationUnformatted.replaceAll("M","");

                    //if cell is empty
                    if(tempCellString == null)
                    {
                        // Add billboard name and creator to string in cell with no new line
                        tempCellString = " "+billboardName + " by\n " + viewing.billboardCreator +"\n duration: "
                                +duration +" min/s\n start time: " +startTime;
                    }

                    //if cell is already populated
                    else
                    {
                        // Add billboard name and creator to string in cell with new lines
                        tempCellString += "\n\n "+ billboardName + " by \n " + viewing.billboardCreator +"\n duration: "
                                +duration +" min/s\n start time: " +startTime;
                    }

                    // Replace all instances of null with empty string
                    String cellString = tempCellString.replaceAll("null", "");

                    // Populate cell with string
                    data[billboardHour][dayInt] = cellString; // Change this line to "BillboardName by user_classes.User"
                }
            }
        }
        // Returns a two-dimensional array for use in the JTable
        return data;
    }

    /**
     * sourced from https://stackoverflow.com/questions/37768335/how-to-word-wrap-inside-a-jtable-row/37768834
     * WordWrapCellRenderer renders text to wrap within a cell
     */
    class WordWrapCellRenderer extends JTextArea implements TableCellRenderer {
        WordWrapCellRenderer() {
            //set wrap text to true
            setLineWrap(true);
            setWrapStyleWord(true);
        }

        /**
         * Returns JTextArea for cell
         * @param table table being formatted
         * @param value text value of cell
         * @param isSelected
         * @param hasFocus
         * @param row row of cell
         * @param column column of cell
         * @return returns JTextArea
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //if cell is populated
            if (value != null)
            {
                //set text of JTextArea to value in cell
                setText(value.toString());
            }
            //if cell is empty
            else
            {
                //set text to blank
                setText("");
            }
            //set size of JTextArea
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            //if row height cannot display all text
            if (table.getRowHeight(row) != getPreferredSize().height) {
                //set row height to display all text in the cell
                table.setRowHeight(row, getPreferredSize().height);
            }
            //return JTextArea
            return this;
        }
    }

    /**
     * This method creates a Billboards Table view, inside of a JPanel
     * @return Returns a JPanel
     */
    private JPanel addTable() {
        // Create Billboards Table view, inside of a JPanel
        JPanel table = newPanel('X');

        // Add horizontal strut for formatting
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

        //show grid lines on table
        thisWeek.setShowGrid(true);

        //colour grid lines black
        thisWeek.setGridColor(Color.BLACK);

        // Makes columns fixed, columns cannot be rearranged by user
        thisWeek.getTableHeader().setReorderingAllowed(false);

        // Make columns fixed, column widths cannot be changed by user
        // For loop iterates through each day
        for (int columnNum = 0; columnNum <= days.length - 1; columnNum++) {
            //set all columns to non resizable
            thisWeek.getColumnModel().getColumn(columnNum).setResizable(false);
            //set column renderer to wrap text in cell
            thisWeek.getColumnModel().getColumn(columnNum).setCellRenderer(new WordWrapCellRenderer());
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

    /**
     * Listens to button presses and performs the appropriate action/s
     * @param actionEvent JButton pressed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the back button has been clicked
        if (buttonClicked == backButton) {
            // Closes current GUI screen
            dispose();
            forcedClose = true;

            // Open new Control Panel GUI screen
            SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
        }

        // Checks if the logout button has been clicked
        else if (buttonClicked == logoutButton) {
            forcedClose = true;

            // Open new Login screen
            // Remove users session token and proceed to the login screen
            String[] user_input = {"Logout request", ControlPanelClient.sessionToken};
            ControlPanelClient.runClient(user_input);
            // Close the GUI screen
            dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // When this window is being closed, a new Control Panel GUI is opened (simulates going back to previous screen)
        if(!forcedClose) {
            SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
        }
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
    /**
     * populates and runs the GUI screen
     */
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
