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
        // Parse File
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = factory.newDocumentBuilder();
        Document file = build.parse(xml_file);

        // Get elements from Billboard tag
        Element billboard = file.getDocumentElement(); // get any document elements
        String billboardColour = "#FFFFFF"; // set default to white

        // Check for background attribute, if there, set colour to that attribute
        if (billboard.hasAttribute("background")){
            billboardColour = billboard.getAttribute("background");
        }
        Color billboardColourNumber = Color.decode(billboardColour); // decode hashcode

//        NodeList information = file.getElementsByTagName("message");
//        try{
//            String information_text = information.item(0).getFirstChild().getTextContent();
//            System.out.println(information.item(0).getFirstChild().getTextContent());
//        } catch (Exception e){
//            // null
//        }

        // Create frame
        JFrame background = new JFrame("background");

        // Set basic settings for frame; Windows Look, Close on Exit, Billboard Colour, Full Screen, Undecorated, Pack, Set Visible
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        background.setDefaultCloseOperation(EXIT_ON_CLOSE);
        background.getContentPane().setBackground(billboardColourNumber);
        background.setExtendedState(MAXIMIZED_BOTH);
        background.setUndecorated(true);
        background.pack();
        background.setVisible(true);
    }



    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        File file;
        file = new File("./15.xml");
        new BillboardViewer(file);
    }
}
