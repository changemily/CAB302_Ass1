import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import static javax.swing.JOptionPane.YES_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

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

    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException {
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

        // Create Billboard Preview Panel
        JPanel PreviewPanel = new JPanel();

        // Create XML File Path
        String FilePath = new String("C:\\Users\\liamj\\OneDrive\\Desktop\\New folder\\12.xml");
        String outPath = new String("C:\\Users\\liamj\\OneDrive\\Desktop\\New folder\\test.xml");

        // Initialise and add Billboard to Preview Panel
        File f = new File(FilePath);
        Dimension d = new Dimension(600, 350);
        BillboardViewer bb = new BillboardViewer(f, d);
        JPanel billboardPreview = bb.getSizedBillboard();
        PreviewPanel.add(billboardPreview);

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
        ImageURL.setEditable(false);

        //Create ExtraInfoLabel
        JLabel ExtraInfoLabel = new JLabel();
        ExtraInfoLabel.setText("Extra Information Text");
        ExtraInfoLabel.setBackground(Color.white);
        ExtraInfoLabel.setForeground(Color.black);
        ExtraInfoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create ExtraInfoScrollPanel and ExtraInfoText
        JTextArea ExtraInfoText = new JTextArea();
        JScrollPane ExtraInfoScrollPanel;
        {
            ExtraInfoScrollPanel = new JScrollPane();
            ExtraInfoText.setBackground(Color.white);
            ExtraInfoText.setForeground(Color.black);
            ExtraInfoScrollPanel.setViewportView(ExtraInfoText);
        }

        // Create ImageBrowseBttn
        JButton ImageBrowseBttn = new JButton(( new AbstractAction("Browse Image") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser FileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = FileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = FileChooser.getSelectedFile();
                    ImageURL.setText(selectedFile.getAbsolutePath());
                }
            }
        }));
        ImageBrowseBttn.setText("Browse");
        ImageBrowseBttn.setBackground(new Color(230, 230, 230));
        ImageBrowseBttn.setForeground(Color.black);

        // Create SaveBttn
        StreamResult o = new StreamResult(outPath);
        JButton SaveBttn = new JButton( new AbstractAction("Exit")
        {
            @Override
            public void actionPerformed( ActionEvent e ) {
                //bb.setInformationText(ExtraInfoText.getText());
                //bb.setMessageText(MessageField.getText());
                try {
                    bb.writeFile(o);
                 } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (TransformerException ex) {
                    ex.printStackTrace();
                }
            }
        });
        SaveBttn.setText("Save");
        SaveBttn.setBackground(new Color(230, 230, 230));
        SaveBttn.setForeground(Color.black);

        // Create ExitBttn
        JButton ExitBttn = new JButton(( new AbstractAction("Exit")
        {
            @Override
            public void actionPerformed( ActionEvent e ) {
                int a = showConfirmDialog(null, "Have you saved any changes?");
                if(a == YES_OPTION)
                {
                    dispose();
                }
            }
        }));
        ExitBttn.setText("Exit");
        ExitBttn.setBackground(new Color(230, 230, 230));
        ExitBttn.setForeground(Color.black);

        // Create Preview RefreshBttn
        JButton RefreshBttn = new JButton(( new AbstractAction("Exit")
        {
            @Override
            public void actionPerformed( ActionEvent e ) {
                billboardPreview.revalidate();
                billboardPreview.repaint();
            }
        }));
        RefreshBttn.setText("Refresh");
        RefreshBttn.setBackground(new Color(230, 230, 230));
        RefreshBttn.setForeground(Color.black);

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
                bb.setBillboardColour(colour);
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
                        //if(bb.messageExists == true)
                        //{
                        //    bb.setMessageColour(colour);
                        //}
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
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                .addComponent(PreviewPanel, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                                                .addGap(58, 58, 58))
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                .addComponent(RefreshBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                .addGap(311, 311, 311))))
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
                                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(MessageLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                                                .addComponent(ImageURL)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(ImageBrowseBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(MessageField, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(ImageLabel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(MainPanelLayout.createParallelGroup()
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
                                                                        .addComponent(ExtraTextColourPickerLabel, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(ExtraInfoScrollPanel, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(23, 23, 23)))))
                                .addContainerGap())
                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PreviewPanel, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                                .addGap(58, 58, 58))
        );
        MainPanelLayout.setVerticalGroup(
                MainPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(PreviewLabel)
                                .addGap(18, 18, 18)
                                .addComponent(PreviewPanel, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RefreshBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(DividerLine, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(EditLabel)
                                .addGap(1, 1, 1)
                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addComponent(MessageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(MessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ImageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ImageBrowseBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ImageURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addComponent(ExtraInfoLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ExtraInfoScrollPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(SaveBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(ExitBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
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
                                .addGap(40, 40, 40))
        );

        getContentPane().add(MainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
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
        } catch (SAXException e)
        {
            e.printStackTrace();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        } catch (IOException e)
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
