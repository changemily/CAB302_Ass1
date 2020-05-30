import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.*;
import javax.swing.GroupLayout;

import static javax.swing.JOptionPane.*;
/**
 * @author Liam
 */

public class BBSchedulePopup extends JFrame implements Runnable, ActionListener
{
    private ScheduleMultiMap billboardSchedule;
    private JButton removeBttn;
    private JButton scheduleBttn;
    private JButton closeBttn;
    private JSpinner durationSpinner;
    private JSpinner dateTimePicker;
    private JComboBox recurrencePicker;
    private JSpinner recurrenceMins;
    private String billboardName;
    private final String MINUTES_IN_DAY = "1440";
    private final String MINUTES_IN_HOUR = "60";
    private String username;
    private String sessionToken;

    public BBSchedulePopup(String username, String sessionToken, String billboardName, ScheduleMultiMap schedule)
    {
        // Set window title
        super("Schedule Billboard");

        this.billboardName = billboardName;
        this.username = username;
        this.sessionToken = sessionToken;
        this.billboardSchedule = schedule;
    }
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        // Set default look and feel & window properties
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create DurationSpinner
        durationSpinner = createNumberJSpinner(1, 1, 1440);

        // Create durationLabel
        JLabel durationLabel = createLabel("Duration (Minutes)");

        // Create DatePickerLabel
        JLabel datePickerLabel = createLabel("Date and Time");

        // Create Day Time Spinner
        dateTimePicker = createDateJSpinner();

        // Create lists for frequency, day and hour options for combo boxes
        String[] recurrenceOptions = { "No Recurrence","Every X Minute(s)", "Every Hour", "Every Day" };

        // Create recurrenceOptionLabel
        JLabel recurrenceOptionLabel = createLabel("Recurrence");

        // Create frequencySpinner
        recurrenceMins = createNumberJSpinner(0,0,59);

