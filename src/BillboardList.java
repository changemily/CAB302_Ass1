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

import org.w3c.dom.*;
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
     * @param billboardName  Billboards name
     * @param  billboardCreator The billboard creators name
     * @param xmlFile
     */
    public void createEditBillboard(String billboardName, String billboardCreator, String xmlFile) throws Exception {
            //Create a new billboard object
            billboardNew = new Billboard(billboardName, billboardCreator, xmlFile);

            //put billboard in HashMap - value will be replaced if key exists in HashMap
            billboardHashMap.put(billboardName, billboardNew);
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
     * @param billboardName Return type Billboard
     * @return returns HashMap of billboard names and objects
     */

    public Billboard GetBillboardInfo(String billboardName) throws Exception {
        //boolean variable to track whether billboard exists in schedule
        boolean billboardExists = false;
        Billboard billboardInfo = null;

        //For every entry of billboard list
        for (String BillboardName : billboardHashMap.keySet())
        {
            //if given billboard name matches billboard name in billboard list
            if (BillboardName.equals(billboardName))
            {
                //store billboard info in local variable
                billboardInfo = billboardHashMap.get(billboardName);
                billboardExists = true;
                break;
            }
        }

        //if billboard is not in list
        if (billboardExists == false)
        {
            throw new Exception("The billboard does not exist in the billboard list");
        }

        //return billboard info
        return billboardInfo;
    }

    /**
     * @param billboardName Name of billboard being deleted
     */

    public void DeleteBillboard(String billboardName) throws Exception {

        //The code for deleting the billboard info from the schedule.

        //Get the info from schedule for the billboard
        ArrayList<ScheduleInfo> viewings = scheduleMultiMap.getSchedule(billboardName);

        //for each scheduled viewing of the billboard
        for (ScheduleInfo viewing : viewings ) {

            //store schedule info in local vars
            LocalDateTime startTimeScheduled = viewing.startTimeScheduled;
            Duration durationMins = viewing.duration;
            int recurrenceDelay = viewing.recurrenceDelay;
            String billboardCreator = viewing.billboardCreator;

            //create schedule info with viewing details
            ScheduleInfo scheduleInfo = new ScheduleInfo(startTimeScheduled,durationMins, recurrenceDelay, billboardCreator);

            //remove viewing of billboard
            scheduleMultiMap.scheduleRemoveBillboard(billboardName, scheduleInfo);
        }

        //The code for removing the billboard info from the billboardList.
        billboardHashMap.remove(billboardName);
    }

    public void RetrieveDBbillboardList(Connection connection) throws Exception {

        final String SELECT = "SELECT * FROM Billboards ORDER BY billboardName desc";


        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //for every database entry
        while (rs.next())
        {
            //store database info in local variables
            String billboardName = rs.getString(1);
            String billboardCreator = rs.getString(2);
            String xmlFile = rs.getString(3);
           /* String time_scheduled = rs.getString(5);
            String Duration_mins = rs.getString(6);*/

            //create a billboard using the information
            /*Billboard billboard = new Billboard(billboardName, text, bg_colour,
                    image_file, LocalDateTime.parse(time_scheduled), Duration.parse(Duration_mins));*/
            Billboard billboard = new Billboard(billboardName, billboardCreator, xmlFile);

            //store billboard name with corresponding billboard
            billboardHashMap.put(billboardName, billboard);
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();
    }

    public void ClearDBbillboardList(Connection connection) throws SQLException {
        //create statement to connect to db
        Statement st = connection.createStatement();

        //for all entries in billboardHashMap
        for (String billboardName : billboardHashMap.keySet())
        {
            //remove each entry from DB using billboard_name
            st.execute("DELETE FROM Billboards WHERE billboardName=\""+billboardName+"\";");
        }
    }

    //A method for writing the billboardList to the database
    public void WriteToDBbillboard(Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for every billboard name in billboardHashMap
        for (Billboard billboard : billboardHashMap.values() ) {

            //Pass the values of each billboard to the SQL statement.
            String billboardName = billboard.BillboardName;
            String billboardCreator = billboard.BillboardCreator;
            String xmlFile = billboard.XMLFile;

            st.executeQuery("INSERT INTO Billboards (billboardName, billboardCreator, xmlFile) " +
                    "VALUES(\""+billboardName+"\",\""+billboardCreator+"\",\""+xmlFile+"\");");
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
        String billboard_creator = "jarod";

        //Use the specs retrieved from the XML to create the billboard
        //createEditBillboard(BillboardName, text, bgColour, imageFile, billboard_creator);
    }


    //A method that accepts an xml file and creates a billboard using it
    public Document exportXML(String billboardName) throws Exception {
        //Use the billboard name provided to find the correct info
        //to store in the xml file
        //Find billboard info and store it in a billboard
        Billboard billboardDetails = GetBillboardInfo(billboardName);

        //Create an xml file to store the information in.
        String xmlFile = billboardDetails.XMLFile;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        //Store the billboard name as the file name.
        Document document = documentBuilder.parse(new File(xmlFile));

        return document;
    }
}