import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * scheduleMultiMap Class
 * This class contains methods that populate the billboard schedule, write the schedule to the database,
 * retrieve schedule info and manipulate the multimap that stores the billboard schedule
 * @author — Emily Chang
 * @version - complete
 */
public class ScheduleMultiMap {

    //
    MultiMap<String, ScheduleInfo> scheduleMultiMap;

    /**
     * constructor that creates a scheduleMultiMap object
     */
    public ScheduleMultiMap() {
        scheduleMultiMap = new MultiMap<>();
    }

    /**
     * Retrieves schedule data from database and stores it in scheduleMultiMap
     * @param connection Database Connection
     * @throws SQLException throws exception if SQL query is invalid, billboard does not exist or combination of billboard &
     * schedule information does not exist
     */
    public void retrieveDBschedule(Connection connection) throws Exception {

        final String SELECT = "SELECT * FROM schedule ORDER BY startTimeScheduled desc";

        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //for every database entry
        while (rs.next())
        {
            //store database info in local variables
            String billboardName = rs.getString(1);
            String startTimeScheduled = rs.getString(2);
            String duration = rs.getString(3);
            String recurrence = rs.getString(4);
            String billboardCreator = rs.getString(5);

            //store time scheduled and duration pair in array schedule_info
            ScheduleInfo schedule_info = new ScheduleInfo(LocalDateTime.parse(startTimeScheduled),
                    Duration.parse(duration), Integer.parseInt(recurrence), billboardCreator);

            //store billboard name with corresponding times scheduled and durations
            scheduleMultiMap.put(billboardName, schedule_info);
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();
    }

    /**
     * Clears Schedule table of database
     * @param connection Database connection
     * @throws SQLException throws exception if SQL query is invalid
     */
    public void clearDBschedule(Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for all entries in schedule
        for (String billboardName : scheduleMultiMap.keySet())
        {
            //remove entry from DB
            st.execute("DELETE FROM Schedule WHERE billboardName=\""+billboardName+"\";");
        }
    }

    /**
     * Writes schedule data in scheduleMultiMap to database
     * @param connection Database Connection
     * @throws SQLException throws exception if SQL query is invalid
     */
    public void writeToDbschedule(Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for every billboard name in Billboard_schedule
        for (String billboardName : scheduleMultiMap.keySet() ) {

            //create array list to store viewings of billboard
            ArrayList<ScheduleInfo> viewings = scheduleMultiMap.get(billboardName);

            //for every viewing of billboard
            for ( ScheduleInfo viewing : viewings ) {

                //store scheduleMultiMap info in local variables
                String startTimeScheduled = viewing.startTimeScheduled.toString();
                String duration = viewing.duration.toString();
                String recurrenceDelay = String.valueOf(viewing.recurrenceDelay);
                String billboardCreator = viewing.billboardCreator;

                //execute SQL query
                st.executeQuery("INSERT INTO Schedule (billboardName,startTimeScheduled, Duration,recurrenceDelay, billboardCreator) " +
                        "VALUES(\""+billboardName+"\",\""+startTimeScheduled+"\",\""+duration+"\",\""+recurrenceDelay+"\",\""+billboardCreator+"\");");
            }
        }

        //close statement
        st.close();
    }

    /**
     * Lists billboards that have been scheduled
     * @return MultiMap containing billboard name and an array list storing schedule info
     */

    public MultiMap<String, ScheduleInfo> viewSchedule()
    {
        return scheduleMultiMap;
    }

    /**
     * Schedules billboards and accounts for overlapping viewings
     * @param newBillboardName name of billboard being scheduled
     * @param newBBStartTime Time (date) Billboard is scheduled for showing
     * @param newBBDuration Duration (minutes) Billboard is displayed for
     * @param recurrenceDelay recurrence delay of billboard being scheduled
     * @param billboardList list that contains all billboards created
     * @param billboardCreator creator of billboard
     * @throws Exception if Billboard does not exist & if duration is out of range or the time scheduled is in the past
     */
    public void scheduleBillboard(String newBillboardName, LocalDateTime newBBStartTime, Duration newBBDuration,
                                  int recurrenceDelay, HashMap<String, Billboard> billboardList, String billboardCreator) throws Exception{

        //if the time scheduled is in the past
        if (newBBStartTime.isBefore(LocalDateTime.now()))
        {
            //throw exception
            throw new Exception("Time scheduled must be after"+ LocalDateTime.now());
        }

        //boolean variable to track whether billboard is in billboard list
        boolean billboardExists = false;

        //For every billboard in billboardList
        for (Map.Entry<String, Billboard> billboardListEntry : billboardList.entrySet()){

            //if billboard name is in billboard list
            if(billboardListEntry.getKey().equals(newBillboardName))
            {
                billboardExists = true;
                break;
            }
        }

        //if billboard does not exist in billboardList
        if(billboardExists == false)
        {
            throw new Exception("You cannot schedule a billboard that does not exist");
        }

        //if Billboard_schedule is empty
        if(scheduleMultiMap.isEmpty())
        {
            //create schedule info for billboard
            ScheduleInfo scheduleInfo = new ScheduleInfo(newBBStartTime, newBBDuration, recurrenceDelay, billboardCreator);

            //add billboard to schedule
            scheduleMultiMap.put(newBillboardName, scheduleInfo);
        }

        else
        {
            outerloop:
            //For every existing billboard of scheduleMultiMap
            for (String existingBillboard : new ArrayList<String>(scheduleMultiMap.keySet()))
            {
                //calculate the end time of new billboard viewing
                LocalDateTime newBBEndTime= newBBStartTime.plus(newBBDuration);

                //create collection to store viewings of billboard
                Collection<ScheduleInfo>viewings = scheduleMultiMap.get(existingBillboard);

                //for every viewing of billboard
                for ( ScheduleInfo viewing : viewings ) {

                    //store viewing info of existing billboard in local variables
                    Duration existBBDuration = viewing.duration;
                    LocalDateTime existBBStartTime = viewing.startTimeScheduled;

                    //get recurrence and creator of existing bb
                    int existBBRecurrence = viewing.recurrenceDelay;
                    String existBBCreator = viewing.billboardCreator;

                    //calculate end time of new billboard viewing
                    LocalDateTime existBBEndTime = existBBStartTime.plus(existBBDuration);

                    //---------------------------------------------------------------------------------------
                    // OVERLAPPING BILLBOARDS
                    //check if new billboard start time is after existing billboard start time
                    boolean startIsAfter = newBBStartTime.isAfter(existBBStartTime);

                    //check if new billboard start time is before existing billboard end time
                    boolean startIsBefore = newBBStartTime.isBefore(existBBEndTime);

                    //check if new billboard start time is between existing billboard start and end time
                    boolean startIsBetween = startIsAfter && startIsBefore;

                    //check if new billboard start time is equal to existing billboard start time
                    boolean startTimesEqual = (newBBStartTime.isEqual(existBBStartTime));


                    //check if new billboard end time is after existing billboard start time
                    boolean endtIsAfter = newBBEndTime.isAfter(existBBStartTime);

                    //check if new billboard end time is before existing billboard end time
                    boolean endIsBefore = newBBEndTime.isBefore(existBBEndTime);

                    //check if new billboard start time is between existing billboard start and end time
                    boolean endIsBetween = endtIsAfter && endIsBefore;

                    //check if new billboard start time is equal to existing billboard start time
                    boolean endTimesEqual = (newBBEndTime.isEqual(existBBEndTime));


                    //check if existing billboard start time is after new billboard start time
                    boolean existStartIsAfter = existBBStartTime.isAfter(newBBStartTime);

                    //check if existing billboard start time is before new billboard end time
                    boolean existStartIsBefore = existBBStartTime.isBefore(newBBEndTime);

                    //check if new billboard start time is between existing billboard start and end time
                    boolean existStartIsBetween = existStartIsAfter && existStartIsBefore;


                    //check if existing billboard end time is after new billboard start time
                    boolean existEndIsAfter = existBBEndTime.isAfter(newBBStartTime);

                    //check if existing billboard end time is before new billboard end time
                    boolean existEndIsBefore = existBBEndTime.isBefore(newBBEndTime);

                    //check if new billboard start time is between existing billboard start and end time
                    boolean existEndIsBetween = existEndIsAfter && existEndIsBefore;

                    // OVERLAP MATCHING BILLBOARDS
                    //if new and existing schedules match OR if new viewing fully overlaps existing
                    if((existStartIsBetween || startTimesEqual) == true && (existEndIsBetween || endTimesEqual == true))
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove entry from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else{
                            //remove existing viewing from schedule
                            scheduleMultiMap.remove(existingBillboard,viewing);
                        }

                    }

                    // SPLIT EXISTING SCHEDULE - NEW START AND END TIME DURING EXISTING VIEWING
                    //if new end time is during existing viewing and new start and end times do not match existing
                    else if ((startIsBetween == true && endIsBetween == true) && (startTimesEqual == false && endTimesEqual == false))
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove entry from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else{
                            //remove existing viewing from schedule
                            scheduleMultiMap.remove(existingBillboard,viewing);
                        }

                        //calculate the new durations of existing billboard split viewing
                        Duration newDuration1 = Duration.between(existBBStartTime, newBBStartTime);
                        Duration newDuration2 = Duration.between(newBBEndTime, existBBEndTime);

                        //create new scheduleInfo object for existing billboard
                        ScheduleInfo newScheduleInfo1 = new ScheduleInfo(existBBStartTime,newDuration1, existBBRecurrence, existBBCreator);
                        ScheduleInfo newScheduleInfo2 = new ScheduleInfo(newBBEndTime,newDuration2, existBBRecurrence, existBBCreator);

                        //reschedule existing billboard for new times
                        scheduleMultiMap.put(existingBillboard, newScheduleInfo1);
                        scheduleMultiMap.put(existingBillboard, newScheduleInfo2);
                    }

                    // NEW START TIME DURING EXISTING VIEWING
                    //if new start time is during existing viewing
                    else if(startIsBetween == true)
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove entry from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else{
                            //remove existing viewing from schedule
                            scheduleMultiMap.remove(existingBillboard,viewing);
                        }

                        //calculate new duration of existing billboard

                        Duration newDuration = Duration.between(existBBStartTime, newBBStartTime);

                        //create new scheduleInfo object for existing billboard
                        ScheduleInfo newScheduleInfo = new ScheduleInfo(existBBStartTime,newDuration, existBBRecurrence, existBBCreator);

                        //reschedule existing billboard for new time
                        scheduleMultiMap.put(existingBillboard, newScheduleInfo);
                    }

                    // NEW END TIME DURING EXISTING VIEWING
                    //if new end time is during existing viewing
                    else if (endIsBetween == true)
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove entry from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else{
                            //remove existing viewing from schedule
                            scheduleMultiMap.remove(existingBillboard,viewing);
                        }

                        //calculate new duration of existing billboard
                        Duration newDuration = Duration.between(newBBEndTime, existBBEndTime);

                        //create new scheduleInfo object for existing billboard
                        ScheduleInfo newScheduleInfo = new ScheduleInfo(newBBEndTime,newDuration, existBBRecurrence, existBBCreator);

                        //reschedule existing billboard for new time
                        scheduleMultiMap.put(existingBillboard, newScheduleInfo);
                    }
                }
            }

            //Schedule new billboard
            //create schedule info for billboard
            ScheduleInfo scheduleInfo = new ScheduleInfo(newBBStartTime, newBBDuration, recurrenceDelay, billboardCreator);

            //add viewing to schedule
            scheduleMultiMap.put(newBillboardName, scheduleInfo);
        }
    }

    /**
     * Removes viewing from Billboard schedule multimap
     * @param billboardName Name of billboard being removed from schedule
     * @param scheduleInfo info of scheduled viewing being removed
     * @throws Exception throws exception if the billboard does not exist in the schedule & if the
     */

    public void scheduleRemoveBillboard(String billboardName, ScheduleInfo scheduleInfo) throws Exception
    {
        //boolean variable to track whether billboard is in schedule
        boolean billboardExists = false;

        //boolean variable to track whether given viewing is in schedule
        boolean viewingExists = false;

        outerloop:
        //For every entry of scheduleMultiMap
        for (String BillboardName : scheduleMultiMap.keySet())
        {
            //create collection to store viewings of billboard
            Collection<ScheduleInfo>viewings = scheduleMultiMap.get(billboardName);

            //for every viewing of billboard
            for ( ScheduleInfo viewing : viewings ) {

                //if billboard name is listed in schedule
                if(BillboardName.equals(billboardName))
                {
                    billboardExists = true;

                    //if number of viewings is 1
                    if (viewings.size() == 1)
                    {
                        viewingExists = true;
                        //remove entry from schedule
                        scheduleMultiMap.remove(billboardName);
                        break outerloop;
                    }

                    //if combination of billboard name, and schedule info is listed in schedule
                    else if(viewing.startTimeScheduled.equals(scheduleInfo.startTimeScheduled))
                    {
                        viewingExists = true;

                        //remove scheduled viewing from Billboard key
                        scheduleMultiMap.remove(billboardName,viewing);
                        break outerloop;
                    }
                }
            }
        }

        //if billboard name is not listed in schedule
        if(billboardExists == false)
        {
            //throw exception
            throw new Exception("The billboard does not exist in the schedule");
        }

        //else if combination of billboard name, and schedule info is not in schedule
        else if(viewingExists == false)
        {
            //throw exception
            throw new Exception("The given schedule information for " + billboardName +
                    " does not exist");
        }
    }

    /**
     * Returns array list of scheduled viewings for the given billboard
     * @param billboardName name of the billboard schedule information is being retrieved from
     * @return an array list of the schedule information for each of the billboard's viewing
     */

    public ArrayList<ScheduleInfo> getSchedule(String billboardName) throws Exception
    {
        //array list to store scheduled times of single billboard
        ArrayList<ScheduleInfo> singleBBschedule = new ArrayList<>();

        //boolean variable to track whether billboard exists in schedule
        boolean billboardExists = false;

        //For every entry of scheduleMultiMap
        for (String BillboardName : scheduleMultiMap.keySet())
        {
            //if given billboard name matches billboard name in schedule
            if (BillboardName.equals(billboardName))
            {
                //store viewings of billboard in singleBBschedule collection
                singleBBschedule = scheduleMultiMap.get(billboardName);
                billboardExists = true;
                break;
            }
        }

        //if billboard is not scheduled
        if (billboardExists == false)
        {
            throw new Exception("The billboard does not exist in the schedule");
        }

        //return array list of scheduled times for single billboard
        return singleBBschedule;
    }
}