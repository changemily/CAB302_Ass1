import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Schedule_Info class
 * This class contains methods that construct Schedule_Info objects
 * @author Emily Chang
 * @version complete
 */
public class ScheduleInfo implements Serializable {
    final int minutes_inDay = 1440;
    final int minutes_inHour = 60;

    LocalDateTime startTimeScheduled;
    Duration duration;
    int recurrenceDelay;
    String billboardCreator;

    /**
     *Constructor that is used to create Schedule_info object. Schedule_info object is assigned a start time scheduled,
     * duration (mins) & recurrence type
     * @param startTime_scheduled start time billboard is scheduled for
     * @param duration_mins duration billboard is scheduled for
     * @param recurrence_delay time till next recurrence billboard is scheduled for
     * @param billboardCreator creator of billboard
     * @throws Exception throws exception if recurrence is invalid, duration or start time scheduled is out of range
     */
    public ScheduleInfo(LocalDateTime startTime_scheduled, Duration duration_mins, int recurrence_delay , String billboardCreator)throws Exception
    {

        this.billboardCreator = billboardCreator;

        ////set time billboard is scheduled for showing
        this.startTimeScheduled = startTime_scheduled;

        //if duration is negative or zero
        if (duration_mins.isNegative() || duration_mins.isZero())
        {
            //throw exception
            throw new Exception("Duration out of range");
        }

        else
        {
            //set duration billboard is displayed for
            this.duration = duration_mins;
        }

        //returns negative if recurrence_delay is less than duration
        int compare = Duration.ofMinutes(recurrence_delay).compareTo(this.duration);

        //if the recurrence is smaller than duration
        if (compare < 0 && recurrence_delay!=0)
        {
            //throw exception
            throw new Exception("Recurrence delay cannot be smaller than the duration of the billboard viewing");
        }

        //if the recurrence option is invalid
        if (recurrence_delay != minutes_inDay && recurrence_delay != minutes_inHour && !(recurrence_delay < minutes_inHour) && recurrence_delay != 0)
        {
            //throw exception
            throw new Exception("The given recurrence option is invalid, please specify whether the billboard recurs " +
                    "every day, hour, minute or none");
        }

        else
        {
            //set recurrence of billboard
            this.recurrenceDelay = recurrence_delay;
        }

    }

}
