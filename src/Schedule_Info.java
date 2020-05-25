import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Schedule_Info class
 * This class contains methods that construct Schedule_Info objects
 * @author Emily Chang
 * @version complete
 */
public class Schedule_Info implements Serializable {
    LocalDateTime StartTime_Scheduled;
    Duration duration;
    String Recurrence;
    String Billboard_creator;

    /**
     *Constructor that is used to create Schedule_info object. Schedule_info object is assigned a start time scheduled,
     * duration (mins) & recurrence type
     * @param startTime_scheduled start time billboard is scheduled for
     * @param duration_mins duration billboard is scheduled for
     * @param recurrence type of recurrence billboard is scheduled for
     * @param billboard_creator
     * @throws Exception throws exception if recurrence is invalid, duration or start time scheduled is out of range
     */
    public Schedule_Info(LocalDateTime startTime_scheduled, Duration duration_mins, String recurrence , String billboard_creator)throws Exception
    {

        this.Billboard_creator = billboard_creator;

        ////set time billboard is scheduled for showing
        this.StartTime_Scheduled = startTime_scheduled;

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

        //if the recurrence option is invalid
        if (!recurrence.equals("day") && !recurrence.equals("hour")  && !recurrence.equals("minute") && !recurrence.equals("none"))
        {
            //throw exception
            throw new Exception("The given recurrence option is invalid, please specify whether the billboard recurs " +
                    "every day, hour, minute or none");
        }

        else
        {
            //set recurrence of billboard
            this.Recurrence = recurrence;
        }

    }

}
