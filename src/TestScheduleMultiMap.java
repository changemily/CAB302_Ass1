import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestScheduleMultiMap {

    //define xml string to use in tests
    public static final String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <picture url=\"https://cloudstor.aarnet.edu.au/plus/s/vYipYcT3VHa1uNt/download\" />\n" +
            "    <information>Billboard with picture (with URL attribute) and information text only. The picture is now centred within the top 2/3 of the image and the information text is centred in the remaining space below the image.</information>\n" +
            "</billboard>";

    ScheduleMultiMap billboardSchedule;
    BillboardList billboardList;

    //construct billboard list to use in tests
    @BeforeEach
    public void setupBbList()
    {
        billboardList = new BillboardList();
    }

    //Test 1: Constructing a schedule ScheduleMultiMap object
    @BeforeEach
    @Test public void setUpBbSchedule() {
        billboardSchedule = new ScheduleMultiMap();
    }

    //Test 2: Schedule billboard that has not been scheduled
    @Test
    public void scheduleBillboardTest() throws Exception
    {
        //add billboard being scheduled to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);

        //schedule billboard
        billboardSchedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5),0, billboardList.listBillboards(), "jarod");

        //extract schedule info of billboard 1 into ArrayList
        ArrayList<ScheduleInfo> scheduleInfo = billboardSchedule.getViewings("Billboard_1");

        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), scheduleInfo.get(0).startTimeScheduled);
        assertEquals(Duration.ofMinutes(5), scheduleInfo.get(0).duration);

    }

    //Test 2.1: Schedule billboard that has previously been scheduled in a vacant time slot
    @Test
    public void scheduleBillboardTest2() throws Exception
    {
        //add billboard to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);

        //schedule billboard
        billboardSchedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5), 0,billboardList.listBillboards(), "jarod");

        //schedule the same billboard for a different time
        billboardSchedule.scheduleBillboard("Billboard_1",  LocalDateTime.parse("2021-04-15T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //extract schedule info of billboard 1 into an array list
        ArrayList<ScheduleInfo>scheduleInfo = billboardSchedule.getViewings("Billboard_1");

        //Test if both entries have been saved
        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-15T10:00:00.00"), scheduleInfo.get(1).startTimeScheduled);
        assertEquals(Duration.ofMinutes(10), scheduleInfo.get(1).duration);

        //Test if schedule information matches info entered
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), scheduleInfo.get(0).startTimeScheduled);
        assertEquals(Duration.ofMinutes(5), scheduleInfo.get(0).duration);
    }

    //Test 2.2: Schedule billboard for 1 viewing in time slot that matches another billboard scheduled- should
    //take precedence over existing billboard scheduled
    //viewings match
    @Test
    public void matchingSchedules() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 in a vacant time slot - 10:00-10:10
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10),0, billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a non-vacant time slot - 10:00-10:10
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 = billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //check if billboard 1 has been removed
        //exception is thrown when retrieving info of non-existent billboard
        assertThrows(Exception.class,() -> {
            billboardSchedule.getViewings("Billboard_1");
        });
    }


    //Test 2.3: Schedule billboard for 2 viewings, 1 being in a time slot that matches another billboard scheduled-
    //should take precedence over existing billboard scheduled
    @Test
    public void matchingSchedules2() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 twice in vacant time slots
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10),0, billboardList.listBillboards(), "jarod");

        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-06-10T10:00:00.00"),
                Duration.ofMinutes(10),0, billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a non-vacant time slot
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 = billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is NOT scheduled for 01-05-2021 for 10 mins
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1 = timeDurationB1.get(0).startTimeScheduled;

        //check if Billboard 1 is not scheduled for that time
        assertNotEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduledB1);
    }

    //Test 2.4: Schedule billboard for 2 viewings, 1 being in a time slot that matches another billboard scheduled-
    //should take precedence over existing billboard scheduled
    //existing viewing is fully overlapped by new viewing
    @Test
    public void newScheduleFullOverlap() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1","jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2","jarod", xmlFile);

        //Schedule billboard 1 - displayed from 10:00 -10:01
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-06-10T10:00:00.00"),
                Duration.ofMinutes(1),0, billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 to fully overlap billboard 1 in the schedule - displayed from 9:59 - 10:09
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-06-10T09:59:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //-------------------------------------------------------------
        //Test that billboard 1 is NOT in the schedule
        assertEquals(false, billboardSchedule.scheduleMultiMap.containsKey("Billboard_1"));

        //Test that billboard 2 is in the schedule
        assertEquals(true, billboardSchedule.scheduleMultiMap.containsKey("Billboard_2"));
    }

    //Test 2.5: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //start times of existing and new viewing are equal
    @Test
    public void overlapScheduleStartMatch() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a non-vacant time slot from 10:00-10:05 on 01-05-2021, matching billboard 1 start time
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(5), 0,billboardList.listBillboards(),"jarod");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 =  billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(5), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1 = timeDurationB1.get(0).startTimeScheduled;
        Duration durationB1 = timeDurationB1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"),startTimeScheduledB1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.6: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //new start time during duration of existing viewing
    @Test
    public void overlapScheduleStartBetween() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a non-vacant time slot from 10:05-10:15 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:05:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 =  billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"), startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1 = timeDurationB1.get(0).startTimeScheduled;
        Duration durationB1 = timeDurationB1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:00 - 10:05 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduledB1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.7: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //End time of new viewing is equal to end time of existing viewing
    @Test
    public void overlapScheduleEndsEqual() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "emily");

        //Schedule billboard 2 in a non-vacant time slot from 10:05-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T10:05:00.00"),
                Duration.ofMinutes(5), 0,billboardList.listBillboards(), "emily");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 =  billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"), startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(5), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1 = timeDurationB1.get(0).startTimeScheduled;
        Duration durationB1 = timeDurationB1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:00 - 10:05 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduledB1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.8: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //End time of new billboard is during the duration of an existing viewing
    @Test
    public void overlapScheduleEndsBetween() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 in a vacant time slot from 10:00-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a non-vacant time slot from 9:55-10:05 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-01T09:55:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 =  billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduled_B2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T09:55:00.00"), startTimeScheduled_B2);
        assertEquals(Duration.ofMinutes(10), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1 = timeDurationB1.get(0).startTimeScheduled;
        Duration durationB1 = timeDurationB1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"),startTimeScheduledB1);
        assertEquals(Duration.ofMinutes(5), durationB1);
    }

    //Test 2.9: Schedule billboard that overlaps non-vacant time slot - should take precedence over existing billboard scheduled
    //Start and end time of new viewing is during the duration of an existing viewing
    @Test
    public void overlapScheduleStartEndBetween() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);

        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);

        //Schedule billboard 1 in a non-vacant time slot from 9:55-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-01T09:55:00.00"),
                Duration.ofMinutes(15), 0,billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a vacant time slot from 10:00-10:05 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_2",LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(5), 0,billboardList.listBillboards(), "jarod");


        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB2 =  billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"), startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(5), durationB2);

        //-------------------------------------------------------------
        //Test that billboard 1 is now scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1_1 = timeDurationB1.get(0).startTimeScheduled;
        Duration durationB1_1 = timeDurationB1.get(0).duration;

        //check if Billboard 1 is scheduled for 9:55 - 10:00 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T09:55:00.00"),startTimeScheduledB1_1);
        assertEquals(Duration.ofMinutes(5), durationB1_1);

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1_2 = timeDurationB1.get(1).startTimeScheduled;
        Duration durationB1_2 = timeDurationB1.get(1).duration;

        //check if Billboard 1 is scheduled for 10:05 - 10:10 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"),startTimeScheduledB1_2);
        assertEquals(Duration.ofMinutes(5), durationB1_2);
    }

    //Test 2.10: Schedule billboard that overlaps 2 non-vacant time slots - should take precedence over existing billboard scheduled
    @Test
    public void overlap2Viewings() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2","jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_3","jarod", xmlFile);

        //Schedule billboard 1 in a non-vacant time slot from 10:00-10:10 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-01T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a vacant time slot from 10:10-10:20 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_2",LocalDateTime.parse("2021-05-01T10:10:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //Schedule billboard 2 in a vacant time slot from 10:05-10:15 on 01-05-2021
        billboardSchedule.scheduleBillboard("Billboard_3",LocalDateTime.parse("2021-05-01T10:05:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(), "jarod");

        //store scheduled time and duration of billboard 2 in an ArrayList
        ArrayList<ScheduleInfo> timeDurationB3 =  billboardSchedule.getViewings("Billboard_3");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB3 = timeDurationB3.get(0).startTimeScheduled;
        Duration durationB3 = timeDurationB3.get(0).duration;

        //check if time scheduled and duration pair match for Billboard 2
        assertEquals(LocalDateTime.parse("2021-05-01T10:05:00.00"), startTimeScheduledB3);
        assertEquals(Duration.ofMinutes(10), durationB3);

        //-------------------------------------------------------------
        //Test that billboard 1 is now scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB1 = billboardSchedule.getViewings("Billboard_1");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB1 = timeDurationB1.get(0).startTimeScheduled;
        Duration durationB1 = timeDurationB1.get(0).duration;

        //check if Billboard 1 is scheduled for 10:00 - 10:05 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:00:00.00"),startTimeScheduledB1);
        assertEquals(Duration.ofMinutes(5), durationB1);

        //-------------------------------------------------------------
        //Test that billboard 2 is now scheduled correctly
        //store scheduled time and duration of billboard 1 in an ArrayList
        ArrayList<ScheduleInfo>  timeDurationB2 = billboardSchedule.getViewings("Billboard_2");

        //extract time scheduled and duration from array list
        LocalDateTime startTimeScheduledB2 = timeDurationB2.get(0).startTimeScheduled;
        Duration durationB2 = timeDurationB2.get(0).duration;

        //check if Billboard 1 is scheduled for 10:15 - 10:20 on 01-05-2021 for 5 mins
        assertEquals(LocalDateTime.parse("2021-05-01T10:15:00.00"),startTimeScheduledB2);
        assertEquals(Duration.ofMinutes(5), durationB2);

    }

    //Test 3: Schedule billboard for an invalid time
    //Date in the past
    @Test
    public void scheduleInvalidTime() throws Exception {
        //add billboard to billboardList
        billboardList.createEditBillboard("Billboard_1","jarod", xmlFile);

        assertThrows(Exception.class,() -> {
            //schedule billboard for date in the past
            billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2000-01-01T10:00:00.00"),
                    Duration.ofMinutes(10), 0,billboardList.listBillboards(),"emily");
        });
    }

    //Test 4: Schedule billboard for an invalid duration
    //Negative duration
    @Test
    public void scheduleNegDuration() throws Exception {
        //add billboard to billboardList
        billboardList.createEditBillboard("Billboard_1","jarod", xmlFile);

        assertThrows(Exception.class,() -> {
            //schedule billboard for negative duration
            billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-01-01T10:00:00.00"),
                    Duration.ofMinutes(-1),0, billboardList.listBillboards(), "jarod");
        });
    }

    //Test 4.1: Schedule billboard for an invalid duration
    //Zero duration
    @Test
    public void scheduleZerDuration() throws Exception {
        //add billboard to billboardList
        billboardList.createEditBillboard("Billboard_1","jarod", xmlFile);

        assertThrows(Exception.class,() -> {
            //schedule billboard for zero duration
            billboardSchedule.scheduleBillboard("Billboard_1",LocalDateTime.parse("2021-01-01T10:00:00.00"),
                    Duration.ofMinutes(0), 0,billboardList.listBillboards(),"jarod");
        });
    }

    //Test 5: Schedule billboard that does not exist
    @Test
    public void scheduleInvalidBillboard()
    {
        assertThrows(Exception.class,() -> {
            //schedule billboard that does not exist
            billboardSchedule.scheduleBillboard("nonexistent",LocalDateTime.parse("01-01-2021T10:00:00.00"),
                    Duration.ofMinutes(10),0, billboardList.listBillboards(), "jarod");
        });
    }

    //Test 6: Checks if exception is thrown when retrieving schedule information from a billboard that does not exist
    @Test
    public void retrieveInvalidBillboard()
    {
        assertThrows(Exception.class,() -> {
            //retrieve schedule for billboard that does not exist
            billboardSchedule.getViewings("nonexistent");
        });
    }

    //Test 7: Checks if a billboard can be removed from the schedule.
    @Test
    public void scheduleRemoveBillboard() throws Exception
    {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_3","jarod", xmlFile);

        //add billboards to schedule
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), 0, billboardList.listBillboards(), "jarod");
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), 0,billboardList.listBillboards(),"jarod");
        billboardSchedule.scheduleBillboard("Billboard_3", LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5),0, billboardList.listBillboards(), "jarod");

        //remove billboard from schedule
        billboardSchedule.removeViewing("Billboard_2", billboardSchedule.getViewings("Billboard_2").get(0));

        //check if billboard 2 has been removed
        //exception is thrown when retrieving info of non-existent billboard
        assertThrows(Exception.class,() -> {
            billboardSchedule.getViewings("Billboard_2");
        });
    }

    //Test 7.1: Checks if a single viewing of a billboard, that has been scheduled multiple times, can be removed from the schedule.
    @Test
    public void scheduleRemoveSameBillboard() throws Exception
    {
        //add billboard to billboardList
        billboardList.createEditBillboard("Billboard_1","jarod", xmlFile);

        //schedule billboard multiple times
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), 0, billboardList.listBillboards(), "jarod");
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(10), 0,billboardList.listBillboards(),"jarod");

        //schedule info of viewing being removed
        ScheduleInfo removedScheduleInfo= new ScheduleInfo(LocalDateTime.parse("2021-01-10T10:00:00.00"),
                Duration.ofMinutes(10), 0, "jarod");

        //remove billboard from schedule
        billboardSchedule.removeViewing("Billboard_1",removedScheduleInfo);

        //get viewings of billboard 1
        ArrayList<ScheduleInfo> viewings = billboardSchedule.getViewings("Billboard_1");

        //check if 1 viewing is in list
        assertEquals(1, viewings.size());

        //check if viewing in list is correct
        assertEquals(LocalDateTime.parse("2021-05-04T10:00:00.00"), viewings.get(0).startTimeScheduled);

    }

    //Test 7.2: Remove scheduling of billboard that is not in the schedule - nonexistent name
    @Test
    public void removeScheduleInvalidBillboardName() throws Exception
    {
        //create schedule info for billboard being removed
        ScheduleInfo scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15),0, "jarod");

        assertThrows(Exception.class,() -> {
            //remove billboard that does not exist in schedule
            billboardSchedule.removeViewing("nonexistent", scheduleInfo);
        });
    }

    //Test 7.3: Remove scheduling of billboard that is not in the schedule - nonexistent name & schedule info combination
    @Test
    public void removeScheduleInvalidBillboard() throws Exception
    {
        //add billboard to billboard list
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);

        //schedule billboard twice
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-10T10:00:00.00"), Duration.ofMinutes(10), 0,
                billboardList.listBillboards(), "jarod");

        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-11T10:00:00.00"), Duration.ofMinutes(10), 0,
                billboardList.listBillboards(), "jarod");

        //schedule info that does not match billboard scheduled
        ScheduleInfo scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15),0, "jarod");

        assertThrows(Exception.class,() -> {
            //remove viewing from schedule that does not exist
            billboardSchedule.removeViewing("Billboard_1", scheduleInfo);
        });
    }

    //Test 8: Test if the billboard schedule can be viewed correctly.
    @Test
    public void viewSchedule() throws Exception {
        //add billboards to billboardList
        billboardList.createEditBillboard("Billboard_1", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_2", "jarod", xmlFile);
        billboardList.createEditBillboard("Billboard_3","jarod", xmlFile);

        //add billboards to schedule
        billboardSchedule.scheduleBillboard("Billboard_1", LocalDateTime.parse("2021-05-05T10:00:00.00"),
                Duration.ofMinutes(10), 0, billboardList.listBillboards(), "jarod");
        billboardSchedule.scheduleBillboard("Billboard_2", LocalDateTime.parse("2021-05-04T10:00:00.00"),
                Duration.ofMinutes(15), 0, billboardList.listBillboards(), "jarod");
        billboardSchedule.scheduleBillboard("Billboard_3", LocalDateTime.parse("2021-05-03T10:00:00.00"),
                Duration.ofMinutes(5), 0, billboardList.listBillboards(), "jarod");

        //store billboard schedule in MultiMap
        MultiMap<String, ScheduleInfo> viewScheduleList = billboardSchedule.viewSchedule();

        //check if billboards are in viewScheduleList View_schedule output
        assertTrue(viewScheduleList.containsKey("Billboard_1"));
        assertTrue(viewScheduleList.containsKey("Billboard_2"));
        assertTrue(viewScheduleList.containsKey("Billboard_3"));

        //check if time scheduled of viewings, associated with each Billboard, are correct
        assertEquals(LocalDateTime.parse("2021-05-05T10:00:00.00"), viewScheduleList.get("Billboard_1").get(0).startTimeScheduled);
        assertEquals(LocalDateTime.parse("2021-05-04T10:00:00.00"), viewScheduleList.get("Billboard_2").get(0).startTimeScheduled);
        assertEquals(LocalDateTime.parse("2021-05-03T10:00:00.00"), viewScheduleList.get("Billboard_3").get(0).startTimeScheduled);

        //check if duration of viewings, associated with each Billboard, are correct
        assertEquals(Duration.ofMinutes(10), viewScheduleList.get("Billboard_1").get(0).duration);
        assertEquals(Duration.ofMinutes(15), viewScheduleList.get("Billboard_2").get(0).duration);
        assertEquals(Duration.ofMinutes(5), viewScheduleList.get("Billboard_3").get(0).duration);

    }
}
