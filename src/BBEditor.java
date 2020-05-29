import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
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
 *
 */

public class BBEditor extends JFrame implements Runnable, ActionListener, ChangeListener
{
    private String billboardName;
    private String tempXMLString;
    private String username;
    private String sessionToken;
    private BillboardViewer bb = null;
    private boolean createdBillboard = false;
    private JPanel mainPanel;
    private JPanel previewPanel;
    private JPanel billboardPreview;
    private JSeparator dividerLine;
    private JLabel previewLabel;
    private JLabel editLabel;
    private JLabel messageLabel;
    private JLabel imageLabel;
    private JLabel backgroundColourPickerLabel;
    private JLabel messageColourPickerLabel;
    private JLabel extraTextColourPickerLabel;
    private JLabel extraInfoLabel;
    private JLabel nameLabel;
    private JTextField imageURL;
    private JTextField messageField;
    private JTextField nameField;
    private JTextArea extraInfoText;
    private JTextArea backgroundColourDisplay;
    private JTextArea messageTextColourDisplay;
    private JTextArea extraTextColourDisplay;
    private JButton imageBrowseBttn;
    private JButton exitBttn;
    private JButton backgroundColourBttn;
    private JButton messageTextColourBttn;
    private JButton extraTextColourBttn;
    private JButton importBttn;
    private JButton exportBttn;
    private JButton previewBttn;
    private JButton saveBttn;
    private JButton backBttn;
    private GroupLayout mainPanelLayout;
    private StreamResult o;
    private Dimension d;

