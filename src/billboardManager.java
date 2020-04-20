import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton
 */

public class billboardManager {
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
    static void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image, LocalDate schedule_time, Duration durationMinutes) {
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




/*

    public void List_Billboards(){

    }



    */
/*

    public void Get_billboard_info(String billboard_name){

    }
    */
/*

    private void View_schedule()
    {
        //returns list of Billboard objects from database
        // billboard's name, creator, time scheduled and duration
    }

    */


    static void Delete_billboard(String billboard_name)
    {
        //remove billboard object from database
    }


/*

    private void Schedule_Remove_billboard(String billboard_name)
    {
        //remove scheduled time from billboard object

    }

*/
}



