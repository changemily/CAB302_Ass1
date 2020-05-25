import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        JSpinner DurationSpinner = new JSpinner();
        DurationSpinner.setFont(DurationSpinner.getFont().deriveFont(DurationSpinner.getFont().getSize() + 7f));

        // Create DurationLabel
        JLabel DurationLabel = new JLabel();
        DurationLabel.setText("Duration (Minutes)");
        DurationLabel.setFont(DurationLabel.getFont().deriveFont(DurationLabel.getFont().getSize() + 5f));
        DurationLabel.setAlignmentX(0.5F);

        // Create DatePickerLabel
        JLabel DatePickerLabel = new JLabel();
        DatePickerLabel.setText("Date Scheduled");
        DatePickerLabel.setFont(DatePickerLabel.getFont().deriveFont(DatePickerLabel.getFont().getSize() + 5f));
        DatePickerLabel.setAlignmentX(0.5F);

        // Create Date Picker
        JLabel DP = new JLabel();

        // Create FrequencyLabel
        JLabel FrequencyLabel = new JLabel();
        FrequencyLabel.setText("Frequency Label");
        FrequencyLabel.setFont(FrequencyLabel.getFont().deriveFont(FrequencyLabel.getFont().getSize() + 5f));
        FrequencyLabel.setAlignmentX(0.5F);

        // Create list of frequency options
        String[] FrequencyOptions = { "Every Minute", "Every Hour", "Every Day" };

        // Create FrequencyPicker
        JComboBox FrequencyPicker = new JComboBox(FrequencyOptions);

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
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addComponent(RemoveBttn, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(SaveBttn, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                                .addGap(26, 26, 26)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(FrequencyPicker, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                                                        .addComponent(DP)
                                                        .addComponent(DurationSpinner, GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                                                .addGap(46, 46, 46)
                                                .addGroup(contentPaneLayout.createParallelGroup()
                                                        .addComponent(DurationLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(FrequencyLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(DatePickerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(DurationSpinner, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(DurationLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addGap(29, 29, 29)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(DP, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(DatePickerLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addGap(29, 29, 29)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(FrequencyPicker, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                        .addComponent(FrequencyLabel, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(RemoveBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                                        .addComponent(SaveBttn, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                                .addGap(17, 17, 17))
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
            String [] user_inputs = {"Schedule Billboard","2","2021-05-25T10:00:00.00", "1", "none"};

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
                String [] user_inputs = {"Remove Schedule","2", "2021-05-25T10:00:00.00", "1", "none"};
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
