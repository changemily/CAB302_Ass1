import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestscheduleMultiMap {

    scheduleMultiMap Billboard_schedule;
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
        Billboard_schedule = new scheduleMultiMap();
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
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5),"none", billboardList);

        //extract schedule info of billboard 1 into collection
        ArrayList<Schedule_info> schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), schedule_info.get(0).Time_scheduled);
        assertEquals(Duration.ofMinutes(5), schedule_info.get(0).duration);

    }

    //Test 2.2: Schedule billboard that has previously been scheduled in a vacant time slot
    @Test
    public void Schedule_billboardTest2() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");

        //schedule billboard
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5), "none",billboardList);

        //schedule the same billboard for a different time
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-15T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList);

        //extract schedule info of billboard 1 into array list
        ArrayList<Schedule_info>schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if both entries have been saved
        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-15T10:00:00.00"), schedule_info.get(0).Time_scheduled);
        assertEquals(Duration.ofMinutes(10), schedule_info.get(0).duration);

        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), schedule_info.get(1).Time_scheduled);
        assertEquals(Duration.ofMinutes(5), schedule_info.get(1).duration);
    }

    //Test 2.3: Schedule billboard in time slot that matches another billboard scheduled- should take precedence over existing billboard scheduled
    //
    @Test
    public void Matching_Schedules() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "red",
                "image.jpg");

        //Schedule billboard 1 in a vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10),"none", billboardList);

        //Schedule billboard 2 in a non-vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList);

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_info> time_duration_b2 = Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime time_scheduledB2 = time_duration_b2.get(0).Time_scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), time_scheduledB2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is NOT scheduled for 01-05-2021 for 10 mins
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_info>  time_duration_b1 = Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime time_scheduledB1 = time_duration_b1.get(0).Time_scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is not scheduled for that time
        assertNotEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),time_scheduledB1);
    }

    //Test 2.4: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //
    @Test
    public void OverlapSchedule() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "red",
                "image.jpg");

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList);

        //Schedule billboard 2 in a non-vacant time slot from 10:00-10:05 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(5), "none",billboardList);

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_info> time_duration_b2 =  Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime time_scheduledB2 = time_duration_b2.get(0).Time_scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), time_scheduledB2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is now scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_info>  time_duration_b1 =Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime time_scheduledB1 = time_duration_b1.get(0).Time_scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),time_scheduledB1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.5: Schedule billboard for an invalid time
    //
    @Test
    public void Schedule_invalidTime() throws Exception
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard for date in the past
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2000-01-01T10:00:00.00"),
                    Duration.ofMinutes(10), "none",billboardList);
        });
    }

    //Test 2.6: Schedule billboard for an invalid duration
    //
    @Test
    public void Schedule_NegDuration() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        assertThrows(Exception.class,() -> {
            //schedule billboard for negative
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-01-01T10:00:00.00"),
                    Duration.ofMinutes(-1),"none", billboardList);
        });
    }

    //Test 2.7: Schedule billboard for an invalid duration
    //
    @Test
    public void Schedule_ZerDuration() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        assertThrows(Exception.class,() -> {
            //schedule billboard for negative
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-01-01T10:00:00.00"),
                    Duration.ofMinutes(0), "none",billboardList);
        });
    }

    //Test 2.8: Schedule billboard that does not exist
    @Test
    public void Schedule_invalidBillboard() throws Exception
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard that does not exist
            Billboard_schedule.scheduleBillboard("nonexistent",LocalDateTime.parse("01-01-2021T10:00:00.00"),
                    Duration.ofMinutes(10),"none", billboardList);
        });
    }

    //Test 3: Checks if exception is thrown when retrieving schedule information from a billboard that does not exist
    @Test
    public void Retrieve_invalid_billboard () throws Exception
    {
        assertThrows(Exception.class,() -> {
            Billboard_schedule.getSchedule("nonexistent");
        });
    }

    //Test 4: Checks if a billboard can be removed from the schedule.
    @Test
    public void Schedule_Remove_billboard() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_2", "hello", "red",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_3", "goodbye", "black",
                "image.jpg");

        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), "none", billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none",billboardList);
        Schedule_info BB2_schedule_info = new Schedule_info(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none");
        Billboard_schedule.scheduleBillboard("Billboard_3", LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5),"none", billboardList);

        //remove billboard from schedule
        Billboard_schedule.Schedule_Remove_billboard("Billboard_2",BB2_schedule_info);

        //check if billboard has been removed
        //exception is thrown when retrieving info of non-existent billboard
        assertThrows(Exception.class,() -> {
            Billboard_schedule.getSchedule("Billboard_2");
        });
    }

    //Test 4.2: Remove scheduling of billboard that is not in the schedule - nonexistent name
    //
    @Test
    public void RemoveSchedule_invalidBillboardName() throws Exception
    {
        Schedule_info schedule_info = new Schedule_info(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none");


        assertThrows(Exception.class,() -> {
            //remove billboard from schedule that does not exist
            Billboard_schedule.Schedule_Remove_billboard("nonexistent", schedule_info);
        });
    }

    //Test 4.3: Remove scheduling of billboard that is not in the schedule - nonexistent name,time & duration combination
    //
    @Test
    public void RemoveSchedule_invalidBillboard() throws Exception
    {
        //add billboard to billboard list
        billboardList.Create_edit_Billboard("Billboard_1", "hello","red","No Image",
                LocalDateTime.parse("2021-05-10T10:00:00.00"), 10);
        //schedule billboard
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-10T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList);

        //schedule info that does not match bb scheduled
        Schedule_info schedule_info = new Schedule_info(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none");

        assertThrows(Exception.class,() -> {
            //remove billboard from schedule that does not exist
            Billboard_schedule.Schedule_Remove_billboard("Billboard_1", schedule_info);
        });
    }

    //Test 5: Test if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule() throws Exception {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "blue",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_2", "hello", "red",
                "image.jpg");
        billboardList.Create_edit_Billboard("Billboard_3", "goodbye", "black",
                "image.jpg");

        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-05T10:00:00.00"),
                Duration.ofMinutes(10), "none", billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none", billboardList);
        Billboard_schedule.scheduleBillboard("Billboard_3", LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5), "none", billboardList);

        //store billboard schedule in temp HashMap
        MultiMap<String, Schedule_info> viewSchedule_list = Billboard_schedule.View_schedule();

        //check if output matches stored data and is in alphabetical order
        HashMap<String, Schedule_info> ExpectedSchedule_list = new HashMap<String, Schedule_info>();


        //Create ordered HashMap to compare to
        Schedule_info schedule_info3 = new Schedule_info(LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5), "none");
        ExpectedSchedule_list.put("Billboard_3", schedule_info3);

        Schedule_info schedule_info2 = new Schedule_info(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none");
        ExpectedSchedule_list.put("Billboard_2", schedule_info2);

        Schedule_info schedule_info1 = new Schedule_info(LocalDateTime.parse("2021-05-05T10:00:00.00"),
                Duration.ofMinutes(10), "none");
        ExpectedSchedule_list.put("Billboard_1", schedule_info1);

        //compare expected HashMap to Billboard_schedule HashMap to see if they match
        // for every entry of viewSchedule_list
        for (String billboard_name : viewSchedule_list.keySet()) {
            //create ArrayList to store viewings of billboard
            ArrayList<Schedule_info> viewings = viewSchedule_list.get(billboard_name);

            //for every viewing of billboard
            for (Schedule_info viewing : viewings) {

                // for every entry of Billboard_schedule
                for (HashMap.Entry<String, Schedule_info> expected_listEntry : ExpectedSchedule_list.entrySet()) {

                    //check if all billboards and info are listed correctly and ordered by date
                    assertEquals(expected_listEntry.getKey(), billboard_name);
                    assertEquals(expected_listEntry.getValue(), viewing);
                }
            }
        }
    }
}