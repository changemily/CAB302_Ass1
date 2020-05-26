import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.GroupLayout;
import static javax.swing.JOptionPane.*;

/**
 * @author Liam
 */

public class BBSchedulePopup extends JFrame implements Runnable, ActionListener
{
    JButton RemoveBttn;
    JButton SaveBttn;
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
        JSpinner DurationSpinner = new JSpinner(durationModel);
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

        // Create lists for frequency, day and hour options for combo boxes
        String[] FrequencyOptions = { "Every X Minute(s)", "Every Hour", "Every Day" };
        String[] dayOptions = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] timeOptions = { "12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am",
                "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm",};

        // Create Day Picker
        JComboBox dayPicker = new JComboBox(dayOptions);

        // Create Time Picker
        JComboBox timePicker = new JComboBox(timeOptions);

        // Create FrequencyLabel
        JLabel FrequencyLabel = new JLabel();
        FrequencyLabel.setText("Repeat Frequency");
        FrequencyLabel.setFont(FrequencyLabel.getFont().deriveFont(FrequencyLabel.getFont().getSize() + 5f));
        FrequencyLabel.setAlignmentX(0.5F);

        // Create frequencySpinner
        SpinnerModel frequencyModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner frequencySpinner = new JSpinner(frequencyModel);
        frequencySpinner.setFont(DurationSpinner.getFont().deriveFont(DurationSpinner.getFont().getSize() + 7f));

        // Create FrequencyPicker
        JComboBox FrequencyPicker = new JComboBox(FrequencyOptions);
        FrequencyPicker.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(FrequencyPicker.getSelectedIndex() == 0) {
                    frequencySpinner.setVisible(true);
                } else
                {
                    frequencySpinner.setVisible(false);
                }
            }
        });

        // Create Remove Button
        RemoveBttn = new JButton();
                /*( new AbstractAction("Remove") {
                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        int a = showConfirmDialog(null, "Are you sure you want to remove this billboard?");
                        if(a == YES_OPTION)
                        {
                            dispose();
                        }
                    }
                }));*/
        RemoveBttn.addActionListener(this);
        RemoveBttn.setText("Remove From Schedule");
        RemoveBttn.setAlignmentX(0.5F);

        // Create Save Button
        SaveBttn = new JButton();
        SaveBttn.addActionListener(this);
        SaveBttn.setText("Save Changes");
        SaveBttn.setAlignmentX(0.5F);

        // Set Group Layout
        GroupLayout contentPaneLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(RemoveBttn, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(SaveBttn, GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(frequencySpinner, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(200, Short.MAX_VALUE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(dayPicker, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(timePicker, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(FrequencyPicker, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
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
                                        .addComponent(dayPicker, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(DatePickerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timePicker, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(FrequencyLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(FrequencyPicker, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(frequencySpinner, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(RemoveBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                        .addComponent(SaveBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        if (buttonClicked== SaveBttn) {
            System.out.println("Save pressed");

            //FOR TESTING
            String current_time = LocalDateTime.now().plus(Duration.ofMinutes(2)).toString();
            //change user inputs to GUI inputs
            String [] user_inputs = {"Schedule Billboard","3",current_time, "1", "0"};

            //Schedule billboard with viewing details given by user
            ControlPanelClient.Run_Client(user_inputs);

            //FOR TESTING
            System.out.println("request: "+user_inputs[0]);
            System.out.println("bb name: "+user_inputs[1]);
            System.out.println("Start time: "+user_inputs[2]);
            System.out.println("Duration: "+user_inputs[3]);
            System.out.println("recurrence: "+user_inputs[4]);

        }

        else if (buttonClicked== RemoveBttn) {

            int a = showConfirmDialog(null, "Are you sure you want to remove this billboard?");
            if(a == YES_OPTION)
            {
                //change user inputs to GUI inputs
                String [] user_inputs = {"Remove Schedule","2", "2021-05-25T10:00:00.00", "1", "0"};
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
