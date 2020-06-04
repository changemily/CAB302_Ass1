package guis;

import billboard.Billboard;
import billboard.BillboardViewer;
import network.ControlPanelClient;
import org.xml.sax.SAXException;
import schedule.ScheduleMultiMap;
import users.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 * @author - Nickhil Nischal (GUI, Buttons), Harry Estreich (Buttons, Permissions)
 * @version - Final
 */
public class GUIBillboardControlPanel extends JFrame implements Runnable, ActionListener, WindowListener, ListSelectionListener, DocumentListener {
    // Billboard HashMap
    final HashMap<String, Billboard> billboardListH;

    // Current user
    final User currentUser;

    // Schedule MultiMap
    final ScheduleMultiMap schedule;

    // User's username
    final String username;

    // User's sessions token
    final String sessionToken;

    // Boolean to specify whether closeable or not
    boolean closeable = true;

    // Currently open billboard
    String openBillboard;
    String openSchedule;

    /**
     * Method used to create a GUI window for the Billboard Control Panel Screen
     * @param username Used for setting the username
     * @param sessionToken The sessionToken associated with the user
     * @param BillboardList A list of billboards passed in
     * @param currentUser Current user
     * @param schedule Schedule MultiMap
     */
    public GUIBillboardControlPanel(String username, String sessionToken, HashMap<String, billboard.Billboard> BillboardList, User currentUser, ScheduleMultiMap schedule) {
        // Set window title
        super("Billboard Control Panel");

        // The user's username
        this.username = username;

        // The user's session token
        this.sessionToken = sessionToken;

        // Current user
        this.currentUser = currentUser;

        // Billboard list HashMap
        billboardListH = BillboardList;

        // Schedule MultiMap
        this.schedule = schedule;
    }

    // Clickable buttons and list
    private JButton backButton;
    private JButton logoutButton;
    private JButton editBillboardButton;
    private JButton deleteBillboardButton;
    private JButton scheduleBillboardButton;
    private JButton createBillboardButton;

    // Interactive JTextField for search
    private JTextField search;

    // Interactive JList for displaying billboards
    private JList<String> billboardList;

    // String elements
    private String billboardXML = null;
    private String billboardName;

    // JPanels used throughout GUI
    private JPanel mainPanel;
    private JPanel billboardPreview;
    private JPanel buttonPanel;
    private JPanel createBillboardPanel;

    // Set dimension size for billboard preview
    private final Dimension DIMENSION = new Dimension(400,200);

    // Billboard viewer
    private BillboardViewer Billboard;

    // Master Array of billboards (and creators), used in billboard search checking
    String[] billboardListWithCreatorArray;

    // Variable list used to populate JList when user is searching
    final DefaultListModel<String> billboardWithCreatorListModel = new DefaultListModel<>();

