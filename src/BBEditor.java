import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Liam
 * To do: complete button functionaity, make preview panel dislplay actual preview
 */

public class BBEditor extends JFrame implements Runnable, ActionListener, ChangeListener
{
    public BBEditor()
    {
        // Set window title
        super("Billboard Editor");
    }

    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        // Set default look and feel & window properties
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create MainPanel
        JPanel MainPanel = new JPanel();

        // Create DividerLine
        JSeparator DividerLine = new JSeparator();

        // Create PreviewLabel
        JLabel PreviewLabel = new JLabel();
        PreviewLabel.setText("Preview");
        PreviewLabel.setBackground(Color.white);
        PreviewLabel.setForeground(Color.black);
        PreviewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PreviewLabel.setFont(PreviewLabel.getFont().deriveFont(PreviewLabel.getFont().getStyle() | Font.BOLD));

        // Create EditLabel
        JLabel EditLabel = new JLabel();
        EditLabel.setText("Edit");
        EditLabel.setBackground(Color.white);
        EditLabel.setForeground(Color.black);
        EditLabel.setHorizontalAlignment(SwingConstants.CENTER);
        EditLabel.setFont(EditLabel.getFont().deriveFont(EditLabel.getFont().getStyle() | Font.BOLD));

        // Create MessageField
        JTextField MessageField = new JTextField();
        MessageField.setBackground(Color.white);
        MessageField.setForeground(Color.black);

        // Create MessageLabel
        JLabel MessageLabel = new JLabel();
        MessageLabel.setText("Message Text");
        MessageLabel.setBackground(Color.white);
        MessageLabel.setForeground(Color.black);
        MessageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create ImageLabel
        JLabel ImageLabel = new JLabel();
        ImageLabel.setText("Image File");
        ImageLabel.setBackground(Color.white);
        ImageLabel.setForeground(Color.black);
        ImageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create PreviewScrollPanel and PreviewBox
        JScrollPane PreviewScrollPanel;
        {
            PreviewScrollPanel = new JScrollPane();
            JTextPane PreviewBox = new JTextPane();
            PreviewBox.setBackground(Color.white);
            PreviewBox.setForeground(Color.black);
            PreviewScrollPanel.setViewportView(PreviewBox);
            PreviewBox.setEditable(false);
        }

        // Create BackgroundColourPickerLabel
        JLabel BackgroundColourPickerLabel = new JLabel();
        BackgroundColourPickerLabel.setText("Background Colour");
        BackgroundColourPickerLabel.setBackground(Color.white);
        BackgroundColourPickerLabel.setForeground(Color.black);
        BackgroundColourPickerLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create MessageColourPickerLabel
        JLabel MessageColourPickerLabel = new JLabel();
        MessageColourPickerLabel.setText("Message Text Colour");
        MessageColourPickerLabel.setBackground(Color.white);
        MessageColourPickerLabel.setForeground(Color.black);
        MessageColourPickerLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create ExtraTextColourPickerLabel
        JLabel ExtraTextColourPickerLabel = new JLabel();
        ExtraTextColourPickerLabel.setText("Extra Information Text Colour");
        ExtraTextColourPickerLabel.setBackground(Color.white);
        ExtraTextColourPickerLabel.setForeground(Color.black);
        ExtraTextColourPickerLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create ImageURL
        JTextField ImageURL = new JTextField();
        ImageURL.setBackground(Color.white);
        ImageURL.setForeground(Color.black);

        //Create ExtraInfoLabel
        JLabel ExtraInfoLabel = new JLabel();
        ExtraInfoLabel.setText("Extra Information Text");
        ExtraInfoLabel.setBackground(Color.white);
        ExtraInfoLabel.setForeground(Color.black);
        ExtraInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create ExtraInfoScrollPanel and ExtraInfoText
        JScrollPane ExtraInfoScrollPanel;
        {
            ExtraInfoScrollPanel = new JScrollPane();
            JTextArea ExtraInfoText = new JTextArea();
            ExtraInfoText.setBackground(Color.white);
            ExtraInfoText.setForeground(Color.black);
            ExtraInfoScrollPanel.setViewportView(ExtraInfoText);
        }

        // Create ImageBrowseBttn
        JButton ImageBrowseBttn = new JButton();
        ImageBrowseBttn.setText("Browse");
        ImageBrowseBttn.setBackground(new Color(230, 230, 230));
        ImageBrowseBttn.setForeground(Color.black);

        // Create SaveBttn
        JButton SaveBttn = new JButton();
        SaveBttn.setText("Save And Exit");
        SaveBttn.setBackground(new Color(230, 230, 230));
        SaveBttn.setForeground(Color.black);

        // Create ExitBttn
        JButton ExitBttn = new JButton(( new AbstractAction("Exit")
        {
            @Override
            public void actionPerformed( ActionEvent e ) {
                System.exit(0);
            }
        }));
        ExitBttn.setText("Exit Without Saving");
        ExitBttn.setBackground(new Color(230, 230, 230));
        ExitBttn.setForeground(Color.black);

