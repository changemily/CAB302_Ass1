package schedule_classes;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * schedule_classes.ScheduleInfo class
 * This class contains methods that construct schedule_classes.ScheduleInfo objects
 * @author Emily Chang
 * @version complete
 */
public class ScheduleInfo implements Serializable {
    //initialize final variables that store the number of minutes in a day and hour
    private final int MINUTES_IN_DAY = 1440;
    private final int MINUTES_IN_HOUR = 60;

    //class attributes
    public LocalDateTime startTimeScheduled;
    public Duration duration;
    public int recurrenceDelay;
    public String billboardCreator;

    /**
     * Constructor that is used to create a schedule_classes.ScheduleInfo object. schedule_classes.ScheduleInfo object is assigned a start time scheduled,
     * duration (mins), recurrence delay(mins) and the creator of the billboard scheduled
     * @param startTimeScheduled start time billboard is scheduled for
     * @param durationMins duration billboard is scheduled for
     * @param recurrenceDelay time till next recurrence of billboard viewing - 0 if there is no recurrence
     * @param billboardCreator creator of billboard that is being scheduled
     * @throws Exception throws exception if recurrence is invalid or if duration is out of range
     */
    public ScheduleInfo(LocalDateTime startTimeScheduled, Duration durationMins, int recurrenceDelay , String billboardCreator) throws Exception
    {
        //set start time of billboard viewing
        this.startTimeScheduled = startTimeScheduled;

        //set billboard creator of billboard being displayed
        this.billboardCreator = billboardCreator;

        //if duration is negative or zero
        if (durationMins.isNegative() || durationMins.isZero())
        {
            //throw exception
            throw new Exception("Duration out of range");
        }

        //if valid duration
        else
        {
            //set duration the billboard is displayed for
            this.duration = durationMins;
        }

        //compare duration of recurrence delay to duration of billboard viewing
        //returns negative int if recurrenceDelay is less than duration
        int compare = Duration.ofMinutes(recurrenceDelay).compareTo(this.duration);

        //if the recurrence is smaller than duration
        if (compare < 0 && recurrenceDelay!=0)
        {
            //throw exception
            throw new Exception("Recurrence delay cannot be smaller than the duration of the billboard viewing");
        }

        //if the recurrence option is invalid - not day, hour or not under 60 mins
        if (recurrenceDelay != MINUTES_IN_DAY && recurrenceDelay != MINUTES_IN_HOUR && !(recurrenceDelay < MINUTES_IN_HOUR) && recurrenceDelay != 0)
        {
            //throw exception
            throw new Exception("The given recurrence option is invalid, please specify whether the billboard recurs " +
                    "every day, hour, minute or none");
        }

        //if recurrence option is valid
        else
        {
            //set recurrence of billboard
            this.recurrenceDelay = recurrenceDelay;
        }
    }
}
