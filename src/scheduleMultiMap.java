import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class scheduleMultiMap {

    MultiMap<String, Schedule_Info> Billboard_schedule;

    //constructor that creates a scheduleHashMap object

    public scheduleMultiMap() {
        Billboard_schedule = new MultiMap<>();
    }

    /**
     * extracts schedule data from database and stores it in Billboard_schedule
     * @throws SQLException throws exception if billboard does not exist or combination of billboard &
     * schedule information does not exist
     */
    public void RetrieveDBschedule() throws Exception {

        final String SELECT = "SELECT * FROM schedule ORDER BY Start_TimeScheduled desc";

        //Connect to database
        Connection connection;
        connection = DBconnection.getInstance();

        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //for every database entry
        while (rs.next())
        {
            //store database info in local variables
            String billboard_name = rs.getString(1);
            String Start_TimeScheduled = rs.getString(2);
            String duration = rs.getString(3);
            String recurrence = rs.getString(4);

            //store time scheduled and duration pair in array schedule_info
            Schedule_Info schedule_info = new Schedule_Info(LocalDateTime.parse(Start_TimeScheduled),
                    Duration.parse(duration), recurrence);

            //store billboard name with corresponding times scheduled and durations
            Billboard_schedule.put(billboard_name, schedule_info);
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();
        //close connection
        connection.close();
    }

    public void Write_To_DBschedule() throws SQLException {
        final String SELECT = "SELECT * FROM schedule ORDER BY Start_TimeScheduled";

        //Connect to database
        Connection connection;
        connection = DBconnection.getInstance();

        //create statement
        Statement st = connection.createStatement();

        ResultSet rs = st.executeQuery(SELECT);

        //clear existing database entries
        while (rs.next()) {
            rs.deleteRow();
        }

        //for every billboard name in Billboard_schedule
        for ( String billboard_name : Billboard_schedule.keySet() ) {

            //create collection to store viewings of billboard
            Collection<Schedule_Info>viewings = Billboard_schedule.get(billboard_name);

            //for every viewing of billboard
            for ( Schedule_Info viewing : viewings ) {
                //store Billboard_schedule info in local variables
                String Start_TimeScheduled = viewing.StartTime_Scheduled.toString();
                String duration = viewing.duration.toString();
                String recurrence = viewing.Recurrence;

                //write to database
                rs.updateString(1,billboard_name);
                rs.updateString(2,Start_TimeScheduled);
                rs.updateString(3,duration);
                rs.updateString(4, recurrence);
            }
        }

        //close ResultSet
        rs.close();
        //close statement
        st.close();
        //close connection
        connection.close();
    }

    /**
     * Lists billboards that have been scheduled
     * @return HashMap containing billboard name and an array list storing time scheduled and duration
     */

    public MultiMap<String, Schedule_Info> View_schedule()
    {
        return Billboard_schedule;
    }

    /**
     *
     * @param NewBB_duration Duration (minutes) Billboard is displayed for
     * @param NewBB_startTime Time (date) Billboard is scheduled for showing
     * @throws Exception if Billboard does not exist & if duration is out of range or the time scheduled is in the past
     */
    public void scheduleBillboard(String new_billboard, LocalDateTime NewBB_startTime, Duration NewBB_duration,
                                  String recurrence, billboardHashMap billboardList) throws Exception{

        //boolean variable to track whether billboard is in billboard list
        boolean billboard_exists = false;

        //For every billboard on billboardList
        for (Map.Entry<String, Billboard> billboardListEntry : (billboardList.List_Billboards()).entrySet()) {

            //if billboard name is in billboard list
            if(billboardListEntry.getKey() == new_billboard)
            {
                billboard_exists = true;
            }
        }

        if(billboard_exists == false)
        {
            throw new Exception("You cannot schedule a billboard that does not exist");
        }

        //if Billboard_schedule is empty
        if(Billboard_schedule.isEmpty())
        {
            //create schedule info for billboard
            Schedule_Info schedule_info = new Schedule_Info(NewBB_startTime, NewBB_duration,recurrence);

            //add billboard to schedule
            Billboard_schedule.put(new_billboard, schedule_info);
        }

        else
        {
            outerloop:
            //For every entry of Billboard_schedule
            for (String existing_billboard : Billboard_schedule.keySet())
            {
                //calculate end time of new billboard viewing
                LocalDateTime NewBB_endTime= NewBB_startTime.plus(NewBB_duration);

                //create collection to store viewings of billboard
                Collection<Schedule_Info>viewings = Billboard_schedule.get(existing_billboard);

                //for every viewing of billboard
                for ( Schedule_Info viewing : viewings ) {

                    //store viewing info of existing billboard in local variables
                    Duration ExistBB_duration = viewing.duration;
                    LocalDateTime ExistBB_startTime = viewing.StartTime_Scheduled;

                    //calculate end time of new billboard viewing
                    LocalDateTime ExistBB_endTime = ExistBB_startTime.plus(ExistBB_duration);

                    //---------------------------------------------------------------------------------------
                    // OVERLAPPING BILLBOARDS
                    //check if new billboard start time is after existing billboard start time
                    boolean isAfter = NewBB_startTime.isAfter(ExistBB_startTime);

                    //check if new billboard start time is before existing billboard end time
                    boolean isBefore = NewBB_startTime.isBefore(ExistBB_endTime) || NewBB_startTime.isEqual(ExistBB_endTime);

                    boolean isBetween = isAfter && isBefore;

                    boolean isEqual = (NewBB_startTime.isEqual(ExistBB_startTime));

                    // OVERLAP MATCHING BILLBOARDS
                    //if start and end time of new billboard matches start and end time of existing billboard
                    if(NewBB_startTime.isEqual(ExistBB_startTime)&& NewBB_endTime.isEqual(ExistBB_endTime))
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove entry from schedule
                            Billboard_schedule.remove(existing_billboard);
                        }
                        else{
                            //remove existing viewing from schedule
                            Billboard_schedule.remove(existing_billboard,viewing);
                        }

                        //create new schedule_info object for new billboard
                        Schedule_Info new_schedule_info = new Schedule_Info(NewBB_startTime, NewBB_duration, recurrence);

                        //schedule new billboard for given time
                        Billboard_schedule.put(new_billboard, new_schedule_info);

                        break;

                    }

                    // OVERLAPPING BILLBOARDS
                    //if scheduled time is between start and end time of existing billboard or start times are equal
                    else if(((isAfter && isBefore)||isEqual) == true)
                    {
                        //if number of viewings is 1
                        if (viewings.size() == 1)
                        {
                            //remove entry from schedule
                            Billboard_schedule.remove(existing_billboard);
                        }
                        else{
                            //remove existing viewing from schedule
                            Billboard_schedule.remove(existing_billboard,viewing);
                        }

                        //if start times are equal
                        if (isEqual)
                        {
                            //calculate new duration of existing billboard
                            Duration new_duration = Duration.between(NewBB_endTime, ExistBB_endTime);

                            //create new schedule_info object for existing billboard
                            Schedule_Info new_schedule_info = new Schedule_Info(NewBB_endTime,new_duration, recurrence);

                            //reschedule existing billboard for new time
                            Billboard_schedule.put(existing_billboard, new_schedule_info);

                        }

                        //if scheduled time is between start and end time
                        else if (isBetween)
                        {
                            //calculate new duration of existing billboard
                            Duration new_duration = Duration.between(NewBB_startTime, ExistBB_endTime);

                            //create new schedule_info object for existing billboard
                            Schedule_Info new_schedule_info = new Schedule_Info(ExistBB_startTime,new_duration, recurrence);

                            //reschedule existing billboard for new time
                            Billboard_schedule.put(existing_billboard, new_schedule_info);
                        }

                        //create new schedule_info object for new billboard
                        Schedule_Info newBB_schedule_info = new Schedule_Info(NewBB_startTime, NewBB_duration, recurrence);

                        //schedule new billboard for given time
                        Billboard_schedule.put(new_billboard, newBB_schedule_info);
                        break outerloop;
                    }

                    //NO SCHEDULE CLASHES
                    else
                    {
                        //create schedule info for billboard
                        Schedule_Info schedule_info = new Schedule_Info(NewBB_startTime, NewBB_duration,recurrence);

                        //add viewing to schedule
                        Billboard_schedule.put(new_billboard, schedule_info);
                        break outerloop;
                    }
                }

            }
        }
    }

    /**
     *
     * @param billboard_name Name of billboard being removed from schedule
     * @param schedule_info info of scheduled viewing being removed
     * @throws Exception throws exception if the billboard does not exist in the schedule & if the
     */

    public void Schedule_Remove_billboard(String billboard_name, Schedule_Info schedule_info) throws Exception
    {
        //boolean variable to track whether billboard is in schedule
        boolean billboard_exists = false;

        //boolean variable to track whether given viewing is in schedule
        boolean viewing_exists = false;

        outerloop:
        //For every entry of Billboard_schedule
        for (String BillboardName : Billboard_schedule.keySet())
        {
            //create collection to store viewings of billboard
            Collection<Schedule_Info>viewings = Billboard_schedule.get(billboard_name);

            //for every viewing of billboard
            for ( Schedule_Info viewing : viewings ) {

                //if billboard name is listed in schedule
                if(BillboardName == billboard_name)
                {
                    billboard_exists = true;

                    //if number of viewings is 1
                    if (viewings.size() == 1)
                    {
                        //remove entry from schedule
                        Billboard_schedule.remove(billboard_name);
                    }

                    //if combination of billboard name, and schedule info is listed in schedule
                    if(viewing == schedule_info)
                    {
                        viewing_exists = true;
                        //remove scheduled viewing from Billboard key
                        Billboard_schedule.remove(billboard_name,schedule_info);
                        break outerloop;
                    }
                }

            }
        }

        //if billboard name is not listed in schedule
        if(billboard_exists == false)
        {
            //throw exception
            throw new Exception("The billboard does not exist in the schedule");
        }

        //else if combination of billboard name, and schedule info is not in schedule
        else if(viewing_exists == false)
        {
            //throw exception
            throw new Exception("The given schedule information for " + billboard_name +
                    " does not exist");
        }
    }

    /**
     *
     * @param billboard_name name of the billboard schedule information is being retrieved from
     * @return an array list of the times & durations the billboard is scheduled for
     */

    public ArrayList<Schedule_Info> getSchedule(String billboard_name) throws Exception
    {
        //array list to store scheduled times of single billboard
        ArrayList<Schedule_Info> singleBBschedule = new ArrayList<>();

        //boolean variable to track whether billboard exists in schedule
        boolean billboard_exists = false;

        //For every entry of Billboard_schedule
        for (String BillboardName : Billboard_schedule.keySet())
        {
            //if given billboard name matches billboard name in schedule
            if (BillboardName == billboard_name)
            {
                //store viewings of billboard in singleBBschedule collection
                singleBBschedule = Billboard_schedule.get(billboard_name);
                billboard_exists = true;
                break;
            }
        }

        //if billboard is not scheduled
        if (billboard_exists == false)
        {
            throw new Exception("The billboard does not exist in the schedule");
        }

        //return array list of scheduled times for single billboard
        return singleBBschedule;
    }
}