        // Create FrequencyPicker
        recurrencePicker = new JComboBox(recurrenceOptions);
        recurrencePicker.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(recurrencePicker.getSelectedIndex() == 1) {
                    //resize GUI to fit recurrencMins spinner
                    pack();
                    recurrenceMins.setVisible(true);
                } else
                {
                    recurrenceMins.setVisible(false);
                }
            }
        });

        // Create Remove Button
        removeBttn = createButton("Remove From Schedule");
        removeBttn.setMargin(new Insets(0, 0, 0, 0));

        // Create Schedule Button
        scheduleBttn = createButton("Schedule Billboard");

        // Create Back Button
        closeBttn = createButton("Close");

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
                                        .addComponent(closeBttn, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(removeBttn, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(scheduleBttn, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(recurrenceMins, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(recurrenceMins, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scheduleBttn, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(removeBttn, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(closeBttn, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JSpinner createNumberJSpinner(int value, int min, int max) {
        SpinnerModel spinnerModel;

        //set spinner model to number
        spinnerModel = new SpinnerNumberModel(value, min, max, 1);

        //create new JSpinner
        JSpinner jspinner = new JSpinner(spinnerModel);

        //set font and size of JSpinner
        return jspinner;
    }

    private JSpinner createDateJSpinner() {
        //get current time in calendar format
        Calendar cal = Calendar.getInstance();
        //add 1 minute to current time as you cannot schedule in the past
        cal.add(Calendar.MINUTE, 1);

        //create date spinner model with adjusted time and time adjustable up to minutes
        SpinnerModel spinnerModel = new SpinnerDateModel(cal.getTime(), null, null, Calendar.MINUTE);

        //create new JSpinner
        JSpinner jspinner = new JSpinner(spinnerModel);

        return jspinner;
    }

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
     *
     * @param dateTime
     * @return
     */
    private String calendarToLocalDateTime (String dateTime)
    {
        //create calendar and add date time of viewing to it
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getContentPane(), e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        //convert calendar to LocalDateTime
        TimeZone tz = cal.getTimeZone();
        ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(cal.toInstant(), zid);
        String localDateTimeString = localDateTime.toString();

        return localDateTimeString;
    }

    /**
     *
     * @param recurrenceOption
     * @return
     */
    private String getReccurrenceDelay(String recurrenceOption){

        //initialize variable that stores recurrence delay
        String recurrenceDelay ="";

        //if billboard viewing does not recur
        if (recurrenceOption == "No Recurrence")
        {
            //set recurrence delay to 0 if billboard viewing does not recur
            recurrenceDelay = "0";
        }

        //if billboard viewing recurs every X minutes
        else if (recurrenceOption == "Every X Minute(s)")
        {
            //retrieve recurrence X minutes recurrence delay
            recurrenceDelay = recurrenceMins.getValue().toString();
        }

        //if billboard viewing recurs every hour
        else if (recurrenceOption == "Every Hour")
        {
            recurrenceDelay = MINUTES_IN_HOUR;
        }

        //if billboard viewing recurs every day
        else if (recurrenceOption == "Every Day")
        {
            recurrenceDelay = MINUTES_IN_DAY;
        }

        return recurrenceDelay;

    }
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        //get date time string from user input
        String dateTime = dateTimePicker.getValue().toString();

        //format to LocalDateTime as string
        String startTimeString = calendarToLocalDateTime(dateTime);

        //convert to type LocalDateTime
        LocalDateTime startTime = LocalDateTime.parse(startTimeString);

        //get duration from spinner
        String duration = durationSpinner.getValue().toString();

        //get recurrence option from spinner
        String recurrenceOption = recurrencePicker.getSelectedItem().toString();

        //get recurrence delay for users' recurrence choice as a string
        String recurrenceDelay = getReccurrenceDelay(recurrenceOption);

        //if schedule button is selected
        if (buttonClicked== scheduleBttn) {
            System.out.println("Save pressed");

            //change user inputs to GUI inputs
            String [] user_inputs = {"Schedule Billboard", billboardName,startTimeString, duration, recurrenceDelay};

            //get current time
            LocalDateTime currentTime = LocalDateTime.now();

            //if start time is in the past
            if(startTime.isBefore(currentTime))
            {
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You cannot schedule a billboard to display in the past");
            }

            //if start time is valid
            else{
                //Schedule billboard with viewing details given by user
                ControlPanelClient.Run_Client(user_inputs);
                showMessageDialog(null, "Billboard Successfully Scheduled");

                //FOR TESTING
                System.out.println("request: "+user_inputs[0]);
                System.out.println("bb name: "+user_inputs[1]);
                System.out.println("Start time: "+user_inputs[2]);
                System.out.println("Duration: "+user_inputs[3]);
                System.out.println("recurrence: "+user_inputs[4]);
            }

        }

        //if remove schedule button is selected
        else if (buttonClicked== removeBttn) {

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
                    //boolean to check if billboard exists in schedule
                    boolean billboardExists = false;

                    //boolean to check if viewing exists in schedule
                    boolean validSchedule = false;
                    //try retrieving schedule info of billboard
                    try {
                        ArrayList<ScheduleInfo> viewings = billboardSchedule.getSchedule(billboardName);
                        billboardExists = true;
                        //for all viewings of the billboard
                        for(ScheduleInfo viewing : viewings)
                        {
                            //get start time of viewing
                            LocalDateTime viewingStartTime = viewing.startTimeScheduled;

                            System.out.println("startTime: " + startTime);
                            System.out.println("viewingStartTime: " + viewingStartTime);

                            //if user selected start time is equal to start time of viewing stored
                            if(startTime.equals(viewingStartTime))
                            {
                                validSchedule = true;
                                //change user inputs to GUI inputs
                                String [] user_inputs = {"Remove Schedule", billboardName, startTimeString, duration, recurrenceDelay};

                                //remove viewing from schedule with viewing details given by user
                                ControlPanelClient.Run_Client(user_inputs);
                                //dispose check pop up
                                dispose();
                                //dispose schedule billboard pop up
                                dispose();
                                break;
                            }
                        }
                    }
                    //if billboard does not exist in schedule
                    catch (Exception e) {
                        //display error pop up
                        JOptionPane.showMessageDialog(this, e,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                    //if user selected start time is equal to start time of viewing stored
                    if (!validSchedule)
                    {
                        //display error pop up
                        JOptionPane.showMessageDialog(this,
                                "The viewing of " +billboardName+" for "+ startTime+" does not exist in the schedule");
                    }

                    if (billboardExists)
                    {
                        //change user inputs to GUI inputs
                        String [] user_inputs = {"Remove Schedule", billboardName, startTimeString, duration, recurrenceDelay};

                        //remove viewing from schedule with viewing details given by user
                        ControlPanelClient.Run_Client(user_inputs);
                        //dispose check pop up
                        dispose();
                        //dispose schedule billboard pop up
                        dispose();
                    }

                }
            }
        }
    }

    @Override
    public void run()
    {
        try
        {
            createGUI();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

}
