import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.JOptionPane.*;

/**
 * Billboard Control Panel class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Billboard Control Panel
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Minor button functionality still needs to be added; further refinement required
 */
public class ControlPanelGUIBillboardControlPanel extends JFrame implements Runnable, ActionListener, WindowListener, DocumentListener {
    HashMap<String, Billboard> billboardListH;
    User currentUser;
    ScheduleMultiMap schedule;
    String username;
    String sessionToken;
    boolean closeable = true;

    /**
     * Method used to create a GUI window for the Billboard Control Panel Screen
     * @param username Used for setting the username
     * @param sessionToken The sessionToken associated with the user
     * @param BillboardList A list of billboards passed in
     */
    public ControlPanelGUIBillboardControlPanel(String username, String sessionToken, HashMap<String, Billboard> BillboardList, User currentUser, ScheduleMultiMap schedule) {
        // Set window title
        super("Billboard Control Panel");
        this.username = username;
        this.sessionToken = sessionToken;
        this.currentUser = currentUser;
        billboardListH = BillboardList;
        this.schedule = schedule;
    }

    // Clickable buttons and list
    private JButton backButton;
    private JButton logoutButton;
    private JTextField search;
    private JList billboardList;
    private JButton editBillboardButton;
    private JButton deleteBillboardButton;
    private JButton scheduleBillboardButton;
    private JButton createBillboardButton;
    private String billboardXML = null;
    private String billboardName;
    private String xmlFile;
    private JPanel mainPanel;
    private JPanel billboardPreview;
    private JPanel buttonPanel;
    private JPanel createBillboardPanel;
    private final Dimension DIMENSION = new Dimension(400,200);
    private BillboardViewer Billboard;
    // Xml string used when there was no selection made
    private final String NO_SELECTION_XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <message>Select a billboard to preview</message>\n" +
            "</billboard>";
    // Xml string used when the list of billboards in empty
    private final String EMPTY_LIST_XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <message>No billboards stored in the database</message>\n" +
            "</billboard>";
    // Default xml file to be used for creating a new xml file.
    public static final String XML_TEMPLATE = "<?xml version='1.0' encoding='UTF-8'?><billboard><picture url=" +
            "'https://cloudstor.aarnet.edu.au/plus/s/vYipYcT3VHa1uNt/download'/><information>Default text, delete or change." +
            "To use an image link it in the URL section."+
            "</information></billboard>";

