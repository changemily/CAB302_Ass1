
/**
 * Billboard class
 * This class contains methods that construct and edit billboards
 * @author — Emily Chang
 * @version - under development
 */

public class Billboard implements java.io.Serializable{

    //class attributes
    String BillboardName;
    String BillboardText;
    String BgColour;
    String ImageFile;
    String BillboardCreator;

    /**
     * Constructor that is used to create Billboard object. Billboard has an assigned a name, text,
     * background colour, image file and creator
     * @param billboardName Billboard's name
     * @param text Text set on Billboard
     * @param bgColour Background colour of Billboard
     * @param imageFile file name of image on Billboard
     * @param billboardCreator Username of the person who has created the billboard
     */
    Billboard(String billboardName, String text, String bgColour, String imageFile,
              String billboardCreator)
    {
        //set billboard properties
        this.BillboardName = billboardName;
        this.BillboardText = text;
        this.BgColour = bgColour;
        this.BillboardCreator = billboardCreator;
        this.ImageFile = imageFile;
    }
}