    public BBEditor(String username, String sessionToken, String billboardName, String XMLString)
    {
        // Set window title
        super("Billboard Editor");
        this.billboardName = billboardName;
        tempXMLString = XMLString;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    public BBEditor(String username, String sessionToken){
        super("Billboard Editor");
        this.billboardName = null;
        tempXMLString = "<billboard></billboard>";
        createdBillboard = true;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException {
        // Set default look and feel & window properties
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create mainPanel
        mainPanel = new JPanel();

        // Create Billboard Preview Panel
        previewPanel = new JPanel();

        // Initialise and add Billboard to Preview Panel
        d = new Dimension(600, 350);
        bb = new BillboardViewer(tempXMLString, d);
        billboardPreview = bb.getSizedBillboard();
        previewPanel.add(billboardPreview);

        // Create dividerLine
        dividerLine = new JSeparator();

        // Create previewLabel
        previewLabel = createLabel("Preview");
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setFont(previewLabel.getFont().deriveFont(previewLabel.getFont().getStyle() | Font.BOLD));

        // Create editLabel
        editLabel = createLabel("Edit");
        editLabel.setHorizontalAlignment(SwingConstants.CENTER);
        editLabel.setFont(editLabel.getFont().deriveFont(editLabel.getFont().getStyle() | Font.BOLD));

        // Create messageLabel
        messageLabel = createLabel("Message Text");

        // Create imageLabel
        imageLabel = createLabel("Image File");

        // Create backgroundColourPickerLabel
        backgroundColourPickerLabel = createLabel("Background Colour");

        // Create messageColourPickerLabel
        messageColourPickerLabel = createLabel("Message Text Colour");

        // Create extraTextColourPickerLabel
        extraTextColourPickerLabel = createLabel("Extra Information Text Colour");

        //Create extraInfoLabel
        extraInfoLabel = createLabel("Extra Information Text");

        // Create nameLabel
        nameLabel = createLabel("Billboard Name");
        if(!createdBillboard)
        {
            nameLabel.setVisible(false);
        } else
        {
            nameLabel.setVisible(true);
        }

        // Create messageField
        messageField = createTextField();

        // Create imageURL
        imageURL = createTextField();
        imageURL.setMaximumSize(new Dimension(230, 10));

        // Create nameField
        nameField = createTextField();
        if(!createdBillboard)
        {
            nameField.setVisible(false);
        } else
        {
            nameField.setVisible(true);
        }

        // Create extraInfoScrollPanel and extraInfoText
        extraInfoText = new JTextArea();
        JScrollPane extraInfoScrollPanel;
        {
            extraInfoScrollPanel = new JScrollPane();
            extraInfoText.setBackground(Color.white);
            extraInfoText.setForeground(Color.black);
            extraInfoScrollPanel.setViewportView(extraInfoText);
        }

        // Initialise field values
        messageField.setText(bb.getMessageText());
        extraInfoText.setText(bb.getInformationText());
        if(bb.getPictureURL() != null) {
            imageURL.setText(bb.getPictureURL());
        }
        else if(bb.getPictureDataString() != null){
            imageURL.setText("Encoded Image");
        }

        // Create BackgroundColourDisplay
        backgroundColourDisplay = createColourDiplay();
        backgroundColourDisplay.setBackground(bb.getBillboardColour());

        // Create MessageTextColourDisplay
        messageTextColourDisplay = createColourDiplay();
        messageTextColourDisplay.setBackground(bb.getMessageColour());

        // Create ExtraTextColourDisplay
        extraTextColourDisplay = createColourDiplay();
        extraTextColourDisplay.setBackground(bb.getInformationColour());

        // Create imageBrowseBttn
        imageBrowseBttn = createButton("Browse");

        // Create SaveBttn
        saveBttn = createButton("Save");

        // Create ExitBttn
        exitBttn = createButton("Exit");

        // Create BackgroundColourBttn
        backgroundColourBttn = createButton("Browse");

        //Create MessageTextColourBttn
        messageTextColourBttn = createButton("Browse");

        //Create ExtraTextColourBttn
        extraTextColourBttn = createButton("Browse");

        // Create importBttn
        importBttn = createButton("Import");

        // Create exportBttn
        String outPath = "./" + billboardName + ".xml";
        o = new StreamResult(outPath);
        exportBttn = createButton("Export");

        // Create previewBttn
        previewBttn = createButton("Preview");

        // Create backBttn
        backBttn = createButton("Back");

        // Set Layout for EVERYTHING
        mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup()
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(mainPanelLayout.createParallelGroup()
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                .addComponent(previewBttn, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(exitBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(importBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(exportBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(saveBttn)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                                                .addComponent(backBttn))
                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                .addComponent(messageLabel)
                                                                                .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                                                        .addComponent(imageURL)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(imageBrowseBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(messageField, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(imageLabel))
                                                                        .addComponent(nameLabel)
                                                                        .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                        .addComponent(extraInfoLabel)
                                                                        .addComponent(backgroundColourPickerLabel)
                                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                                        .addComponent(backgroundColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(extraTextColourDisplay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                                                .addComponent(backgroundColourBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                                                        .addComponent(messageColourPickerLabel)
                                                                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                                                                .addComponent(messageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(messageTextColourBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))))
                                                                                        .addComponent(extraTextColourBttn, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)))
                                                                        .addComponent(extraTextColourPickerLabel)
                                                                        .addComponent(extraInfoScrollPanel, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE))))
                                                .addGap(23, 23, 23))
                                        .addComponent(previewPanel, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dividerLine)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                        .addComponent(previewLabel)
                                                        .addComponent(editLabel))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(previewLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(previewPanel, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addComponent(dividerLine, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(editLabel)
                                .addGap(19, 19, 19)
                                .addGroup(mainPanelLayout.createParallelGroup()
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(extraInfoLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(extraInfoScrollPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(messageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(imageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(imageBrowseBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(imageURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                .addGroup(mainPanelLayout.createParallelGroup()
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(backgroundColourPickerLabel)
                                                        .addComponent(messageColourPickerLabel))
                                                .addGap(4, 4, 4)
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(backgroundColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(backgroundColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(messageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(messageTextColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(nameLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)))
                                .addGroup(mainPanelLayout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                .addComponent(extraTextColourPickerLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(extraTextColourBttn, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(extraTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                .addGap(66, 66, 66))
                                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(exitBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(previewBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(importBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(exportBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(saveBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(backBttn, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                                .addGap(20, 20, 20))))
        );

        getContentPane().add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createLabel(String labelText)
    {
        JLabel label = new JLabel();
        label.setText(labelText);
        label.setBackground(Color.white);
        label.setForeground(Color.black);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JTextField createTextField()
    {
        JTextField text = new JTextField();
        text.setBackground(Color.white);
        text.setForeground(Color.black);
        return text;
    }

    private JTextArea createColourDiplay()
    {
        JTextArea colourDisplay = new JTextArea();
        colourDisplay.setEditable(false);
        return colourDisplay;
    }

    private JButton createButton(String buttonName)
    {
        JButton button = new JButton();
        button.setText(buttonName);
        button.setBackground(new Color(230, 230, 230));
        button.setForeground(Color.black);
        button.addActionListener(this);
        return button;
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
        Object buttonClicked = e.getSource();

        if(buttonClicked == imageBrowseBttn)
        {
            JFileChooser FileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = FileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = FileChooser.getSelectedFile();
                imageURL.setText(selectedFile.getAbsolutePath());
            }
        } else if(buttonClicked == saveBttn){
            //Variables to store for the billboard later
            boolean Break = false;
            if(createdBillboard && !Break) {
                billboardName = nameField.getText();
                System.out.println("billboardNameInput: "+billboardName);
                //if name has NOT been inputted in text field
                if (billboardName.equals("")||billboardName.equals(" ")) {
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You must select a name for the billboard");
                    Break = true;
                }
            }
            else {
                System.out.println(billboardName);
            }

            if(!messageField.getText().equals(""))
            {
                bb.setMessageText(messageField.getText());
                bb.setMessageExists(true);
            }
            else{
                bb.setMessageExists(false);
            }

            if(!extraInfoText.getText().equals(""))
            {
                bb.setInformationText(extraInfoText.getText());
                bb.setInformationExists(true);
            }
            else{
                bb.setInformationExists(false);
            }

            if(!imageURL.getText().equals(""))
            {
                bb.setPictureExists(true);
                try{
                    URL urlString = new URL(imageURL.getText());
                    bb.setUrlExists(true);
                    bb.setDataExists(false);
                    bb.setPictureURL(imageURL.getText());
                } catch(MalformedURLException m){
                    bb.setDataExists(true);
                    bb.setUrlExists(false);
                    File f = new File(imageURL.getText());
                    try {
                        if(!imageURL.getText().equals("Encoded Image")) {
                            FileInputStream imageFile = new FileInputStream(f);
                            byte[] imageData = imageFile.readAllBytes();
                            bb.setPictureDataString(Base64.getEncoder().encodeToString(imageData));
                        }
                    } catch (IOException fileNotFoundException) {
                        JOptionPane.showMessageDialog(getContentPane(), fileNotFoundException,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                        Break = true;
                    }
                }
            }
            else{
                bb.setPictureExists(false);
            }

            if(!Break) {
                try {
                    tempXMLString = bb.updateXMLString();
                    JOptionPane.showMessageDialog(getContentPane(), "Billboard Successfully Saved to Database");
                } catch (ParserConfigurationException | TransformerException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }

            //Test if it worked
            if(!Break) {
                System.out.println(tempXMLString);

                String[] user_inputs = {"Create edit billboard", billboardName, username, tempXMLString};

                //Schedule billboard with viewing details given by user
                ControlPanelClient.Run_Client(user_inputs);
            }
        } else if (buttonClicked == exitBttn)
        {
            int a = showConfirmDialog(null, "Have you saved any changes?");
            if(a == YES_OPTION)
            {
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("ControlPanelGUIBillboardControlPanel") || (fr.getClass().getName().equals("ControlPanelGUI")))){
                        fr.dispose();
                        if((fr.getClass().getName().equals("ControlPanelGUIBillboardControlPanel") || (fr.getClass().getName().equals("ControlPanelGUI")))){
                            fr.dispose();
                        }
                    }
                }
                dispose();
                //run Billboard Control Panel GUI
                String [] user_input = {"List billboards"};
                //request schedule and run calendar GUI
                ControlPanelClient.Run_Client(user_input);
            }
        } else if(buttonClicked == backgroundColourBttn)
        {
            Color originalColour = bb.getBillboardColour();
            Color colour = JColorChooser.showDialog(null,
                    "Select a color", bb.getBillboardColour());
            backgroundColourDisplay.setBackground(colour);
            try {
                bb.setBillboardColour(colour);
                backgroundColourDisplay.setBackground(colour);
            }catch (Exception f){
                bb.setBillboardColour(originalColour);
                backgroundColourDisplay.setBackground(originalColour);
            }
        } else if(buttonClicked == messageTextColourBttn)
        {
            Color originalColour = bb.getMessageColour();
            Color colour = JColorChooser.showDialog(null,
                    "Select a color", bb.getMessageColour());
            try {
                bb.setMessageColour(colour);
                messageTextColourDisplay.setBackground(colour);
            } catch (Exception f){
                bb.setMessageColour(originalColour);
                messageTextColourDisplay.setBackground(originalColour);
            }
        } else if(buttonClicked == extraTextColourBttn)
        {
            Color originalColour = bb.getInformationColour();
            Color colour = JColorChooser.showDialog(null,
                    "Select a color", bb.getInformationColour());
            extraTextColourDisplay.setBackground(colour);
            try {
                bb.setInformationColour(colour);
                extraTextColourDisplay.setBackground(colour);
            }catch (Exception f){
                bb.setInformationColour(originalColour);
                extraTextColourDisplay.setBackground(originalColour);
            }
        } else if(buttonClicked == importBttn)
        {
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

                    previewPanel.removeAll();
                    previewPanel.revalidate();
                    previewPanel.repaint();
                    try {
                        bb = new BillboardViewer(tempXMLString, d);
                    } catch (ParserConfigurationException | IOException | SAXException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(getContentPane(), ex,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    JPanel billboardPreview = bb.getSizedBillboard();
                    previewPanel.add(billboardPreview);
                    previewPanel.revalidate();
                    previewPanel.repaint();

                    messageField.setText(bb.getMessageText());
                    extraInfoText.setText(bb.getInformationText());
                    if(bb.getPictureURL() != null){
                        imageURL.setText(bb.getPictureURL());
                    }
                    else{
                        imageURL.setText("Encoded Image");
                    }
                    backgroundColourDisplay.setBackground(bb.getBillboardColour());
                    messageTextColourDisplay.setBackground(bb.getMessageColour());
                    extraTextColourDisplay.setBackground(bb.getInformationColour());
                    JOptionPane.showMessageDialog(getContentPane(),"Billboard Successfully Imported");


                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(),"File could not be found, please check the filepath and try again. ", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        } else if(buttonClicked == exportBttn)
        {
            if(!messageField.getText().equals(""))
            {
                bb.setMessageText(messageField.getText());
                bb.setMessageExists(true);
            }
            else{
                bb.setMessageExists(false);
            }

            if(!extraInfoText.getText().equals(""))
            {
                bb.setInformationText(extraInfoText.getText());
                bb.setInformationExists(true);
            }
            else{
                bb.setInformationExists(false);
            }

            if(!imageURL.getText().equals(""))
            {
                bb.setPictureExists(true);
                try{
                    URL urlString = new URL(imageURL.getText());
                    bb.setUrlExists(true);
                    bb.setDataExists(false);
                    bb.setPictureURL(imageURL.getText());
                } catch(MalformedURLException m){
                    bb.setDataExists(true);
                    bb.setUrlExists(false);
                    File f = new File(imageURL.getText());
                    JOptionPane.showMessageDialog(getContentPane(), m,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(getContentPane(),"Billboard Successfully Exported");
            } catch (ParserConfigurationException | TransformerException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), ex,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else if(buttonClicked == previewBttn)
        {
            if(!messageField.getText().equals(""))
            {
                bb.setMessageText(messageField.getText());
                bb.setMessageExists(true);
            }
            else{
                bb.setMessageExists(false);
            }

            if(!extraInfoText.getText().equals(""))
            {
                bb.setInformationText(extraInfoText.getText());
                bb.setInformationExists(true);
            }
            else{
                bb.setInformationExists(false);
            }

            if(!imageURL.getText().equals(""))
            {
                bb.setPictureExists(true);
                try{
                    URL urlString = new URL(imageURL.getText());
                    bb.setUrlExists(true);
                    bb.setDataExists(false);
                    bb.setPictureURL(imageURL.getText());
                } catch(MalformedURLException m){
                    bb.setDataExists(true);
                    bb.setUrlExists(false);
                    File f = new File(imageURL.getText());
                    try {
                        FileInputStream imageFile = new FileInputStream(f);
                        byte[] imageData = imageFile.readAllBytes();
                        bb.setPictureDataString(Base64.getEncoder().encodeToString(imageData));
                    } catch (IOException fileNotFoundException) {
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

            previewPanel.removeAll();
            previewPanel.revalidate();
            previewPanel.repaint();

            try {
                bb = new BillboardViewer(tempXMLString, d);
            } catch (ParserConfigurationException | IOException | SAXException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), ex,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            JPanel billboardPreview = bb.getSizedBillboard();
            previewPanel.add(billboardPreview);
            previewPanel.revalidate();
            previewPanel.repaint();
        }


    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new BBEditor("admin", "1234"));
    }
}
