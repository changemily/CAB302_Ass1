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
    JButton RemoveBttn;
    JButton ScheduleBttn;
    JSpinner DurationSpinner;
    JSpinner dateTimePicker;
    JComboBox RecurrencePicker;
    JSpinner frequencySpinner;
    final String MINUTES_IN_DAY = "1440";
    final String MINUTES_IN_HOUR = "60";

    public BBSchedulePopup()
    {
        // Set window title
        super("Billboard Viewer");
    }
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        // Set default look and feel & window properties
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create DurationSpinner
        SpinnerModel durationModel = new SpinnerNumberModel(0, 0, 1440, 1);
        DurationSpinner = new JSpinner(durationModel);
        DurationSpinner.setFont(DurationSpinner.getFont().deriveFont(DurationSpinner.getFont().getSize() + 7f));

        // Create DurationLabel
        JLabel DurationLabel = new JLabel();
        DurationLabel.setText("Duration (Minutes)");
        DurationLabel.setFont(DurationLabel.getFont().deriveFont(DurationLabel.getFont().getSize() + 5f));
        DurationLabel.setAlignmentX(0.5F);

        // Create DatePickerLabel
        JLabel DatePickerLabel = new JLabel();
        DatePickerLabel.setText("Date and Time");
        DatePickerLabel.setFont(DatePickerLabel.getFont().deriveFont(DatePickerLabel.getFont().getSize() + 5f));
        DatePickerLabel.setAlignmentX(0.5F);

        // Create Day Time Spinner
        SpinnerModel dayTimePickerModel = new SpinnerDateModel();
        dateTimePicker = new JSpinner(dayTimePickerModel);
        dateTimePicker.setFont(DurationSpinner.getFont().deriveFont(DurationSpinner.getFont().getSize() + 7f));

        // Create lists for frequency, day and hour options for combo boxes
        String[] FrequencyOptions = { "No Recurrence","Every X Minute(s)", "Every Hour", "Every Day" };

        // Create FrequencyLabel
        JLabel FrequencyLabel = new JLabel();
        FrequencyLabel.setText("Recurrence");
        FrequencyLabel.setFont(FrequencyLabel.getFont().deriveFont(FrequencyLabel.getFont().getSize() + 5f));
        FrequencyLabel.setAlignmentX(0.5F);

        // Create frequencySpinner
        SpinnerModel frequencyModel = new SpinnerNumberModel(0, 0, 59, 1);
        frequencySpinner = new JSpinner(frequencyModel);
        frequencySpinner.setFont(DurationSpinner.getFont().deriveFont(DurationSpinner.getFont().getSize() + 7f));

        // Create FrequencyPicker
        RecurrencePicker = new JComboBox(FrequencyOptions);
        RecurrencePicker.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(RecurrencePicker.getSelectedIndex() == 1) {
                    frequencySpinner.setVisible(true);
                } else
                {
                    frequencySpinner.setVisible(false);
                }
            }
        });

        // Create Remove Button
        RemoveBttn = new JButton();
        RemoveBttn.addActionListener(this);
        RemoveBttn.setText("Remove From Schedule");
        RemoveBttn.setAlignmentX(0.5F);

        // Create Save Button
        ScheduleBttn = new JButton();
        ScheduleBttn.addActionListener(this);
        ScheduleBttn.setText("Schedule Billboard");
        ScheduleBttn.setAlignmentX(0.5F);

        // Set Group Layout
        GroupLayout contentPaneLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(RemoveBttn, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(ScheduleBttn, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(frequencySpinner, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(200, Short.MAX_VALUE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(dateTimePicker, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(RecurrencePicker, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(DurationSpinner, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(DurationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(DatePickerLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(FrequencyLabel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18))))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(DurationLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(DurationSpinner, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dateTimePicker, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(DatePickerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(FrequencyLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(RecurrencePicker, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(frequencySpinner, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(RemoveBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                        .addComponent(ScheduleBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     *
     * @param dateTime
     * @return
     */
    public String calendarToLocalDateTime (String dateTime)
    {
        //create calendar and add date time of viewing to it
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
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
    public String getReccurrenceDelay(String recurrenceOption){

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
            recurrenceDelay = frequencySpinner.getValue().toString();
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

        //format to LocalDateTimes
        String localDateTimeString = calendarToLocalDateTime(dateTime);

        //get duration from spinner
        String duration = DurationSpinner.getValue().toString();

        //get recurrence option from spinner
        String recurrenceOption = RecurrencePicker.getSelectedItem().toString();

        //get recurrence delay for users' recurrence choice as a string
        String recurrenceDelay = getReccurrenceDelay(recurrenceOption);

        //if schedule button is selected
        if (buttonClicked== ScheduleBttn) {
            System.out.println("Save pressed");

            //change user inputs to GUI inputs
            String [] user_inputs = {"Schedule Billboard","3",localDateTimeString, duration, recurrenceDelay};

            //Schedule billboard with viewing details given by user
            ControlPanelClient.Run_Client(user_inputs);

            //FOR TESTING
            System.out.println("request: "+user_inputs[0]);
            System.out.println("bb name: "+user_inputs[1]);
            System.out.println("Start time: "+user_inputs[2]);
            System.out.println("Duration: "+user_inputs[3]);
            System.out.println("recurrence: "+user_inputs[4]);

        }

        //if remove schedule button is selected
        else if (buttonClicked== RemoveBttn) {

            int a = showConfirmDialog(null, "Are you sure you want to remove this billboard?");
            if(a == YES_OPTION)
            {
                //change user inputs to GUI inputs
                String [] user_inputs = {"Remove Schedule","3",localDateTimeString, duration, recurrenceDelay};

                //remove viewing from schedule with viewing details given by user
                ControlPanelClient.Run_Client(user_inputs);
                dispose();
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
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new BBSchedulePopup());
    }

}
