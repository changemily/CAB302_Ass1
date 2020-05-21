
/**
 * Billboard class
 * This class contains methods that construct and edit billboards
 * @author — Emily Chang
 * @version - under development
 */

public class Billboard implements java.io.Serializable{
    String Billboard_name;
    String Billboard_text;
    String Bg_colour;
    String Image_file;
    String Billboard_creator;


    /**
     *Overloaded method  - Optional for Billboard  to be scheduled for display
     * Constructor that is used to create object Billboard. Billboard has an assigned a name, text,
     * background colour, image file, scheduled viewing time, and duration the billboard is displayed for
     * @param billboard_name Billboard's name
     * @param text Text set on Billboard
     * @param bg_colour Background colour of Billboard
     * @param image_file file name of image on Billboard
     * @param billboard_creator Username of the person who has created the billboard
     */
    Billboard(String billboard_name, String text, String bg_colour, String image_file ,
                          String billboard_creator)
    {
        //set billboard properties
        this.Billboard_name = billboard_name;
        this.Billboard_text = text;
        this.Bg_colour = bg_colour;
        this.Billboard_creator = billboard_creator;

        //if there is an image on the billboard
        if (!(image_file == "No Image"))
        {
            this.Image_file = image_file;
        }
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
        //set image file name
        this.Image_file = file_name;
    }

}
