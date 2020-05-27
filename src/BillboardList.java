import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans
 * @version - under development
 */
public class BillboardList implements java.io.Serializable {

    //Setup a hashmap for tracking billboards.
    static HashMap<String, Billboard> billboardHashMap;

    //Setup a schedule multimap
    ScheduleMultiMap scheduleMultiMap = new ScheduleMultiMap();

    //constructor that creates HashMap
    BillboardList() {
        billboardHashMap = new HashMap<>(); // use tree map to sort by key
    }

    //Create a new billboard object
    static Billboard billboardNew;

    /**
     * Method to create and edit billboards
     * Parameters -
     *
     * @param billboard_name  Billboards name
     * @param text            Text on the billboard
     * @param bg_colour       Background colour of the billboard
     * @param image           (optional)
     */


    public void createEditBillboard(String billboard_name, String text, String bg_colour, String image,
                                    String billboard_creator) throws Exception {
        //Check if image exists
        boolean exists = Files.exists(Paths.get(image));
        //Check if bg_colour is valid
        Color bgColour = SystemColor.decode(bg_colour);

        //Check for issues before creating the billboard.
        if(image == ""){
            throw new Exception("There was no image inputted. Please specify an image or opt for 'No Image'.");
        }
        else if (!image.equals("No Image") && exists == false)
        {
            throw new Exception ("The image does not exist. Please input a valid image.");
        }else if (bgColour == null) {
            throw new Exception ("The background colour entered is invalid. Please enter a valid colour.");
        }else{
            //Create a new billboard object
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image, billboard_creator);

            //put billboard in HashMap - value will be replaced if key exists in HashMap
            billboardHashMap.put(billboard_name, billboardNew);
        }
    }


    /**
     * Method for listing current billboards
     * Return type void
     */

    public HashMap<String, Billboard> listBillboards() {
        return billboardHashMap;
    }

    /**
     * Method for retrieving billboard information from database
     *
     * @param billboard_name Return type Billboard
     * @return returns HashMap of billboard names and objects
     */

    public Billboard Get_billboard_info(String billboard_name) throws Exception {
        //boolean variable to track whether billboard exists in schedule
        boolean billboard_exists = false;
        Billboard billboard_info = null;

        //For every entry of billboard list
        for (String BillboardName : billboardHashMap.keySet())
        {
            //if given billboard name matches billboard name in billboard list
            if (BillboardName.equals(billboard_name))
            {
                //store billboard info in local variable
                billboard_info = billboardHashMap.get(billboard_name);
                billboard_exists = true;
                break;
            }
        }

        //if billboard is not in list
        if (billboard_exists == false)
        {
            throw new Exception("The billboard does not exist in the billboard list");
        }

        //return billboard info
        return billboard_info;
    }

    /**
     * @param billboard_name Name of billboard being deleted
     */

    public void Delete_billboard(String billboard_name) throws Exception {

        //The code for deleting the billboard info from the schedule.

        //Get the info from schedule for the billboard
        ArrayList<ScheduleInfo> viewings = scheduleMultiMap.getSchedule(billboard_name);

        //for each scheduled viewing of the billboard
        for (ScheduleInfo viewing : viewings ) {

            //store schedule info in local vars
            LocalDateTime startTime_scheduled = viewing.startTimeScheduled;
            Duration duration_mins = viewing.duration;
            int recurrence_delay = viewing.recurrenceDelay;
            String billboard_creator = viewing.billboardCreator;

            //create schedule info with viewing details
            ScheduleInfo Schedule_info = new ScheduleInfo(startTime_scheduled,duration_mins, recurrence_delay, billboard_creator);

            //remove viewing of billboard
            scheduleMultiMap.scheduleRemoveBillboard(billboard_name, Schedule_info);
        }

        //The code for removing the billboard info from the billboardList.
        billboardHashMap.remove(billboard_name);
    }

    public void RetrieveDBbillboardList(Connection connection) throws Exception {

        final String SELECT = "SELECT * FROM Billboards ORDER BY billboard_name desc";


        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //for every database entry
        while (rs.next())
        {
            //store database info in local variables
            String billboard_name = rs.getString(1);
            String text = rs.getString(2);
            String bg_colour = rs.getString(3);
            String image_file = rs.getString(4);
            String billboard_creator = rs.getString(5);
           /* String time_scheduled = rs.getString(5);
            String Duration_mins = rs.getString(6);*/

            //create a billboard using the information
            /*Billboard billboard = new Billboard(billboard_name, text, bg_colour,
                    image_file, LocalDateTime.parse(time_scheduled), Duration.parse(Duration_mins));*/
            Billboard billboard = new Billboard(billboard_name, text, bg_colour,
                    image_file, billboard_creator);

            //store billboard name with corresponding billboard
            billboardHashMap.put(billboard_name, billboard);
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();
    }

    public void Clear_DBbillboardList(Connection connection) throws SQLException {
        //create statement to connect to db
        Statement st = connection.createStatement();

        //for all entries in billboardHashMap
        for (String billboard_name : billboardHashMap.keySet())
        {
            //remove each entry from DB using billboard_name
            st.execute("DELETE FROM Billboards WHERE billboard_name=\""+billboard_name+"\";");
        }
    }

    //A method for writing the billboardList to the database
    public void Write_To_DBbillboard(Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for every billboard name in billboardHashMap
        for (Billboard billboard : billboardHashMap.values() ) {

            //Pass the values of each billboard to the SQL statement.
            String billboard_name = billboard.BillboardName;
            String text = billboard.BillboardText;
            String bg_colour = billboard.BgColour;
            String image_file = billboard.ImageFile;
            String billboard_creator = billboard.BillboardCreator;

            st.executeQuery("INSERT INTO Billboards (billboard_name, text, bg_colour, image_file, billboard_creator) " +
                    "VALUES(\""+billboard_name+"\",\""+text+"\",\""+bg_colour+"\",\""+image_file+"\",\""+billboard_creator+"\");");
        }

        //close statement
        st.close();
    }

    //A method that edits a users XML file
    public void editXMLFile(String xmlFile, String billboardName, String billboardText,
                            String backgroundColour, String billboardImage) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(xmlFile));

        //Update the Billboard name
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node billboard = (Node) xPath.compile("/billboard/billboardName").evaluate(document, XPathConstants.NODE);
        billboard.setTextContent(billboardName);

        //Update

        //Save the changes back to the XML file
        Transformer transFormer = TransformerFactory.newInstance().newTransformer();
        transFormer.setOutputProperty(OutputKeys.INDENT, "yes");
        transFormer.setOutputProperty(OutputKeys.METHOD, "xml");

        DOMSource dom = new DOMSource(document);
        StreamResult stream = new StreamResult(new File(xmlFile));
        transFormer.transform(dom, stream);
    }


    //A method that accepts an xml file and creates a billboard using it
    public void importXML(String xmlFile) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(xmlFile);

        //Use information in XNL to create a billboard.
        //Get access to the XML files nodes and elements
        NodeList listNodes = document.getElementsByTagName("billboard");
        Node node = listNodes.item(0);
        Element a = (Element) node;

        //Getting the billboard Name
        String BillboardName = xmlFile;

        //Getting the billboard Text
        String text = String.valueOf((Element) a.getElementsByTagName("billboard").item(0));

        //Getting the billboard bg Colour
        String bgColour = ((Element) node).getAttribute("billboard");

        //Getting the billboard image File
        String imageFile = String.valueOf((Element) a.getElementsByTagName("billboard").item(1));

        //Setting a default billboard time Scheduled
        LocalDateTime timeSchedule = null;

        //Setting a default billboard Duration in minutes
        Duration durationMinutes = Duration.ofMinutes(5);

        //Setting the recurrence to a default empty
        String recurrence = "none";

        //Billboard creator
        String billboard_creator = "emily";

        //Use the specs retrieved from the XML to create the billboard
        createEditBillboard(BillboardName, text, bgColour, imageFile, billboard_creator);
    }


    //A method that accepts an xml file and creates a billboard using it
    public void exportXML(String billboardName) throws Exception {
        //Use the billboard name provided to find the correct info
        //to store in the xml file
        //Find billboard info and store it in a billboard
        Billboard billboardDetails = Get_billboard_info(billboardName);

        //Create an xml file to store the information in.
        String xmlFile = billboardDetails.BillboardName;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        //Store the billboard name as the file name.
        Document document = documentBuilder.parse(new File(xmlFile));

        //Store the text of the billboard in the xml file.
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node billboardText = (Node) xPath.compile("/billboard/message").evaluate(document, XPathConstants.NODE);
        billboardText.setTextContent(billboardDetails.BillboardText);

        //Store the bg colour of the billboard in the xml file.
        Element bgColour = (Element) document.getElementsByTagName("billboard").item(0);
        bgColour.setAttribute("background", billboardDetails.BgColour);

        //Store the text of the billboard in the xml file.
        xPath = XPathFactory.newInstance().newXPath();
        Node billboardImage = (Node) xPath.compile("/billboard/picture").evaluate(document, XPathConstants.NODE);
        billboardImage.setTextContent(billboardDetails.ImageFile);
    }
}