        // Create BackgroundColourDisplay
        JTextArea BackgroundColourDisplay = new JTextArea();
        BackgroundColourDisplay.setEditable(false);

        // Create MessageTextColourDisplay
        JTextArea MessageTextColourDisplay = new JTextArea();
        MessageTextColourDisplay.setEditable(false);

        // Create ExtraTextColourDisplay
        JTextArea ExtraTextColourDisplay = new JTextArea();
        ExtraTextColourDisplay.setEditable(false);

        // Create BackgroundColourBttn
        JButton BackgroundColourBttn = new JButton(
                ( new AbstractAction("Colour") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                Color initialcolour = Color.RED;
                Color colour = JColorChooser.showDialog(null,
                        "Select a color", initialcolour);
                BackgroundColourDisplay.setBackground(colour);
            }
        }));
        BackgroundColourBttn.setText("Browse");
        BackgroundColourBttn.setBackground(new Color(230, 230, 230));
        BackgroundColourBttn.setForeground(Color.black);

        //Create MessageTextColourBttn
        JButton MessageTextColourBttn = new JButton(
                ( new AbstractAction("Colour") {
                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        Color initialcolour = Color.RED;
                        Color colour = JColorChooser.showDialog(null,
                                "Select a color", initialcolour);
                        MessageTextColourDisplay.setBackground(colour);
                    }
                }));
        MessageTextColourBttn.setText("Browse");
        MessageTextColourBttn.setBackground(new Color(230, 230, 230));
        MessageTextColourBttn.setForeground(Color.black);

        //Create ExtraTextColourBttn
        JButton ExtraTextColourBttn = new JButton(
                ( new AbstractAction("Colour") {
                    @Override
                    public void actionPerformed( ActionEvent e ) {
                        Color initialcolour = Color.RED;
                        Color colour = JColorChooser.showDialog(null,
                                "Select a color", initialcolour);
                        ExtraTextColourDisplay.setBackground(colour);
                    }
                }));
        ExtraTextColourBttn.setText("Browse");
        ExtraTextColourBttn.setBackground(new Color(230, 230, 230));
        ExtraTextColourBttn.setForeground(Color.black);


        // Set Layout for EVERYTHING
        GroupLayout MainPanelLayout = new GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
                MainPanelLayout.createParallelGroup()
                        .addGroup(MainPanelLayout.createSequentialGroup()
                                .addGap(337, 337, 337)
                                .addComponent(PreviewLabel)
                                .addContainerGap(336, Short.MAX_VALUE))
                        .addGroup(MainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addComponent(DividerLine)
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 345, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(EditLabel)
                                                .addGap(340, 340, 340))
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                .addComponent(SaveBttn, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(ExitBttn)
                                                                .addGap(425, 425, 425))
                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                                        .addComponent(MessageLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                                .addComponent(ImageURL, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(ImageBrowseBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(MessageField, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(ImageLabel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                                        .addComponent(ExtraInfoScrollPanel, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(ExtraInfoLabel)
                                                                        .addComponent(BackgroundColourPickerLabel, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                                                        .addComponent(BackgroundColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(ExtraTextColourDisplay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                                                .addComponent(BackgroundColourBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                                                                        .addComponent(MessageColourPickerLabel, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
                                                                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                                                                .addComponent(MessageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(MessageTextColourBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))))
                                                                                        .addComponent(ExtraTextColourBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)))
                                                                        .addComponent(ExtraTextColourPickerLabel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 23, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap())
                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addContainerGap(62, Short.MAX_VALUE)
                                .addComponent(PreviewScrollPanel, GroupLayout.PREFERRED_SIZE, 601, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(55, Short.MAX_VALUE))
        );
        MainPanelLayout.setVerticalGroup(
                MainPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(PreviewLabel)
                                .addGap(12, 12, 12)
                                .addComponent(PreviewScrollPanel, GroupLayout.PREFERRED_SIZE, 364, GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(DividerLine, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EditLabel)
                                .addGap(1, 1, 1)
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addComponent(MessageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(MessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ImageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ImageURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ImageBrowseBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(SaveBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ExitBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addComponent(ExtraInfoLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ExtraInfoScrollPanel, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(BackgroundColourPickerLabel)
                                                        .addComponent(MessageColourPickerLabel))
                                                .addGap(4, 4, 4)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(BackgroundColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(BackgroundColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(MessageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(MessageTextColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ExtraTextColourPickerLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ExtraTextColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ExtraTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                .addGap(26, 26, 26)))
                                .addGap(20, 20, 20))
        );

        getContentPane().add(MainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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

    @Override
    public void stateChanged(ChangeEvent c)
    {

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new BBEditor());
    }
}
