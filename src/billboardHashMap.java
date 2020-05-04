import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans
 * @version - under development
 */
public class billboardHashMap {

    //Setup a hashmap for tracking billboards.
    HashMap<String, Billboard> billboardList;

    //Setup a schedule multimap
    scheduleMultiMap scheduleMultiMap = new scheduleMultiMap();

    //constructor that creates HashMap
    billboardHashMap() {
        billboardList = new HashMap<>(); // use tree map to sort by key
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
        billboardList.put(billboard_name, billboardNew);

        //add schedule info of bb to schedule multi map
        this.scheduleMultiMap.scheduleBillboard(billboard_name,schedule_time, durationMinutes,recurrence,billboardList);
    }

    //For creating and editing billboards without the optional parameters.
    public void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image) {
        //Create a new billboard object
        billboardNew = new Billboard(billboard_name, text,
                bg_colour, image);

        //put billboard in HashMap - value will be replaced if key exists in HashMap
        billboardList.put(billboard_name, billboardNew);
    }

    /**
     * Method for listing current billboards
     * Return type void
     */

    public HashMap<String, Billboard> List_Billboards() {
        return billboardList;
    }

    /**
     * Method for retrieving billboard information from database
     *
     * @param billboard_name Return type Billboard
     * @return
     */

    public ArrayList<Schedule_Info> Get_billboard_info(String billboard_name) throws Exception {
        //Find the correct billboard using its name

        //For the real thing
        return this.scheduleMultiMap.getSchedule(billboard_name);

        //FOR TESTING PURPOSES
//        Billboard billboard_test = new Billboard(billboard_name, "hello", "green", "image.jpg");
//        return billboard_test;
    }

    /**
     * @param billboard_name Name of billboard being deleted
     */

    public void Delete_billboard(String billboard_name) throws Exception {

        //The code for deleting the billboard info from the schedule.
        //Get the billboard info and store it in an arrayList
        ArrayList<Schedule_Info> billboard_info = Get_billboard_info(billboard_name);

        //Transfer the info from the arrayList to the Schedule_info object
        Schedule_Info Schedule_info = new Schedule_Info(LocalDateTime.parse((CharSequence) billboard_info.get(0)),
                Duration.parse((CharSequence) billboard_info.get(1)),
                String.valueOf(billboard_info.get(2)));

        //Use the billboard name given and the information collected about the billboard
        //to remove the billboard from the billboard schedule.
        this.scheduleMultiMap.Schedule_Remove_billboard(billboard_name, Schedule_info);

        //The code for removing the billboard info from the billboardList.
        billboardList.remove(billboard_name);
    }

}
