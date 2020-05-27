import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * ScheduleInfo class
 * This class contains methods that construct Schedule_Info objects
 * @author Emily Chang
 * @version complete
 */
public class ScheduleInfo implements Serializable {
    private final int MINUTES_IN_DAY = 1440;
    private final int MINUTES_IN_HOUR = 60;

    LocalDateTime startTimeScheduled;
    Duration duration;
    int recurrenceDelay;
    String billboardCreator;

    /**
     *Constructor that is used to create Schedule_info object. Schedule_info object is assigned a start time scheduled,
     * duration (mins) & recurrence type
     * @param startTimeScheduled start time billboard is scheduled for
     * @param durationMins duration billboard is scheduled for
     * @param recurrenceDelay time till next recurrence billboard is scheduled for
     * @param billboardCreator creator of billboard
     * @throws Exception throws exception if recurrence is invalid, duration or start time scheduled is out of range
     */
    public ScheduleInfo(LocalDateTime startTimeScheduled, Duration durationMins, int recurrenceDelay , String billboardCreator) throws Exception
    {

        this.billboardCreator = billboardCreator;

        //set time billboard is scheduled for showing
        this.startTimeScheduled = startTimeScheduled;

        //if duration is negative or zero
        if (durationMins.isNegative() || durationMins.isZero())
        {
            //throw exception
            throw new Exception("Duration out of range");
        }

        else
        {
            //set duration billboard is displayed for
            this.duration = durationMins;
        }

        //returns negative if recurrenceDelay is less than duration
        int compare = Duration.ofMinutes(recurrenceDelay).compareTo(this.duration);

        //if the recurrence is smaller than duration
        if (compare < 0 && recurrenceDelay!=0)
        {
            //throw exception
            throw new Exception("Recurrence delay cannot be smaller than the duration of the billboard viewing");
        }

        //if the recurrence option is invalid
        if (recurrenceDelay != MINUTES_IN_DAY && recurrenceDelay != MINUTES_IN_HOUR && !(recurrenceDelay < MINUTES_IN_HOUR) && recurrenceDelay != 0)
        {
            //throw exception
            throw new Exception("The given recurrence option is invalid, please specify whether the billboard recurs " +
                    "every day, hour, minute or none");
        }

        else
        {
            //set recurrence of billboard
            this.recurrenceDelay = recurrenceDelay;
        }

    }

}
