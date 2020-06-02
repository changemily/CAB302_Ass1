package billboard_classes;

/**
 * billboard_classes.Billboard class
 * This class contains a constructor that constructs billboard_classes.Billboard objects
 * @author — Emily Chang
 * @version - complete
 */

public class Billboard implements java.io.Serializable{

    //class attributes
    public String BillboardName;
    public String BillboardCreator;
    public String XMLFile;

    /**
     * Constructor that is used to create billboard_classes.Billboard object. billboard_classes.Billboard has an assigned a name, text,
     * background colour, image file and creator
     * @param billboardName billboard_classes.Billboard's name
     * @param billboardCreator Username of the person who has created the billboard
     */
    public Billboard(String billboardName, String billboardCreator, String xmlFile)
    {
        //set billboard properties
        this.BillboardName = billboardName;
        this.BillboardCreator = billboardCreator;
        this.XMLFile = xmlFile;
    }
}
