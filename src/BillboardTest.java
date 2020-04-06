import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BillboardTest {

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
                LocalDate.parse("2020-04-06"),5);
    }

    @Test
    // Test 1.3: Constructing a Billboard object without image
    public void setUpBillboard_img() {
        Billboard billboard3 = new Billboard("Billboard 2", "Hello World", "red", "No Image",
                LocalDate.parse("2020-04-06"),5);
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

    // Test 4: Schedule_billboard - invalid time (time in the past)
    @Test
    public void Schedule_billboard() throws Exception{
        assertThrows(Exception.class, () -> {
            billboard.Schedule_billboard(5, LocalDate.parse("2000-11-01"));
        });
    }
    // Test 5: Schedule_billboard - Negative duration
    @Test
    public void Negative_duration() throws Exception{
        assertThrows(Exception.class, () -> {
            billboard.Schedule_billboard(-1, LocalDate.parse("2000-11-01"));
        });
    }

    // Test 5: Schedule_billboard - Zero duration
    @Test
    public void Zero_duration() throws Exception{
        assertThrows(Exception.class, () -> {
            billboard.Schedule_billboard(0, LocalDate.parse("2000-11-01"));
        });
    }
}
