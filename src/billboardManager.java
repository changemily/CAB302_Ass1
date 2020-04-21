import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton
 */

public class billboardManager {

    //Obtain_data - Extract serialized objects from database, deserialize and store in list of objects

    //Store_data - Serialize objects in list and write to DB


    //Create a new billboard object
    static Billboard billboardNew;
    //static Billboard billboardNew2;

    /**
     * Method to create and edit billboards
     * Parameters -
     * @param billboard_name  Billboards name
     * @param text            Text on the billboard
     * @param bg_colour       Background colour of the billboard
     * @param image           (optional)
     * @param schedule_time   time (optional):void
     * @param durationMinutes The duration in minutes it will be displayed
     */

    //For creating and editing billboards provided all possible parameters.
    static void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image,
                                      LocalDate schedule_time, Duration durationMinutes) {
        //Create a new billboard object
        //Search for existing billboard
        boolean Truth = billboardManagerTest.billboardList.containsKey("Billboard_1");
        if (Truth){
            //billboardManagerTest.billboardList.get("Billboard_1").Bg_colour = bg_colour;
            //Create new billboard to the users updated specs
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image, schedule_time, Integer.parseInt(String.valueOf(durationMinutes)));
            //Edit the old billboard by replacing it with the new billboard.
            billboardManagerTest.billboardList.put("Billboard_1", billboardNew);
        }else
        {
            billboardManagerTest.billboardList.containsKey("Billboard_1");
            //Create a billboard using the parameters provided.
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image, schedule_time, Integer.parseInt(String.valueOf(durationMinutes)));
        }
    }

    //For creating and editing billboards without the optional parameters.
    public static void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image) {
        //Create a new billboard object
        //Search for existing billboard
        boolean Truth = billboardManagerTest.billboardList.containsKey("Billboard_1");
        if (Truth){
            //Create new billboard to the users updated specs
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image);
            //Edit the old billboard by replacing it with the new billboard.
            billboardManagerTest.billboardList.put("Billboard_1", billboardNew);
        }else
        {
            billboardManagerTest.billboardList.containsKey("Billboard_1");
            //Create a billboard using the parameters provided.
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image);
        }
    }

    /**
     * Method for listing current billboards
     * Return type void
     */

    public static HashMap<String, Billboard> List_Billboards(){

        HashMap<String, Billboard> test_map = new HashMap<String, Billboard>();

        return test_map;
    }

    /**
     * Method for retrieving billboard information from database
     * @param billboard_name
     * Return type void
     */

    public static void Get_billboard_info(String billboard_name){
        //retrieve billboard info from database
    }

    /**
     * List of billboards that have been scheduled
     */

    public static HashMap<String, HashMap> View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration
        HashMap<String, HashMap> test_map = new HashMap<String, HashMap>();

        return test_map;
    }

    /**
     *
     * @param billboard_name Name of billboard being deleted
     */

    public static void Delete_billboard(String billboard_name)
    {
        //remove schedule information of Billboard object
        //remove billboard object from HashMap
    }

    /**
     *
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @throws Exception if duration is out of range or the time scheduled is in the past
     */
    public static void scheduleBillboard(Billboard billboard, int Duration_mins, LocalDate time_scheduled) throws Exception{

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
     *
     * @param billboard_name Name of billboard being removed from schedule
     */

    public static void Schedule_Remove_billboard(String billboard_name)
    {
        //remove scheduled time from billboard object in HashMap
        //remove from schedule HashMap

    }

}



