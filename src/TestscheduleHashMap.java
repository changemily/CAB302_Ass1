import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestscheduleHashMap {

    scheduleHashMap Billboard_schedule;
    billboardHashMap billboardList;

    @BeforeEach
    public void setupBbList()
    {
        billboardList = new billboardHashMap();
    }
    /* Test 1: Constructing a schedule HashMap object
     */
    @BeforeEach
    @Test public void setUpBbSchedule() {
        Billboard_schedule = new scheduleHashMap<Object>();
    }

    //Test 2: Schedule billboard that has not been scheduled
    //
    @Test
    public void Schedule_billboardTest() throws Exception
    {
        //add billboard being scheduled to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");

        //schedule a billboard
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDate.parse("22-04-2021"), 5, billboardList);

        //extract schedule info of billboard 1 into array list
        ArrayList<ArrayList<Object>>schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if schedule information matches info entered
        assertEquals(LocalDate.parse("22-04-2021"), schedule_info.get(1).get(1));
        assertEquals(5, schedule_info.get(1).get(2));

    }

    //Test 2.2: Schedule billboard that has previously been scheduled in a vacant time slot
    @Test
    public void Schedule_billboardTest2() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg",LocalDate.parse("22-04-2021"), 5);

        //schedule the same billboard for a different time
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDate.parse("15-04-2021"),10, billboardList);
        //extract schedule info of billboard 1 into array list
        ArrayList<ArrayList<Object>> schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if both entries have been saved
        //Test if schedule information matches info entered
        assertEquals(LocalDate.parse("15-04-2021"), schedule_info.get(1).get(1));
        assertEquals(10, schedule_info.get(1).get(2));

        //Test if schedule information matches info entered
        assertEquals(LocalDate.parse("22-04-2021"), schedule_info.get(2).get(1));
        assertEquals(5, schedule_info.get(2).get(2));
    }

    //Test 2.3: Schedule billboard in non-vacant time slot - should take precedence over existing billboard scheduled
    //
    @Test
    public void Schedule_billboardTest3() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "red",
                "image.jpg");

        //Schedule billboard 1 in a vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDate.parse("01-05-2021"),10, billboardList);

        //Schedule billboard 2 in a non-vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDate.parse("01-05-2021"), 10, billboardList);

        //ArrayList to store scheduled times and durations of billboard 2
        ArrayList<ArrayList<Object>>  time_duration_b2 = new ArrayList<ArrayList<Object>> ();

        //store scheduled time and duration of billboard 2 in an ArrayList
        time_duration_b2 = Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDate time_scheduledB2 = (LocalDate)time_duration_b2.get(1).get(1);
        int durationB2 = (int)time_duration_b2.get(1).get(2);

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDate.parse("01-05-2021"), time_scheduledB2);
        assertEquals(10, durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is NOT scheduled for 01-05-2021 for 10 mins
        //ArrayList to store scheduled times and durations of billboard 1
        ArrayList<ArrayList<Object>>  time_duration_b1 = new ArrayList<ArrayList<Object>>();

        //store scheduled time and duration of billboard 1 in an ArrayList
        time_duration_b1 = Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDate time_scheduledB1 = (LocalDate)time_duration_b1.get(1).get(1);
        int durationB1 = (int)time_duration_b1.get(1).get(2);

        //check if Billboard 1 is not scheduled for that time
        assertNotEquals(LocalDate.parse("01-05-2021"),time_scheduledB1);
        assertNotEquals(10, durationB1);
    }

    //Test 2.4: Schedule billboard for an invalid time
    //
    @Test
    public void Schedule_invalidTime() throws Exception
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard for date in the past
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDate.parse("01-01-2000"),10, billboardList);
        });
    }

    //Test 2.5: Schedule billboard for an invalid duration
    //
    @Test
    public void Schedule_NegDuration() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        assertThrows(Exception.class,() -> {
            //schedule billboard for negative
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDate.parse("01-01-2000"),-1, billboardList);
        });
    }

    //Test 2.6: Schedule billboard for an invalid duration
    //
    @Test
    public void Schedule_ZerDuration() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        assertThrows(Exception.class,() -> {
            //schedule billboard for negative
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDate.parse("01-01-2000"),0, billboardList);
        });
    }

    //Test 2.7: Schedule billboard that does not exist
    @Test
    public void Schedule_invalidBillboard() throws Exception
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard that does not exist
            Billboard_schedule.scheduleBillboard("nonexistent",LocalDate.parse("01-01-2000"),10, billboardList);
        });
    }

    //Test 3: Checks if exception is thrown when retrieving information from a billboard that does not exist
    @Test
    public void Retrieve_invalid_billboard (String billboard_name) throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");

        assertThrows(Exception.class,() -> {
            Billboard_schedule.getSchedule("Billboard");
        });
    }

    //Test 4: Checks if a billboard can be removed from the schedule.
    @Test
    public void Schedule_Remove_billboard(String billboard_name) throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_2", "hello", "red",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_3", "goodbye", "black",
                "image.jpg");

        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDate.parse("10-05-2021"), 10, billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDate.parse("04-05-2021"), 15, billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_3", LocalDate.parse("03-05-2021"), 5, billboardList);

        //remove billboard from schedule
        Billboard_schedule.Schedule_Remove_billboard("Billboard_2",
                Billboard_schedule.getSchedule("Billboard_2"));

        //check if billboard has been removed
        //exception is thrown when retrieving info of non-existent billboard
        assertThrows(Exception.class,() -> {
            Billboard_schedule.getSchedule("Billboard_2");
        });
    }

    //Test 4.2: Remove schedule of billboard that is not in the schedule
    //
    @Test
    public void RemoveSchedule_invalidBillboard() throws Exception
    {
        ArrayList <Object> schedule_info = new ArrayList<Object>();

        schedule_info.add(LocalDate.parse("03-05-2021"));

        schedule_info.add(5);

        assertThrows(Exception.class,() -> {
            //remove billboard from schedule that does not exist
            Billboard_schedule.Schedule_Remove_billboard("nonexistent", schedule_info);
        });
    }

    //Test 5: Test if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_2", "hello", "red",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_3", "goodbye", "black",
                "image.jpg");

        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDate.parse("10-05-2021"), 10, billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDate.parse("04-05-2021"), 15, billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_3", LocalDate.parse("03-05-2021"), 5, billboardList);

        //store billboard schedule in temp HashMap
        HashMap<String, ArrayList<Object>> viewSchedule_list= new HashMap<String, ArrayList<Object>>();

        //store output in viewSchedule_list
        viewSchedule_list = Billboard_schedule.View_schedule();

        //check if output matches stored data and is in alphabetical order
        HashMap<String, ArrayList<Object>> ExpectedSchedule_list= new HashMap<String, ArrayList<Object>>();


        //Create ordered HashMap to compare to
        ArrayList <Object> schedule_info3 = new ArrayList<Object>();
        schedule_info3.add(LocalDate.parse("03-05-2021"));
        schedule_info3.add(5);
        ExpectedSchedule_list.put("Billboard_3", schedule_info3);

        ArrayList <Object> schedule_info2 = new ArrayList<Object>();
        schedule_info2.add(LocalDate.parse("04-05-2021"));
        schedule_info2.add(15);
        ExpectedSchedule_list.put("Billboard_2", schedule_info2);

        ArrayList <Object> schedule_info1 = new ArrayList<Object>();
        schedule_info1.add(LocalDate.parse("10-05-2021"));
        schedule_info1.add(10);
        ExpectedSchedule_list.put("Billboard_1", schedule_info1);


        //compare expected HashMap to Billboard_schedule HashMap to see if they match
        // for every entry of viewSchedule_list
        for (HashMap.Entry<String, ArrayList<Object>> view_listEntry : viewSchedule_list.entrySet()) {

            // for every entry of Billboard_schedule
            for (HashMap.Entry<String, ArrayList<Object>> expected_listEntry : ExpectedSchedule_list.entrySet()) {

                //check if all billboards and info are listed correctly and ordered by date
                assertEquals(expected_listEntry.getKey(),view_listEntry.getKey());
                assertEquals(expected_listEntry.getValue(),view_listEntry.getValue());
            }
        }

    }

}
