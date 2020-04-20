import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains methods that test the functionality of billboardManager class methods
 *  * @author — Jarod Evans & Emily Chang
 *  * @version - skeleton
 */

public class billboardManagerTest{

    HashMap<String, Billboard> billboardList;

    HashMap<String,HashMap> Billboard_schedule;
    Billboard Billboard_1;
    Billboard Billboard_2;
    Billboard Billboard_3;
    Billboard Billboard_4;
    Billboard Billboard_5;
    Billboard Billboard_6;
    Billboard Billboard_7;
    Billboard Billboard_8;

    //Create mock HashMap of billboards
    @BeforeEach
    public void Create_bbMap(){
        //Create billboards to store in Billboard HashMap
        Billboard_1 = new Billboard("Billboard_1", "hello world", "red", "image.jpg");
        Billboard_2 = new Billboard("Billboard_2", "CAB302",
                "yellow", "image.jpg", LocalDate.parse("20-04-2020"), 5);
        Billboard_3 = new Billboard("Billboard_3", "goodbye",
                "blue", "image.jpg", LocalDate.parse("30-01-2020"), 15);
        Billboard_4 = new Billboard("Billboard_4", "test",
                "green", "image.jpg");
        Billboard_5 = new Billboard("Billboard_5", "hello",
                "orange", "No Image");
        Billboard_7 = new Billboard("Billboard_7", "goodbye",
                "blue", "image.jpg", LocalDate.parse("25-01-2020"), 20);
        Billboard_8 = new Billboard("Billboard_8", "test",
                "blue", "image.jpg", LocalDate.parse("30-01-2020"), 9);


        //create Billboard HashMap to store billboards
        billboardList = new HashMap<String, Billboard>();

        //populate HashMap
        billboardList.put(Billboard_1.Billboard_name, Billboard_1);
        billboardList.put(Billboard_2.Billboard_name, Billboard_2);
        billboardList.put(Billboard_3.Billboard_name, Billboard_3);
        billboardList.put(Billboard_4.Billboard_name, Billboard_4);
        billboardList.put(Billboard_5.Billboard_name, Billboard_5);

        //populate HashMap
        Billboard_schedule.put(Billboard_2.Billboard_name, Billboard_2.Time_duration);
        Billboard_schedule.put(Billboard_3.Billboard_name, Billboard_3.Time_duration);
        Billboard_schedule.put(Billboard_7.Billboard_name, Billboard_7.Time_duration);
        Billboard_schedule.put(Billboard_8.Billboard_name, Billboard_8.Time_duration);

    }

    //Test 1: Checks if a billboard can be created and added to HashMap.
    @Test
    public void Create_Billboard(){

        //Billboard with no scheduled viewing
        billboardManager.Create_edit_Billboard("Billboard_6","new billboard", "blue", "No Image");

        assertEquals(true, billboardList.containsKey("Billboard_6"));

        //Billboard with scheduled viewing and image
        billboardManager.Create_edit_Billboard("Billboard_7","new billboard", "blue",
                "image.jpg", LocalDate.parse("20-04-2020"), 5);

        assertEquals(true, billboardList.containsKey("Billboard_7"));

    }

    //Test 2: Checks if a billboard can be edited.
    @Test
    public void Edit_Billboard(){

    }

    //Test 3: Checks if all current billboards can be listed.
    @Test
    public void List_Billboards(){

    }

    //Test 4: Checks if all information pertaining to a specified billboard can be obtained.
    @Test
    public void Get_billboard_info(String billboard_name){

    }

    //Test 5: Checks if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule()
    {

    }

    //Test 6: Checks if a specified billboard can be deleted.
    @Test
    public void Delete_billboard()
    {
        billboardManager.Delete_billboard("Billboard_1");

        assertEquals(false, billboardList.containsKey("Billboard_1"));

    }

    //Test 6.0: Schedule billboard in vacant time slot
    //
    @Test
    public void Schedule_billboardTest()
    {
        billboardManager.scheduleBillboard("",);
    }

    //Test 6.1: Schedule billboard that is has not been scheduled
    //
    @Test
    public void Schedule_billboardTest2()
    {

    }

    //Test 6.2: Schedule billboard in non-vacant time slot - should take precedence over existing billboard scheduled
    //
    @Test
    public void Schedule_billboardTest3()
    {

    }

    //Test 7: Checks if a billboard can be removed from schedule.
    @Test
    public void Schedule_Remove_billboard(String billboard_name)
    {


    }

}