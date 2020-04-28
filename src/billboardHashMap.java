import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton
 */
public class billboardHashMap {

    HashMap<String, Billboard> billboardList;

    //constructor that creates HashMap

    billboardHashMap() {
        billboardList = new HashMap<String, Billboard>(); // use tree map to sort by key
    }

//Obtain_data - Extract serialized objects from database, deserialize and store in HashMap of objects
    // Extract serialized objects from database using sql queries
    // deserialize objects
    //store in HashMap

    // needs to be done for schedule and billboard tables in DB


    //Store_data - Serialize objects in list and write to DB


    //Create a new billboard object
    static Billboard billboardNew;
    //static Billboard billboardNew2;

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

    // NEEDS TO BE EDITED TO USE HASHMAP RETURNED FROM OBTAIN_DATA METHOD
    //For creating and editing billboards provided all possible parameters.
    public void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image,
                               LocalDateTime schedule_time, int durationMinutes) {
        //Create a new billboard object
        billboardNew = new Billboard(billboard_name, text,
                bg_colour, image, schedule_time, durationMinutes);

        //put billboard in HashMap - value will be replaced if key exists in HashMap
        billboardList.put(billboard_name, billboardNew);
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
     */

    public Billboard Get_billboard_info(String billboard_name) {
        //retrieve billboard info from database

        //FOR TESTING PURPOSES
        Billboard billboard_test = new Billboard("billboard_test", "hello", "green", "image.jpg");
        return billboard_test;
    }

    /**
     * @param billboard_name Name of billboard being deleted
     */

    public static void Delete_billboard(String billboard_name) {
        //remove schedule information of Billboard object
        //remove billboard object from HashMap
    }

}
