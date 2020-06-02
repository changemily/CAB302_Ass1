package billboard;

/**
 * Billboard class
 * This class contains a constructor that constructs Billboard objects
 * @author — Emily Chang
 * @version - complete
 */

public class Billboard implements java.io.Serializable{

    //class attributes
    public String BillboardName;
    public String BillboardCreator;
    public String XMLFile;

    /**
     * Constructor that is used to create Billboard object, Billboard has an assigned a name, text,
     * background colour, image file and creator
     * @param billboardName Billboard's name
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
