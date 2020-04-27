import java.time.LocalDateTime;

public class Schedule_info {
    LocalDateTime Time_scheduled;
    int Duration;


    public Schedule_info( LocalDateTime time_scheduled, int duration_mins)
    {
        this.Time_scheduled = time_scheduled;
        this.Duration = duration_mins;
    }
}
