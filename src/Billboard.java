import java.time.Duration;
import java.time.LocalDate;

/**
 * Billboard class
 * This class contains methods that construct and edit billboards
 * @author — Emily Chang
 * @version - skeleton
 */

public class Billboard {
    String Billboard_name;
    String Billboard_text;
    String bg_colour;
    String image_file;
    LocalDate time_scheduled;
    Duration duration;

    /**
     *Constructor that is used to create object Billboard. Billboard has an assigned a name, text
     *  and background colour
     * @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     */
    public void Billboard(String billboard_name, String text, String bg_colour)
    {

    }

    /**
     *Constructor that is used to create object Billboard. Billboard has an assigned a name, text,
     * background colour, image file, scheduled viewing time, and duration the billboard is displayed for
     * @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     * @param image_file file name of image on Billboard
     * @param time_schedule Time (date) Billboard is scheduled for showing
     * @param duration Duration (minutes) Billboard is displayed for
     */
    public void Billboard(String billboard_name, String text, String bg_colour, String image_file ,
                          LocalDate time_schedule, Duration duration)
    {

    }

    /**
     *Sets text of given Billboard
     * @param text Text set on Billboard
     */
    public void Set_text(String text)
    {

    }

    /**
     *Sets Background colour of given Billboard
     * @param bg_colour Background colour of Billboard
     */
    public void Bg_colour(String bg_colour)
    {

    }

    /**
     *Adds image to given Billboard
     * @param file_name file name of image to add to Billboard
     */
    public void Add_image(String file_name)
    {

    }

    /**
     *Schedules billboard for given time and duration
     * @param Billboard_name Billboard's name
     * @param duration Duration (minutes) Billboard is displayed for
     * @param time_schedule Time (date) Billboard is scheduled for showing
     */
    public void Schedule_billboard(String Billboard_name, Duration duration, LocalDate time_schedule)
    {

    }

}
