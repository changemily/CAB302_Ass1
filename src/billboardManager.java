
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;


/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton update
 */


public class billboardManager {
    //Variables
    //Counter for the amount of billboards in circulation.
    int counter;
    //An array for storing billboard info.
    String[][] billboard_info;
    String[] bb_info;


    //Obtain_data - Extract serialized objects from database, deserialize and store in list of objects

    //Store_data - Serialize objects in list and write to DB

/**
     * Method to create and edit billboards
     * Parameters -
     *  @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     * @param image_file file name of image on Billboard
     */

    public static void Create_edit_Billboard (String billboard_name, String text, String bg_colour, String image_file){

        //Create billboard object
        Billboard billboard  = new Billboard(billboard_name, text, bg_colour, image_file);

        //if Billboard exists
        //edit billboard info

        //if billboard does not exist
        //Store Billboard info in Database.
    }

    /**
     * Overloaded method  - Optional for Billboard to be scheduled for display
     * Method to create and edit billboards
     * Parameters -
     *  @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     * @param image_file file name of image on Billboard
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     * */
    public static void Create_edit_Billboard (String billboard_name, String text, String bg_colour, String image_file,
    LocalDate time_scheduled, int Duration_mins){

        //Create billboard object
        Billboard billboard  = new Billboard(billboard_name, text, bg_colour, image_file,
                time_scheduled, Duration_mins);

        //if Billboard exists
            //edit billboard info

        //if billboard does not exist
            //Store Billboard info in Database.

        //if billboard is not scheduled for this time, add to schedule HashMap
        //store time_scheduled & Duration_mins in a Time_scheduled HashMap
        //store Billboard_name & Time_scheduled HashMap in Billboard_schedule HashMap
    }

/**
    * Method for listing current billboards
    * Return type void
    */

    public static HashMap<String, HashMap> List_Billboards(){

        HashMap<String, HashMap> test_map = new HashMap<String, HashMap>();

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

    public static void View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration
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

        //edit schedule information of Billboard object
        billboard.Schedule_billboard(Duration_mins, time_scheduled);

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



