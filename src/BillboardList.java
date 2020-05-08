import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.io.File;
import java.io.IOException;
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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans
 * @version - under development
 */
public class BillboardList {

    //Setup a hashmap for tracking billboards.
    HashMap<String, Billboard> billboardHashMap;

    //Setup a schedule multimap
    scheduleMultiMap scheduleMultiMap = new scheduleMultiMap();

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
     * @param schedule_time   time (optional):void
     * @param durationMinutes The duration in minutes it will be displayed
     */
    

    public void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image,
                               LocalDateTime schedule_time, Duration durationMinutes,String recurrence) throws Exception {
        //Create a new billboard object
        billboardNew = new Billboard(billboard_name, text,
                bg_colour, image, schedule_time, durationMinutes);

        //put billboard in HashMap - value will be replaced if key exists in HashMap
        billboardHashMap.put(billboard_name, billboardNew);

        //add schedule info of bb to schedule multi map
        this.scheduleMultiMap.scheduleBillboard(billboard_name,schedule_time, durationMinutes,recurrence, billboardHashMap);
    }

    //For creating and editing billboards without the optional parameters.
    public void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image) {
        //Create a new billboard object
        billboardNew = new Billboard(billboard_name, text,
                bg_colour, image);

        //put billboard in HashMap - value will be replaced if key exists in HashMap
        billboardHashMap.put(billboard_name, billboardNew);
    }

    /**
     * Method for listing current billboards
     * Return type void
     */

    public HashMap<String, Billboard> List_Billboards() {
        return billboardHashMap;
    }

    /**
     * Method for retrieving billboard information from database
     *
     * @param billboard_name Return type Billboard
     * @return
     */

    public Billboard Get_billboard_info(String billboard_name) throws Exception {
        //boolean variable to track whether billboard exists in schedule
        boolean billboard_exists = false;
        Billboard billboard_info = null;

        //For every entry of billboard list
        for (String BillboardName : billboardHashMap.keySet())
        {
            //if given billboard name matches billboard name in schedule
            if (BillboardName.equals(billboard_name))
            {
                //store viewings of billboard in singleBBschedule collection
                billboard_info = billboardHashMap.get(billboard_name);
                billboard_exists = true;
                break;
            }
        }

        //if billboard is not scheduled
        if (billboard_exists == false)
        {
            throw new Exception("The billboard does not exist in the schedule");
        }

        //return billboard
        return billboard_info;
    }

    /**
     * @param billboard_name Name of billboard being deleted
     */

    public void Delete_billboard(String billboard_name) throws Exception {

        //The code for deleting the billboard info from the schedule.

        //Get the info from schedule for the billboard
        ArrayList<Schedule_Info> viewings = scheduleMultiMap.getSchedule(billboard_name);

        //for each scheduled viewing of the billboard
        for (Schedule_Info viewing : viewings ) {

            //store schedule info in local vars
            LocalDateTime startTime_scheduled = viewing.StartTime_Scheduled;
            Duration duration_mins = viewing.duration;
            String recurrence = viewing.Recurrence;

            //create schedule info with viewing details
            Schedule_Info Schedule_info = new Schedule_Info(startTime_scheduled,duration_mins,recurrence);

            //remove viewing of billboard
            this.scheduleMultiMap.Schedule_Remove_billboard(billboard_name, Schedule_info);
        }

        //The code for removing the billboard info from the billboardList.
        billboardHashMap.remove(billboard_name);
    }

    //A method that edits a users XML file
    public void editXMLFile(String xmlFile, String billboardName, String billboardText,
                            String backgroundColour, String billboardImage) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File("xmlFile"));

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
        StreamResult stream = new StreamResult(new File("xmlFile"));
        transFormer.transform(dom, stream);
    }

}