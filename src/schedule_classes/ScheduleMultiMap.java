package schedule_classes;

import billboard_classes.Billboard;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * scheduleMultiMap Class
 * This class contains methods that populate the billboard schedule, write the schedule to the database,
 * retrieves viewings of billboards and manipulates the multimap that stores the billboard schedule
 * @author — Emily Chang
 * @version - complete
 */
public class ScheduleMultiMap implements java.io.Serializable  {

    //Multimap that stores billboard name as the key and schedule information as the values
    public MultiMap<String, ScheduleInfo> scheduleMultiMap;

    /**
     * constructor that creates a scheduleMultiMap object
     */
    public ScheduleMultiMap() {
        scheduleMultiMap = new MultiMap<>();
    }

    /**
     * Retrieves schedule data from database and stores it in the schedule_classes.MultiMap of the scheduleMultiMap object
     * @param connection Database Connection
     * @throws SQLException throws exception if SQL query is invalid, billboard does not exist or combination of billboard &
     * schedule information does not exist
     */
    public void retrieveDBschedule(Connection connection) throws Exception {
        //clear multimap
        //for every billboard in Billboard_schedule
        for (String billboardName : new ArrayList<>(scheduleMultiMap.keySet())) {
            //remove entry
            scheduleMultiMap.remove(billboardName);
        }

        //SQL query that selects all viewings in the schedule and orders them by start time descending
        final String SELECT = "SELECT * FROM schedule ORDER BY startTimeScheduled desc";

        //create statement
        Statement st = connection.createStatement();

        //create result set that holds all viewings in the schedule, ordered by start time descending
        ResultSet rs = st.executeQuery(SELECT);

        //for every row in the schedule table
        while (rs.next())
        {
            //store database info in local variables
            String billboardName = rs.getString(1);
            String startTimeScheduled = rs.getString(2);
            String duration = rs.getString(3);
            String recurrence = rs.getString(4);
            String billboardCreator = rs.getString(5);

            //create schedule info object for viewing, with data retrieved from DB
            ScheduleInfo schedule_info = new ScheduleInfo(LocalDateTime.parse(startTimeScheduled),
                    Duration.parse(duration), Integer.parseInt(recurrence), billboardCreator);

            //store billboard name with corresponding schedule information in local multimap
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
    public void writeToDBschedule(Connection connection) throws SQLException {
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

                //Store viewing information in database
                st.executeQuery("INSERT INTO Schedule (billboardName,startTimeScheduled, Duration,recurrenceDelay, billboardCreator) " +
                        "VALUES(\""+billboardName+"\",\""+startTimeScheduled+"\",\""+duration+"\",\""+recurrenceDelay+"\",\""+billboardCreator+"\");");
            }
        }

        //close statement
        st.close();
    }

    /**
     * Returns array of all viewings that have been scheduled
     * @return schedule_classes.MultiMap containing billboard name as the key and schedule info as the value
     */

    public MultiMap<String, ScheduleInfo> viewSchedule()
    {
        return scheduleMultiMap;
    }

