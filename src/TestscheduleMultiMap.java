import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestscheduleMultiMap {

    ScheduleMultiMap Billboard_schedule;
    BillboardList billboardList;

    @BeforeEach
    public void setupBbList()
    {
        billboardList = new BillboardList();
    }

    /* Test 1: Constructing a schedule HashMap object
     */
    @BeforeEach
    @Test public void setUpBbSchedule() {
        Billboard_schedule = new ScheduleMultiMap();
    }

    //Test 2: Schedule billboard that has not been scheduled
    //
    @Test
    public void Schedule_billboardTest() throws Exception
    {
        //add billboard being scheduled to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        //schedule a billboard
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5),"none", billboardList.List_Billboards(), "emily");

        //extract schedule info of billboard 1 into collection
        ArrayList<Schedule_Info> schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), schedule_info.get(0).StartTime_Scheduled);
        assertEquals(Duration.ofMinutes(5), schedule_info.get(0).duration);

    }

    //Test 2.1: Schedule billboard that has previously been scheduled in a vacant time slot
    @Test
    public void Schedule_billboardTest2() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        //schedule billboard
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5), "none",billboardList.List_Billboards(), "emily");

        //schedule the same billboard for a different time
        Billboard_schedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-15T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //extract schedule info of billboard 1 into array list
        ArrayList<Schedule_Info>schedule_info = Billboard_schedule.getSchedule("Billboard_1");

        //Test if both entries have been saved
        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-15T10:00:00.00"), schedule_info.get(1).StartTime_Scheduled);
        assertEquals(Duration.ofMinutes(10), schedule_info.get(1).duration);

        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), schedule_info.get(0).StartTime_Scheduled);
        assertEquals(Duration.ofMinutes(5), schedule_info.get(0).duration);
    }

    //Test 2.2: Schedule billboard for 1 viewing  in time slot that matches another billboard scheduled- should
    // take precedence over existing billboard scheduled
    //
    @Test
    public void Matching_Schedules() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 in a vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10),"none", billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 = Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //check if billboard has been removed
        //exception is thrown when retrieving info of non-existent billboard
        assertThrows(Exception.class,() -> {
            Billboard_schedule.getSchedule("Billboard_1");
        });
    }

    //Test 2.3: Schedule billboard for 2 viewings, 1 being in a time slot that matches another billboard scheduled-
    // should take precedence over existing billboard scheduled
    //
    @Test
    public void Matching_Schedules2() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 twice in vacant time slots
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10),"none", billboardList.List_Billboards(), "emily");

        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-06-10T10:00:00.00"),
                Duration.ofMinutes(10),"none", billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 = Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is NOT scheduled for 01-05-2021 for 10 mins
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_Info>  time_duration_b1 = Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1 = time_duration_b1.get(0).StartTime_Scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is not scheduled for that time
        assertNotEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduled_B1);
    }

    //Test 2.3: Schedule billboard for 2 viewings, 1 being in a time slot that matches another billboard scheduled-
    // should take precedence over existing billboard scheduled
    //Start and end time of existing viewing is between start and end of new schedule
    @Test
    public void newScheduleFullOverlap() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 - displayed from 10:00 -10:01
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-06-10T10:00:00.00"),
                Duration.ofMinutes(1),"none", billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 to fully overlap billboard 1 in the schedule - displayed from 9:59 - 10:09
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-06-10T09:59:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //-------------------------------------------------------------
        //Test that billboard 1 is NOT in the schedule
        assertEquals(false, Billboard_schedule.Schedule_MultiMap.containsKey("Billboard_1"));

        //Test that billboard 2 is in the schedule
        assertEquals(true, Billboard_schedule.Schedule_MultiMap.containsKey("Billboard_2"));
    }

    //Test 2.4: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //Equal start times
    @Test
    public void OverlapSchedule_startMatch() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot from 10:00-10:05 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(5), "none",billboardList.List_Billboards(),"emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 =  Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(5), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_Info>  time_duration_b1 =Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1 = time_duration_b1.get(0).StartTime_Scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"),startTimeScheduled_B1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.5: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //Start time between start and end of existing viewing scheduled
    @Test
    public void OverlapSchedule_startBetween() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot from 10:05-10:15 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:05:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 =  Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_Info>  time_duration_b1 = Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1 = time_duration_b1.get(0).StartTime_Scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:00 - 10:05 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduled_B1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.6: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //End time of new viewing is equal to end time of existing viewing
    @Test
    public void OverlapSchedule_endsEqual() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot from 10:05-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:05:00.00"),
                Duration.ofMinutes(5), "none",billboardList.List_Billboards(), "emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 =  Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(5), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_Info>  time_duration_b1 =Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1 = time_duration_b1.get(0).StartTime_Scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:00 - 10:05 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduled_B1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.6: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //End time between start and end of existing viewing scheduled
    @Test
    public void OverlapSchedule_endsBetween() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot from 9:55-10:05 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T09:55:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(), "emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 =  Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T09:55:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_Info>  time_duration_b1 =Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1 = time_duration_b1.get(0).StartTime_Scheduled;
        Duration durationB1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"),startTimeScheduled_B1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.6: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //Start and end time between start and end of existing viewing scheduled
    @Test
    public void OverlapSchedule_StartEndBetween() throws Exception
    {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");

        billboardList.Create_edit_Billboard("Billboard_2", "hello world", "#000000",
                "No Image", "emily");

        //Schedule billboard 1 in a non-vacant time slot from 9:55-10:10 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-01T09:55:00.00"),
                Duration.ofMinutes(15), "none",billboardList.List_Billboards(), "emily");

        //Schedule billboard 2 in a vacant time slot from 10:00-10:05 on 01-05-2021
        Billboard_schedule.scheduleBillboard("Billboard_2",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(5), "none",billboardList.List_Billboards(), "emily");


        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<Schedule_Info> time_duration_b2 =  Billboard_schedule.getSchedule("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = time_duration_b2.get(0).StartTime_Scheduled;
        Duration durationB2 = time_duration_b2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(5), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is now scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<Schedule_Info>  time_duration_b1 =Billboard_schedule.getSchedule("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1_1 = time_duration_b1.get(0).StartTime_Scheduled;
        Duration durationB1_1 = time_duration_b1.get(0).duration;

        //check if Billboard 1 is scheduled for 9:55 - 10:00 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T09:55:00.00"),startTimeScheduled_B1_1);
        assertEquals(Duration.ofMinutes(5), durationB1_1);

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B1_2 = time_duration_b1.get(1).StartTime_Scheduled;
        Duration durationB1_2 = time_duration_b1.get(1).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"),startTimeScheduled_B1_2);
        assertEquals(Duration.ofMinutes(5), durationB1_2);
    }

    //Test 2.6: Schedule billboard for an invalid time
    //
    @Test
    public void Schedule_invalidTime()
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard for date in the past
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2000-01-01T10:00:00.00"),
                    Duration.ofMinutes(10), "none",billboardList.List_Billboards(),"emily");
        });
    }

    //Test 2.7: Schedule billboard for an invalid duration
    //
    @Test
    public void Schedule_NegDuration() throws Exception {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");
        assertThrows(Exception.class,() -> {
            //schedule billboard for negative
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-01-01T10:00:00.00"),
                    Duration.ofMinutes(-1),"none", billboardList.List_Billboards(), "emily");
        });
    }

    //Test 2.8: Schedule billboard for an invalid duration
    //
    @Test
    public void Schedule_ZerDuration() throws Exception {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");
        assertThrows(Exception.class,() -> {
            //schedule billboard for negative
            Billboard_schedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-01-01T10:00:00.00"),
                    Duration.ofMinutes(0), "none",billboardList.List_Billboards(),"emily");
        });
    }

    //Test 2.9: Schedule billboard that does not exist
    @Test
    public void Schedule_invalidBillboard()
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard that does not exist
            Billboard_schedule.scheduleBillboard("nonexistent",LocalDateTime.parse("01-01-2021T10:00:00.00"),
                    Duration.ofMinutes(10),"none", billboardList.List_Billboards(), "emily");
        });
    }

    //Test 3: Checks if exception is thrown when retrieving schedule information from a billboard that does not exist
    @Test
    public void Retrieve_invalid_billboard ()
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
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image","emily");
        billboardList.Create_edit_Billboard("Billboard_2", "hello", "#000000",
                "No Image","emily");
        billboardList.Create_edit_Billboard("Billboard_3", "goodbye", "#000000",
                "No Image","emily");

        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), "none", billboardList.List_Billboards(), "emily");
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none",billboardList.List_Billboards(),"emily");
        Billboard_schedule.scheduleBillboard("Billboard_3", LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5),"none", billboardList.List_Billboards(), "emily");

        //remove billboard from schedule
        Billboard_schedule.Schedule_Remove_billboard("Billboard_2",Billboard_schedule.getSchedule("Billboard_2").get(0));

        //check if billboard has been removed
        //exception is thrown when retrieving info of non-existent billboard
        assertThrows(Exception.class,() -> {
            Billboard_schedule.getSchedule("Billboard_2");
        });
    }

    //Test 4: Checks if a single viewing of a billboard, scheduled multiple times, can be removed from the schedule.
    @Test
    public void Schedule_Remove_Same_billboard() throws Exception
    {
        //add billboard to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image","emily");

        //schedule billboard multiple times
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), "none", billboardList.List_Billboards(), "emily");
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(10), "none",billboardList.List_Billboards(),"emily");

        //get viewings of billboard 1
        ArrayList<Schedule_Info> billboard_viewings = Billboard_schedule.getSchedule("Billboard_1");

        //schedule info of viewing being removed
        Schedule_Info removed_schedule_info= new Schedule_Info(LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), "none", "emily");

        //remove billboard from schedule
        Billboard_schedule.Schedule_Remove_billboard("Billboard_1",removed_schedule_info);

        //get viewings of billboard 1
        ArrayList<Schedule_Info> viewings = Billboard_schedule.getSchedule("Billboard_1");

        //check if 1 viewing is in list
        assertEquals(1, viewings.size());

        //check if viewing in list is correct
        assertEquals(LocalDateTime.parse("2021-05-04T10:00:00.00"), viewings.get(0).StartTime_Scheduled);


    }

    //Test 4.2: Remove scheduling of billboard that is not in the schedule - nonexistent name
    //
    @Test
    public void RemoveSchedule_invalidBillboardName() throws Exception
    {
        Schedule_Info schedule_info = new Schedule_Info(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15),"none", "emily");


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
        billboardList.Create_edit_Billboard("Billboard_1", "hello","#000000","No Image", "emily");

        //schedule billboard twice
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-10T10:00:00.00"), Duration.ofMinutes(10), "none",
                billboardList.List_Billboards(), "emily");

        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-11T10:00:00.00"), Duration.ofMinutes(10), "none",
                billboardList.List_Billboards(), "emily");

        //schedule info that does not match bb scheduled
        Schedule_Info schedule_info = new Schedule_Info(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15),"none", "emily");

        assertThrows(Exception.class,() -> {
            //remove viewing from schedule that does not exist
            Billboard_schedule.Schedule_Remove_billboard("Billboard_1", schedule_info);
        });
    }

    //Test 5: Test if the billboard schedule can be viewed correctly.
    @Test
    public void View_schedule() throws Exception {
        //add billboards to billboardList
        billboardList.Create_edit_Billboard("Billboard_1", "new billboard", "#000000",
                "No Image", "emily");
        billboardList.Create_edit_Billboard("Billboard_2", "hello", "#000000",
                "No Image", "emily");
        billboardList.Create_edit_Billboard("Billboard_3", "goodbye", "#000000",
                "No Image", "emily");

        //add billboards to schedule
        Billboard_schedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-05T10:00:00.00"),
                Duration.ofMinutes(10), "none", billboardList.List_Billboards(), "emily");
        Billboard_schedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), "none", billboardList.List_Billboards(), "emily");
        Billboard_schedule.scheduleBillboard("Billboard_3", LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5), "none", billboardList.List_Billboards(), "emily");

        //store billboard schedule in MultiMap
        MultiMap<String, Schedule_Info> viewSchedule_list = Billboard_schedule.View_schedule();

        //check if billboards are in viewSchedule_list View_schedule output
        assertTrue(viewSchedule_list.containsKey("Billboard_1"));
        assertTrue(viewSchedule_list.containsKey("Billboard_2"));
        assertTrue(viewSchedule_list.containsKey("Billboard_3"));

        //check if time scheduled of viewings, associated with each Billboard, are correct
        assertEquals(LocalDateTime.parse("2021-05-05T10:00:00.00"), viewSchedule_list.get("Billboard_1").get(0).StartTime_Scheduled);
        assertEquals(LocalDateTime.parse("2021-05-04T10:00:00.00"), viewSchedule_list.get("Billboard_2").get(0).StartTime_Scheduled);
        assertEquals(LocalDateTime.parse("2021-05-03T10:00:00.00"), viewSchedule_list.get("Billboard_3").get(0).StartTime_Scheduled);

        //check if duration of viewings, associated with each Billboard, are correct
        assertEquals(Duration.ofMinutes(10), viewSchedule_list.get("Billboard_1").get(0).duration);
        assertEquals(Duration.ofMinutes(15), viewSchedule_list.get("Billboard_2").get(0).duration);
        assertEquals(Duration.ofMinutes(5), viewSchedule_list.get("Billboard_3").get(0).duration);

    }

}
