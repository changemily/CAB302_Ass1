import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Billboard Control Panel class for Control Panel GUI
 * This class contains a Main method and method that creates a GUI window for the Billboard Control Panel
 * @author - Nickhil Nischal
 * @version - under development
 *
 * NOTES: Current version is a basic design; button functionality still needs to be added; further refinement required;
 *        contains some code used during testing which will be removed
 */
public class ControlPanelGUIBillboardControlPanel extends JFrame implements Runnable, ActionListener {
    // Clickable buttons and list
    private JList billboardList;
    private JButton editBillboardButton;
    private JButton scheduleBillboardButton;
    private JButton createBillboardButton;
    private String billboardXML="./5.xml"; //CHANGE TO DEFAULT NOTHING TO DISPLAY XML
    private BillboardList billboard_list = new BillboardList();
    private String billboardName;

    public ControlPanelGUIBillboardControlPanel() throws Exception {
        // Set window title
        super("Billboard Control Panel");

        //FOR TESTING
        billboard_list.createEditBillboard("1", "hello", "#000000", "No Image", "jarod");
        billboard_list.createEditBillboard("2", "hello", "#000000", "No Image", "jarod");
        billboard_list.createEditBillboard("3", "hello", "#000000", "No Image", "jarod");


    }

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

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create billboards JPanel
        JPanel billboardPanel = new JPanel();
        billboardPanel.setLayout(new BoxLayout(billboardPanel, BoxLayout.X_AXIS));

        //Create billboard JList, and add it to billboard JPanel
        billboardList = createJList(billboardPanel);

        // Add billboard preview image, inside of a JPanel
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.add(Box.createVerticalStrut(50));
        //JLabel billboardList = new JLabel("ADD BILLBOARD PREVIEW HERE");

       /* String imagePath = "C:\\Users\\Nickhil N.NPN-ASUS\\OneDrive\\Documents\\Nickhil's Documents\\QUT\\3rd Year 2020\\Semester 1\\CAB302 - Software Development\\Major Project\\qutLogo.jpg";
        BufferedImage image = ImageIO.read(new File(imagePath));
        Image image2 = image.getScaledInstance(-1,300, Image.SCALE_DEFAULT);
        JLabel billboardPreview = new JLabel(new ImageIcon(image2));
        */

        //create new viewer to display xml
        File file = new File(billboardXML);
        BillboardViewer Billboard = new BillboardViewer(file, new Dimension(400,200));
        JPanel billboardPreview = Billboard.getSizedBillboard();
        previewPanel.add(billboardPreview);


        // Create button JPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        // Create and add Edit Billboard button, inside button JPanel
        editBillboardButton = createButton("Edit");
        buttonPanel.add(editBillboardButton);

        // Create and add Schedule Billboard button, inside button JPanel
        scheduleBillboardButton = createButton("Schedule");
        buttonPanel.add(scheduleBillboardButton);
        previewPanel.add(buttonPanel); // Add button JPanel to billboard preview JPanel

        // Create create billboard JPanel
        JPanel createBillboardPanel = new JPanel();
        createBillboardPanel.setLayout(new BoxLayout(createBillboardPanel, BoxLayout.X_AXIS));

        // Create and add Create Billboard button, inside billboard create billboard JPanel; and add formatting
        createBillboardButton = createButton("Create Billboard");
        createBillboardPanel.add(Box.createVerticalStrut(30));
        createBillboardPanel.add(createBillboardButton);
        createBillboardPanel.add(Box.createVerticalStrut(100));

        // Add create billboard JPanel to preview panel JPanel
        previewPanel.add(createBillboardPanel);

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(Box.createHorizontalStrut(100));
        getContentPane().add(billboardPanel);
        getContentPane().add(Box.createHorizontalStrut(40));
        getContentPane().add(previewPanel);
        getContentPane().add(Box.createHorizontalStrut(100));

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * This method creates a JList, returns a JList
     * @return Returns JList
     */
    private JList createJList(JPanel panel) {
        //For Testing
        //Billboard with no scheduled viewing
//        try {
//            billboard_list.Create_edit_Billboard("Billboard1", "new billboard", "#4287f5", "No Image", "jarod");
//            billboard_list.Create_edit_Billboard("Billboard2", "new billboard", "#4287f5", "No Image", "Is");
//            billboard_list.Create_edit_Billboard("Billboard3", "new billboard", "#4287f5", "No Image", "The");
//            billboard_list.Create_edit_Billboard("Billboard4", "new billboard", "#4287f5", "No Image", "Best");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //Int counter for assigning values in the array
        int counter = 1;

        //Setup an array to return creator and billboard
        String[] billboardListWithCreator = new String[billboard_list.billboardHashMap.size()+1];

        //Get the Creator of each billboard in the billboard list
        for(Map.Entry<String, Billboard> billboardEntry : billboard_list.billboardHashMap.entrySet()){
            //Initialize array with the new size and repopulate it
            //billboardListWithCreator = new String[counter];
            //Assign creator name to position one in the 2D array
            billboardListWithCreator[counter] = billboardEntry.getValue().Billboard_creator;
            //Assign billboard name to the second position in the 2D array
            billboardListWithCreator[counter] = billboardListWithCreator[counter] + ", "
                    + billboardEntry.getValue().Billboard_name;
            counter++;
        }

        // Create new JPanel for spacing and formatting
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(Box.createVerticalStrut(50));

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

    // Changes billboard XML when a user selects a billboard from the list
    public void valueChanged(ListSelectionEvent event) {
        //get string stored in current cell of list
        String cellSelected = billboardList.getSelectedValue().toString();

        //remove creator from list
        String billboardSelected = cellSelected.replaceAll(".*, ", "");

        //get billboard xml file name
        billboardXML = "./"+billboardSelected+".xml";

        //store billboard name of selected cell
        billboardName = billboardSelected;
        System.out.println("bb xml: "+billboardXML);
        System.out.println("bb name: "+billboardName);
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Get button that has been clicked - event source
        Object buttonClicked = actionEvent.getSource();

        if (buttonClicked==editBillboardButton || buttonClicked == createBillboardButton) {
            //run Billboard editor/creator GUI
            SwingUtilities.invokeLater(new BBEditor());
        }

        else if (buttonClicked==scheduleBillboardButton) {
            //run schedule billboard GUI pop up
            SwingUtilities.invokeLater(new BBSchedulePopup(billboardName));
        }

    }

    @Override
    public void run() {
        try {
            createGUI();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new ControlPanelGUIBillboardControlPanel());
    }

}
