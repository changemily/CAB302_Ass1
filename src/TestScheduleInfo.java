import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestScheduleInfo {

    ScheduleInfo schedule_info;
    final int minutes_inDay = 1440;
    final int minutes_inHour = 60;

    // Test 1: Constructing a Schedule info object with scheduled time
    @BeforeEach
    @Test
    public void setUpBillboard() throws Exception {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                5, "emily");
    }

    @Test
    // Test 2: construct schedule info object that recurs daily
    public void recurrence_day() throws Exception {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                minutes_inDay, "emily");

        assertEquals(minutes_inDay, schedule_info.recurrenceDelay);
    }

    @Test
    // Test 3: construct schedule info object that recurs hourly
    public void recurrence_hour() throws Exception {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                minutes_inHour, "emily");

        assertEquals(minutes_inHour, schedule_info.recurrenceDelay);
    }

    @Test
    // Test 4: construct schedule info object that recurs every 3 mins
    public void recurrence_min() throws Exception {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                3, "emily");

        assertEquals(3, schedule_info.recurrenceDelay);
    }

    @Test
    // Test 4: construct schedule info object that recurs every 3 mins
    public void recurrence_none() throws Exception {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(1),
                0, "emily");

        assertEquals(0, schedule_info.recurrenceDelay);
    }

    @Test
    // Test 5: construct schedule info object with invalid recurrence delay - invalid option, larger than 60 mins and not an hr or day
    public void invalid_recurrence() throws Exception {
        assertThrows(Exception.class,() -> {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                61, "emily");
        });
    }

    @Test
    // Test 6: construct schedule info object with invalid recurrence delay - smaller than duration
    public void invalid_recurrence2() throws Exception {
        assertThrows(Exception.class,() -> {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                2, "emily");
        });
    }

    @Test
    //Test 7: construct schedule info object with valid duration - Negative duration
    public void Valid_duration() throws Exception {
        schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(5),
                10, "emily");
        assertEquals(Duration.ofMinutes(5), schedule_info.duration);
    }

    @Test
    //Test 8: construct schedule info object with invalid duration - Negative duration
    public void Neg_duration() throws Exception {
        assertThrows(Exception.class,() -> {
            schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(-5),
                    2, "emily");
        });
    }

    @Test
    //Test 9: construct schedule info object with invalid duration - Zero duration
    public void Zer_duration() throws Exception {
        assertThrows(Exception.class,() -> {
            schedule_info = new ScheduleInfo(LocalDateTime.parse("2021-05-26T10:00:00"), Duration.ofMinutes(0),
                    2, "emily");
        });
    }
}
