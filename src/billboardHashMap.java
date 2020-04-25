import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Billboard Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Jarod Evans & Emily Chang
 * @version - skeleton
 */
public class billboardHashMap<E> extends HashMap {

    static HashMap<String, ArrayList> billboardList;

    ArrayList<E> billboardValues;

    //constructor that creates HashMap

    public billboardHashMap() {
        billboardList = new HashMap<String, ArrayList>(); // use tree map to sort by key
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
    static void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image,
                                      LocalDate schedule_time, Duration durationMinutes) {
        //Create a new billboard object
        //Search for existing billboard
        boolean Truth = TestbillboardHashMap.billboardList.containsKey("Billboard_1");
        if (Truth) {
            //billboardManagerTest.billboardList.get("Billboard_1").Bg_colour = bg_colour;
            //Create new billboard to the users updated specs
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image, schedule_time, Integer.parseInt(String.valueOf(durationMinutes)));
            //Edit the old billboard by replacing it with the new billboard.
            TestbillboardHashMap.billboardList.put("Billboard_1", billboardNew);
        } else {
            TestbillboardHashMap.billboardList.containsKey("Billboard_1");
            //Create a billboard using the parameters provided.
            billboardNew = new Billboard(billboard_name, text,
                    bg_colour, image, schedule_time, Integer.parseInt(String.valueOf(durationMinutes)));
        }
    }

    //For creating and editing billboards without the optional parameters.
    public static void Create_edit_Billboard(String billboard_name, String text, String bg_colour, String image) {
        //Create a new billboard object
        //Search for existing billboard
        boolean Contains_billboard = TestbillboardHashMap.billboardList.containsKey("Billboard_1");

        //Create new billboard to fit users' specs
        billboardNew = new Billboard(billboard_name, text, bg_colour, image);

        if (Contains_billboard == true) {

            //remove existing billboard from HashMap


        }
        // Insert billboard with updated specs.
        TestbillboardHashMap.billboardList.put("Billboard_1", billboardNew);
    }

    /**
     * Method for listing current billboards
     * Return type void
     */

    public static HashMap<String, Billboard> List_Billboards() {

        //FOR TESTING PURPOSES
        HashMap<String, Billboard> test_map = new HashMap<String, Billboard>();

        return test_map;
    }

    /**
     * Method for retrieving billboard information from database
     *
     * @param billboard_name Return type Billboard
     */

    public static Billboard Get_billboard_info(String billboard_name) {
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
