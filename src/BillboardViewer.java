import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
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
            messageBool = true;
        } catch (Exception e){
            messageBool = false;
        }
        try {
            messageColour = message.item(0).getAttributes().getNamedItem("colour").getTextContent();
            messageColourCode = Color.decode(messageColour);
        } catch (Exception e){
            messageColour = "#000000";
            messageColourCode = Color.decode(messageColour);
        }
        System.out.println(messageText);
        System.out.println(messageColour);

        // Get Information
        NodeList information = file.getElementsByTagName("information");
        String informationText = "";
        String informationColour;
        boolean informationBool;
        Color informationColourCode;
        try{
            informationText = information.item(0).getTextContent();
            informationBool = true;
        } catch (Exception e){
            informationBool = false;
        }

        try {
            informationColour = information.item(0).getAttributes().getNamedItem("colour").getTextContent();
            informationColourCode = Color.decode(informationColour);
        } catch (Exception e){
            informationColour = "#000000";
            informationColourCode = Color.decode(informationColour);
        }
        System.out.println(informationText);
        System.out.println(informationColour);

        // Get picture data
        NodeList picture = file.getElementsByTagName("picture");
        String pictureText;
        boolean urlBool;
        //boolean dataBool;
        try{
            pictureText = picture.item(0).getAttributes().getNamedItem("url").getTextContent();
            System.out.println(pictureText);
            URL pictureURL = new URL(pictureText);
            BufferedImage pictureImage = ImageIO.read(pictureURL);
            urlBool = true;
        } catch (Exception e)
        {
            urlBool = false;
        }

        try{
            if(!urlBool){
                pictureText = picture.item(0).getAttributes().getNamedItem("data").getTextContent();
                System.out.println(pictureText);
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] pictureBytes = decoder.decode(pictureText);
                ByteArrayInputStream byteInput = new ByteArrayInputStream(pictureBytes);
                BufferedImage pictureImage = ImageIO.read(byteInput);
                //dataBool = true;
            }
        } catch (Exception e){
            //dataBool = false;
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
            JLabel messageLabel = new JLabel();
            displayMessage(billboard, messageLabel, messageText, messageColourCode, screenSize, yLocation);
        }

        if(!messageBool && informationBool) {
            int yLocation = 0;
            JLabel informationLabel = new JLabel();
            displayInformation(billboard, informationLabel, informationText, informationColourCode, screenSize, yLocation);
        }

        if(messageBool && informationBool) {
            int yLocation = 0;
            JLabel messageLabel = new JLabel();
            displayMessage(billboard, messageLabel, messageText, messageColourCode, screenSize, yLocation);
            yLocation = 1;
            JLabel informationLabel = new JLabel();
            displayInformation(billboard, informationLabel, informationText, informationColourCode, screenSize, yLocation, messageLabel);
        }


        // Pack billboard and set visible
        billboard.pack();
        billboard.setExtendedState(MAXIMIZED_BOTH);
        billboard.setVisible(true);
    }

    public void setMessageFontSize(JLabel textLabel, Dimension screenSize, double buffer){
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

    public void setInformationFontSize(JEditorPane area, JLabel textLabel, Dimension screenSize, double widthBuffer, double heightBuffer){
        Font textFont = textLabel.getFont();
        int fontSize = textFont.getSize();
        String text = textLabel.getText();

        int stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
        int stringHeight = textLabel.getFontMetrics(textFont).getHeight();
        double stringLines = stringSize / (screenSize.width * widthBuffer);

        while(stringHeight * stringLines < (screenSize.height * heightBuffer)){
            fontSize += 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));
            textFont = textLabel.getFont();
            stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
            stringHeight = textLabel.getFontMetrics(textFont).getHeight();
            stringLines = stringSize / (screenSize.width * widthBuffer);
        }
        double stringLineRound = Math.floor(stringLines);

        while(stringLines > stringLineRound){
            fontSize -= 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));
            textFont = textLabel.getFont();
            stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
            stringLines = stringSize / (screenSize.width * widthBuffer);
        }
        area.setFont(new Font(textLabel.getFont().getName(), Font.PLAIN, textLabel.getFont().getSize()));
        area.setSize((int)(screenSize.width * 0.75), (int)(screenSize.height * 0.5));
        area.setEditable(false);
    }

    public void setInformationFontSize(JEditorPane area, JLabel textLabel, Dimension screenSize, double widthBuffer, double heightBuffer, JLabel messageLabel){
        Font textFont = textLabel.getFont();
        int fontSize = textFont.getSize();
        String text = textLabel.getText();

        int stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
        int stringHeight = textLabel.getFontMetrics(textFont).getHeight();
        double stringLines = stringSize / (screenSize.width * widthBuffer);

        while((stringHeight * stringLines < (screenSize.height * heightBuffer)) && (fontSize < (messageLabel.getFont().getSize() * 0.75))){
            fontSize += 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));
            textFont = textLabel.getFont();
            stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
            stringHeight = textLabel.getFontMetrics(textFont).getHeight();
            stringLines = stringSize / (screenSize.width * widthBuffer);
        }
        double stringLineRound = Math.floor(stringLines);

        while(stringLines > stringLineRound){
            fontSize -= 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));
            textFont = textLabel.getFont();
            stringSize = textLabel.getFontMetrics(textFont).stringWidth(text);
            stringLines = stringSize / (screenSize.width * widthBuffer);
        }
        area.setFont(new Font(textLabel.getFont().getName(), Font.PLAIN, textLabel.getFont().getSize()));
        area.setSize((int)(screenSize.width * 0.75), (int)(screenSize.height * 0.5));
        area.setEditable(false);
    }

    public void displayMessage(JFrame billboard, JLabel message, String text, Color ColourCode, Dimension screenSize, int ylocation){
        double buffer = 0.95;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = ylocation;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        message.setText(text);
        setMessageFontSize(message, screenSize, buffer);
        message.setForeground(ColourCode);
        billboard.add(message, c);
    }

    public void displayInformation(JFrame billboard, JLabel information, String text, Color ColourCode, Dimension screenSize, int ylocation){
        double widthBuffer = 0.70;
        double heightBuffer = 0.5;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = ylocation;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        information.setText(text);
        JEditorPane informationPane = new JEditorPane("text/plain", text);
        setInformationFontSize(informationPane, information, screenSize, widthBuffer, heightBuffer);
        information.setForeground(ColourCode);
        billboard.add(informationPane, c);
    }

    public void displayInformation(JFrame billboard, JLabel information, String text, Color ColourCode, Dimension screenSize, int ylocation, JLabel message){
        double widthBuffer = 0.75;
        double heightBuffer = 0.25;
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = ylocation;
        c.weighty = 1;
        c.anchor = GridBagConstraints.CENTER;
        information.setText(text);
        JEditorPane informationPane = new JEditorPane("text/plain", text);
        setInformationFontSize(informationPane, information, screenSize, widthBuffer, heightBuffer, message);
        information.setForeground(ColourCode);
        billboard.add(informationPane, c);
    }



    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        File file;
        file = new File("./2.xml");
        new BillboardViewer(file);
    }
}
