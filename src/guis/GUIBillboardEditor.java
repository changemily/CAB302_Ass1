package guis;

import billboard.Billboard;
import billboard.BillboardViewer;
import network.ControlPanelClient;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import static javax.swing.JOptionPane.*;
/**
 * Create/Edit User class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Billboard Editor Screen
 * @author - Liam Dines (GUI), Harry Estreich (buttons, preview screen methods)
 * @version - Final
 */

public class GUIBillboardEditor extends JFrame implements Runnable, ActionListener, ChangeListener, WindowListener
{
    // Main global variables
    private String billboardName;
    private String tempXMLString;
    private final String username;
    private final String sessionToken;
    private HashMap<String, Billboard> billboardList;
    private final boolean createBillboard; // true if billboard is being created
    private StreamResult o;
    private Dimension d;

    // Java Swing Components
    // Billboard Viewer class
    private BillboardViewer bb = null;

    // Billboard panel
    private JPanel previewPanel;

    // Text fields
    private JTextField imageURL;
    private JTextField messageField;
    private JTextField nameField;

    // Text areas
    private JTextArea informationText;
    private JTextArea backgroundColourDisplay;
    private JTextArea messageTextColourDisplay;
    private JTextArea informationColourDisplay;

    // Buttons
    private JButton imageBrowseButton;
    private JButton exitButton;
    private JButton backgroundColourButton;
    private JButton messageTextColourButton;
    private JButton informationTextColourButton;
    private JButton importButton;
    private JButton exportButton;
    private JButton previewButton;
    private JButton saveButton;


    /**
     * Constructor for editing a billboard
     * @param username username of creator
     * @param sessionToken session token for user
     * @param billboardName name of billboard edited
     * @param XMLString billboard xml string
     */
    public GUIBillboardEditor(String username, String sessionToken, String billboardName, String XMLString)
    {
        // Set window title
        super("Billboard Editor");

        // Set variables
        this.billboardName = billboardName;
        tempXMLString = XMLString;
        createBillboard = false;
        this.username = username;
        this.sessionToken = sessionToken;
    }

    /**
     * Constructor for creating a billboard
     * @param username username of creator
     * @param sessionToken session token for user
     * @param billboardList list of billboards, for checking name
     */
    public GUIBillboardEditor(String username, String sessionToken, HashMap<String, Billboard> billboardList){
        super("Billboard Creator");

        // Set variables
        this.billboardName = null;
        tempXMLString = "<billboard></billboard>"; // empty billboard
        createBillboard = true;
        this.username = username;
        this.sessionToken = sessionToken;
        this.billboardList = billboardList;
    }