    /**
     * Method used to create a GUI window for the Billboard Control Panel
     * @throws ClassNotFoundException Exception thrown by setLookAndFeel: when an application tries to load in a class
     * through its string name but no definition for the class with the specified name could be found.
     * @throws UnsupportedLookAndFeelException Exception thrown by setLookAndFeel: an IllegalAccessException is thrown
     * when an application tries to reflectively create an instance (other than an array), set or get a field, or invoke
     * a method, but the currently executing method does not have access to the definition of the specified class, field,
     * method or constructor.
     * @throws InstantiationException Exception thrown by setLookAndFeel: an exception that indicates the requested
     * look & feel management classes are not present on the user's system.
     * @throws IllegalAccessException Exception thrown by setLookAndFeel: when an application tries to create an
     * instance of a class using the newInstance method in class Class, but the specified class object cannot be instantiated
     * @throws IOException Exception thrown: related to failed or interrupted I/O operations.
     * @throws ParserConfigurationException Exception thrown: related to configuration error.
     * @throws SAXException Exception thrown: related to XML.
     */
    private void createGUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException, ParserConfigurationException, SAXException {
        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation set to Dispose on Close, so when user closes this screen, only this screen closes (keeps Control Panel GUI running)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Make window non resizable
        setResizable(false);

        // Create billboards JPanel
        JPanel billboardPanel = new JPanel();
        billboardPanel.setLayout(new BoxLayout(billboardPanel, BoxLayout.Y_AXIS)); // Set box layout

        // Add billboard preview image, inside of a JPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Set box layout
        mainPanel.add(Box.createVerticalStrut(50)); // Add vertical strut to JPanel

        // If billboard list is empty
        if(billboardListH.size() == 0)
        {
            // Create new billboard preview with empty billboard list message
            // Xml string used when the list of billboards in empty
            String EMPTY_LIST_XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<billboard>\n" +
                    "    <message>No billboards stored in the database</message>\n" +
                    "</billboard>";
            Billboard = new BillboardViewer(EMPTY_LIST_XML_STRING, DIMENSION);
        }
        // Else billboard list is not empty
        else
        {
            // Create new billboard preview with empty xml
            // Xml string used when there was no selection made
            String NO_SELECTION_XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<billboard>\n" +
                    "    <message>Select a billboard to preview</message>\n" +
                    "</billboard>";
            Billboard = new BillboardViewer(NO_SELECTION_XML_STRING, DIMENSION);
        }

        // Set billboard preview size
        billboardPreview = Billboard.getSizedBillboard();
        mainPanel.add(billboardPreview); // Add billboard preview to main JPanel

        // If billboard list is NOT empty
        if(billboardListH.size() >0)
        {
            // Add strut for formatting
            billboardPanel.add(Box.createVerticalStrut(50));

            // Create search JPanel, with X axis Box Layout
            JPanel searchPanel = new JPanel();

            // Create Search Billboard JLabel
            JLabel label = new JLabel("Search Billboard");

            // Add JLabel to specified JPanel
            searchPanel.add(label);

            // Formatting
            searchPanel.add(Box.createHorizontalStrut(10));

            // Create search JTextField, inside search JPanel
            search = newTextField(searchPanel);

            // Add search JPanel to billboard JPanel
            billboardPanel.add(searchPanel);

            // Add strut for formatting
            billboardPanel.add(Box.createVerticalStrut(20));

            // Populate the master array of billboardsWithCreator, with information
            billboardListWithCreatorArray = populateBillboardsArray();

            // Create billboard JList, and add it to billboard JPanel
            billboardList = createJList(billboardWithCreatorListModel, billboardListWithCreatorArray, billboardPanel);
        }

        // Create button JPanel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2)); // Set grid layout of button JPanel

        // Create and add Edit Billboard button, inside button JPanel
        editBillboardButton = createButton("Edit Billboard");
        buttonPanel.add(editBillboardButton); // Add JButton to button JPanel

        // Create and add Schedule Billboard button, inside button JPanel
        scheduleBillboardButton = createButton("Manage Schedule");
        buttonPanel.add(scheduleBillboardButton); // Add JButton to button JPanel
        mainPanel.add(buttonPanel); // Add button JPanel to billboard preview JPanel

        // Create create billboard JPanel
        createBillboardPanel = new JPanel();
        createBillboardPanel.setLayout(new BoxLayout(createBillboardPanel, BoxLayout.X_AXIS)); // Set box layout

        // Create and add Create Billboard button, inside billboard create billboard JPanel; and add formatting
        createBillboardButton = createButton("Create Billboard");
        createBillboardPanel.add(Box.createVerticalStrut(30)); // Add vertical strut
        createBillboardPanel.add(createBillboardButton); // Add JButton to JPanel
        createBillboardPanel.add(Box.createVerticalStrut(100)); // Add vertical strut

        // Create and add Create Delete button
        deleteBillboardButton = createButton("Delete Billboard");
        createBillboardPanel.add(Box.createVerticalStrut(30)); // Add vertical strut
        createBillboardPanel.add(deleteBillboardButton); // Add JButton to JPanel
        createBillboardPanel.add(Box.createVerticalStrut(100)); // Add vertical strut

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
        getContentPane().add(Box.createVerticalStrut(20)); // Formatting
        getContentPane().add(topPanel); // Add top JPanel to content pane
        getContentPane().add(bottomPanel); // Add bottom JPanel to content pane

        // Add Window Listener, used for when window is closed
        addWindowListener(this);

        // Display window
        pack(); // Pack all contents of the GUI
        setLocationRelativeTo(null); // centers window
        setVisible(true); // Make GUI visible
    }

    /**
     * This method creates a new JTextField, and adds it to a specified JPanel
     * @param panel The JPanel that the created JTextField is to be added to
     * @return Returns a JTextField
     */
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
     * This method populates an array with real billboard and creator information
     * @return Returns a String array
     */
    private String[] populateBillboardsArray() {
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
            // Increase counter
            counter++;
        }

