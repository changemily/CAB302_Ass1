import java.time.LocalDate;

public class Schedule_info {
    LocalDate Time_scheduled;
    int Duration;


    public Schedule_info( LocalDate time_scheduled, int duration_mins)
    {
        this.Time_scheduled = time_scheduled;
        this.Duration = duration_mins;
    }
}
