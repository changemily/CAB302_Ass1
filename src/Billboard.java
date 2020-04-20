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
    Duration duration;

    /**
     *Constructor that is used to create object Billboard. Billboard has an assigned a name, text
     *  and background colour
     * @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     * @param image_file
     */
    Billboard(String billboard_name, String text, String bg_colour, String image_file)
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
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     */
    Billboard(String billboard_name, String text, String bg_colour, String image_file ,
                          LocalDate time_scheduled, int Duration_mins)
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
        this.Time_scheduled = time_scheduled;
        //convert int to Duration
        this.duration = Duration.ofMinutes(Duration_mins);
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
    public void Bg_colour(String bg_colour) throws Exception
    {
        //if bg colour does not exist
        //if (!(bg_colour == ) )
        //{
            //throw exception
            //throw new Exception("Colour does not exist");
        //}
        //else
        //{
            //set billboard bg colour
            this.Bg_colour = bg_colour;
        //}
    }

    /**
     *Adds image to given Billboard
     * @param file_name file name of image to add to Billboard
     */
    public void Add_image(String file_name) throws Exception
    {
        //if file name does not exist
        //throw new exception
        this.Image_file = file_name;
    }

    /**
     *Schedules billboard for given time and duration
     * @param Duration_mins Duration (minutes) Billboard is displayed for
     * @param time_scheduled Time (date) Billboard is scheduled for showing
     * @throws Exception if duration is out of range or the time scheduled is in the past
     */
    public void Schedule_billboard(int Duration_mins, LocalDate time_scheduled) throws Exception
    {
        //if duration is negative
        if (Duration_mins <0)
        {
            //throw exception
            throw new Exception("Duration out of range");
        }

        else
        {
            //set duration billboard is displayed for
            this.duration = Duration.ofMinutes(Duration_mins);
        }

        //if the time scheduled is in the past
        if (time_scheduled.compareTo(LocalDate.now())<0)
        {
            //throw exception
            throw new Exception("Time scheduled must be after"+ LocalDate.now());
        }

        else
        {
            ////set time billboard is scheduled for showing
            this.Time_scheduled = time_scheduled;
        }
    }

}
