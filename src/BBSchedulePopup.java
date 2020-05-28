import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private JButton removeBttn;
    private JButton scheduleBttn;
    private JSpinner durationSpinner;
    private JSpinner dateTimePicker;
    private JComboBox recurrencePicker;
    private JSpinner recurrenceMins;
    private String billboardName;
    private final String MINUTES_IN_DAY = "1440";
    private final String MINUTES_IN_HOUR = "60";
    private String username;
    private String sessionToken;

    public BBSchedulePopup(String username, String sessionToken, String billboardName)
    {
        // Set window title
        super("Schedule Billboard");

        this.billboardName = billboardName;
        this.username = username;
        this.sessionToken = sessionToken;
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

        // Create Save Button
        scheduleBttn = createButton("Schedule Billboard");

        // Set Group Layout
        GroupLayout contentPaneLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(removeBttn, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(scheduleBttn, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(recurrenceMins, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(200, Short.MAX_VALUE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(dateTimePicker, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(recurrencePicker, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(durationSpinner, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(durationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(datePickerLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(recurrenceOptionLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18))))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(durationLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(durationSpinner, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dateTimePicker, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(datePickerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(recurrenceOptionLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(recurrencePicker, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(recurrenceMins, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(removeBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                        .addComponent(scheduleBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
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
        jspinner.setFont(jspinner.getFont().deriveFont(jspinner.getFont().getSize() + 7f));
        return jspinner;
    }

    private JSpinner createDateJSpinner() {
        SpinnerModel spinnerModel;

        //set spinner model to dayTime
        spinnerModel = new SpinnerDateModel();

        //create new JSpinner
        JSpinner jspinner = new JSpinner(spinnerModel);

        //set font and size of JSpinner
        jspinner.setFont(durationSpinner.getFont().deriveFont(durationSpinner.getFont().getSize() + 7f));
        return jspinner;
    }

    private JLabel createLabel(String labelText)
    {
        //create new JLabel
        JLabel label = new JLabel();

        //set text of label
        label.setText(labelText);

        //format label's font, size and alignment
        label.setFont(label.getFont().deriveFont(label.getFont().getSize() + 5f));
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
        button.setAlignmentX(0.5F);
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

        //get date time string
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
                    //change user inputs to GUI inputs
                    String [] user_inputs = {"Remove Schedule", billboardName, startTimeString, duration, recurrenceDelay};

                    //remove viewing from schedule with viewing details given by user
                    ControlPanelClient.Run_Client(user_inputs);
                    dispose();
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

    public static void main(String[] args)
    {
        //SwingUtilities.invokeLater(new BBSchedulePopup(BillboardName));
    }

}
