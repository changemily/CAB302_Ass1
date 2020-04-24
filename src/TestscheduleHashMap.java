import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestscheduleHashMap<E> {

    scheduleHashMap Billboard_schedule;

    /* Test 1: Constructing a schedule HashMap object
     */
    @BeforeEach
    @Test public void setUpBbSchedule() {
        Billboard_schedule = new scheduleHashMap<E>();
    }

    //Test 2.0: Schedule billboard that has not been scheduled
    //
    @Test
    public void Schedule_billboardTest() throws Exception
    {
        //schedule a billboard
        Billboard_schedule.scheduleBillboard("Billboard_1", 5, LocalDate.parse("22-04-2020"));

        //extract schedule info of billboard 1 into array list
        ArrayList<E> schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if schedule information matches info entered
        assertEquals(5, schedule_info.get(1));
        assertEquals(LocalDate.parse("22-04-2020"), schedule_info.get(2));

    }

    //Test 6.1: Schedule billboard that has previously been scheduled in a vacant time slot
    @Test
    public void Schedule_billboardTest2() throws Exception
    {

        //schedule a billboard
        Billboard_schedule.scheduleBillboard("Billboard_1", 5, LocalDate.parse("22-04-2020"));
        //extract schedule info of billboard 1 into array list
        ArrayList<E> schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //schedule the same billboard for a different time
        Billboard_schedule.scheduleBillboard("Billboard_1", 10, LocalDate.parse("15-04-2020"));
        //extract schedule info of billboard 1 into array list
        ArrayList<E> schedule_info2 = Billboard_schedule.getSchedule("Billboard_1");


        //Test if both entries have been saved
        //Test if schedule information matches info entered
        assertEquals(5, schedule_info.get(1));
        assertEquals(LocalDate.parse("22-04-2020"), schedule_info.get(2));

        //Test if schedule information matches info entered
        assertEquals(10, schedule_info2.get(1));
        assertEquals(LocalDate.parse("15-04-2020"), schedule_info2.get(2));

    }

    //Test 6.2: Schedule billboard in non-vacant time slot - should take precedence over existing billboard scheduled
    //
    @Test
    public void Schedule_billboardTest3() throws Exception
    {
        //Schedule billboard 1 in a vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_1", 10,LocalDate.parse("01-05-2020"));

        //Schedule billboard 2 in a non-vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_2", 10,LocalDate.parse("01-05-2020"));

        //ArrayList to store time and duration of billboard 2
        ArrayList<E> time_duration_b2 = new ArrayList<E>();

        //store scheduled time and duration of billboard 2 in an ArrayList
        time_duration_b2 = Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDate time_scheduledB2 = (LocalDate)time_duration_b2.get(1);
        int durationB2 = (int)time_duration_b2.get(2);

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDate.parse("01-05-2020"), time_scheduledB2);
        assertEquals(10, durationB2);

        //-------------------------------------------------------------

        //ArrayList to store time and duration of billboard 1
        ArrayList<E> time_duration_b1 = new ArrayList<E>();

        //store scheduled time and duration of billboard 1 in an ArrayList
        time_duration_b1 = Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDate time_scheduledB1 = (LocalDate)time_duration_b1.get(1);
        int durationB1 = (int)time_duration_b1.get(2);

        //check if Billboard 1 is not scheduled for that time
        assertNotEquals(LocalDate.parse("01-05-2020"),time_scheduledB1);
        assertNotEquals(10, durationB1);
    }

    //Test 7: Checks if a billboard can be removed from schedule.
    @Test
    public void Schedule_Remove_billboard(String billboard_name)
    {
        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", 10,LocalDate.parse("01-05-2020"));

        //remove billboard from schedule
        Billboard_schedule.Schedule_Remove_billboard("Billboard_2");
        assertEquals(false, Billboard_schedule.containsKey("Billboard_2"));

    }

    //Test 2: Test if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule()
    {
        //store billboard schedule in temp HashMap
        HashMap<String, HashMap> temp_list= new HashMap<String,HashMap>();

        temp_list = Billboard_schedule.View_schedule();

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



}
