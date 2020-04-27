import java.time.Duration;
import java.time.LocalDateTime;

public class Schedule_info {
    LocalDateTime Time_scheduled;
    Duration duration;
    String Recurrence;


    public Schedule_info( LocalDateTime time_scheduled, Duration duration_mins, String recurrence)throws Exception
    {
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

        //if the time scheduled is in the past
        if (time_scheduled.compareTo(LocalDateTime.now())<0)
        {
            //throw exception
            throw new Exception("Time scheduled must be after"+ LocalDateTime.now());
        }

        else
        {
            ////set time billboard is scheduled for showing
            this.Time_scheduled = time_scheduled;
        }

        //if the recurrence option is invalid
        if (recurrence!= "day" && recurrence!= "hour" && recurrence!= "minute" && recurrence!= "none")
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