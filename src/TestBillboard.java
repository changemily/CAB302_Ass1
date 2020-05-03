import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains methods that test the functionality of Billboard class methods
 *  * @author — Emily Chang
 *  * @version - 2
 */
public class TestBillboard {

    Billboard billboard;

    // Test 1: Constructing a Billboard object without with scheduled time
    @BeforeEach @Test
    public void setUpBillboard() {
        billboard = new Billboard("Billboard 1", "Hello World", "blue", "image.jpg");
    }

    @Test
    // Test 1.2: Constructing a Billboard object with scheduled time
    public void setUpBillboardTime() {
        Billboard billboard2 = new Billboard("Billboard 2", "Hello World", "red", "image.jpg",
                LocalDateTime.parse("2020-04-06T10:00:00.00"), Duration.ofMinutes(5));
    }

    @Test
    // Test 1.3: Constructing a Billboard object without image
    public void setUpBillboard_img() {
        Billboard billboard3 = new Billboard("Billboard 2", "Hello World", "red", "No Image",
                LocalDateTime.parse("2020-04-06T10:00:00.00"),Duration.ofMinutes(5));
    }

    @Test
    // Test 2: Set bg colour - colour does not exist
    public void set_bgColour() throws Exception{
        assertThrows(Exception.class, () -> {
            billboard.Bg_colour("girl");
        });
    }
    // Test 3: Set image - file does not exist
    @Test
    public void set_image() throws Exception{
        assertThrows(Exception.class, () -> {
            billboard.Add_image("nonexistent");
        });
    }
}
