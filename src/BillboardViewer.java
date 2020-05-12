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
import javax.swing.border.EmptyBorder;
import javax.swing.text.StyledDocument;
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
        boolean pictureBool;
        BufferedImage pictureImage = null;
        try{
            pictureText = picture.item(0).getAttributes().getNamedItem("url").getTextContent();
            System.out.println(pictureText);
            URL pictureURL = new URL(pictureText);
            pictureImage = ImageIO.read(pictureURL);
            urlBool = true;
            pictureBool = true;
        } catch (Exception e)
        {
            urlBool = false;
            pictureBool = false;
        }

        try{
            if(!urlBool){
                pictureText = picture.item(0).getAttributes().getNamedItem("data").getTextContent();
                System.out.println(pictureText);
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] pictureBytes = decoder.decode(pictureText);
                ByteArrayInputStream byteInput = new ByteArrayInputStream(pictureBytes);
                pictureImage = ImageIO.read(byteInput);
                pictureBool = true;
            }
        } catch (Exception e){
            pictureBool = false;
        }


        // Create frame, and set basic settings
        JFrame billboard = new JFrame("background");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        billboard.setDefaultCloseOperation(EXIT_ON_CLOSE);
        billboard.getContentPane().setBackground(billboardColourCode);
        billboard.setUndecorated(true);
        billboard.setLayout(new GridBagLayout());

        if(messageBool && !informationBool && !pictureBool) {
            int yLocation = 0;
            JLabel messageLabel = new JLabel();
            displayMessage(billboard, messageLabel, messageText, messageColourCode, screenSize, yLocation, false, 0);
        }

        if(!messageBool && informationBool && !pictureBool) {
            int yLocation = 0;
            JLabel informationLabel = new JLabel();
            displayInformation(billboard, informationLabel, informationText, informationColourCode, billboardColourCode, screenSize, yLocation, false, 0);
        }

        if(!messageBool && !informationBool && pictureBool) {
            int yLocation = 0;
            displayPicture(billboard, pictureImage, screenSize, yLocation, 1, false);
        }

        if(messageBool && informationBool && !pictureBool) {
            int yLocation = 0;
            JLabel messageLabel = new JLabel();
            displayMessage(billboard, messageLabel, messageText, messageColourCode, screenSize, yLocation, false,0);
            yLocation = 1;
            JLabel informationLabel = new JLabel();
            displayInformation(billboard, informationLabel, informationText, informationColourCode, billboardColourCode, screenSize, yLocation, messageLabel, false, 0);
        }

        if(messageBool && !informationBool && pictureBool){
            int ylocation = 1;
            int padding = displayPicture(billboard, pictureImage, screenSize, ylocation, 2, true);
            ylocation = 0;
            JLabel messageLabel = new JLabel();
            displayMessage(billboard, messageLabel, messageText, messageColourCode, screenSize, ylocation, true, padding);
        }

        if(!messageBool && informationBool && pictureBool){
            int ylocation = 0;
            int padding = displayPicture(billboard, pictureImage, screenSize, ylocation, 2, false);
            ylocation = 1;
            JLabel informationLabel = new JLabel();
            displayInformation(billboard, informationLabel, informationText, informationColourCode, billboardColourCode, screenSize, ylocation, true, padding);
        }


        // Pack billboard and set visible
        billboard.pack();
        billboard.setExtendedState(MAXIMIZED_BOTH);
        billboard.setVisible(true);
    }

    public int setMessageFontSize(JLabel textLabel, Dimension screenSize, double buffer){
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
        return textLabel.getFontMetrics(textFont).getHeight();
    }

    public int setInformationFontSize(JEditorPane area, JLabel textLabel, Dimension screenSize, double widthBuffer, double heightBuffer){
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
            System.out.println(stringLines);
        }
        area.setFont(new Font(textLabel.getFont().getName(), Font.PLAIN, textLabel.getFont().getSize()));
        area.setSize((int)(screenSize.width * 0.75), (int)(screenSize.height * 0.5));
        area.setEditable(false);
        return((int)(stringHeight * stringLineRound));
    }

    public int setInformationFontSize(JEditorPane area, JLabel textLabel, Dimension screenSize, double widthBuffer, double heightBuffer, JLabel messageLabel){
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
        return((int)(stringHeight * stringLineRound));
    }

    public BufferedImage resizePicture(BufferedImage picture,  Dimension screenSize, double buffer){
        int currentHeight = picture.getHeight();
        int currentWidth = picture.getWidth();
        double currentRatio = (double)currentHeight / (double)currentWidth;

        int maxHeight = (int)(screenSize.height * buffer);
        int maxWidth = (int)(screenSize.width * buffer);

        int newWidth; int newHeight;

        if(currentRatio >= 1){
            double ratioIncrease = (double)maxHeight / (double)currentHeight;
            newHeight = maxHeight;
            newWidth = (int)(currentWidth * ratioIncrease);
        }
        else{ // currentRatio < 1
            double ratioIncrease = (double)maxWidth / (double)currentWidth;
            newWidth = maxWidth;
            newHeight = (int)(currentHeight * ratioIncrease);
        }

        System.out.println(newHeight);
        System.out.println(newWidth);

        Image tempImage = picture.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tempImage, 0, 0, null);
        g2d.dispose();
        return resizedImage;
    }



    public void displayMessage(JFrame billboard, JLabel message, String text, Color ColourCode, Dimension screenSize, int ylocation, boolean pictureWeight, int padding){
        double weight;
        double screenHeight = (1.0)/(3.0);
        if(pictureWeight){
            weight = 0.5;
            screenHeight = (1.0)/(3.0);
        }
        else{
            weight = 1;
        }
        double buffer = 0.95;
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = ylocation;
        c.weighty = weight;
        c.anchor = GridBagConstraints.CENTER;
        c.ipady = padding + (int)(screenHeight * screenSize.height);
        System.out.println(c.ipady);
        message.setText(text);
        int fontHeight = setMessageFontSize(message, screenSize, buffer);
        c.ipady = (c.ipady - fontHeight)/2;
        System.out.println(c.ipady);
        message.setForeground(ColourCode);
        billboard.add(message, c);
    }

    public void displayInformation(JFrame billboard, JLabel information, String text, Color ColourCode, Color BackgroundColourCode, Dimension screenSize, int ylocation, boolean pictureWeight, int padding){
        double widthBuffer = 0.70;
        double heightBuffer;
        double weight;
        double screenHeight = (1.0)/(3.0);
        if(pictureWeight){
            weight = 0.5;
            heightBuffer = (1.0)/(6.0);
        }
        else{
            weight = 1;
            heightBuffer = 0.5;
        }
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = ylocation;
        c.weighty = weight;
        c.anchor = GridBagConstraints.PAGE_START;
        double cInset = padding + (int)(screenHeight * screenSize.height);
        information.setText(text);
        JEditorPane informationPane = new JEditorPane("text/plain", text);
        int fontHeight = setInformationFontSize(informationPane, information, screenSize, widthBuffer, heightBuffer);
        Font informationFont = informationPane.getFont();
        JEditorPane htmlInformationPane = new JEditorPane();
        htmlInformationPane.setContentType("text/html");
        htmlInformationPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        htmlInformationPane.setFont(informationFont);
        htmlInformationPane.setText("<center>" + text + "</center");
        htmlInformationPane.setPreferredSize(new Dimension((int)(screenSize.width * widthBuffer), fontHeight));
        htmlInformationPane.setEditable(false);
        cInset = (cInset - (fontHeight))/8;
        c.insets = new Insets((int)cInset, 0, 0,0);
        htmlInformationPane.setForeground(Color.GREEN);
        htmlInformationPane.setBackground(Color.YELLOW);
        billboard.add(htmlInformationPane, c);
    }

    public void displayInformation(JFrame billboard, JLabel information, String text, Color ColourCode, Color BackgroundColourCode, Dimension screenSize, int ylocation, JLabel message, boolean pictureWeight, int padding){
        double widthBuffer = 0.70;
        double heightBuffer;
        double weight;
        double screenHeight = (1.0)/(3.0);
        if(pictureWeight){
            weight = 0.5;
            heightBuffer = (1.0)/(6.0);
        }
        else{
            weight = 1;
            heightBuffer = 0.25;
        }
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = ylocation;
        c.weighty = weight;
        c.anchor = GridBagConstraints.PAGE_START;
        double cInset = padding + (int)(screenHeight * screenSize.height);
        information.setText(text);
        JEditorPane informationPane = new JEditorPane("text/plain", text);
        int fontHeight = setInformationFontSize(informationPane, information, screenSize, widthBuffer, heightBuffer, message);
        Font informationFont = informationPane.getFont();
        JEditorPane htmlInformationPane = new JEditorPane();
        htmlInformationPane.setContentType("text/html");
        htmlInformationPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        htmlInformationPane.setFont(informationFont);
        htmlInformationPane.setText("<center>" + text + "</center");
        htmlInformationPane.setPreferredSize(new Dimension((int)(screenSize.width * (0.75)), fontHeight));
        htmlInformationPane.setEditable(false);
        cInset = (cInset - fontHeight)/2;
        c.insets = new Insets((int)cInset, 0, 0,0);
        htmlInformationPane.setForeground(ColourCode);
        htmlInformationPane.setBackground(BackgroundColourCode);
        billboard.add(htmlInformationPane, c);
    }

    public int displayPicture(JFrame billboard, BufferedImage picture, Dimension screenSize, int ylocation, int items, boolean message){
        double buffer = 0.5;
        double weight = 1;
        if(items == 3){
            buffer = 1.0/3.0;
        }

        GridBagConstraints c = new GridBagConstraints();
        if(items == 1){
            c.anchor = GridBagConstraints.CENTER;
        }
        else if(items == 2 && message){
            c.anchor = GridBagConstraints.PAGE_START;
        }
        else if(items == 2){ // info/picture
            c.anchor = GridBagConstraints.CENTER;
        }

        c.gridx = 0;
        c.gridy = ylocation;
        c.weighty = weight;
        BufferedImage resizedPicture = resizePicture(picture, screenSize, buffer);
        JLabel resizedImage = new JLabel(new ImageIcon(resizedPicture));
        System.out.println(resizedImage.getHeight());

        billboard.add(resizedImage, c);
        if(items == 1){
            return 0;
        }
        else if(items == 2){
            return (int)((screenSize.height * (2.0/3.0)) - resizedPicture.getHeight());
        }
        else{ // items == 3
            return (int)((screenSize.height * (1.0/3.0)) - resizedPicture.getHeight());
        }
    }



    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        File file;
        file = new File("./5.xml");
        new BillboardViewer(file);
    }
}
