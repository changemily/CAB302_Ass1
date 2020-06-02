package test_classes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import schedule_classes.ScheduleInfo;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestScheduleInfo {

    ScheduleInfo scheduleInfo;
    //define no. minutes in a day and hour
    final int MINUTES_IN_DAY = 1440;
    final int MINUTES_IN_HOUR = 60;

    //Test 1: Constructing a Schedule info object
    @BeforeEach
    @Test
    public void setUpScheduleInfo() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                0, "emily");
    }

    //Test 2: construct a schedule info object that recurs daily
    @Test
    public void recurrenceDay() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                MINUTES_IN_DAY, "emily");

        Assertions.assertEquals(MINUTES_IN_DAY, scheduleInfo.recurrenceDelay);
    }

    //Test 3: construct a schedule info object that recurs hourly
    @Test
    public void recurrenceHour() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                MINUTES_IN_HOUR, "emily");

        Assertions.assertEquals(MINUTES_IN_HOUR, scheduleInfo.recurrenceDelay);
    }

    //Test 4: construct a schedule info object that recurs every 3 mins
    @Test
    public void recurrenceMin() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                3, "emily");

        Assertions.assertEquals(3, scheduleInfo.recurrenceDelay);
    }

    //Test 5: construct a schedule info object that recurs every minute
    @Test
    public void recurrenceInfinite() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                1, "emily");

        Assertions.assertEquals(1, scheduleInfo.recurrenceDelay);
    }

    //Test 6: construct a schedule info object that does not recur
    @Test
    public void recurrenceNone() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                0, "emily");

        Assertions.assertEquals(0, scheduleInfo.recurrenceDelay);
    }

    //Test 7: construct a schedule info object with an invalid recurrence delay - invalid option, larger than 60 mins and not an hr or day
    @Test
    public void invalidRecurrence() throws Exception {
        assertThrows(Exception.class,() -> {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                61, "emily");
        });
    }

    //Test 8: construct a schedule info object with an invalid recurrence delay - smaller than duration
    @Test
    public void invalidRecurrence2() throws Exception {
        assertThrows(Exception.class,() -> {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                2, "emily");
        });
    }

    //Test 9: construct a schedule info object with a valid duration
    @Test
    public void validDuration() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                10, "emily");
        Assertions.assertEquals(Duration.ofMinutes(5), scheduleInfo.duration);
    }

    //Test 10: construct schedule info object with an invalid duration - Negative duration
    @Test
    public void negDuration() throws Exception {
        assertThrows(Exception.class,() -> {
            scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(-5),
                    2, "emily");
        });
    }

    //Test 11: construct schedule info object with an invalid duration - Zero duration
    @Test
    public void zerDuration() throws Exception {
        assertThrows(Exception.class,() -> {
            scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(0),
                    2, "emily");
        });
    }
}
