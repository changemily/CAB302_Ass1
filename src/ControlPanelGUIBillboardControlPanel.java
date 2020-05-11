import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
 *        contains code used during testing which will be removed
 */
public class ControlPanelGUIBillboardControlPanel extends JFrame {
    /**
     * Method used to create a GUI window for the Billboard Control Panel
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     * @throws IOException Exception handling
     */
    public ControlPanelGUIBillboardControlPanel() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        // Set window title
        super("Billboard Control Panel");

        // Set look and feel of GUI to resemble operating system look and feel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // Default close operation, so window does not continue running after it is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add billboard list, inside of a JPanel
        JPanel billboardPanel = new JPanel();
        billboardPanel.setLayout(new BoxLayout(billboardPanel, BoxLayout.X_AXIS));
        //JList billboardList = new JList();
        String billboards[] = {"Test 1", "Test 2", "Test 3", "Test 4", "Test 5", "Test 6", "Test 7", "Test 9", "Test 10"};
        billboardPanel.add(new JList(billboards));

        // Add billboard preview image, inside of a JPanel
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.add(Box.createVerticalStrut(50));
        //JLabel billboardList = new JLabel("ADD BILLBOARD PREVIEW HERE");
        //previewPanel.add(billboardList);

        String imagePath = "qutLogo.jpg";
        BufferedImage image = ImageIO.read(new File(imagePath));
        Image image2 = image.getScaledInstance(-1,300, Image.SCALE_DEFAULT);
        JLabel billboardPreview = new JLabel(new ImageIcon(image2));
        previewPanel.add(billboardPreview);

        // Add Edit Billboard button, inside of a JPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        JButton editBillboard = new JButton("Edit");
        //buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(editBillboard);

        // Add Schedule Billboard button, inside button JPanel
        //buttonPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.X_AXIS));
        JButton scheduleBillboard = new JButton("Schedule");
        buttonPanel.add(scheduleBillboard);
        previewPanel.add(buttonPanel); // Add button JPanel to billboard preview JPanel

        // Add Create Billboard button, inside billboard preview JPanel
        JPanel createBillboardPanel = new JPanel();
        createBillboardPanel.setLayout(new BoxLayout(createBillboardPanel, BoxLayout.X_AXIS));
        JButton createBillboardButton = new JButton("Create Billboard");
        createBillboardPanel.add(createBillboardButton);
        createBillboardPanel.add(Box.createVerticalStrut(30));
        createBillboardPanel.add(Box.createHorizontalGlue());
        createBillboardPanel.add(createBillboardButton);
        createBillboardPanel.add(Box.createHorizontalGlue());
        createBillboardPanel.add(Box.createVerticalStrut(100));

        previewPanel.add(createBillboardPanel);

        // Window formatting
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(Box.createHorizontalStrut(100));
        //getContentPane().add(Box.createVerticalStrut(200));
        getContentPane().add(billboardPanel);
        getContentPane().add(Box.createHorizontalStrut(40));
        getContentPane().add(previewPanel);
        getContentPane().add(Box.createHorizontalStrut(100));
        //getContentPane().add(createBillboardPanel);
        //getContentPane().add(Box.createVerticalStrut(20));
        //getContentPane().add(usernamePanel);
        //getContentPane().add(Box.createVerticalStrut(10));
        //getContentPane().add(labelPassword);
        //getContentPane().add(Box.createVerticalStrut(20));
        //getContentPane().add(loginPanel);
        //getContentPane().add(Box.createVerticalStrut(50));

        // Display window
        setLocation(new Point(100,100));
        pack();
        setLocationRelativeTo(null); // centers window
        setVisible(true);
    }

    /**
     * Main method which creates a GUI window for the Billboard Control Panel
     * @param args This method takes no arguments
     * @throws ClassNotFoundException Exception handling
     * @throws UnsupportedLookAndFeelException Exception handling
     * @throws InstantiationException Exception handling
     * @throws IllegalAccessException Exception handling
     * @throws IOException Exception handling
     */
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        new ControlPanelGUIBillboardControlPanel();
    }
}