    /**
     * Method used to create a GUI window for the Billboard Control Panel
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     * @throws IOException Exception handling
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, ParserConfigurationException, SAXException {
        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation set to Dispose on Close, so when user closes this screen, only this screen closes (keeps Control Panel GUI running)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create billboards JPanel
        JPanel billboardPanel = new JPanel();
        billboardPanel.setLayout(new BoxLayout(billboardPanel, BoxLayout.Y_AXIS));

        // Add billboard preview image, inside of a JPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalStrut(50));

        // If billboard list is empty
        if(billboardListH.size() == 0)
        {
            // Create new billboard preview with empty billboard list message
            Billboard = new BillboardViewer(EMPTY_LIST_XML_STRING, DIMENSION);
        }

        else
        {
            // Create new billboard preview with empty xml
            Billboard = new BillboardViewer(NO_SELECTION_XML_STRING, DIMENSION);
        }

        billboardPreview = Billboard.getSizedBillboard();
        mainPanel.add(billboardPreview);

        // If billboard list is NOT empty
        if(billboardListH.size() >0)
        {
            billboardPanel.add(Box.createVerticalStrut(50));

            // Create search JPanel, with X axis Box Layout
            JPanel searchPanel = new JPanel();

            //searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));

            JLabel label = new JLabel("Search Billboard");

            // Add JLabel to specified JPanel
            searchPanel.add(label);

            // Formatting
            searchPanel.add(Box.createHorizontalStrut(10));

            // Create search JTextField, inside search JPanel
            search = newTextField(searchPanel);

            billboardPanel.add(searchPanel);

            billboardPanel.add(Box.createVerticalStrut(20));

            //Create billboard JList, and add it to billboard JPanel
            billboardList = createJList(billboardPanel);
        }

        // Create button JPanel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        // Create and add Edit Billboard button, inside button JPanel
        editBillboardButton = createButton("Edit Billboard");
        buttonPanel.add(editBillboardButton);

        // Create and add Schedule Billboard button, inside button JPanel
        scheduleBillboardButton = createButton("Schedule Billboard");
        buttonPanel.add(scheduleBillboardButton);
        mainPanel.add(buttonPanel); // Add button JPanel to billboard preview JPanel

        // Create create billboard JPanel
        createBillboardPanel = new JPanel();
        createBillboardPanel.setLayout(new BoxLayout(createBillboardPanel, BoxLayout.X_AXIS));

        // Create and add Create Billboard button, inside billboard create billboard JPanel; and add formatting
        createBillboardButton = createButton("Create Billboard");
        createBillboardPanel.add(Box.createVerticalStrut(30));
        createBillboardPanel.add(createBillboardButton);
        createBillboardPanel.add(Box.createVerticalStrut(100));

        // Create and add Create Delete button
        deleteBillboardButton = createButton("Delete Billboard");
        createBillboardPanel.add(Box.createVerticalStrut(30));
        createBillboardPanel.add(deleteBillboardButton);
        createBillboardPanel.add(Box.createVerticalStrut(100));

        // Add create billboard JPanel to preview panel JPanel
        mainPanel.add(createBillboardPanel);

        // Create bottom JPanel
        JPanel bottomPanel = new JPanel();
        // Set layout for bottom JPanel
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        // Add Strut to bottom panel for formatting
        bottomPanel.add(Box.createHorizontalStrut(100));
        // Add billboard JPanel to bottom JPanel
        bottomPanel.add(billboardPanel);
        // Add Strut to bottom panel for formatting
        bottomPanel.add(Box.createHorizontalStrut(40));
        // Add main JPanel to bottom JPanel
        bottomPanel.add(mainPanel);
        // Add Strut to bottom panel for formatting
        bottomPanel.add(Box.createHorizontalStrut(100));


        // Create top JPanel
        JPanel topPanel = new JPanel();
        // Set layout for bottom JPanel
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        // Add strut for formatting
        topPanel.add(Box.createHorizontalStrut(20));
        // Create back JButton
        backButton = createButton("Back");
        // Add back button to top JPanel
        topPanel.add(backButton);
        // Add horizontal glue in between buttons for spacing
        topPanel.add(Box.createHorizontalGlue());
        // Create logout JButton
        logoutButton = createButton("Logout");
        // Add logout button to top JPanel
        topPanel.add(logoutButton);
        // Add strut for formatting
        topPanel.add(Box.createHorizontalStrut(20));


        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().add(Box.createVerticalStrut(20)); // formatting
        getContentPane().add(topPanel);
        //getContentPane().add(Box.createHorizontalStrut(100));
        getContentPane().add(bottomPanel);
        //getContentPane().add(Box.createHorizontalStrut(40));
        //getContentPane().add(mainPanel);
        //getContentPane().add(Box.createHorizontalStrut(100));

        // Add Window Listener, used for when window is closed
        addWindowListener(this);

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    private JTextField newTextField(JPanel panel) {
        // Create new JTextField
        JTextField textField = new JTextField(10);

        // Add document listener for text field search
        textField.getDocument().addDocumentListener(this);

        // Add the JTextField to the specified JPanel
        panel.add(textField);

        // Returns a JTextField
        return textField;
    }

    /**
     * This method creates a JList, returns a JList
     * @return Returns JList
     */
    private JList createJList(JPanel panel) {
        // Int counter for assigning values in the array
        int counter = 0;

        // Setup an array to return creator and billboard
        String[] billboardListWithCreator = new String[billboardListH.size()+1];

        // Get the Creator of each billboard in the billboard list
        for(Map.Entry<String, Billboard> billboardEntry : billboardListH.entrySet()){
            //Assign creator name to position one in the 2D array
            billboardListWithCreator[counter] = billboardEntry.getValue().BillboardCreator;
            //Assign billboard name to the second position in the 2D array
            billboardListWithCreator[counter] = billboardEntry.getValue().BillboardName + ", "
                    + billboardListWithCreator[counter];
            counter++;
        }

        // Create new JPanel for spacing and formatting
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        // Create new JList
        JList list = new JList(billboardListWithCreator);

        // Create JScrollPane
        JScrollPane scroll = new JScrollPane(list);

        // Add JScrollPane to the specified JPanel
        panel2.add(scroll);

        // Formatting
        panel2.add(Box.createVerticalStrut(50));

        // Add list selection listener, so billboard preview changes when different billboard is clicked
        list.addListSelectionListener(this::valueChanged);

        // Add JPanel 2, to JPanel
        panel.add(panel2);

        // Return JPanel
        return list;
    }


