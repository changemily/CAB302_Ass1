import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains methods that test the functionality of ScheduleInfo class methods
 * @author — Emily Chang
 * @version - 2
 */
public class TestScheduleInfo {

    ScheduleInfo scheduleInfo;
    final int MINUTES_IN_DAY = 1440;
    final int MINUTES_IN_HOUR = 60;

    /**
     * Test 1: Constructing a Schedule info object with scheduled time
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @BeforeEach
    @Test
    public void setUpScheduleInfo() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                5, "emily");
    }

    /**
     * Test 2: construct schedule info object that recurs daily
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void recurrenceDay() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                MINUTES_IN_DAY, "emily");

        assertEquals(MINUTES_IN_DAY, scheduleInfo.recurrenceDelay);
    }

    /**
     * Test 3: construct schedule info object that recurs hourly
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void recurrenceHour() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                MINUTES_IN_HOUR, "emily");

        assertEquals(MINUTES_IN_HOUR, scheduleInfo.recurrenceDelay);
    }

    /**
     * Test 4: construct schedule info object that recurs every 3 mins
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void recurrenceMin() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                3, "emily");

        assertEquals(3, scheduleInfo.recurrenceDelay);
    }

    /**
     * Test 5: construct schedule info object that recurs every minute
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void recurrenceInfinite() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                1, "emily");

        assertEquals(1, scheduleInfo.recurrenceDelay);
    }

    /**
     * Test 6: construct schedule info object that recurs every 3 mins
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void recurrenceNone() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                0, "emily");

        assertEquals(0, scheduleInfo.recurrenceDelay);
    }

    /**
     * Test 7: construct schedule info object with invalid recurrence delay
     * - invalid option, larger than 60 mins and not an hr or day
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void invalidRecurrence() throws Exception {
        assertThrows(Exception.class,() -> {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                61, "emily");
        });
    }

    /**
     * Test 8: construct schedule info object with invalid recurrence delay - smaller than duration
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void invalidRecurrence2() throws Exception {
        assertThrows(Exception.class,() -> {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                2, "emily");
        });
    }

    /**
     * Test 9: construct schedule info object with valid duration - Negative duration
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void validDuration() throws Exception {
        scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                10, "emily");
        assertEquals(Duration.ofMinutes(5), scheduleInfo.duration);
    }

    /**
     * Test 10: construct schedule info object with invalid duration - Negative duration
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void negDuration() throws Exception {
        assertThrows(Exception.class,() -> {
            scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(-5),
                    2, "emily");
        });
    }

    /**
     * Test 11: construct schedule info object with invalid duration - Zero duration
     * @throws Exception invalid duration, invalid recurrence delay
     */
    @Test
    public void zerDuration() throws Exception {
        assertThrows(Exception.class,() -> {
            scheduleInfo = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(0),
                    2, "emily");
        });
    }
}