        // Returns a String array of billboards and their creator
        return billboardListWithCreator;
    }

    /**
     * This method creates a JList from a specified list model, then populates the list model from a populated String
     * array, then the JList is added to a specified JPanel
     * @param listModel The list model to be written to
     * @param array The array to fetch information from
     * @param panel The JPanel that the created JList is to be added to
     * @return Returns JList
     */
    private JList<String> createJList(DefaultListModel<String> listModel, String[] array, JPanel panel) {

        // Create new JPanel for spacing and formatting
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS)); // Add box layout

        // Create new JList
        JList<String> list = new JList<>(billboardWithCreatorListModel);

        // For each element in the provided array, add the element to the list model
        for (String a : array) {
            // Add element a to the list model
            listModel.addElement(a);
        }

        // Create JScrollPane
        JScrollPane scroll = new JScrollPane(list);

        // Add JScrollPane to the specified JPanel
        panel2.add(scroll);

        // Formatting
        panel2.add(Box.createVerticalStrut(50));

        // Add list selection listener, so billboard preview changes when different billboard is clicked
        list.addListSelectionListener(this);

        // Add JPanel 2, to JPanel
        panel.add(panel2);

        // Return JPanel
        return list;
    }

    /**
     * This method creates a is used to evaluate which billboards is being clicked on in the list
     * Changes billboard XML when a user selects a billboard from the list
     * @param event List selection event for JList
     */
    public void valueChanged(ListSelectionEvent event) {
        // Get string stored in current cell of list
        try {
            String cellSelected;
            cellSelected = billboardList.getSelectedValue();

            // Remove creator from list
            String billboardSelected = cellSelected.replaceAll(",.*", "");

            // Set billboard name
            billboardName = billboardSelected;

            // Set what the selected billboard object
            Billboard billboardObjectSelected = billboardListH.get(billboardSelected);

            //get billboard xml file name
            billboardXML = billboardObjectSelected.XMLFile;

            // Remove all elements of GUI screen
            mainPanel.removeAll();
            mainPanel.revalidate();
            mainPanel.repaint();

            // Try to set billboard
            try {
                Billboard = new BillboardViewer(billboardXML, DIMENSION);
            }
            // Catch exception
            catch (ParserConfigurationException | IOException | SAXException ex) {
                // Show a pop up with the error that was caught
                JOptionPane.showMessageDialog(getContentPane(), ex,
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            // Set box layout of main JPanel
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(Box.createVerticalStrut(50)); // Add vertical strut
            billboardPreview = Billboard.getSizedBillboard(); // Set billboard preview size

            // Add control panel components back to panel
            mainPanel.add(billboardPreview);
            mainPanel.add(buttonPanel);
            mainPanel.add(createBillboardPanel);
        }
        // Catch exception
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
     * @param actionEvent JButton pressed
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        // Checks if the back button has been clicked
        if (buttonClicked == backButton) {
            // Check that editor isn't open
            int frameCount = 0;
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("guis.GUIBillboardEditor"))){
                    if(fr.isVisible()){
                        frameCount += 1;
                    }
                }
            }

            // If editor is open, check if user is sure
            if(frameCount > 0) {
                int a = showConfirmDialog(null, "This will close your current billboard editor screen and you will lose any changes");
                if (a == YES_OPTION) {
                    // Close all editors
                    allFrames = Frame.getFrames();
                    for (Frame fr : allFrames) {
                        if ((fr.getClass().getName().equals("guis.GUIBillboardEditor"))) {
                            fr.dispose();
                        }
                    }
                    // Go back to menu
                    SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
                    dispose();
                    closeable = false;
                }
            }
            else{
                // Go back to menu
                SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
                dispose();
                closeable = false;
            }
        }

        // Checks if the logout button has been clicked
        else if (buttonClicked == logoutButton) {
            // Check that editor isn't open
            int frameCount = 0;
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("guis.GUIBillboardEditor")) || (fr.getClass().getName().equals("guis.GUIBillboardSchedulePopup"))){
                    if(fr.isVisible()){
                        frameCount += 1;
                    }
                }
            }

            // If editor is open, check if user is sure
            if(frameCount > 0) {
                int a = showConfirmDialog(null, "This will close your current popups and you will lose any changes");
                if (a == YES_OPTION) {
                    // Close all editors
                    allFrames = Frame.getFrames();
                    for (Frame fr : allFrames) {
                        if ((fr.getClass().getName().equals("guis.GUIBillboardEditor")) || (fr.getClass().getName().equals("guis.GUIBillboardSchedulePopup"))) {
                            fr.dispose();
                        }
                    }
                    // Open new Login screen
                    // Remove users session token and proceed to the login screen
                    String[] user_input = {"Logout request", ControlPanelClient.sessionToken};
                    ControlPanelClient.runClient(user_input);
                    // Close the GUI screen
                    closeable = false;
                    dispose();
                }
            }
            else{
                // Open new Login screen
                // Remove users session token and proceed to the login screen
                String[] user_input = {"Logout request", ControlPanelClient.sessionToken};
                ControlPanelClient.runClient(user_input);
                // Close the GUI screen
                closeable = false;
                dispose();
            }
        }

        // Checks if the edit billboard button has been clicked
        else if (buttonClicked == editBillboardButton) {
            // If billboard has not been selected in list
            if (billboardName == null) {
                // Display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a billboard in the list to edit");
            }
            // If billboard has been selected
            else {
                // Get string stored in current cell of list
                String cellSelected;
                cellSelected = billboardList.getSelectedValue();

                // Remove creator from list
                String billboardSelected = cellSelected.replaceAll(",.*", "");
                billboardName = billboardSelected;
                Billboard billboardObjectSelected = billboardListH.get(billboardSelected);

                // Get billboard creator, and current username
                String billboardCreator = billboardObjectSelected.BillboardCreator;
                String currentUsername = currentUser.username;

                // Check whether user created billboard
                boolean creatorCheck = billboardCreator.equals(currentUsername);

                // Check whether billboard is scheduled
                boolean scheduleCheck;
                try {
                    schedule.getViewings(billboardName);
                    scheduleCheck = true;
                }
                catch (Exception e) {
                    scheduleCheck = false;
                }

                // Check if user allowed to edit billboards
                if((currentUser.permissions.contains("Edit All Billboards")) || (creatorCheck && !scheduleCheck && currentUser.permissions.contains("Create Billboards"))){
                    //Retrieve the xml file associated with the name
                    String xmlFile = billboardXML;

                    // Check that editor isn't already open
                    int frameCount = 0;
                    Frame[] allFrames = Frame.getFrames();
                    for(Frame fr : allFrames){
                        if((fr.getClass().getName().equals("guis.GUIBillboardEditor"))){
                            if(fr.isVisible()){
                                frameCount += 1;
                            }
                        }
                    }

                    // If editor is open, check if user is sure
                    if(frameCount > 0) {
                        int a = showConfirmDialog(null, "This will close your current billboard editor screen and you will lose any changes");
                        if (a == YES_OPTION) {
                            // Close all editors
                            allFrames = Frame.getFrames();
                            for (Frame fr : allFrames) {
                                if ((fr.getClass().getName().equals("guis.GUIBillboardEditor"))) {
                                    fr.dispose();
                                }
                            }
                            // Create new one
                            openBillboard = billboardName;
                            SwingUtilities.invokeLater(new GUIBillboardEditor(username, sessionToken, billboardName, xmlFile));
                            closeable = false;
                        }
                    }
                    else{
                        // If none open, just open editor
                        openBillboard = billboardName;
                        SwingUtilities.invokeLater(new GUIBillboardEditor(username, sessionToken, billboardName, xmlFile));
                        closeable = false;
                    }
                }
                else{
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You do not have permission to edit this billboards");
                }
            }
        }

        // Checks if the create billboard button has been clicked
        else if (buttonClicked == createBillboardButton) {
            // Open the editor with a new file
            if (currentUser.permissions.contains("Create Billboards")) {
                // Check that editor isn't already open
                int frameCount = 0;
                Frame[] allFrames = Frame.getFrames();
                for(Frame fr : allFrames){
                    if((fr.getClass().getName().equals("guis.GUIBillboardEditor"))){
                        if(fr.isVisible()){
                            frameCount += 1;
                        }
                    }
                }

                // If editor is open, check if user is sure
                if(frameCount > 0) {
                    int a = showConfirmDialog(null, "This will close your current billboard editor screen and you will lose any changes");
                    if (a == YES_OPTION) {
                        // close all editors
                        allFrames = Frame.getFrames();
                        for (Frame fr : allFrames) {
                            if ((fr.getClass().getName().equals("guis.GUIBillboardEditor"))) {
                                fr.dispose();
                            }
                        }
                        // open new editor
                        SwingUtilities.invokeLater(new GUIBillboardEditor(username, sessionToken, billboardListH));
                        closeable = false;
                    }
                }
                else{
                    // open new editor
                    SwingUtilities.invokeLater(new GUIBillboardEditor(username, sessionToken, billboardListH));
                    closeable = false;
                }
            }
            else{
                // Display error pop up
                JOptionPane.showMessageDialog(this,
                        "You do not permission to create a billboard");
            }
        }

        // Checks if the delete billboard button has been clicked
        else if (buttonClicked == deleteBillboardButton) {
            // Check that editor isn't already open
            int frameCount = 0;
            Frame[] allFrames = Frame.getFrames();
            for(Frame fr : allFrames){
                if((fr.getClass().getName().equals("guis.GUIBillboardEditor")) || (fr.getClass().getName().equals("guis.GUIBillboardSchedulePopup"))){
                    if(fr.isVisible()){
                        frameCount += 1;
                    }
                }
            }

            boolean Break = false;
            if(frameCount > 0)
            {
                //get string stored in current cell of list
                String cellSelected;
                cellSelected = billboardList.getSelectedValue();
                //remove creator from list
                String billboardSelected = cellSelected.replaceAll(",.*", "");
                if(billboardSelected.equals(openBillboard) || (billboardSelected.equals(openSchedule))){
                    //display error pop up
                    JOptionPane.showMessageDialog(this,
                            "You can't delete open billboard");
                    Break = true;
                }
            }

            // if no billboard selected, error
            if(billboardName == null && !Break)
            {
                //display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a billboard in the list to delete");
            }
            else if(!Break){
                //get string stored in current cell of list
                String cellSelected;
                cellSelected = billboardList.getSelectedValue();

                //remove creator from list
                String billboardSelected = cellSelected.replaceAll(",.*", "");
                billboardName = billboardSelected;
                Billboard billboardObjectSelected = billboardListH.get(billboardSelected);
                //get billboard creator, and current username
                String billboardCreator = billboardObjectSelected.BillboardCreator;
                String currentUsername = currentUser.username;

                // Check whether user created billboard
                boolean creatorCheck = billboardCreator.equals(currentUsername);

                // Check if billboard scheduled
                boolean scheduleCheck;
                try {
                    schedule.getViewings(billboardName);
                    scheduleCheck = true;
                }
                catch (Exception e) {
                    scheduleCheck = false;
                }

                // Check if user allowed to delete given their permissions
                if((currentUser.permissions.contains("Edit All Billboards")) || (creatorCheck && !scheduleCheck && currentUser.permissions.contains("Create Billboards"))){
                    //Open the editor with a new file
                    try {
                        int a = showConfirmDialog(null, "Are you sure you want to delete this billboard?");
                        if (a == YES_OPTION) {
                            //adjust user inputs to delete given billboard
                            String[] userInputs = {"Delete billboard", billboardName};
                            //Delete billboard with viewing details given by user
                            ControlPanelClient.runClient(userInputs);
                            JOptionPane.showMessageDialog(this,
                            "Billboard Successfully Deleted");
                            dispose();
                            //Refresh control panel
                            userInputs = new String[]{"List billboards", ControlPanelClient.sessionToken};
                            ControlPanelClient.runClient(userInputs);
                            closeable = false;
                        }

                    } catch (Exception e) {
                        // Show a pop up with the error that was caught
                        JOptionPane.showMessageDialog(getContentPane(), e,
                                "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    if(!(currentUser.permissions.contains("Create Billboards"))) {
                        //display error pop up
                        JOptionPane.showMessageDialog(this,
                                "You do not have permission to delete this billboards");
                    }
                    else{
                        //display error pop up
                        JOptionPane.showMessageDialog(this,
                                "You do not have permission to delete scheduled billboards");
                    }
                }
            }
        }

        // Checks if the schedule billboard button has been clicked
        else if (buttonClicked == scheduleBillboardButton) {
            // If billboard has not been selected in list
            if(billboardName == null)
            {
                // Display error pop up
                JOptionPane.showMessageDialog(this,
                        "You must select a billboard in the list to schedule");
            }
            // If billboard has been selected
            else
            {
                // Check user has correct permissions
                if (currentUser.permissions.contains("Schedule Billboards")) {
                    int frameCount = 0;
                    // check no schedule GUIs are open
                    Frame[] allFrames = Frame.getFrames();
                    for(Frame fr : allFrames){
                        if((fr.getClass().getName().equals("guis.GUIBillboardSchedulePopup"))){
                            if(fr.isVisible()){
                                frameCount += 1;
                            }
                        }
                    }

                    // if open, check user is sure they want to reset it
                    if(frameCount > 0) {
                        int a = showConfirmDialog(null, "This will close your current scheduling screen and you will lose any changes");
                        if (a == YES_OPTION) {
                            // close all schedule screens
                            allFrames = Frame.getFrames();
                            for (Frame fr : allFrames) {
                                if ((fr.getClass().getName().equals("guis.GUIBillboardSchedulePopup"))) {
                                    fr.dispose();
                                }
                            }
                            // new schedule GUI
                            openSchedule = billboardName;
                            SwingUtilities.invokeLater(new GUIBillboardSchedulePopup(username, sessionToken, billboardName, schedule));
                            closeable = false;
                        }
                    }
                    else{
                        // new schedule GUI
                        openSchedule = billboardName;
                        SwingUtilities.invokeLater(new GUIBillboardSchedulePopup(username, sessionToken, billboardName, schedule));
                        closeable = false;
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

    /**
     * This method checks the user's search within a given array, and updates the search results accordingly in the JList live
     * Used for the Document Listener (insertUpdate, removeUpdate, and changedUpdate)
     * @param model The model list to be changed, translates to the JList
     * @param array The master array to be checked against
     */
    private void listSearch(DefaultListModel<String> model, String[] array) {
        // Try executing the search
        try {
            // Converts any input by the user to lowercase characters (making the check case insensitive)
            String textLower = search.getText().toLowerCase();

            // Iterates through each element in the provided master array
            for (String a : array) {
                //  Checks if string entered in JTextField is not present in the master array (lowercase for case insensitivity)
                if (!a.toLowerCase().contains(textLower)) {
                    // If list model already contains the element a
                    if (model.contains(a)) {
                        // Remove the element a from the model
                        model.removeElement(a);
                    }
                }
                // The string entered is present in the master array
                else {
                    // If list model does not already contain the element a
                    if (!model.contains(a)) {
                        // Add the the element a to the model
                        model.addElement(a);
                    }
                }
            }
        }

        // Ignore any exception thrown
        catch (Exception ignored) {
            // Do nothing
        }
    }


    /**
     * Populates and runs the GUI
     */
    @Override
    public void run() {
        // Try to run the GUI
        try {
            createGUI();
        }
        // Catches an exception and displays an appropriate error message dialog
        catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException | IOException | ParserConfigurationException | SAXException e) {
            // Show a pop up with the error that was caught
            JOptionPane.showMessageDialog(this, e,
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Window opened event
     * @param e Window event
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Window closing event
     * @param e Window event
     */
    @Override
    public void windowClosing(WindowEvent e) {

    }

    /**
     * Window closed event
     * @param e Window event
     */
    @Override
    public void windowClosed(WindowEvent e) {
        if(closeable) {
            // Check that editor isn't open
            int frameCount = 0;
            Frame[] allFrames = Frame.getFrames();
            for (Frame fr : allFrames) {
                if ((fr.getClass().getName().equals("guis.GUIBillboardEditor"))) {
                    if (fr.isVisible()) {
                        frameCount += 1;
                    }
                }
            }

            // If editor is open, check if user is sure
            if (frameCount > 0) {
                int a = showConfirmDialog(null, "This will close your current billboard editor screen and you will lose any changes");
                if (a == YES_OPTION) {
                    // Close all editors
                    allFrames = Frame.getFrames();
                    for (Frame fr : allFrames) {
                        if ((fr.getClass().getName().equals("guis.GUIBillboardEditor"))) {
                            fr.dispose();
                        }
                    }
                    // Go back to menu
                    SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
                    dispose();
                    closeable = false;
                }
            } else {
                // Go back to menu
                SwingUtilities.invokeLater(new GUIMainMenu(username, sessionToken));
                dispose();
                closeable = false;
            }
        }
    }

    /**
     * Window iconified event
     * @param e Window event
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Window DeIconified event
     * @param e Window event
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Window activated event
     * @param e Window event
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Window deactivated event
     * @param e Window event
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * Insert update for JTextField
     * @param e Document event
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        // Execute search
        listSearch(billboardWithCreatorListModel, billboardListWithCreatorArray);
    }

    /**
     * Remove update for JTextField
     * @param e Document update
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        // Execute search
        listSearch(billboardWithCreatorListModel, billboardListWithCreatorArray);
    }

    /**
     * Changed update for JTextField
     * @param e Document event
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
        // Execute search
        listSearch(billboardWithCreatorListModel, billboardListWithCreatorArray);
    }
}