    /**
     * This method creates a is used to evaluate which billboards is being clicked on in the list
     * @return Returns JList
     */
    // Changes billboard XML when a user selects a billboard from the list
    private void valueChanged(ListSelectionEvent event){
        //get string stored in current cell of list
        try {
            String cellSelected = billboardList.getSelectedValue().toString();

            //remove creator from list
            String billboardSelected = cellSelected.replaceAll(",.*", "");

            billboardName = billboardSelected;

            Billboard billboardObjectSelected = billboardListH.get(billboardSelected);

            //get billboard xml file name
            billboardXML = billboardObjectSelected.XMLFile;

            //remove all elements of GUI screen
            mainPanel.removeAll();
            mainPanel.revalidate();
            mainPanel.repaint();

            try {
                Billboard = new BillboardViewer(billboardXML, DIMENSION);
            } catch (ParserConfigurationException | IOException | SAXException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), ex,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(Box.createVerticalStrut(50));
            billboardPreview = Billboard.getSizedBillboard();

            //add control panel components back to panel
            mainPanel.add(billboardPreview);
            mainPanel.add(buttonPanel);
            mainPanel.add(createBillboardPanel);


            System.out.println("bb xml: " + billboardXML);
            System.out.println("bb name: " + billboardName);
        }
        catch (Exception e){
            // don't do anything
        }
    }


    /**
     * This method creates a JButton, when given a display name for the button, returns a JButton
     * @param buttonName Name of button (which is displayed in GUI)
     * @return Returns JButton
     */
    private JButton createButton(String buttonName) {
        // Create JButton
        JButton button = new JButton(buttonName);

        // Create action listener for JButton
        button.addActionListener(this);

        // Return JButton
        return button;
    }


