import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBillboard {
    //define xml string to use in tests
    public static final String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <picture url=\"https://cloudstor.aarnet.edu.au/plus/s/vYipYcT3VHa1uNt/download\" />\n" +
            "    <information>Billboard with picture (with URL attribute) and information text only. The picture is now centred within the top 2/3 of the image and the information text is centred in the remaining space below the image.</information>\n" +
            "</billboard>";

    //Test 1: Constructing a Billboard object with an image
    @BeforeEach @Test
    public void setUpBillboard() {
        new Billboard("Billboard 1",  "jarod", xmlFile);
    }

    @Test
    //Test 2: Constructing a Billboard object without image
    public void setUpBillboardImg() {
        //new Billboard("Billboard 2", "Hello World", "red", "No Image","jarod");
    }
}
