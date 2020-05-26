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

    /**
     * Test 1: Constructing a Billboard object with scheduled time
     */
    @BeforeEach @Test
    public void setUpBillboard() {
        billboard = new Billboard("Billboard 1", "Hello World", "blue", "image.jpg", "jarod");
    }

    @Test
    /**
     * Test 2: Constructing a Billboard object without image
     */
    public void setUpBillboard_img() {
        Billboard billboard3 = new Billboard("Billboard 2", "Hello World", "red", "No Image","jarod");
    }
}