    /**
     * This method listens for an action to be performed
     * @return Returns JList
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the back button has been clicked
        if (buttonClicked == backButton) {
            // Closes current GUI screen
            dispose();
            closeable = false;
            // Open new Control Panel GUI screen
            SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
        }

        // Checks if the logout button has been clicked
        else if (buttonClicked == logoutButton) {
            // Closes current GUI screen
            dispose();
            closeable = false;
            // Open new Login screen
            SwingUtilities.invokeLater(new ControlPanelGUILoginScreen());
        }

        // Checks if the edit billboard button has been clicked
        else if (buttonClicked == editBillboardButton) {
            //if billboard has not been selected in list
            if (billboardName == null) {
                //display error pop up\
                JOptionPane.showMessageDialog(this,
                        "You must select a billboard in the list to edit");
            }
            //if billboard has been selected
            else {
                //get string stored in current cell of list
                String cellSelected = billboardList.getSelectedValue().toString();

                //remove creator from list
                String billboardSelected = cellSelected.replaceAll(",.*", "");
                billboardName = billboardSelected;
                Billboard billboardObjectSelected = billboardListH.get(billboardSelected);
                //get billboard creator, and current username
                String billboardCreator = billboardObjectSelected.BillboardCreator;
                String currentUsername = currentUser.Username;
                boolean creatorCheck = billboardCreator.equals(currentUsername);
                boolean scheduleCheck;
                try {
                    schedule.getSchedule(billboardName);
                    scheduleCheck = true;
                }
                catch (Exception e) {
                    scheduleCheck = false;
                }
                if(((!creatorCheck) && currentUser.Permissions.contains("Edit All Billboards")) || (creatorCheck && (currentUser.Permissions.contains("Create Billboards") && !scheduleCheck))){
                    //Retrieve the xml file associated with the name
                    try {
                        xmlFile = billboardXML;
                        closeable = false;

                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(getContentPane(), e,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.println("xmlFile: " + xmlFile);
                    //run Billboard editor/creator GUI

                    // check that editor isn't already open
                    int frameCount = 0;
                    Frame[] allFrames = Frame.getFrames();
                    for(Frame fr : allFrames){
                        if((fr.getClass().getName().equals("BBEditor"))){
                            if(fr.isVisible()){
                                frameCount += 1;
                            }
                        }
                    }

                    if(frameCount > 0) {
                        int a = showConfirmDialog(null, "This will close your current billboard editor screen and you will lose any changes");
                        if (a == YES_OPTION) {
                            allFrames = Frame.getFrames();
                            for (Frame fr : allFrames) {
                                if ((fr.getClass().getName().equals("BBEditor"))) {
                                    fr.dispose();
                                }
                            }
                            SwingUtilities.invokeLater(new BBEditor(username, sessionToken, billboardName, xmlFile));
                        }
                    }
                    else{
                        SwingUtilities.invokeLater(new BBEditor(username, sessionToken, billboardName, xmlFile));
                    }
                }
                else{
                    //display error pop up\
                    JOptionPane.showMessageDialog(this,
                            "You do not have permission to edit this billboards");
                }
            }
        }

        // Checks if the create billboard button has been clicked
        else if (buttonClicked == createBillboardButton) {
            //Open the editor with a new file
            if (currentUser.Permissions.contains("Create Billboards")) {
                try {
                    xmlFile = XML_TEMPLATE;
                    closeable = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(getContentPane(), e,
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                System.out.println("xmlFile: " + xmlFile);

                // check that editor isn't already open
                int frameCount = 0;
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("BBEditor"))){
                        if(fr.isVisible()){
                            frameCount += 1;
                        }
                    }
                }

                if(frameCount > 0) {
                    int a = showConfirmDialog(null, "This will close your current billboard editor screen and you will lose any changes");
                    if (a == YES_OPTION) {
                        allFrames = Frame.getFrames();
                        for (Frame fr : allFrames) {
                            if ((fr.getClass().getName().equals("BBEditor"))) {
                                fr.dispose();
                            }
                        }
                        SwingUtilities.invokeLater(new BBEditor(username, sessionToken, billboardListH));
                    }
                }
                else{
                    SwingUtilities.invokeLater(new BBEditor(username, sessionToken, billboardListH));
                }
            }
            else{
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You do not permission to create a billboard");
            }
        }

        // Checks if the delete billboard button has been clicked
        else if (buttonClicked == deleteBillboardButton) {
            if(billboardName == null)
            {
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a billboard in the list to delete");
            }
            else{
                //get string stored in current cell of list
                String cellSelected = billboardList.getSelectedValue().toString();

                //remove creator from list
                String billboardSelected = cellSelected.replaceAll(",.*", "");
                billboardName = billboardSelected;
                Billboard billboardObjectSelected = billboardListH.get(billboardSelected);
                //get billboard creator, and current username
                String billboardCreator = billboardObjectSelected.BillboardCreator;
                String currentUsername = currentUser.Username;
                boolean creatorCheck = billboardCreator.equals(currentUsername);
                boolean scheduleCheck;
                try {
                    schedule.getSchedule(billboardName);
                    scheduleCheck = true;
                }
                catch (Exception e) {
                    scheduleCheck = false;
                }
                if(((!creatorCheck) && currentUser.Permissions.contains("Edit All Billboards")) || (creatorCheck && (currentUser.Permissions.contains("Create Billboards") && !scheduleCheck))) {
                    //Open the editor with a new file
                    try {
                        //xmlFile = billboard_list.GetBillboardInfo(billboardXML).XMLFile;
                        int a = showConfirmDialog(null, "Are you sure you want to delete this billboard?");
                        if (a == YES_OPTION) {
                            //adjust user inputs to delete given billboard
                            String[] user_inputs = {"Delete billboard", billboardName};
                            //Delete billboard with viewing details given by user
                            ControlPanelClient.Run_Client(user_inputs);
                            //Refresh control panel
                            user_inputs = new String[]{"List billboards"};
                            ControlPanelClient.Run_Client(user_inputs);
                            closeable = false;
                            dispose();
                            //JOptionPane.showMessageDialog(this,
                            //        "Billboard Successfully Deleted");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(getContentPane(), e,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                    System.out.println("xmlFile: " + xmlFile);
                }
                else{
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You do not have permission to delete this billboards");
                }
            }
        }

        // Checks if the schedule billboard button has been clicked
        else if (buttonClicked == scheduleBillboardButton) {
            //if schedule pop up is not already open


            //if billboard has not been selected in list
            if(billboardName == null)
            {
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a billboard in the list to schedule");
            }
            //if billboard has been selected
            else
            {
                if (currentUser.Permissions.contains("Schedule Billboards")) {
                    int frameCount = 0;
                    Frame[] allFrames = Frame.getFrames();
                    for(Frame fr : allFrames){
                        if((fr.getClass().getName().equals("BBSchedulePopup"))){
                            if(fr.isVisible()){
                                frameCount += 1;
                            }
                        }
                    }

                    if(frameCount > 0) {
                        int a = showConfirmDialog(null, "This will close your current scheduling screen and you will lose any changes");
                        if (a == YES_OPTION) {
                            allFrames = Frame.getFrames();
                            for (Frame fr : allFrames) {
                                if ((fr.getClass().getName().equals("BBSchedulePopup"))) {
                                    fr.dispose();
                                }
                            }
                            SwingUtilities.invokeLater(new BBSchedulePopup(username, sessionToken, billboardName));
                        }
                    }
                    else{
                        SwingUtilities.invokeLater(new BBSchedulePopup(username, sessionToken, billboardName));
                    }

                }
                else{
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You do not have permission to schedule billboards");
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // When this window is being closed, a new Control Panel GUI is opened (simulates going back to previous screen)
        if(closeable) {
            SwingUtilities.invokeLater(new ControlPanelGUI(username, sessionToken));
        }
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

    @Override
    public void insertUpdate(DocumentEvent e) {

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
