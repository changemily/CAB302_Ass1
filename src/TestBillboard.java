import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class contains methods that test the functionality of the Billboard class
 *  * @author — Emily Chang
 *  * @version - 2
 */
public class TestBillboard {

    /**
     * Test 1: Constructing a Billboard object with an image
     */
    @BeforeEach @Test
    public void setUpBillboard() {
        new Billboard("Billboard 1", "Hello World", "blue", "image.jpg", "jarod");
    }

    @Test
    /**
     * Test 2: Constructing a Billboard object without image
     */
    public void setUpBillboardImg() {
        new Billboard("Billboard 2", "Hello World", "red", "No Image","jarod");
    }
}
