import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestscheduleHashMap<E> {

    ArrayList<E> schedule_info;

    HashMap<String, ArrayList> Billboard_schedule;

    //Test 5: Checks if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule()
    {
        //store billboard schedule in temp HashMap
        HashMap<String, HashMap> temp_list= new HashMap<String,HashMap>();

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

    //Test 6.0: Schedule billboard that has not been scheduled
    //
    @Test
    public void Schedule_billboardTest() throws Exception
    {

        billboardManager.scheduleBillboard(Billboard_1, 5, LocalDate.parse("22-04-2020"));
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

        //Hashmap to store time and duration of billboard 2
        HashMap<LocalDate, Duration> time_duration_b2 = new HashMap<LocalDate, Duration>();

        //store scheduled time and duration of billboard 1 in temp HashMap
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
