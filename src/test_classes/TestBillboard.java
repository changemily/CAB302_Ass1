package test_classes;

import billboard_classes.Billboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBillboard {
    //define xml string to use in tests
    public static final String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <picture url=\"https://cloudstor.aarnet.edu.au/plus/s/vYipYcT3VHa1uNt/download\" />\n" +
            "    <information>billboard_classes.Billboard with picture (with URL attribute) and information text only. The picture is now centred within the top 2/3 of the image and the information text is centred in the remaining space below the image.</information>\n" +
            "</billboard>";

    //Test 1: Constructing a billboard_classes.Billboard object
    @BeforeEach @Test
    public void setUpBillboard() {
        new Billboard("billboard_classes.Billboard 1",  "jarod", xmlFile);
    }
}
