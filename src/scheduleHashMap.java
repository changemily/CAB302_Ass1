import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

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
    public void RetrieveDBschedule() throws SQLException {

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
            int duration = rs.getInt(3);

            //store time scheduled and duration pair in array schedule_info
            Schedule_info schedule_info = new Schedule_info(LocalDateTime.parse(Time_scheduled), duration);

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
            int duration = schedule_info.Duration;

            //write to database
            rs.updateString(1,billboard_name);
            rs.updateString(2,Time_scheduled);
            rs.updateInt(3,duration);
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
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @throws Exception if duration is out of range or the time scheduled is in the past
     */
    public void scheduleBillboard(String billboard_name, LocalDateTime time_scheduled, int Duration_mins,
                                         HashMap<String, ArrayList<Object>> billboardList) throws Exception{

        //if scheduled time matches
        //remove from overlapped billboard from schedule
        //schedule new billboard for given time

        //if scheduled time overlaps
        //remove from overlapped billboard from schedule
        //reschedule overlapped billboard for new time
        //schedule new billboard for given time


        //Add new viewing time and duration to HashMap

        //else
        //edit schedule information of Billboard object

        //Add new viewing time and duration to HashMap

    }
    /**
     * Removes billboard from schedule
     * @param billboard_name Name of billboard being removed from schedule
     */

    public void Schedule_Remove_billboard(String billboard_name, Schedule_info schedule_info) throws Exception
    {
        //For every entry of Billboard_schedule
        for (HashMap.Entry<String, Schedule_info> schedule_Entry : Billboard_schedule.entrySet())
        {
            //if billboard name is not listed in schedule
            if(schedule_Entry.getKey() != billboard_name)
            {
                //throw exception
                throw new Exception("The billboard does not exist in the schedule");
            }

            //if combination of billboard name, and schedule info is not in schedule
            else if(schedule_Entry.getValue() != schedule_info)
            {
                //throw exception
                throw new Exception("The given schedule information given for" + billboard_name +
                        "does not exist");
            }

            else{
                //remove scheduled viewing from Billboard_schedule
                Billboard_schedule.remove(billboard_name,schedule_info);
            }
        }
    }

    /**
     *
     * @param billboard_name name of the billboard schedule information is being retrieved from
     * @return an array list of the times & durations the billboard is scheduled for
     */

    public ArrayList<Schedule_info> getSchedule(String billboard_name)
    {
        //array list to store scheduled times of single billboard
        ArrayList<Schedule_info> singleBBschedule = new ArrayList<>();

        //For every entry of Billboard_schedule
        for (HashMap.Entry<String, Schedule_info> schedule_Entry : Billboard_schedule.entrySet())
        {
            //if given billboard name matches billboard name in Billboard_schedule
            if (schedule_Entry.getKey() == billboard_name)
            {
                //add schedule info to array list
                singleBBschedule.add(Billboard_schedule.get(billboard_name));
            }
        }

        //return array list of scheduled times for single billboard
        return singleBBschedule;
    }
}
