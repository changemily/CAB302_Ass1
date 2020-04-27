import org.mariadb.jdbc.internal.com.read.resultset.SelectResultSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class scheduleHashMap<Object> {

    HashMap<String, Schedule_info> Billboard_schedule;

    //constructor that creates a scheduleHashMap object

    public scheduleHashMap() {
        Billboard_schedule = new HashMap<>();
    }

    /**
     * extracts schedule data from database and stores it in Billboard_schedule
     * @throws SQLException throws exception if billboard does not exist or combination of billboard &
     * schedule information does not exist
     */
    public void RetrieveDBschedule() throws Exception {

        final String SELECT = "SELECT * FROM schedule ORDER BY time_scheduled desc";

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
            String Time_scheduled = rs.getString(2);
            String duration = rs.getString(3);
            String recurrence = rs.getString(4);

            //store time scheduled and duration pair in array schedule_info
            Schedule_info schedule_info = new Schedule_info(LocalDateTime.parse(Time_scheduled),
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
        final String SELECT = "SELECT * FROM schedule ORDER BY time_scheduled";

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

        //for every item in Billboard_schedule
        for ( String billboard_name : Billboard_schedule.keySet() ) {

            //store Billboard_schedule info in local variables
            //create temp array list to store time scheduled and duration pair
            Schedule_info schedule_info = Billboard_schedule.get(billboard_name);
            String Time_scheduled = schedule_info.Time_scheduled.toString();
            String duration = schedule_info.duration.toString();
            String recurrence = schedule_info.Recurrence;

            //write to database
            rs.updateString(1,billboard_name);
            rs.updateString(2,Time_scheduled);
            rs.updateString(3,duration);
            rs.updateString(4, recurrence);
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

    public HashMap<String, Schedule_info> View_schedule()
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
                                  String recurrence, HashMap<String, ArrayList<Object>> billboardList) throws Exception{

        //boolean variable to track whether billboard is in billboard list
        boolean billboard_exists = false;

        //For every billboard on billboardList
        for (Map.Entry<String, ArrayList<Object>> billboardListEntry : billboardList.entrySet()) {

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

        else {
            //For every entry of Billboard_schedule
            for (HashMap.Entry<String, Schedule_info> schedule_Entry : Billboard_schedule.entrySet())
            {
                //store existing_billboard in local variable
                String existing_billboard = schedule_Entry.getKey();

                //variable to store end time of new billboard viewing
                LocalDateTime NewBB_endTime;

                //calculate end time of new billboard viewing
                NewBB_endTime = NewBB_startTime.plus(NewBB_duration);

             //---------------------------------------------------------------------------------------
                //variable to store end time of existing billboard viewing
                LocalDateTime ExistBB_endTime;

                //store values of existing billboard in local variables
                Duration ExistBB_duration = schedule_Entry.getValue().duration;
                LocalDateTime ExistBB_startTime = schedule_Entry.getValue().Time_scheduled;

                //calculate end time of new billboard viewing
                ExistBB_endTime = ExistBB_startTime.plus(ExistBB_duration);

                //check if new billboard start time is after existing billboard start time
                boolean isAfter = NewBB_startTime.isAfter(ExistBB_startTime);

                //check if new billboard start time is before existing billboard end time
                boolean isBefore = NewBB_startTime.isBefore(ExistBB_endTime);

                //if start and end time of new billboard matches start and end time of existing billboard
                if(NewBB_startTime == ExistBB_startTime && NewBB_endTime == ExistBB_endTime)
                {
                    //remove existing billboard from schedule
                    Billboard_schedule.remove(existing_billboard);

                    //create new schedule_info object for new billboard
                    Schedule_info new_schedule_info = new Schedule_info(NewBB_startTime, NewBB_duration, recurrence);

                    //schedule new billboard for given time
                    Billboard_schedule.put(new_billboard, new_schedule_info);

                }

                //if scheduled time is between start and end time of existing billboard
                else if((isAfter && isBefore) == true)
                {
                    //remove existing billboard from schedule
                    Billboard_schedule.remove(existing_billboard);

                    //calculate new duration of existing billboard
                    Duration new_duration = Duration.between(NewBB_startTime, ExistBB_endTime);

                    //create new schedule_info object for existing billboard
                    Schedule_info new_schedule_info = new Schedule_info(NewBB_endTime,new_duration, recurrence);

                    //reschedule existing billboard for new time
                    Billboard_schedule.put(existing_billboard, new_schedule_info);

                    //create new schedule_info object for new billboard
                    Schedule_info newBB_schedule_info = new Schedule_info(NewBB_startTime, NewBB_duration, recurrence);

                    //schedule new billboard for given time
                    Billboard_schedule.put(new_billboard, newBB_schedule_info);
                }

                //else if recurring billboard && between given times (HH:MM:ss)
                {
                    //if recurring every day
                    //reschedule existing billboard for next available day

                    //if recurring every hour
                    //reschedule existing billboard for next available hour

                    //if recurring every minute
                    //reschedule existing billboard for next available minute

                    //create new schedule_info object for new billboard
                    Schedule_info newBB_schedule_info = new Schedule_info(NewBB_startTime, NewBB_duration,recurrence);

                    //schedule new billboard for given time
                    Billboard_schedule.put(new_billboard, newBB_schedule_info);
                }

                //else
                {
                    //create schedule info for billboard
                    Schedule_info schedule_info = new Schedule_info(NewBB_startTime, NewBB_duration,recurrence);

                    //add billboard to schedule
                    Billboard_schedule.put(new_billboard, schedule_info);
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

    public void Schedule_Remove_billboard(String billboard_name, Schedule_info schedule_info) throws Exception
    {
        //boolean variable to track whether billboard is in schedule
        boolean billboard_exists = false;

        //boolean variable to track whether given viewing is in schedule
        boolean viewing_exists = false;

        //For every entry of Billboard_schedule
        for (HashMap.Entry<String, Schedule_info> schedule_Entry : Billboard_schedule.entrySet())
        {
            //if billboard name is listed in schedule
            if(schedule_Entry.getKey() == billboard_name)
            {
                billboard_exists = true;

                //if combination of billboard name, and schedule info is listed in schedule
                if(schedule_Entry.getValue() == schedule_info)
                {
                    viewing_exists = true;
                    //remove scheduled viewing from Billboard_schedule
                    Billboard_schedule.remove(billboard_name,schedule_info);
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
            throw new Exception("The given schedule information given for" + billboard_name +
                    "does not exist");
        }
    }

    /**
     *
     * @param billboard_name name of the billboard schedule information is being retrieved from
     * @return an array list of the times & durations the billboard is scheduled for
     */

    public ArrayList<Schedule_info> getSchedule(String billboard_name) throws Exception
    {
        //array list to store scheduled times of single billboard
        ArrayList<Schedule_info> singleBBschedule = new ArrayList<>();

        //boolean variable to track whether billboard exists in schedule
        boolean billboard_exists = false;

        //For every entry of Billboard_schedule
        for (HashMap.Entry<String, Schedule_info> schedule_Entry : Billboard_schedule.entrySet())
        {
            //if given billboard name matches billboard name in Billboard_schedule
            if (schedule_Entry.getKey() == billboard_name)
            {
                //add schedule info to array list
                singleBBschedule.add(Billboard_schedule.get(billboard_name));
                billboard_exists = true;
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
