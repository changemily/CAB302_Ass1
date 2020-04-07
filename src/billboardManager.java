import java.time.Duration;
import java.time.LocalDate;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton
 */

public class billboardManager {
    /**
     * Method to create and edit billboards
     * Parameters -
     * @param billboard_name
     * @param text
     * @param bg_colour
     * @param image (optional)
     * @param scheduled_time (optional):void
     * @param durationMinutes
     */
    public void Create_edit_Billboard (String billboard_name, String text, String bg_colour, String image,
    LocalDate scheduled_time, Duration durationMinutes){

    }



    /**
    * Method for listing current billboards
    * Return type void
    */
    public void List_Billboards(){

    }



    /**
     * Method for getting billboard information
     * @param billboard_name
     * Return type void
     */
    public void Get_billboard_info(String billboard_name){

    }

    /**
     * List of billboards that have been scheduled
     */
    private void View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration
    }

    /**
     *
     * @param billboard_name Name of billboard being deleted
     */
    private void Delete_billboard(String billboard_name)
    {
        //remove billboard object from database
    }

    /**
     *
     * @param billboard_name Name of billboard being removed from schedule
     */
    private void Schedule_Remove_billboard(String billboard_name)
    {
        //remove scheduled time from billboard object

    }


}


