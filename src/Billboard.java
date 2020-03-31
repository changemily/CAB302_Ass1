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
    String Bg_colour;
    String Image_file;
    LocalDate Time_scheduled;
    Duration Duration;

    /**
     *Constructor that is used to create object Billboard. Billboard has an assigned a name, text
     *  and background colour
     * @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     * @param image_file
     */
    public void Billboard(String billboard_name, String text, String bg_colour, String image_file)
    {
        //set billboard properties
        this.Billboard_name = billboard_name;
        this.Billboard_text = text;
        this.Bg_colour = bg_colour;

        //if there is an image on the billboard
        if (!(image_file == "No Image"))
        {
            this.Image_file = image_file;
        }
    }

    /**
     *Overloaded method  - Optional for Billboard  to be scheduled for display
     * Constructor that is used to create object Billboard. Billboard has an assigned a name, text,
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
        //set billboard properties
        this.Billboard_name = billboard_name;
        this.Billboard_text = text;
        this.Bg_colour = bg_colour;
        //if there is an image on the billboard
        if (!(image_file == "No Image"))
        {
            this.Image_file = image_file;
        }
        this.Time_scheduled = time_schedule;
        this.Duration = duration;
    }

    /**
     *Sets text of given Billboard
     * @param text Text set on Billboard
     */
    public void Set_text(String text)
    {
        //set billboard text
        this.Billboard_text = text;
    }

    /**
     *Sets Background colour of given Billboard
     * @param bg_colour Background colour of Billboard
     */
    public void Bg_colour(String bg_colour)
    {
        //set billboard bg colour
        this.Bg_colour = bg_colour;
    }

    /**
     *Adds image to given Billboard
     * @param file_name file name of image to add to Billboard
     */
    public void Add_image(String file_name)
    {
        this.Image_file = file_name;
    }

    /**
     *Schedules billboard for given time and duration
     * @param duration Duration (minutes) Billboard is displayed for
     * @param time_schedule Time (date) Billboard is scheduled for showing
     */
    public void Schedule_billboard(Duration duration, LocalDate time_schedule)
    {
        //set billboard time schedule and duration
        this.Duration = duration;
        this.Time_scheduled = time_schedule;
    }

}
