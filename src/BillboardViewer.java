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
    // Component labels
    private final JLabel messageLabel = new JLabel();
    private final JEditorPane informationPane = new JEditorPane();
    private final JLabel pictureLabel = new JLabel();

    private final String colourBlack = "#000000";

    // Billboard Details
    private Document parsedFile;
    private final Dimension screenSize;
    private int billboardVariation;

    // Billboard Details
    private Color billboardColourCode;

    private String messageText;
    private Color messageColourCode;
    private boolean messageExists;
    private int messageFontHeight;

    private String informationText;
    private Color informationColourCode;
    private boolean informationExists;
    private int informationFontHeight;

    BufferedImage pictureImage;
    BufferedImage resizedPicture;
    private boolean pictureExists;


    /**
     * Constructor creates a GUI on an xml file in full screen
     * @param xml_file (File)
     * @throws ParserConfigurationException (error)
     * @throws IOException (error)
     * @throws SAXException (error)
     * @throws ClassNotFoundException (error)
     * @throws UnsupportedLookAndFeelException (error)
     * @throws InstantiationException (error)
     * @throws IllegalAccessException (error)
     */
    public BillboardViewer(File xml_file) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Find screen size
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Parse File
        parseFile(xml_file);

        //// Find Elements from xml_file
        findBillboardCode(parsedFile);
        // Billboard Details
        findMessageDetails(parsedFile);
        findInformationDetails(parsedFile);
        findPictureDetails(parsedFile);

        // Create frame, and set basic settings
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame billboard = createBillboard();

        billboardVariation = calculateVariation();

        switch(billboardVariation){
            case 1:
                setMessage();
                break;
            case 2:
                setInformation();
                break;
            case 3:
                setPicture();
                break;
            case 4:
                setMessage();
                setInformation();
                break;
            case 5:
                setMessage();
                setPicture();
                break;
            case 6:
                setInformation();
                setPicture();
                break;
            case 7:
                setMessage();
                setInformation();
                setPicture();
                break;
        }

        // Display billboard
        displayAll(billboard);

        // Pack billboard and set visible
        billboard.pack();
        billboard.setExtendedState(MAXIMIZED_BOTH);
        billboard.setVisible(true);
    }

    /**
     * Constructor creates a GUI on an xml file in set size
     * @param xml_file (File)
     * @param size (Dimension)
     * @throws ParserConfigurationException (error)
     * @throws IOException (error)
     * @throws SAXException (error)
     * @throws ClassNotFoundException (error)
     * @throws UnsupportedLookAndFeelException (error)
     * @throws InstantiationException (error)
     * @throws IllegalAccessException (error)
     */
    public BillboardViewer(File xml_file, Dimension size) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        // Find screen size
        screenSize = size;
        // Parse File
        parseFile(xml_file);

        //// Find Elements from xml_file
        findBillboardCode(parsedFile);
        // Billboard Details
        findMessageDetails(parsedFile);
        findInformationDetails(parsedFile);
        findPictureDetails(parsedFile);

        // Create frame, and set basic settings
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame billboard = createBillboard();
        billboard.setPreferredSize(size);

        billboardVariation = calculateVariation();

        switch(billboardVariation){
            case 1:
                setMessage();
                break;
            case 2:
                setInformation();
                break;
            case 3:
                setPicture();
                break;
            case 4:
                setMessage();
                setInformation();
                break;
            case 5:
                setMessage();
                setPicture();
                break;
            case 6:
                setInformation();
                setPicture();
                break;
            case 7:
                setMessage();
                setInformation();
                setPicture();
                break;
        }

        // Display billboard
        displayAll(billboard);

        // Pack billboard and set visible
        billboard.pack();
        billboard.setVisible(true);
    }

    /**
     * Method for parsing the xml_file
     * @param xml_file (File)
     */
    private void parseFile(File xml_file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // create a new instance
        DocumentBuilder build = factory.newDocumentBuilder(); // create a new factory builder
        parsedFile = build.parse(xml_file); // parse xml_file into file and return
    }

    /**
     * Method for determining background colour, if none found, choose white
     * @param file (Document)
     */
    private void findBillboardCode(Document file){
        String billboardColour;
        String colourWhite = "#FFFFFF";

        Element billboardElement = file.getDocumentElement(); // find any document elements
        if(billboardElement.hasAttribute("background")){ // check for background attribute
            billboardColour = billboardElement.getAttribute("background"); // set backgroundColour to attribute
        }
        else{ // no attribute
            // Colour variables
            billboardColour = colourWhite;
        }
        billboardColourCode = Color.decode(billboardColour); // decode colour string to colour code which java swing uses
    }

    /**
     * Method for finding message details, it sets global variables messageText, messageColourCode and messageExists
     * @param file (Document)
     */
    private void findMessageDetails(Document file){
        NodeList message = file.getElementsByTagName("message"); // Find message element
        String messageColour;

        try{ // try to find messageText, if fails, messageExists = false
            messageText = message.item(0).getTextContent();
            messageExists = true;
        } catch (Exception e){
            messageExists = false;
        }

        if(messageExists){ // if messageExists, then try to find messageColour, if fails, set to black
            try {
                messageColour = message.item(0).getAttributes().getNamedItem("colour").getTextContent();
            } catch (Exception e){
                messageColour = colourBlack;
            }
            messageColourCode = Color.decode(messageColour);
        }
    }

    /**
     * Method for finding information details, it sets global variables informationText, informationColourCode and informationExists
     * @param file (Document)
     */
    private void findInformationDetails(Document file){
        NodeList information = file.getElementsByTagName("information"); // Find information element
        String informationColour;

        try{ // try to find informationText, if fails, informationExists = false
            informationText = information.item(0).getTextContent();
            informationExists = true;
        } catch (Exception e){
            informationExists = false;
        }

        if(informationExists){ // if informationExists, then try to find informationColour, if fails, set to black
            try{
                informationColour = information.item(0).getAttributes().getNamedItem("colour").getTextContent();
            } catch (Exception e){
                informationColour = colourBlack;
            }
            informationColourCode = Color.decode(informationColour);
        }
    }

    /**
     * Method for finding picture details, it sets global variables pictureImage and pictureExists
     * @param file (Document)
     */
    private void findPictureDetails(Document file){
        NodeList picture = file.getElementsByTagName("picture"); // Find picture element
        boolean urlExists;

        try{ // try to find urlString, if passes, read url and pictureExists = true, if fails, urlExists = false
            String urlString = picture.item(0).getAttributes().getNamedItem("url").getTextContent();
            URL pictureURL = new URL(urlString);
            pictureImage = ImageIO.read(pictureURL);
            urlExists = true;
            pictureExists = true;
        } catch (Exception e){
            urlExists = false;
        }

        if(!urlExists){ // if !urlExists, try to find dataString, if passes, read data and pictureExists = true, if fails, pictureExists = false
            try{
                String dataString = picture.item(0).getAttributes().getNamedItem("data").getTextContent();
                Base64.Decoder decoder = Base64.getDecoder(); // decodes BASE64 data
                byte[] pictureBytes = decoder.decode(dataString);
                ByteArrayInputStream byteInput = new ByteArrayInputStream(pictureBytes); // creates byte array
                pictureImage = ImageIO.read(byteInput);
                pictureExists = true;
            } catch (Exception e){
                pictureExists = false;
            }
        }
    }

    /**
     * Create a billboard with full screen settings
     * @return (JFrame)
     */
    private JFrame createBillboard(){
        JFrame billboard = new JFrame();
        billboard.setDefaultCloseOperation(EXIT_ON_CLOSE);
        billboard.getContentPane().setBackground(billboardColourCode);
        billboard.setUndecorated(true);
        billboard.setLayout(new BoxLayout(billboard.getContentPane(), BoxLayout.Y_AXIS));
        return billboard;
    }

    /**
     * Calculate which variation of billboard the file is
     * @return (int)
     */
    private int calculateVariation(){
        if(messageExists && !informationExists && !pictureExists) {
            return 1; // message
        }
        else if(!messageExists && informationExists && !pictureExists) {
            return 2; // information
        }
        else if(!messageExists && !informationExists && pictureExists) {
            return 3; // picture
        }
        else if(messageExists && informationExists && !pictureExists) {
            return 4; // message/information
        }
        else if(messageExists && !informationExists){ // picture must be true
            return 5; // message/picture
        }
        else if(!messageExists && informationExists){ // picture must be true
            return 6; // information/picture
        }
        else{ // all true
            return 7; // message/information/picture
        }
    }

    /**
     * Method for setting the maximum font size
     * @param textLabel (JLabel)
     */
    private int setMessageFontSize(JLabel textLabel){
        // Calculate dimensions
        // Percent size limit for message text
        double messageBuffer = 0.95;
        double maxWidth = (screenSize.width * messageBuffer);

        Font textFont = textLabel.getFont(); // Font
        int fontSize = textFont.getSize(); // Font Size
        int stringSize = textLabel.getFontMetrics(textFont).stringWidth(messageText); // Calculate string width

        while(stringSize < maxWidth){ // While stringWidth is within maximum width
            fontSize += 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));

            // Calculate font metrics again
            textFont = textLabel.getFont();
            stringSize = textLabel.getFontMetrics(textFont).stringWidth(messageText);
        }
        return textLabel.getFontMetrics(textFont).getHeight();
    }

    /**
     * Method for finding the maximum font size for informationText
     * @param textLabel (JLabel)
     * @param heightBuffer (double)
     * @return (int)
     */
    private int setInformationFontSize(JLabel textLabel, double heightBuffer){
        // Calculate maximum dimensions
        // Percent size limit for information text
        double informationBuffer = 0.7;
        double maxWidth = (screenSize.width * informationBuffer);
        double maxHeight = (screenSize.height * heightBuffer);

        // Calculate max width if message exists
        // ratio for maximum size of information text compared to message text
        double informationRatio = 0.75;
        double messageMax = messageLabel.getFont().getSize() * informationRatio;

        // Calculate font metrics
        Font textFont = textLabel.getFont(); // Font
        int fontSize = textFont.getSize(); // Font Size
        int fontWidth = textLabel.getFontMetrics(textFont).stringWidth(informationText); // String Width in current font size
        int fontHeight = textLabel.getFontMetrics(textFont).getHeight(); // String Height in current font size
        double fontLines = fontWidth / maxWidth; // Number of lines current font takes up
        double totalHeight= fontHeight * fontLines; // Total height of JEditorPane

        // While JEditorPane is less then max height and if messageExists, fontSize is not >75% of the size of the messageFont
        while(totalHeight < maxHeight && (!messageExists || ((fontSize < messageMax)))){
            fontSize += 1; // add ++
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize)); // set font to new size

            // Calculate font metrics again
            textFont = textLabel.getFont();
            fontWidth = textLabel.getFontMetrics(textFont).stringWidth(informationText);
            fontHeight = textLabel.getFontMetrics(textFont).getHeight();
            fontLines = fontWidth / maxWidth;
            totalHeight = fontHeight * fontLines;
        }

        double stringLineRound = Math.floor(fontLines); // round down current lines of editorPane

        while(fontLines > stringLineRound){ // reduce font size to fit under rounded line count
            fontSize -= 1;
            textLabel.setFont(new Font(textFont.getName(), Font.PLAIN, fontSize));

            // Calculate font metrics again
            textFont = textLabel.getFont();
            fontWidth = textLabel.getFontMetrics(textFont).stringWidth(textLabel.getText());
            fontLines = fontWidth / maxWidth;
        }
        return((int)(fontHeight * stringLineRound));
    }

    /**
     * Method for resizing pictures
     * @param picture (BufferedImage)
     * @return (BufferedImage)
     */
    public BufferedImage resizePicture(BufferedImage picture, double pictureBuffer){
        // Picture size
        int currentHeight = picture.getHeight();
        int currentWidth = picture.getWidth();

        // Picture size ratio
        double currentRatio = (double)currentHeight / (double)currentWidth;

        // Maximum picture sizes given buffer
        // Percent size max limit for pictureImage
        int maxHeight = (int)(screenSize.height * pictureBuffer);
        int maxWidth = (int)(screenSize.width * pictureBuffer);

        int newWidth; int newHeight;

        if(currentRatio >= 1){ // size to maximum height
            double ratioIncrease = (double)maxHeight / (double)currentHeight;
            newHeight = maxHeight;
            newWidth = (int)(currentWidth * ratioIncrease);
        }
        else{ // currentRatio < 1, size to maximum width
            double ratioIncrease = (double)maxWidth / (double)currentWidth;
            newWidth = maxWidth;
            newHeight = (int)(currentHeight * ratioIncrease);
        }

        // Scale new image with new size
        Image tempImage = picture.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tempImage, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }
    /**
     * Method for setting the messageLabel
     */
    private void setMessage(){
        //GridBagConstraints constraints = setConstraints(yLocation, messageWeight);
        // Define messageLabel
        messageLabel.setText(messageText);
        messageLabel.setForeground(messageColourCode);
        messageFontHeight = setMessageFontSize(messageLabel); // set font to fit buffer
    }

    /**
     * Method for setting all variables for the informationPane
     * @param informationLabel (JLabel)
     * @param fontHeight (int)
     * @param informationFont (Font)
     */
    private void setInformationPane(JLabel informationLabel, int fontHeight, Font informationFont){
        informationPane.setContentType("text/html"); // set to HTML
        informationPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE); // set editable display to true
        informationPane.setFont(informationFont); // set to same font as informationLabel, which was used to test maximum size
        informationPane.setText("<center>" + informationLabel.getText() + "</center>");
        // Percent size max limit for information text, taking into account word wrapping
        double informationMaxBuffer = 0.8;
        informationPane.setPreferredSize(new Dimension((int)(screenSize.width * informationMaxBuffer), fontHeight)); // set size to maximum size
        informationPane.setMinimumSize(informationPane.getPreferredSize());
        informationPane.setMaximumSize(informationPane.getPreferredSize());
        informationPane.setEditable(false);
        informationPane.setForeground(informationColourCode);
        informationPane.setBackground(billboardColourCode);
    }

    /**
     * Method for setting the informationPane
     */
    public void setInformation(){
        double heightBuffer; // vertical size limit for info text
        double oneBufferHeight = 1.0 / 2.0;
        double twoBufferHeight = 1.0 / 4.0;
        double threeBufferHeight = 1.0 / 6.0;

        // Calculate height, depending on which other items exist
        // Percent height limit for info/message text, that is 1/3 of screen
        if(!messageExists && !pictureExists){
            // Percent height limit for info/message text, this is full screen
            heightBuffer = oneBufferHeight;
        }
        else if(!pictureExists){
            heightBuffer = twoBufferHeight;
        }
        else if(!messageExists){
            // Percent height limit for info/message text, that is 1/2 of screen
            heightBuffer = threeBufferHeight;
        }
        else{
            heightBuffer = threeBufferHeight;
        }


        // Create an informationLabel that is used to calculate the maximum size that the text can fit
        JLabel informationLabel = new JLabel(informationText); // create an informationLabel which is used to find the maximum fontHeight
        informationFontHeight = setInformationFontSize(informationLabel, heightBuffer); // calculate maximum size of informationFont pane
        Font informationFont =  new Font(informationLabel.getFont().getName(), Font.PLAIN, informationLabel.getFont().getSize());

        // Set variables for informationPane
        setInformationPane(informationLabel, informationFontHeight, informationFont);
    }

    /**
     * Method for setting the pictureLabel
     */
    public void setPicture(){
        double pictureBuffer = 0.5;
        if(billboardVariation == 7){
            pictureBuffer = (1.0/3.0);
        }
        // Resize picture
        resizedPicture = resizePicture(pictureImage, pictureBuffer);

        // Add picture to label and add to JPanel
        pictureLabel.setIcon(new ImageIcon(resizedPicture));
    }

    /**
     * Creates a billboard based of which billboardVariation it is
     * The add.(Box.createRigidArea) is set based upon the specifications
     * @param billboard (JFrame)
     */
    public void displayAll(JFrame billboard){
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        informationPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        pictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        switch(billboardVariation){
            case 1: // Set messageLabel to middle
                billboard.add(Box.createRigidArea(new Dimension(0,(screenSize.height/2) - (messageFontHeight / 2))));
                billboard.add(messageLabel);
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/2) - (messageFontHeight / 2))));
                break;
            case 2: // Set informationPane to middle
                billboard.add(Box.createRigidArea(new Dimension(0,(screenSize.height/2) - (informationFontHeight / 2))));
                billboard.add(informationPane);
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/2) - (informationFontHeight / 2))));
                break;
            case 3: // Set pictureLabel to middle
                billboard.add(Box.createRigidArea(new Dimension(0,(screenSize.height/2) - (pictureImage.getHeight() / 2))));
                billboard.add(pictureLabel);
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/2) - (pictureImage.getHeight() / 2))));
                break;
            case 4: // Set messageLabel to middle of top half, informationPane to middle of top half
                billboard.add(Box.createRigidArea(new Dimension(0,(screenSize.height/4) - (messageFontHeight / 2))));
                billboard.add(messageLabel);
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/4) - (messageFontHeight / 2))));
                billboard.add(Box.createRigidArea(new Dimension(0,(screenSize.height/4) - (informationFontHeight / 2))));
                billboard.add(informationPane);
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/4) - (informationFontHeight / 2))));
                break;
            case 5: // Set pictureLabel to middle of bottom 2/3rds, messageLabel to middle of rest
                billboard.add(Box.createRigidArea(new Dimension(0,(2*(screenSize.height/3) - (resizedPicture.getHeight() / 2))/2 - (messageFontHeight / 2))));
                billboard.add(messageLabel);
                billboard.add(Box.createRigidArea(new Dimension(0,(2*(screenSize.height/3) - (resizedPicture.getHeight() / 2))/2 - (messageFontHeight / 2))));
                billboard.add(pictureLabel);
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/3) - (resizedPicture.getHeight() / 2))));
                break;
            case 6: // Set pictureLabel to middle of top 2/3rds, informationPane to middle of rest
                billboard.add(Box.createRigidArea(new Dimension(0, (screenSize.height/3) - (resizedPicture.getHeight() / 2))));
                billboard.add(pictureLabel);
                billboard.add(Box.createRigidArea(new Dimension(0,(2*(screenSize.height/3) - (resizedPicture.getHeight() / 2))/2 - (informationFontHeight / 2))));
                billboard.add(informationPane);
                billboard.add(Box.createRigidArea(new Dimension(0,(2*(screenSize.height/3) - (resizedPicture.getHeight() / 2))/2 - (informationFontHeight / 2))));
                break;
            case 7: // Set picture to middle of middle 1/3rds, pictureLabel to middle of top section, informationPane to middle of bottom section
                billboard.add(Box.createRigidArea(new Dimension(0,((screenSize.height/2) - (resizedPicture.getHeight() / 2))/2 - (messageFontHeight / 2))));
                billboard.add(messageLabel);
                billboard.add(Box.createRigidArea(new Dimension(0,((screenSize.height/2) - (resizedPicture.getHeight() / 2))/2 - (messageFontHeight / 2))));
                billboard.add(pictureLabel);
                billboard.add(Box.createRigidArea(new Dimension(0,((screenSize.height/2) - (resizedPicture.getHeight() / 2))/2 - (informationFontHeight / 2))));
                billboard.add(informationPane);
                billboard.add(Box.createRigidArea(new Dimension(0,((screenSize.height/2) - (resizedPicture.getHeight() / 2))/2 - (informationFontHeight / 2))));
                System.out.println(informationText);
                break;
        }

    }



    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        File file;
        file = new File("./10.xml");
        new BillboardViewer(file);
        //new BillboardViewer(file, new Dimension(1000,1000));
    }
}
