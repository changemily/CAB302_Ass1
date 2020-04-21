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

public class billboardManagerTest {

    static HashMap<String, Billboard> billboardList;

    HashMap<String, HashMap> Billboard_schedule;
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
    public void Create_bbMap() {
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
    public void Create_Billboard() {

        //Billboard with no scheduled viewing
        billboardManager.Create_edit_Billboard("Billboard_6", "new billboard", "blue", "No Image");

        assertEquals(true, billboardList.containsKey("Billboard_6"));

        //Billboard with scheduled viewing and image
        billboardManager.Create_edit_Billboard("Billboard_7", "new billboard", "blue",
                "image.jpg", LocalDate.parse("20-04-2020"), Duration.parse("5"));

        assertEquals(true, billboardList.containsKey("Billboard_7"));

    }

    //Test 2: Checks if a billboard can be edited.
    @Test
    public void Edit_Billboard() {

    }

    //Test 3: Checks if all current billboards can be listed.
    @Test
    public void List_Billboards() {
        //store billboard schedule in temp HashMap
        HashMap<String, Billboard> temp_list= new HashMap<String, Billboard>();

        temp_list = billboardManager.List_Billboards();

        //compare temp HashMap to billboardList HashMap to see if they match
        // for every entry of temp_list
        for (HashMap.Entry<String, Billboard> temp_listEntry : temp_list.entrySet()) {

            // for every entry of billboardList
            for (HashMap.Entry<String, Billboard> original_entry : billboardList.entrySet()) {
                //check if all billboards are listed
                assertEquals(original_entry.getKey(),temp_listEntry.getKey());
            }
        }
    }

    //Test 4: Checks if all information pertaining to a specified billboard can be obtained.
    @Test
    public void Get_billboard_info(String billboard_name) {

    }

    //Test 5: Checks if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule()
    {
        //store billboard schedule in temp HashMap
        HashMap<String,HashMap> temp_list= new HashMap<String,HashMap>();

        temp_list = billboardManager.View_schedule();

        //compare temp HashMap to Billboard_schedule HashMap to see if they match
        // for every entry of temp_list
        for (HashMap.Entry<String, HashMap> temp_listEntry : temp_list.entrySet()) {

            // for every entry of Billboard_schedule
            for (HashMap.Entry<String, HashMap> original_entry : Billboard_schedule.entrySet()) {
                //check if all billboards are listed
                assertEquals(original_entry.getKey(),temp_listEntry.getKey());
            }
        }
    }

    //Test 6: Checks if a specified billboard can be deleted.
    @Test
    public void Delete_billboard()
    {
        billboardManager.Delete_billboard("Billboard_1");

        assertEquals(false, billboardList.containsKey("Billboard_1"));

    }

    //Test 6.0: Schedule billboard that has not been scheduled
    //
    @Test
    public void Schedule_billboardTest() throws Exception
    {

        billboardManager.scheduleBillboard(Billboard_1, 5,LocalDate.parse("22-04-2020"));
        HashMap<LocalDate, Duration> time_duration = new HashMap<LocalDate, Duration>();

        //store scheduled time and duration of billboard one in temp HashMap
        time_duration = Billboard_schedule.get(Billboard_1.Billboard_name);

        //check if time scheduled and duration pair match for Billboard_1
        assertEquals(5, time_duration.get(LocalDate.parse("22-04-2020")));

    }

    //Test 6.1: Schedule billboard that has previously been scheduled in a vacant time slot
    @Test
    public void Schedule_billboardTest2() throws Exception
    {
        //Schedule billboard in a vacant time slot
        billboardManager.scheduleBillboard(Billboard_2, 10,LocalDate.parse("01-05-2020"));
        HashMap<LocalDate, Duration> time_duration = new HashMap<LocalDate, Duration>();

        //store scheduled time and duration of billboard one in temp HashMap
        time_duration = Billboard_schedule.get(Billboard_2.Billboard_name);

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(10, time_duration.get(LocalDate.parse("01-05-2020")));

    }

    //Test 6.2: Schedule billboard in non-vacant time slot - should take precedence over existing billboard scheduled
    //
    @Test
    public void Schedule_billboardTest3() throws Exception
    {
        //Schedule billboard 1 in a vacant time slot
        billboardManager.scheduleBillboard(Billboard_1, 10,LocalDate.parse("01-05-2020"));

        //Schedule billboard 2 in a non-vacant time slot
        billboardManager.scheduleBillboard(Billboard_2, 10,LocalDate.parse("01-05-2020"));


        HashMap<LocalDate, Duration> time_duration_b2 = new HashMap<LocalDate, Duration>();

        //store scheduled time and duration of billboard one in temp HashMap
        time_duration_b2 = Billboard_schedule.get(Billboard_2.Billboard_name);

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(10, time_duration_b2.get(LocalDate.parse("01-05-2020")));



        HashMap<LocalDate, Duration> time_duration_b1 = new HashMap<LocalDate, Duration>();

        //store scheduled time and duration of billboard 1 in temp HashMap
        time_duration_b1 = Billboard_schedule.get(Billboard_1.Billboard_name);

        //check if Billboard 1 is not scheduled for that time

    }

    //Test 7: Checks if a billboard can be removed from schedule.
    @Test
    public void Schedule_Remove_billboard(String billboard_name)
    {

        billboardManager.Schedule_Remove_billboard("Billboard_2");
        assertEquals(false, Billboard_schedule.containsKey("Billboard_2"));

    }

}