package guis;

import network.ControlPanelClient;
import schedule.ScheduleInfo;
import schedule.ScheduleMultiMap;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.*;
import static javax.swing.JOptionPane.*;

/**
 * This class contains methods that create a schedule pop up and determine its interactivity
 * @author Emily Chang & Liam Dines
 * @version complete
 */
public class GUIBillboardSchedulePopup extends JFrame implements Runnable, ActionListener
{
    private final ScheduleMultiMap billboardSchedule;
    private JButton removeButton;
    private JButton scheduleButton;
    private JButton closeButton;
    private JSpinner durationSpinner;
    private JSpinner dateTimePicker;
    private JComboBox<? extends String> recurrencePicker;
    private JSpinner recurrenceMinutes;
    private final String billboardName;
    private final String username;
    private final String sessionToken;

    /**
     * Constructor that creates an instance of a JFrame GUI window
     * @param username current user's username
     * @param sessionToken current session token of user
     * @param billboardName current billboard selected in Billboard Control Panel GUI screen
     * @param schedule billboard schedule
     */
    public GUIBillboardSchedulePopup(String username, String sessionToken, String billboardName, ScheduleMultiMap schedule)
    {
        // Set window title
        super("Schedule Billboard");

        //set values for current billboardName, username, sessionToken & schedule
        this.billboardName = billboardName;
        this.username = username;
        this.sessionToken = sessionToken;
        this.billboardSchedule = schedule;
    }

    /**
     * This method populates the JFrame with GUI components - JButtons, JSpinners and JLabels
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
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        // Set default look and feel & window properties
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //make window non resizable
        setResizable(false);

        // Create DurationSpinner
        durationSpinner = createNumberJSpinner(1, 1, 1440);

        // Create durationLabel
        JLabel durationLabel = createLabel("Duration (Minutes)");

        // Create DatePickerLabel
        JLabel datePickerLabel = createLabel("Date and Time");

        // Create Date Time Spinner
        dateTimePicker = createDateJSpinner();

        // Create recurrence option label
        JLabel recurrenceOptionLabel = createLabel("Recurrence");

        // Create list for recurrence options for combo boxes
        String[] recurrenceOptions = { "No Recurrence","Every X Minute(s)", "Every Hour", "Every Day" };

        // Create frequencySpinner for every x minutes
        recurrenceMinutes = createNumberJSpinner(0,0,59);

        // Create recurrence picker
        recurrencePicker = new JComboBox<>(recurrenceOptions);

        //add listener to recurrencePicker
        recurrencePicker.addItemListener(e -> {
            //if "Every X Minute(s)" option is selected
            if(recurrencePicker.getSelectedIndex() == 1) {
                //resize GUI to fit recurrenceMinutes spinner
                pack();
                //make minute selection visible
                recurrenceMinutes.setVisible(true);
            }
            else
            {
                //do NOT make minute selection visible
                recurrenceMinutes.setVisible(false);
            }
        });

        // Create Remove Button
        removeButton = createButton("Remove From Schedule");
        //format Remove Button
        removeButton.setMargin(new Insets(0, 0, 0, 0));

        // Create Schedule Button
        scheduleButton = createButton("Schedule Billboard");

        // Create Back Button
        closeButton = createButton("Close");

        // Set Group Layout
        GroupLayout contentPaneLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(durationSpinner, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(durationLabel))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(dateTimePicker, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(recurrencePicker, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(recurrenceOptionLabel)
                                                        .addComponent(datePickerLabel)))
                                        .addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(removeButton, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scheduleButton, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recurrenceMinutes, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(durationSpinner, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                                        .addComponent(durationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dateTimePicker, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(datePickerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(recurrencePicker, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recurrenceOptionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(recurrenceMinutes, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scheduleButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(removeButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        //do NOT make minute selection visible
        recurrenceMinutes.setVisible(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates JSpinner with number spinner model
     * @param startingValue starting value of spinner
     * @param min minimum value of spinner
     * @param max maximum value of spinner
     * @return JSpinner with given starting value, max and min
     */
    private JSpinner createNumberJSpinner(int startingValue, int min, int max) {
        SpinnerModel spinnerModel;

        //set spinner model to number
        spinnerModel = new SpinnerNumberModel(startingValue, min, max, 1);

        //create new JSpinner
        JSpinner jspinner = new JSpinner(spinnerModel);

        return jspinner;
    }

