import java.time.LocalDateTime;

public class Schedule_info {
    LocalDateTime Time_scheduled;
    int Duration;


    public Schedule_info( LocalDateTime time_scheduled, int duration_mins)throws Exception
    {
        //if duration is negative
        if (duration_mins <0)
        {
            //throw exception
            throw new Exception("Duration out of range");
        }

        else
        {
            //set duration billboard is displayed for
            this.Duration = duration_mins;
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
    }

}
