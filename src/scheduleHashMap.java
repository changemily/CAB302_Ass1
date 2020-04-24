import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class scheduleHashMap<E> {

    ArrayList<E> schedule_info;

    HashMap<String, ArrayList<E>> Billboard_schedule;

//constructor that creates HashMap

    public scheduleHashMap() {
        Billboard_schedule = new HashMap<String, ArrayList<E>>();
    }
    /**
     * List of billboards that have been scheduled
     */

    public HashMap<String, ArrayList<E>> View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration

        //FOR TESTING PURPOSES
        HashMap<String, HashMap> test_map = new HashMap<String, HashMap>();

        return test_map;
    }

    /**
     *
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @throws Exception if duration is out of range or the time scheduled is in the past
     */
    public static void scheduleBillboard(String billboard_name, LocalDate time_scheduled, int Duration_mins) throws Exception{

        // if scheduled time matches
        //remove from schedule
        //edit schedule information of Billboard object
        billboard.Schedule_billboard(Duration_mins, time_scheduled);

        //Add new viewing time and duration to HashMap

        //else
        //edit schedule information of Billboard object

        //Add new viewing time and duration to HashMap

    }
    /**
     * Removes billboard from schedule
     * @param billboard_name Name of billboard being removed from schedule
     */

    public static void Schedule_Remove_billboard(String billboard_name)
    {
        //remove scheduled time from billboard object in HashMap
        //remove from schedule HashMap

    }

    /**
     *
     * @param billboard_name
     * @return an array list of the times & durations the billboard is scheduled for
     */
    public ArrayList<ArrayList<E>> getSchedule(String billboard_name)
    {
        //retrieve
        //sort list

        return

    }

}