    /**
     * Schedules billboards, accounting for overlapping viewings
     * @param newBillboardName name of billboard being scheduled
     * @param newBBStartTime start time (date) billboard_classes.Billboard is scheduled for showing
     * @param newBBDuration Duration (minutes) billboard_classes.Billboard is displayed for
     * @param recurrenceDelay recurrence delay (minutes) of billboard being scheduled
     * @param billboardList list that contains all billboards created
     * @param billboardCreator creator of billboard being scheduled
     * @throws Exception if billboard_classes.Billboard does not exist & if duration is out of range or the time scheduled is in the past
     */
    public void scheduleBillboard(String newBillboardName, LocalDateTime newBBStartTime, Duration newBBDuration,
                                  int recurrenceDelay, HashMap<String, Billboard> billboardList, String billboardCreator) throws Exception{

        //check if schedule information given is valid
        checkValidSchedule(newBillboardName, newBBStartTime, billboardList);

        //if schedule is empty
        if(scheduleMultiMap.isEmpty())
        {
            //create schedule info for billboard
            ScheduleInfo scheduleInfo = new ScheduleInfo(newBBStartTime, newBBDuration, recurrenceDelay, billboardCreator);

            //add billboard to schedule
            scheduleMultiMap.put(newBillboardName, scheduleInfo);
        }

        else
        {
            //For every existing billboard of scheduleMultiMap
            for (String existingBillboard : new ArrayList<>(scheduleMultiMap.keySet()))
            {
                //calculate the end time of the new billboard viewing
                LocalDateTime newBBEndTime= newBBStartTime.plus(newBBDuration);

                //create ArrayList to store viewings of billboard
                ArrayList<ScheduleInfo> viewings = new ArrayList<>(scheduleMultiMap.get(existingBillboard));

                //for every viewing of billboard
                for ( ScheduleInfo viewing : viewings) {

                    //store duration and start time of existing billboard in local variables
                    Duration existBBDuration = viewing.duration;
                    LocalDateTime existBBStartTime = viewing.startTimeScheduled;

                    //get recurrence and creator of existing billboard
                    int existBBRecurrence = viewing.recurrenceDelay;
                    String existBBCreator = viewing.billboardCreator;

                    //calculate end time of new billboard viewing
                    LocalDateTime existBBEndTime = existBBStartTime.plus(existBBDuration);

                    //---------------------------------------------------------------------------------------
                    //Determine value of boolean variables for schedule overlap checks

                    //New billboard start time
                    //check if new billboard start time is after existing billboard start time
                    boolean newBBStartTimeAfter = newBBStartTime.isAfter(existBBStartTime);
                    //check if new billboard start time is before existing billboard end time
                    boolean newBBStartTimeBefore = newBBStartTime.isBefore(existBBEndTime);
                    //check if new billboard start time is during existing billboard's duration
                    boolean newBBStartTimeBetween = newBBStartTimeAfter && newBBStartTimeBefore;
                    //check if new billboard start time is equal to existing billboard start time
                    boolean startTimesEqual = (newBBStartTime.isEqual(existBBStartTime));

                    //New billboard end time
                    //check if new billboard end time is after existing billboard start time
                    boolean newBBEndTimeAfter = newBBEndTime.isAfter(existBBStartTime);
                    //check if new billboard end time is before existing billboard end time
                    boolean newBBEndTimeBefore = newBBEndTime.isBefore(existBBEndTime);
                    //check if new billboard end time is during existing billboard's duration
                    boolean newBBEndIsBetween = newBBEndTimeAfter && newBBEndTimeBefore;
                    //check if new billboard end time is equal to existing billboard end time
                    boolean endTimesEqual = (newBBEndTime.isEqual(existBBEndTime));

                    //Existing billboard start time
                    //check if existing billboard start time is after new billboard start time
                    boolean existBBStartTimeAfter = existBBStartTime.isAfter(newBBStartTime);
                    //check if existing billboard start time is before new billboard end time
                    boolean existBBStartTimeBefore = existBBStartTime.isBefore(newBBEndTime);
                    //check if existing billboard start time is during new billboard's duration
                    boolean existBBStartTimeBetween = existBBStartTimeAfter && existBBStartTimeBefore;

                    //Existing billboard end time
                    //check if existing billboard end time is after new billboard start time
                    boolean existBBEndTimeAfter = existBBEndTime.isAfter(newBBStartTime);
                    //check if existing billboard end time is before new billboard end time
                    boolean existBBEndTimeBefore = existBBEndTime.isBefore(newBBEndTime);
                    //check if existing billboard end time is during new billboard's duration
                    boolean existBBEndTimeBetween = existBBEndTimeAfter && existBBEndTimeBefore;

                    //-----------------------------------------------------------------------------------------------------------------------------
                    //CHECK FOR SCHEDULE OVERLAPS

                    // FULL OVERLAP OF VIEWINGS
                    //if new and existing schedules match OR if new viewing fully overlaps existing
                    if((existBBStartTimeBetween || startTimesEqual) == true && (existBBEndTimeBetween || endTimesEqual == true))
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove billboard from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else
                        {
                            //remove existing viewing from schedule
                            scheduleMultiMap.remove(existingBillboard,viewing);
                        }
                    }

                    //-----------------------------------------------------------------------------------------------------------------------------
                    // SPLIT EXISTING SCHEDULE - NEW BILLBOARD'S START AND END TIMES DURING EXISTING VIEWING'S DURATION
                    //if new start and end times are during existing viewing and new start and end times do not match existing
                    else if ((newBBStartTimeBetween == true && newBBEndIsBetween == true) && (startTimesEqual == false && endTimesEqual == false))
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove billboard from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else
                        {
                            //remove existing viewing from schedule
                            scheduleMultiMap.remove(existingBillboard,viewing);
                        }

                        //calculate the new durations of existing billboard - existing viewing is split
                        Duration newDuration1 = Duration.between(existBBStartTime, newBBStartTime);
                        Duration newDuration2 = Duration.between(newBBEndTime, existBBEndTime);

                        //create new scheduleInfo objects for existing billboard
                        ScheduleInfo newScheduleInfo1 = new ScheduleInfo(existBBStartTime,newDuration1, existBBRecurrence, existBBCreator);
                        ScheduleInfo newScheduleInfo2 = new ScheduleInfo(newBBEndTime,newDuration2, existBBRecurrence, existBBCreator);

                        //reschedule existing billboard for new times
                        scheduleMultiMap.put(existingBillboard, newScheduleInfo1);
                        scheduleMultiMap.put(existingBillboard, newScheduleInfo2);
                    }

                    //-----------------------------------------------------------------------------------------------------------------------------
                    // NEW BILLBOARD'S START TIME DURING EXISTING VIEWING'S DURATION
                    //if new billboard's start time is during existing viewing
                    else if(newBBStartTimeBetween == true)
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove billboard from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else
                        {
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

                    //-----------------------------------------------------------------------------------------------------------------------------
                    // NEW BILLBOARD'S END TIME DURING EXISTING VIEWING
                    //if new billboard's end time is during existing viewing
                    else if (newBBEndIsBetween == true)
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove billboard from schedule
                            scheduleMultiMap.remove(existingBillboard);
                        }
                        else
                        {
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

            //SCHEDULE NEW BILLBOARD
            //create schedule info object for new billboard
            ScheduleInfo scheduleInfo = new ScheduleInfo(newBBStartTime, newBBDuration, recurrenceDelay, billboardCreator);

            //add viewing to schedule
            scheduleMultiMap.put(newBillboardName, scheduleInfo);
        }
    }

    /**
     * Checks if schedule information given is valid
     * @param newBillboardName name of billboard being scheduled
     * @param newBBStartTime start time (minutes) of billboard being scheduled
     * @param billboardList list that contains all billboards created
     * @throws Exception if billboard_classes.Billboard does not exist, if duration is out of range or if the start time given is in the past
     */
    private void checkValidSchedule(String newBillboardName, LocalDateTime newBBStartTime, HashMap<String, Billboard> billboardList) throws Exception {
        //CHECK FOR VALID START TIME
        //if the time scheduled is in the past
        if (newBBStartTime.isBefore(LocalDateTime.now()))
        {
            //throw exception
            throw new Exception("Time scheduled must be after"+ LocalDateTime.now());
        }

        //CHECK FOR VALID BILLBOARD
        //boolean variable to track whether billboard is in billboard list
        boolean billboardExists = false;

        //For every billboard in billboardList
        for (Map.Entry<String, Billboard> billboardListEntry : billboardList.entrySet()){

            //if billboard name of billboard being scheduled is in the billboard list
            if(billboardListEntry.getKey().equals(newBillboardName))
            {
                //billboard exists
                billboardExists = true;
                break;
            }
        }

        //if billboard does not exist in billboardList
        if(billboardExists == false)
        {
            //throw exception
            throw new Exception("You cannot schedule a billboard that does not exist");
        }
    }

    /**
     * Removes viewing from schedule multimap
     * @param billboardName Name of billboard being removed from schedule
     * @param scheduleInfo schedule info of viewing being removed
     * @throws Exception throws exception if the billboard does not exist in the schedule or if the viewing does not exist
     */
    public void removeViewing(String billboardName, ScheduleInfo scheduleInfo) throws Exception
    {
        //boolean variable to track whether billboard is in schedule
        boolean billboardExists = false;

        //boolean variable to track whether given viewing is in schedule
        boolean viewingExists = false;

        outerloop:
        //For every entry of scheduleMultiMap
        for (String BillboardName : scheduleMultiMap.keySet())
        {
            //create ArrayList to store viewings of billboard
            ArrayList<ScheduleInfo>viewings = scheduleMultiMap.get(billboardName);

            //for every viewing of billboard
            for ( ScheduleInfo viewing : viewings ) {

                //if billboard name is listed in the schedule
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

                    //if combination of billboard name and schedule info is listed in the schedule
                    else if(viewing.startTimeScheduled.equals(scheduleInfo.startTimeScheduled))
                    {
                        viewingExists = true;

                        //remove scheduled viewing value from billboard_classes.Billboard key
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
     * @param billboardName name of billboard, schedule information is being retrieved for
     * @return an array list of the schedule information, for each of the billboard's viewing
     * @throws Exception throws exception if the billboard does not exist in the schedule
     */
    public ArrayList<ScheduleInfo> getViewings(String billboardName) throws Exception
    {
        //array list to store scheduled times of single billboard
        ArrayList<ScheduleInfo> singleBBschedule = new ArrayList<>();

        //boolean variable to track whether billboard exists in the schedule
        boolean billboardExists = false;

        //For every entry of scheduleMultiMap
        for (String BillboardName : scheduleMultiMap.keySet())
        {
            //if given billboard name matches billboard name in the schedule
            if (BillboardName.equals(billboardName))
            {
                //store viewings of billboard in singleBBschedule ArrayList
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

        //return array list of scheduled times for billboard
        return singleBBschedule;
    }
}