    /**
     * Method for creating a GUI, this creates the base GUI with initial values
     * @throws ClassNotFoundException class not found error
     * @throws UnsupportedLookAndFeelException look and feel error
     * @throws InstantiationException instantiation error
     * @throws IllegalAccessException illegal access error
     * @throws IOException io error
     * @throws SAXException SAX error
     * @throws ParserConfigurationException configuration error
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, SAXException, ParserConfigurationException {
        // Set default look and feel & window properties
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //make window non resizable
        setResizable(false);

        // Create mainPanel
        JPanel mainPanel = new JPanel();

        // Create Billboard Preview Panel
        previewPanel = new JPanel();

        // Initialise and add Billboard to Preview Panel
        d = new Dimension(600, 350);
        bb = new BillboardViewer(tempXMLString, d);
        JPanel billboardPreview = bb.getSizedBillboard();
        previewPanel.add(billboardPreview);

        // Create dividerLine
        JSeparator dividerLine = new JSeparator();

        // Create previewLabel
        JLabel previewLabel = createLabel("Preview");
        previewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        previewLabel.setFont(previewLabel.getFont().deriveFont(previewLabel.getFont().getStyle() | Font.BOLD));

        // Create editLabel
        JLabel editLabel = createLabel("Edit");
        editLabel.setHorizontalAlignment(SwingConstants.CENTER);
        editLabel.setFont(editLabel.getFont().deriveFont(editLabel.getFont().getStyle() | Font.BOLD));

        // Create messageLabel
        JLabel messageLabel = createLabel("Message Text");

        // Create imageLabel
        JLabel imageLabel = createLabel("Image File");

        // Create backgroundColourPickerLabel
        JLabel backgroundColourPickerLabel = createLabel("Background Colour");

        // Create messageColourPickerLabel
        JLabel messageColourPickerLabel = createLabel("Message Text Colour");

        // Create informationColourPickerLabel
        JLabel informationColourPickerLabel = createLabel("Information Text Colour");

        //Create informationLabel
        JLabel informationLabel = createLabel("Information Text");

        // Create nameLabel
        JLabel nameLabel = createLabel("Billboard Name");
        nameLabel.setVisible(createBillboard);

        // Create messageField
        messageField = createTextField();

        // Create imageURL
        imageURL = createTextField();
        imageURL.setMaximumSize(new Dimension(230, 10));

        // Create nameField
        nameField = createTextField();
        nameField.setVisible(createBillboard);

        // Create informationScrollPanel and informationText
        informationText = new JTextArea();
        JScrollPane informationScrollPanel;
        {
            informationScrollPanel = new JScrollPane();
            informationText.setBackground(Color.white);
            informationText.setForeground(Color.black);
            informationScrollPanel.setViewportView(informationText);
        }

        // Initialise field values
        messageField.setText(bb.getMessageText());
        informationText.setText(bb.getInformationText());
        if(bb.getPictureURL() != null) {
            imageURL.setText(bb.getPictureURL());
        }
        else if(bb.getPictureDataString() != null){
            imageURL.setText("Encoded Image");
        }

        // Create BackgroundColourDisplay
        backgroundColourDisplay = createColourDisplay();
        backgroundColourDisplay.setBackground(bb.getBillboardColour());

        // Create MessageTextColourDisplay
        messageTextColourDisplay = createColourDisplay();
        messageTextColourDisplay.setBackground(bb.getMessageColour());

        // Create informationTextColourDisplay
        informationColourDisplay = createColourDisplay();
        informationColourDisplay.setBackground(bb.getInformationColour());

        // Create imageBrowseButton
        imageBrowseButton = createButton("Browse");

        // Create SaveButton
        saveButton = createButton("Save");

        // Create ExitButton
        exitButton = createButton("Exit");

        // Create BackgroundColourButton
        backgroundColourButton = createButton("Browse");

        //Create MessageTextColourButton
        messageTextColourButton = createButton("Browse");

        //Create informationTextColourButton
        informationTextColourButton = createButton("Browse");

        // Create importButton
        importButton = createButton("Import");

        // Create exportButton
        String outPath = "./" + billboardName + ".xml";
        o = new StreamResult(outPath);
        exportButton = createButton("Export");

        // Create previewButton
        previewButton = createButton("Preview");

        // Set Layout for EVERYTHING
        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
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
                                                                .addComponent(previewButton, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(importButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(exportButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(saveButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                                                .addComponent(exitButton))
                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                                .addComponent(messageLabel)
                                                                                .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                                                        .addComponent(imageURL)
                                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(imageBrowseButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(messageField, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(imageLabel))
                                                                        .addComponent(nameLabel)
                                                                        .addComponent(nameField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                        .addComponent(informationLabel)
                                                                        .addComponent(backgroundColourPickerLabel)
                                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                                        .addComponent(backgroundColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(informationColourDisplay, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                                                .addComponent(backgroundColourButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addGroup(mainPanelLayout.createParallelGroup()
                                                                                                        .addComponent(messageColourPickerLabel)
                                                                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                                                                .addComponent(messageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                .addComponent(messageTextColourButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))))
                                                                                        .addComponent(informationTextColourButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)))
                                                                        .addComponent(informationColourPickerLabel)
                                                                        .addComponent(informationScrollPanel, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE))))
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
                                                .addComponent(informationLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(informationScrollPanel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(nameLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(imageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(imageBrowseButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
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
                                                        .addComponent(backgroundColourButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(messageTextColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(messageTextColourButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(messageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)))
                                .addGroup(mainPanelLayout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                .addComponent(informationColourPickerLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(informationTextColourButton, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(informationColourDisplay, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                                .addGap(66, 66, 66))
                                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(previewButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(importButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(exportButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                                .addGap(20, 20, 20))))
        );

        getContentPane().add(mainPanel);

        addWindowListener(this);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * create basic JLabel
     * @param labelText text
     * @return JLabel
     */
    private JLabel createLabel(String labelText)
    {
        JLabel label = new JLabel();
        label.setText(labelText);
        label.setBackground(Color.white);
        label.setForeground(Color.black);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    /**
     * create basic text field
     * @return JTextField
     */
    private JTextField createTextField()
    {
        JTextField text = new JTextField();
        text.setBackground(Color.white);
        text.setForeground(Color.black);
        return text;
    }

    /**
     * create basic colour display
     * @return JTextArea
     */
    private JTextArea createColourDisplay()
    {
        JTextArea colourDisplay = new JTextArea();
        colourDisplay.setEditable(false);
        return colourDisplay;
    }

    /**
     * create basic button
     * @param buttonName string of button name
     * @return JButton
     */
    private JButton createButton(String buttonName)
    {
        JButton button = new JButton();
        button.setText(buttonName);
        button.setBackground(new Color(230, 230, 230));
        button.setForeground(Color.black);
        button.addActionListener(this);
        return button;
    }

    /**
     * Run GUI and catch errors, none expected
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void run()
    {
        try
        {
            createGUI();
        } catch (ClassNotFoundException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (SAXException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (ParserConfigurationException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e)
        {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void stateChanged(ChangeEvent c)
    {

    }

    /**
     * Click on button
     * @param e action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object buttonClicked = e.getSource();

        // Browse for image and load into billboard
        if(buttonClicked == imageBrowseButton)
        {
            JFileChooser FileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = FileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) { // found file
                File selectedFile = FileChooser.getSelectedFile();
                imageURL.setText(selectedFile.getAbsolutePath()); // set url to file
            }

        }
        // Exit from viewer, checks if user wants to save changes
        else if (buttonClicked == exitButton)
        {
            int a = showConfirmDialog(null, "Would you like to save your billboard to the database?");
            if(a == YES_OPTION)
            {
                //save billboard to database
                boolean Break = saveBillboardToDB();
                //closes editor and reloads billboard control panel
                if(!Break) {
                    refreshFrames();
                }
            }
            else if(a == NO_OPTION)
            {
                // Check if billboard control panels
                int frameCount = 0;
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("guis.GUIBillboardControlPanel"))){
                        if(fr.isVisible()){
                            frameCount += 1;
                        }
                    }
                }
                dispose();
                // if not, add one
                if(frameCount == 0){
                    //run Billboard Control Panel GUI
                    String [] user_input = {"List billboards", ControlPanelClient.sessionToken};
                    //request billboard list and run calendar GUI
                    ControlPanelClient.runClient(user_input);
                }
            }
        }
        // Save button clicked, save billboard to db, and refresh control panel
        else if(buttonClicked == saveButton){
            //save billboard to database
            boolean Break = saveBillboardToDB();
            //closes editor and reloads billboard control panel
            if(!Break) {
                refreshFrames();
            }
        // Changed background colour
        }else if(buttonClicked == backgroundColourButton){
            Color originalColour = bb.getBillboardColour();
            Color colour = JColorChooser.showDialog(null,
                    "Select a color", bb.getBillboardColour());
            backgroundColourDisplay.setBackground(colour);
            // set billboard colour to picked colour
            try {
                bb.setBillboardColour(colour);
                backgroundColourDisplay.setBackground(colour);
            }catch (Exception f){
                bb.setBillboardColour(originalColour);
                backgroundColourDisplay.setBackground(originalColour);
            }
        // Changed message colour
        }else if(buttonClicked == messageTextColourButton)
        {
            Color originalColour = bb.getMessageColour();
            Color colour = JColorChooser.showDialog(null,
                    "Select a color", bb.getMessageColour());
            // set message colour to picked colour
            try {
                bb.setMessageColour(colour);
                messageTextColourDisplay.setBackground(colour);
            } catch (Exception f){
                bb.setMessageColour(originalColour);
                messageTextColourDisplay.setBackground(originalColour);
            }
        // Changed informationText colour
        } else if(buttonClicked == informationTextColourButton)
        {
            Color originalColour = bb.getInformationColour();
            Color colour = JColorChooser.showDialog(null,
                    "Select a color", bb.getInformationColour());
            informationColourDisplay.setBackground(colour);
            // set information text colour
            try {
                bb.setInformationColour(colour);
                informationColourDisplay.setBackground(colour);
            }catch (Exception f){
                bb.setInformationColour(originalColour);
                informationColourDisplay.setBackground(originalColour);
            }
        // Import xml from file
        } else if(buttonClicked == importButton)
        {
            JFileChooser FileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int returnValue = FileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) { // file found
                File selectedFile = FileChooser.getSelectedFile();
                File f = new File(selectedFile.getAbsolutePath());
                Reader fileReader; // read file
                try {
                    fileReader = new FileReader(f);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuilder output = new StringBuilder();
                    String stringXML;
                    // append file to one string
                    while((stringXML = bufferedReader.readLine()) != null){
                        output.append(stringXML);
                    }
                    // read string
                    tempXMLString = output.toString();
                    tempXMLString = tempXMLString.replace("\"","'");
                    bufferedReader.close();

                    // remove panel
                    previewPanel.removeAll();
                    previewPanel.revalidate();
                    previewPanel.repaint();
                    // new viewer
                    try {
                        bb = new BillboardViewer(tempXMLString, d);
                    } catch (ParserConfigurationException | IOException | SAXException ex) {
                        // Show a pop up with the error that was caught
                        JOptionPane.showMessageDialog(getContentPane(), ex,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    // add new billboard to panel
                    JPanel billboardPreview = bb.getSizedBillboard();
                    previewPanel.add(billboardPreview);
                    previewPanel.revalidate();
                    previewPanel.repaint();

                    // reset initial values for texts and colours
                    messageField.setText(bb.getMessageText());
                    informationText.setText(bb.getInformationText());
                    if(bb.getPictureURL() != null){
                        imageURL.setText(bb.getPictureURL());
                    }
                    else if(bb.getPictureDataString() != null){ // data string image
                        imageURL.setText("Encoded Image");
                    }
                    backgroundColourDisplay.setBackground(bb.getBillboardColour());
                    messageTextColourDisplay.setBackground(bb.getMessageColour());
                    informationColourDisplay.setBackground(bb.getInformationColour());
                    // successful import
                    JOptionPane.showMessageDialog(getContentPane(),"Billboard Successfully Imported");
                } catch (IOException fileNotFoundException) {
                    // Show a pop up with the error that was caught
                    JOptionPane.showMessageDialog(getContentPane(),"File could not be found, please check the filepath and try again. ", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        // export xml
        } else if(buttonClicked == exportButton)
        {
            boolean Break = updateBillboard(false);

            // write file to current folder
            if(!Break) {
                // create new billboard
                try {
                    bb = new BillboardViewer(tempXMLString, d);
                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    // Show a pop up with the error that was caught
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    bb.writeFile(o);
                    JOptionPane.showMessageDialog(getContentPane(), "Billboard Successfully Exported");
                } catch (ParserConfigurationException | TransformerException ex) {
                    // Show a pop up with the error that was caught
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        // preview button clicked
        } else if(buttonClicked == previewButton) {
            // gather new values
            boolean Break = updateBillboard(false);

            if (!Break) {
                // update xml string of billboard
                try {
                    tempXMLString = bb.updateXMLString();
                } catch (ParserConfigurationException | TransformerException ex) {
                    // Show a pop up with the error that was caught
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                // remove panel
                previewPanel.removeAll();
                previewPanel.revalidate();
                previewPanel.repaint();

                // create new billboard
                try {
                    bb = new BillboardViewer(tempXMLString, d);
                } catch (ParserConfigurationException | IOException | SAXException ex) {
                    // Show a pop up with the error that was caught
                    JOptionPane.showMessageDialog(getContentPane(), ex,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }

                // display new billboard
                JPanel billboardPreview = bb.getSizedBillboard();
                previewPanel.add(billboardPreview);
                previewPanel.revalidate();
                previewPanel.repaint();
            }
        }
    }

    /**
     * Method for closing editor and refreshing billboard control panel
     */
    private void refreshFrames() {
        // Close all current billboard control panels
        Frame[] allFrames = Frame.getFrames();
        for(Frame fr : allFrames){
            if((fr.getClass().getName().equals("guis.GUIBillboardControlPanel"))){
                fr.dispose();
            }
        }
        dispose(); // dispose editor
        //run Billboard Control Panel GUI, creating new GUI
        String [] user_input = {"List billboards", sessionToken};
        ControlPanelClient.runClient(user_input);
    }

    /**
     * Method for saving billboard to database
     * @return boolean, true if fails
     */
    private boolean saveBillboardToDB() {
        boolean Break = false; // set true if fails
        // Check for errors in naming
        if (createBillboard) {
            billboardName = nameField.getText();
            System.out.println("billboardNameInput: " + billboardName);
            //if name has NOT been inputted in text field
            if (nameField.getText().contains("'") || nameField.getText().contains("\"")) {
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "Billboard contains invalid character");
                Break = true;
            }
            if (billboardName.equals("") || billboardName.equals(" ")) { // no name
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a name for the billboard");
                Break = true;
            }
            if (billboardList.containsKey(billboardName)) { // not a new name
                JOptionPane.showMessageDialog(this,
                        "This billboard name is already in use");
                Break = true;
            }
        } else {
            System.out.println(billboardName);
        }

        // Update variables
        Break = updateBillboard(Break);

        if (!Break) {
            try {
                // update xml string
                tempXMLString = bb.updateXMLString();
                JOptionPane.showMessageDialog(getContentPane(), "Billboard Successfully Saved to Database");
            } catch (ParserConfigurationException | TransformerException ex) {
                // Show a pop up with the error that was caught
                JOptionPane.showMessageDialog(getContentPane(), ex,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

        // If not broken add billboard to database
        if (!Break) {
            String[] user_inputs = {"Create edit billboard", billboardName, username, tempXMLString};

            //Schedule billboard with viewing details given by user
            ControlPanelClient.runClient(user_inputs);
        }
        return Break;
    }

    /**
     * update billboard details
     * @param currentBreak set true if fails
     * @return break at end of method
     */
    private boolean updateBillboard(boolean currentBreak){
        boolean Break = currentBreak; // set true if fails
        if (!messageField.getText().equals("")) {
            // set message
            bb.setMessageText(messageField.getText());
            bb.setMessageExists(true);
        } else {
            // no message
            bb.setMessageExists(false);
        }
        if (!informationText.getText().equals("")) {
            // set information
            bb.setInformationText(informationText.getText());
            bb.setInformationExists(true);
        } else {
            // no information
            bb.setInformationExists(false);
        }
        if (!imageURL.getText().equals("")) {
            bb.setPictureExists(true);
            try {
                // check if url exists
                new URL(imageURL.getText());
                bb.setUrlExists(true);
                bb.setDataExists(false);
                bb.setPictureURL(imageURL.getText());
            } catch (MalformedURLException m) {
                // fails
                try {
                    // if image not already encoded, try to find local file
                    if (!imageURL.getText().equals("Encoded Image")) {
                        File f = new File(imageURL.getText());
                        FileInputStream imageFile = new FileInputStream(f);
                        byte[] imageData = imageFile.readAllBytes(); // read bytes
                        bb.setPictureDataString(Base64.getEncoder().encodeToString(imageData)); // set string
                        bb.setDataExists(true);
                        bb.setUrlExists(false);
                    }
                } catch (IOException fileNotFoundException) {
                    // if fails no file and error
                    bb.setPictureExists(false);
                    bb.setDataExists(false);
                    bb.setUrlExists(false);
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "Can't find image file");
                    Break = true;
                }
            }
        } else {
            // no image
            bb.setPictureExists(false);
            bb.setDataExists(false);
            bb.setUrlExists(false);
        }

        // If all empty, then error as billboard is empty
        if (!bb.getMessageExists() && !bb.getPictureExists() && !bb.getInformationExists()) {
            JOptionPane.showMessageDialog(this,
                    "This billboard is empty");
            Break = true;
        }
        return Break;
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * window closing event
     * @param e closing window
     */
    @Override
    public void windowClosing(WindowEvent e) {
        int a = showConfirmDialog(null, "Would you like to save changes to the database?");
        if(a == YES_OPTION) // save and refresh
        {
            //save billboard to database
            boolean Break = saveBillboardToDB();
            //closes editor and reloads billboard control panel
            if(!Break) {
                refreshFrames();
            }
        }
        else if(a == NO_OPTION) // no save
        {
            // check if control panel is still up
            int frameCount = 0;
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("guis.GUIBillboardControlPanel"))){
                    if(fr.isVisible()){
                        frameCount += 1;
                    }
                }
            }
            dispose(); // close editor
            // if no control panels update control panel
            if(frameCount == 0){
                //run Billboard Control Panel GUI
                String [] user_input = {"List billboards", sessionToken};
                //request billboard list and run calendar GUI
                ControlPanelClient.runClient(user_input);
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
