/*
import java.time.Duration;
import java.time.LocalDate;

*/
/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton
 *//*


public class billboardManager {
    //Variables
    String Billboard_Name;
    String Text;
    String bg_Colour;
    String Image;
    localDate Schedule;
    Duration Minutes;
    //Counter for the amount of billboards in circulation.
    int counter;
    //An array for storing billboard info.
    String[][] billboard_info;
    String[] bb_info;



    */
/**
     * Method to create and edit billboards
     * Parameters -
     * @param billboard_name Billboards name
     * @param text Text on the billboard
     * @param bg_colour Background colour of the billboard
     * @param image (optional)
     * @param schedule time (optional):void
     * @param durationMinutes The duration in minutes it will be displayed
     *//*

    public void Create_edit_Billboard (String billboard_name, String text, String bg_colour, String image,
    localDate schedule_time, Duration durationMinutes){
        //Pass params to variables.
        bb_info[0] = (this.Billboard_Name = billboard_name);
        bb_info[1] = (this.Text = text);
        bb_info[2] = (this.bg_Colour = bg_colour);
        bb_info[3] = (this.Image = image);
        bb_info[4] = ToString.((this.Schedule = schedule_time));
        bb_info[5] = ToString.((this.Minutes = durationMinutes));
        //Pass Params to array as info.
        try{
            //Pass users params to billboard creating class.
            Billboard ToString.(Billboard_Name) = new Billboard(Billboard_Name, text,
                    bg_Colour, Image, Schedule, Minutes);
            //Update counter
            counter++;
            //Store billboard information in a 2D array.
            for(int i = 0; i < counter; i++){
                for(int j = 0; j < 6; j++){
                    billboard_info[counter][i] = bb_info[j];
                }
            }
        }catch{
            //Error Message.
            System.out.println("The billboard cannot be created.");
        }
    }



    */
/**
    * Method for listing current billboards
    * Return type void
    *//*

    public void List_Billboards(){

    }



    */
/**
     * Method for getting billboard information
     * @param billboard_name
     * Return type void
     *//*

    public void Get_billboard_info(String billboard_name){

    }

    */
/**
     * List of billboards that have been scheduled
     *//*

    private void View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration
    }

    */
/**
     *
     * @param billboard_name Name of billboard being deleted
     *//*

    private void Delete_billboard(String billboard_name)
    {
        //remove billboard object from database
    }

    */
/**
     *
     * @param billboard_name Name of billboard being removed from schedule
     *//*

    private void Schedule_Remove_billboard(String billboard_name)
    {
        //remove scheduled time from billboard object

    }


}


*/
