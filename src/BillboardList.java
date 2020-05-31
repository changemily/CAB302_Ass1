import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans
 * @version - Final
 */
public class BillboardList implements java.io.Serializable {

    //Setup a hashmap for tracking billboards.
    static HashMap<String, Billboard> billboardHashMap;

    //Setup a schedule multimap
    ScheduleMultiMap scheduleMultiMap = new ScheduleMultiMap();

    //constructor that creates HashMap
    BillboardList() {
        billboardHashMap = new HashMap<>();
    }

    //Create a new billboard object
    static Billboard billboardNew;

    /**
     * Method to create and edit billboards
     * @param billboardName  Billboards name
     * @param  billboardCreator The billboard creators name
     * @param xmlFile String xml file
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
     * @param billboardName Return type Billboard
     * @return returns HashMap of billboard names and objects
     */
    public Billboard getBillboardInfo(String billboardName) throws Exception {
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
     * Method for deleting billboards
     * @param billboardName Name of billboard being deleted
     */
    public void deleteBillboard(String billboardName) throws Exception {
        //The code for deleting the billboard info from the schedule.
        try{
            //Get the info from schedule for the billboard
            ArrayList<ScheduleInfo> viewings = scheduleMultiMap.getViewings(billboardName);
            //for each scheduled viewing of the billboard get the info
            for (ScheduleInfo viewing : viewings ) {

                //store schedule info in local vars
                LocalDateTime startTimeScheduled = viewing.startTimeScheduled;
                Duration durationMins = viewing.duration;
                int recurrenceDelay = viewing.recurrenceDelay;
                String billboardCreator = viewing.billboardCreator;

                //create schedule info with viewing details
                ScheduleInfo scheduleInfo = new ScheduleInfo(startTimeScheduled,durationMins, recurrenceDelay, billboardCreator);

                //remove viewing of billboard
                scheduleMultiMap.removeViewing(billboardName, scheduleInfo);
            }
            billboardHashMap.remove(billboardName);
        }catch(Exception e){
            billboardHashMap.remove(billboardName);
        }
    }

    /**
     * Method for retrieving a list of billboards from the database
     * @param connection A connection for accessing the database
     */
    public void retrieveDBbillboardList(Connection connection) throws Exception {

        final String SELECT = "SELECT * FROM Billboards ORDER BY billboardName desc";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(SELECT);

        //for every database entry
        while (rs.next())
        {
            //store database info in local variables
            String billboardName = rs.getString(1);
            String billboardCreator = rs.getString(2);
            String xmlFile = rs.getString(3);
            //create a billboard using the information
            Billboard billboard = new Billboard(billboardName, billboardCreator, xmlFile);
            //store billboard name with corresponding billboard
            billboardHashMap.put(billboardName, billboard);
        }
        rs.close();
        st.close();
    }

    /**
     * Method for clearing a list of billboards in the database
     * @param connection A connection for accessing the database
     */
    public void clearDBbillboardList(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        //for all entries in billboardHashMap

        for (String billboardName : billboardHashMap.keySet())
        {
            //remove each entry from DB using billboard_name
            st.execute("DELETE FROM Billboards WHERE billboardName=\""+billboardName+"\";");
        }
    }

    /**
     * Method for writing an updated list of billboards from the database
     * @param connection A connection for accessing the database
     */
    public void writeToDBbillboard(Connection connection) throws SQLException {
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
        st.close();
    }
}