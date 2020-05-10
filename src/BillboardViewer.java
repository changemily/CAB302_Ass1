import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.xml.parsers.*;

/**
 * Billboard Viewer Gui
 * This class contains a Main method and method that creates a GUI window for the Billboard Viewer
 * @author - Harry Estreich
 * @version - under development
 * NOTES: Completed background colour
 */

public class BillboardViewer extends JFrame{
    public BillboardViewer(File xml_file) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Find screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        // Parse File
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = factory.newDocumentBuilder();
        Document file = build.parse(xml_file);

        /// Background Colour
        // Get elements from Billboard tag
        Element billboardElement = file.getDocumentElement(); // get any document elements
        String billboardColour = "#FFFFFF"; // set default to white

        // Check for background attribute, if there, set colour to that attribute
        if (billboardElement.hasAttribute("background")){
            billboardColour = billboardElement.getAttribute("background");
        }
        Color billboardColourCode = Color.decode(billboardColour); // decode hashcode


        // Get Message
        NodeList message = file.getElementsByTagName("message");
        String messageText = "";
        String messageColour;
        boolean messageBool;
        Color messageColourCode;
        try{
            messageText = message.item(0).getTextContent();
            try {
                messageColour = message.item(0).getAttributes().getNamedItem("colour").getTextContent();
            } catch (Exception e){
                messageColour = "#000000";
            }
            messageColourCode = Color.decode(messageColour);
            System.out.println(messageText);
            System.out.println(messageColour);
            messageBool = true;
        } catch (Exception e){
            messageBool = false;
            messageColourCode = Color.WHITE;
        }

        // Get Information
        NodeList information = file.getElementsByTagName("information");
        String informationText = "";
        String informationColour;
        boolean informationBool;
        Color informationColourCode;
        try{
            informationText = information.item(0).getTextContent();
            try {
                informationColour = information.item(0).getAttributes().getNamedItem("colour").getTextContent();
                informationBool = true;
            } catch (Exception e){
                informationColour = "#000000";
                informationBool = false;
            }
            informationColourCode = Color.decode(informationColour);
            System.out.println(informationText);
            System.out.println(informationColour);
        } catch (Exception e){
            informationBool = false;
            informationColourCode = Color.WHITE;
        }

        // Create frame, and set basic settings
        JFrame billboard = new JFrame("background");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        billboard.setDefaultCloseOperation(EXIT_ON_CLOSE);
        billboard.getContentPane().setBackground(billboardColourCode);
        billboard.setUndecorated(true);
        billboard.setLayout(new GridBagLayout());

        if(messageBool && !informationBool) {
            int yLocation = 0;
            displayMessage(billboard, messageText, messageColourCode, screenSize, yLocation);
        }

        if(!messageBool && informationBool) {
            int yLocation = 0;
            displayInformation(billboard, informationText, informationColourCode, screenSize, yLocation);
        }

        if(messageBool && informationBool) {
            int yLocation = 0;
            displayMessage(billboard, messageText, messageColourCode, screenSize, yLocation);
            yLocation = 1;
            displayInformation(billboard, informationText, informationColourCode, screenSize, yLocation);
        }


        // Pack billboard and set visible
        billboard.pack();
        billboard.setExtendedState(MAXIMIZED_BOTH);
        billboard.setVisible(true);
    }

    public void setLabelFontSize(JLabel textLabel, Dimension screenSize, double buffer){
        Font textFont = textLabel.getFont();
        int fontSize = textFont.getSize();
        String text = textLabel.getText();

        int stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);

        while(stringSize < (screenSize.width * buffer)){
            fontSize += 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));
            textFont = textLabel.getFont();
            stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
        }
    }

    public void displayMessage(JFrame billboard, String text, Color ColourCode, Dimension screenSize, int ylocation){
        double buffer = 0.95;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = ylocation;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        JLabel messageLabel = new JLabel(text);
        setLabelFontSize(messageLabel, screenSize, buffer);
        messageLabel.setForeground(ColourCode);
        billboard.add(messageLabel, c);
    }

    public void displayInformation(JFrame billboard, String text, Color ColourCode, Dimension screenSize, int ylocation){
        double buffer = 0.75;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = ylocation;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        JLabel informationLabel = new JLabel(text);
        setLabelFontSize(informationLabel, screenSize, buffer);
        informationLabel.setForeground(ColourCode);
        billboard.add(informationLabel, c);
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        File file;
        file = new File("./12.xml");
        new BillboardViewer(file);
    }
}