    /**
     * Creates JSpinner with Date spinner model
     * Spinner starts at 1 min+ the starting time
     * @return JSpinner with Date spinner model
     */
    private JSpinner createDateJSpinner() {
        //get current time in calendar format
        Calendar cal = Calendar.getInstance();
        //round seconds of current time down
        cal.set(Calendar.SECOND, 0);
        //add 1 minute to current time as you cannot schedule in the past
        cal.add(Calendar.MINUTE, 1);

        //create date spinner model with adjusted time and time adjustable up to minutes
        SpinnerModel spinnerModel = new SpinnerDateModel(cal.getTime(), null, null, Calendar.MINUTE);

        //create new JSpinner
        JSpinner jspinner = new JSpinner(spinnerModel);

        return jspinner;
    }

    /**
     * Creates a JLabel with given text
     * @param labelText text of label
     * @return JLabel with given text
     */
    private JLabel createLabel(String labelText)
    {
        //create new JLabel
        JLabel label = new JLabel();

        //set text of label
        label.setText(labelText);

        //format label's font, size and alignment
        label.setAlignmentX(0.5F);
        return label;
    }

    /**
     * Create a JButton with an action listener
     * @param buttonLabel label of button
     * @return JButton with given label and action listener
     */
    private JButton createButton(String buttonLabel)
    {
        // Create new button
        JButton button = new JButton();
        //add action listener to button
        button.addActionListener(this);
        //set text of button
        button.setText(buttonLabel);
        //set alignment of button
        button.setAlignmentX(CENTER_ALIGNMENT);
        return button;
    }

