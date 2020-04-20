
import java.time.Duration;
import java.time.LocalDate;


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
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     */

    public static void Create_edit_Billboard (String billboard_name, String text, String bg_colour, String image_file,
    LocalDate time_scheduled, int Duration_mins){

        //Create billboard object
        Billboard billboard  = new Billboard(billboard_name, text, bg_colour, image_file,
                time_scheduled, Duration_mins);

        //if Billboard exists
            //edit billboard info

        //if billboard does not exist
            //Store Billboard info in Database.
    }

/**
    * Method for listing current billboards
    * Return type void
    */

    public static void List_Billboards(){

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

    private static void View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration
    }

/**
     *
     * @param billboard_name Name of billboard being deleted
     */

    private static void Delete_billboard(String billboard_name)
    {
        //remove billboard object from HashMap
    }


    private static void Schedule_Billboard(int Duration_mins, LocalDate time_scheduled) throws Exception{
        //edit schedule information of Billboard object
        Schedule_Billboard(Duration_mins, time_scheduled);

        //Add new viewing time and duration to HashMap
    }
/**
     *
     * @param billboard_name Name of billboard being removed from schedule
     */

    private static void Schedule_Remove_billboard(String billboard_name)
    {
        //remove scheduled time from billboard object in HashMap
        //remove from schedule HashMap

    }

}



