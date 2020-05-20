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
    JList billboardList;
    JButton editBillboardButton;
    JButton scheduleBillboardButton;
    JButton createBillboardButton;
    String billboardXML="./5.xml";

    public ControlPanelGUIBillboardControlPanel() {
        // Set window title
        super("Billboard Control Panel");
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

        //Create billboard JList
        billboardList = createJList();

        // Add billboard JList to billboard JPanel
        billboardPanel.add(billboardList);

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
        BillboardViewer Billboard = new BillboardViewer(file, new Dimension(150,150));
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
        createBillboardPanel.add(Box.createHorizontalGlue());
        createBillboardPanel.add(createBillboardButton);
        createBillboardPanel.add(Box.createHorizontalGlue());
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
    private JList createJList() {
        // Create billboard JList, and populate with test data
        // NOTE: Needs to be changed to populate billboard JList with real billboard data
        String[] billboards = {"1", "2", "3", "4", "5", "Test 6", "Test 7", "Test 9", "Test 10"};
        JList billboardList = new JList(billboards);
        billboardList.addListSelectionListener(this::valueChanged);
        // Return JPanel
        return billboardList;
    }

    // Changes billboard XML when a user selects a billboard from the list
    public void valueChanged(ListSelectionEvent event) {
        String billboardSelected = billboardList.getSelectedValue().toString();
        billboardXML = "./"+billboardSelected+".xml";

        System.out.println(billboardXML);
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
    public void actionPerformed(ActionEvent e) {

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

    /**
     * Main method which creates a GUI window for the Billboard Control Panel
     * @param args This method takes no arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ControlPanelGUIBillboardControlPanel());
    }
}