    /**
     * converts calendar format, given as string, to LocalDateTime, returned as a string
     * @param dateTime date time in calendar format
     * @return String in LocalDateTime format
     */
    private String calendarToLocalDateTime (String dateTime)
    {
        //create calendar
        Calendar cal = Calendar.getInstance();
        //set date format of calendar
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            //add date time of viewing to it
            cal.setTime(sdf.parse(dateTime));
        } catch (ParseException e) {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(getContentPane(), e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        //convert calendar to LocalDateTime
        TimeZone tz = cal.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(cal.toInstant(), zid);

        //convert LocalDateTime to string
        String localDateTimeString = localDateTime.toString();

        return localDateTimeString;
    }

    /**
     * Returns recurrence delay in terms of minutes, from string given
     * @param recurrenceOption string of recurrence option
     * @return String of recurrence delay in minutes
     */
    private String getRecurrenceDelay(String recurrenceOption){

        //initialize variable that stores recurrence delay
        String recurrenceDelay ="";

        //if billboard viewing does not recur
        if (recurrenceOption == "No Recurrence")
        {
            //set recurrence delay to 0
            recurrenceDelay = "0";
        }

        //if billboard viewing recurs every X minutes
        else if (recurrenceOption == "Every X Minute(s)")
        {
            //retrieve X minutes and set X as the recurrence delay
            recurrenceDelay = recurrenceMinutes.getValue().toString();
        }

        //if billboard viewing recurs every hour
        else if (recurrenceOption == "Every Hour")
        {
            //set recurrence delay to minutes in an hour
            String MINUTES_IN_HOUR = "60";
            recurrenceDelay = MINUTES_IN_HOUR;
        }

        //if billboard viewing recurs every day
        else if (recurrenceOption == "Every Day")
        {
            //set recurrence delay to minutes in a day
            String MINUTES_IN_DAY = "1440";
            recurrenceDelay = MINUTES_IN_DAY;
        }

        return recurrenceDelay;

    }

    /**
     * Listens to button presses and performs the appropriate action/s
     * @param actionEvent JButton pressed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        //get date time string from user input
        String dateTime = dateTimePicker.getValue().toString();

        //format to LocalDateTime as string
        String startTimeString = calendarToLocalDateTime(dateTime);

        //convert to type LocalDateTime and round down in minutes
        LocalDateTime startTime = LocalDateTime.parse(startTimeString).truncatedTo(ChronoUnit.MINUTES);

        //get duration from spinner
        String duration = durationSpinner.getValue().toString();
        //convert duration to type int
        int durationInt = Integer.parseInt(duration);

        //get recurrence option from spinner
        String recurrenceOption = recurrencePicker.getSelectedItem().toString();

        //get recurrence delay for users' recurrence choice as a string
        String recurrenceDelay = getRecurrenceDelay(recurrenceOption);

        //convert recurrenceDelay to type int
        int recurrenceDelayInt = Integer.parseInt(recurrenceDelay);

        //if schedule button is selected
        if (buttonClicked== scheduleButton) {
            //store GUI inputs in user inputs array
            String [] userInputs = {"Schedule Billboard", billboardName,startTimeString, duration, recurrenceDelay};

            //get current time
            LocalDateTime currentTime = LocalDateTime.now();

            //if start time or recurrence delay are invalid
            if((recurrenceDelayInt < durationInt && recurrenceDelayInt!= 0)|| startTime.isBefore(currentTime))
            {
                //check if recurrence delay is valid
                //if duration of recurrence delay is smaller than duration and not no recurrence
                if(recurrenceDelayInt < durationInt && recurrenceDelayInt!= 0)
                {
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You cannot schedule a billboard to recur more frequently than its duration");
                }

                //if start time is in the past
                if(startTime.isBefore(currentTime))
                {
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You cannot schedule a billboard to display in the past");
                }
            }

            //if start time and recurrence delay are valid
            else{
                //schedule billboard
                ControlPanelClient.runClient(userInputs);

                //display confirmation message
                showMessageDialog(null, "Billboard Successfully Scheduled");

                //dispose of pop up and reopen billboard control panel
                frameRefresh();
            }

        }

        //if remove schedule button is selected
        else if (buttonClicked== removeButton) {

            //get current time
            LocalDateTime currentTime = LocalDateTime.now();

            //if start time is in the past
            if(startTime.isBefore(currentTime))
            {
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You cannot remove a billboard viewing that is in the past");
            }

            //if start time is valid
            else{
                //display check window to confirm removal of viewing
                int a = showConfirmDialog(null, "Are you sure you want to remove this viewing?");
                //if yes is selected in check window
                if(a == YES_OPTION)
                {
                    //boolean to check if viewing exists in schedule
                    boolean validSchedule = false;
                    //try retrieving schedule info of billboard
                    try {
                        ArrayList<ScheduleInfo> viewings = billboardSchedule.getViewings(billboardName);
                        //for all viewings of the billboard
                        for(ScheduleInfo viewing : viewings)
                        {
                            //get start time of viewing and round down in minutes
                            LocalDateTime viewingStartTime = viewing.startTimeScheduled.truncatedTo(ChronoUnit.MINUTES);

                            //if user selected start time is equal to start time of viewing stored
                            if(startTime.equals(viewingStartTime))
                            {
                                validSchedule = true;

                                //display confirmation message
                                showMessageDialog(null, "Billboard Successfully Removed From Schedule");

                                //store GUI inputs in user inputs array
                                String [] userInputs = {"Remove Schedule", billboardName, startTimeString, duration, recurrenceDelay};

                                //remove viewing from schedule
                                ControlPanelClient.runClient(userInputs);

                                //dispose pop up and reopen billboard control panel
                                frameRefresh();

                                break;
                            }

                            //if viewing does not exist
                            if (!validSchedule)
                            {
                                //display error pop up
                                JOptionPane.showMessageDialog(this,
                                        "The viewing of " +billboardName+" for "+ startTime+" does not exist in the schedule");
                            }
                        }
                    }
                    //if billboard does not exist in schedule
                    catch (Exception e) {
                        //display error pop up
                        JOptionPane.showMessageDialog(null, "The billboard does not exist in the schedule");
                    }

                }
            }
        }
        else if(buttonClicked == closeButton){
            //dispose pop up and reopen billboard control panel
            frameRefresh();
        }
    }

    /**
     * Closes all pop ups and refreshes the billboard control panel
     */
    private void frameRefresh() {
        //get all frames
        Frame[] allFrames = Frame.getFrames();
        //for every frame
        for (Frame fr : allFrames) {
            //if GUI screen is Billboard Control Panel
            if ((fr.getClass().getName().equals("guis.GUIBillboardControlPanel"))) {
                fr.dispose();
            }
        }
        //dispose schedule info pop up
        dispose();
        String [] userInput = {"List billboards", sessionToken};
        //request billboard list and run Billboard Control Panel GUI
        ControlPanelClient.runClient(userInput);
    }

    /**
     * populates and runs the GUI screen
     */
    @Override
    public void run()
    {
        try
        {
            createGUI();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
