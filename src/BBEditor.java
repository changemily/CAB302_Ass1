import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.management.MalformedObjectNameException;
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
    private String billboardName;
    private String tempXMLString;
    private String username;
    private String sessionToken;
    private BillboardViewer bb = null;
    private boolean createdBillboard = false;

    BillboardList billboardList = new BillboardList();

    public BBEditor(String username, String sessionToken, String billboardName, String XMLString)
    {
        // Set window title
        super("Billboard Editor");
        this.billboardName = billboardName;
        tempXMLString = XMLString;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    public BBEditor(String username, String sessionToken, boolean createdBillboard){
        super("Billboard Editor");
        this.billboardName = null;
        tempXMLString = "<billboard></billboard>";
        this.createdBillboard = createdBillboard;
        this.username = username;
        this.sessionToken = sessionToken;
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

        // Initialise and add Billboard to Preview Panel
        Dimension d = new Dimension(600, 350);
        bb = new BillboardViewer(tempXMLString, d);
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
        ImageURL.setMaximumSize(new Dimension(230, 10));
        ImageURL.setBackground(Color.white);
        ImageURL.setForeground(Color.black);

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
        // Create nameLabel
        JLabel nameLabel = new JLabel();
        nameLabel.setText("Billboard Name");
        nameLabel.setBackground(Color.white);
        nameLabel.setForeground(Color.black);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        if(!createdBillboard)
        {
            nameLabel.setVisible(false);
        } else
        {
            nameLabel.setVisible(true);
        }

        // Create nameField
        JTextField nameField = new JTextField();
        nameField.setBackground(Color.white);
        nameField.setForeground(Color.black);
        if(!createdBillboard)
        {
            nameField.setVisible(false);
        } else
        {
            nameField.setVisible(true);
        }

        // Initialise field values
        MessageField.setText(bb.getMessageText());
        ExtraInfoText.setText(bb.getInformationText());
        if(bb.getPictureURL() != null) {
            ImageURL.setText(bb.getPictureURL());
        }
        else if(bb.getPictureDataString() != null){
            ImageURL.setText("Encoded Image");
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
        JButton SaveBttn = new JButton( new AbstractAction("Save")
        {
            @Override
            public void actionPerformed( ActionEvent e ) {
                //Variables to store for the billboard later
                if(createdBillboard){
                    billboardName = nameField.getText();
                }
                System.out.println(nameField.getText());
                String usernameOfCreator = username;

                //Get what the user has inputted and put it into the file.
                if(!MessageField.getText().equals(""))
                {
                    bb.setMessageText(MessageField.getText());
                    bb.setMessageExists(true);
                }
                else{
                    bb.setMessageExists(false);
                }

                if(!ExtraInfoText.getText().equals(""))
                {
                    bb.setInformationText(ExtraInfoText.getText());
                    bb.setInformationExists(true);
                }
                else{
                    bb.setInformationExists(false);
                }

                if(!ImageURL.getText().equals(""))
                {
                    bb.setPictureExists(true);
                    try{
                        URL urlString = new URL(ImageURL.getText());
                        bb.setUrlExists(true);
                        bb.setDataExists(false);
                        bb.setPictureURL(ImageURL.getText());
                    } catch(MalformedURLException m){
                        bb.setDataExists(true);
                        bb.setUrlExists(false);
                        File f = new File(ImageURL.getText());
                        try {
                            FileInputStream imageFile = new FileInputStream(f);
                            byte[] imageData = imageFile.readAllBytes();
                            bb.setPictureDataString(Base64.getEncoder().encodeToString(imageData));
                        } catch (IOException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                            JOptionPane.showMessageDialog(getContentPane(), fileNotFoundException,
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                else{
                    bb.setPictureExists(false);
                }

                try {
                    tempXMLString = bb.updateXMLString();
                } catch (ParserConfigurationException | TransformerException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                //Test if it worked
                System.out.println(tempXMLString);

                //Store the information for the billboard
                try {
                    //billboardList.createEditBillboard(billboardName, usernameOfCreator, tempXMLString);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String [] user_inputs = {"Create edit billboard",billboardName,usernameOfCreator, tempXMLString};

                //Schedule billboard with viewing details given by user
                ControlPanelClient.Run_Client(user_inputs);

                // all frames to a array
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if(fr.getClass().getName() == "ControlPanelGUIBillboardControlPanel"){
                        fr.dispose();
                    }
                }

                //run Billboard Control Panel GUI
                String [] user_input = {"List billboards"};
                //request schedule and run calendar GUI
                ControlPanelClient.Run_Client(user_input);


                //Close after saving so they know it has been done
                dispose();
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

        // Create BackgroundColourDisplay
        JTextArea BackgroundColourDisplay = new JTextArea();
        BackgroundColourDisplay.setEditable(false);
        BackgroundColourDisplay.setBackground(bb.getBillboardColour());

        // Create MessageTextColourDisplay
        JTextArea MessageTextColourDisplay = new JTextArea();
        MessageTextColourDisplay.setEditable(false);
        MessageTextColourDisplay.setBackground(bb.getMessageColour());

        // Create ExtraTextColourDisplay
        JTextArea ExtraTextColourDisplay = new JTextArea();
        ExtraTextColourDisplay.setEditable(false);
        ExtraTextColourDisplay.setBackground(bb.getInformationColour());

        // Create BackgroundColourBttn
        JButton BackgroundColourBttn = new JButton(
                ( new AbstractAction("Colour") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                Color originalColour = bb.getBillboardColour();
                Color colour = JColorChooser.showDialog(null,
                        "Select a color", bb.getBillboardColour());
                BackgroundColourDisplay.setBackground(colour);
                try {
                    bb.setBillboardColour(colour);
                    BackgroundColourDisplay.setBackground(colour);
                }catch (Exception f){
                    bb.setBillboardColour(originalColour);
                    BackgroundColourDisplay.setBackground(originalColour);
                }
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
                        Color originalColour = bb.getMessageColour();
                        Color colour = JColorChooser.showDialog(null,
                                "Select a color", bb.getMessageColour());
                        try {
                            bb.setMessageColour(colour);
                            MessageTextColourDisplay.setBackground(colour);
                        } catch (Exception f){
                            bb.setMessageColour(originalColour);
                            MessageTextColourDisplay.setBackground(originalColour);
                        }
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
                        Color originalColour = bb.getInformationColour();
                        Color colour = JColorChooser.showDialog(null,
                                "Select a color", bb.getInformationColour());
                        ExtraTextColourDisplay.setBackground(colour);
                        try {
                            bb.setInformationColour(colour);
                            ExtraTextColourDisplay.setBackground(colour);
                        }catch (Exception f){
                            bb.setInformationColour(originalColour);
                            ExtraTextColourDisplay.setBackground(originalColour);
                        }
                    }
                }));
        ExtraTextColourBttn.setText("Browse");
        ExtraTextColourBttn.setBackground(new Color(230, 230, 230));
        ExtraTextColourBttn.setForeground(Color.black);

        // Create importBttn
        JButton importBttn = new JButton(( new AbstractAction("Import") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser FileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int returnValue = FileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = FileChooser.getSelectedFile();
                    File f = new File(selectedFile.getAbsolutePath());
                    Reader fileReader;
                    try {
                        fileReader = new FileReader(f);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        StringBuilder output = new StringBuilder();
                        String stringXML;
                        while((stringXML = bufferedReader.readLine()) != null){
                            output.append(stringXML);
                        }
                        tempXMLString = output.toString();
                        bufferedReader.close();

                        PreviewPanel.removeAll();
                        PreviewPanel.revalidate();
                        PreviewPanel.repaint();
                        try {
                            bb = new BillboardViewer(tempXMLString, d);
                        } catch (ParserConfigurationException | IOException | SAXException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(getContentPane(), ex,
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                        JPanel billboardPreview = bb.getSizedBillboard();
                        PreviewPanel.add(billboardPreview);
                        PreviewPanel.revalidate();
                        PreviewPanel.repaint();

                        MessageField.setText(bb.getMessageText());
                        ExtraInfoText.setText(bb.getInformationText());
                        if(bb.getPictureURL() != null){
                            ImageURL.setText(bb.getPictureURL());
                        }
                        else{
                            ImageURL.setText("Encoded Image");
                        }
                        BackgroundColourDisplay.setBackground(bb.getBillboardColour());
                        MessageTextColourDisplay.setBackground(bb.getMessageColour());
                        ExtraTextColourDisplay.setBackground(bb.getInformationColour());


                    } catch (IOException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                        JOptionPane.showMessageDialog(getContentPane(), fileNotFoundException,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        }));
        importBttn.setText("Import");
        importBttn.setBackground(new Color(230, 230, 230));
        importBttn.setForeground(Color.black);

        // Create exportBttn
        String outPath = "./" + billboardName + ".xml";
        StreamResult o = new StreamResult(outPath);
        JButton exportBttn = new JButton(( new AbstractAction("Export") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!MessageField.getText().equals(""))
                {
                    bb.setMessageText(MessageField.getText());
                    bb.setMessageExists(true);
                }
                else{
                    bb.setMessageExists(false);
                }

                if(!ExtraInfoText.getText().equals(""))
                {
                    bb.setInformationText(ExtraInfoText.getText());
                    bb.setInformationExists(true);
                }
                else{
                    bb.setInformationExists(false);
                }

                if(!ImageURL.getText().equals(""))
                {
                    bb.setPictureExists(true);
                    try{
                        URL urlString = new URL(ImageURL.getText());
                        bb.setUrlExists(true);
                        bb.setDataExists(false);
                        bb.setPictureURL(ImageURL.getText());
                    } catch(MalformedURLException m){
                        bb.setDataExists(true);
                        bb.setUrlExists(false);
                        File f = new File(ImageURL.getText());
                        try {
                            FileInputStream imageFile = new FileInputStream(f);
                            byte[] imageData = imageFile.readAllBytes();
                            bb.setPictureDataString(Base64.getEncoder().encodeToString(imageData));
                        } catch (IOException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                            JOptionPane.showMessageDialog(getContentPane(), fileNotFoundException,
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }
                else{
                    bb.setPictureExists(false);
                }

                try {
                    bb.writeFile(o);
                } catch (ParserConfigurationException | TransformerException ex) {
                    ex.printStackTrace();
                }

            }
        }));
        exportBttn.setText("Export");
        exportBttn.setBackground(new Color(230, 230, 230));
        exportBttn.setForeground(Color.black);

        // Create previewBttn
        JButton previewBttn = new JButton(( new AbstractAction("Preview") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!MessageField.getText().equals(""))
                {
                    bb.setMessageText(MessageField.getText());
                    bb.setMessageExists(true);
                }
                else{
                    bb.setMessageExists(false);
                }

                if(!ExtraInfoText.getText().equals(""))
                {
                    bb.setInformationText(ExtraInfoText.getText());
                    bb.setInformationExists(true);
                }
                else{
                    bb.setInformationExists(false);
                }

                if(!ImageURL.getText().equals(""))
                {
                    bb.setPictureExists(true);
                    try{
                        URL urlString = new URL(ImageURL.getText());
                        bb.setUrlExists(true);
                        bb.setDataExists(false);
                        bb.setPictureURL(ImageURL.getText());
                    } catch(MalformedURLException m){
                        bb.setDataExists(true);
                        bb.setUrlExists(false);
                        File f = new File(ImageURL.getText());
                        try {
                            FileInputStream imageFile = new FileInputStream(f);
                            byte[] imageData = imageFile.readAllBytes();
                            bb.setPictureDataString(Base64.getEncoder().encodeToString(imageData));
                        } catch (IOException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                            JOptionPane.showMessageDialog(getContentPane(), fileNotFoundException,
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }


                    }
                }
                else{
                    bb.setPictureExists(false);
                }

                try {
                    tempXMLString = bb.updateXMLString();
                } catch (ParserConfigurationException | TransformerException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                PreviewPanel.removeAll();
                PreviewPanel.revalidate();
                PreviewPanel.repaint();
                try {
                    bb = new BillboardViewer(tempXMLString, d);
                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                JPanel billboardPreview = bb.getSizedBillboard();
                PreviewPanel.add(billboardPreview);
                PreviewPanel.revalidate();
                PreviewPanel.repaint();
            }
        }));
        previewBttn.setText("Preview");
        previewBttn.setBackground(new Color(230, 230, 230));
        previewBttn.setForeground(Color.black);
        // Set Layout for EVERYTHING
        GroupLayout MainPanelLayout = new GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
                MainPanelLayout.createParallelGroup()
                        .addGroup(MainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                .addComponent(previewBttn, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(ExitBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(importBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(exportBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(SaveBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE))
                                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                                        .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                .addComponent(MessageLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                                                        .addComponent(ImageURL)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(ImageBrowseBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(MessageField, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(ImageLabel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
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
                                                                .addGap(23, 23, 23))))
                                        .addComponent(PreviewPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(DividerLine)
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addGroup(MainPanelLayout.createParallelGroup()
                                                        .addComponent(PreviewLabel)
                                                        .addComponent(EditLabel))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        MainPanelLayout.setVerticalGroup(
                MainPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(PreviewLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PreviewPanel, GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DividerLine, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(EditLabel)
                                .addGap(19, 19, 19)
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addComponent(ExtraInfoLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(ExtraInfoScrollPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addComponent(MessageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(MessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ImageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ImageBrowseBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ImageURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(BackgroundColourPickerLabel)
                                                        .addComponent(MessageColourPickerLabel))
                                                .addGap(4, 4, 4)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(BackgroundColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(BackgroundColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(MessageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(MessageTextColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(MainPanelLayout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(nameLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)))
                                .addGroup(MainPanelLayout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                .addComponent(ExtraTextColourPickerLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ExtraTextColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(ExtraTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                .addGap(66, 66, 66))
                                        .addGroup(GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                                                .addGroup(MainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(ExitBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(SaveBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(importBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(exportBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(previewBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                                .addGap(20, 20, 20))))
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
        } catch (SAXException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(new BBEditor("admin", "124", true));
    }
}
