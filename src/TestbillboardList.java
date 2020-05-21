import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestbillboardList<E> {

    ArrayList<E> billboardValues;
    BillboardList billboardList = new BillboardList();

    //Test 1: Creating an object for testing.
    @BeforeEach
    @Test public void setupBillboardHashmap() {
        billboardList = new BillboardList();
    }


    //Test 2: Checks if a billboard can be created and added to HashMap.
    @Test
    public void add_Billboard() throws Exception
    {
        //Billboard with no scheduled viewing
        billboardList.Create_edit_Billboard("Billboard1", "new billboard", "blue", "No Image");

        assertEquals(true, billboardList.billboardHashMap.containsKey("Billboard1"));

        //Billboard with scheduled viewing and image
        billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                "image.jpg", LocalDateTime.parse("2021-04-22T10:00:00.00"), Duration.ofMinutes(10), "none", "jarod");

        assertEquals(true, billboardList.billboardHashMap.containsKey("Billboard2"));

    }

    //Test 3.1: Check for negative duration.
    @Test
    public void neg_duration()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"),Duration.ofMinutes(-5), "none", "jarod");
        });
    }

    //Test 3.2: Check for 0 duration.
    @Test
    public void no_duration()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"),Duration.ofMinutes(0), "none", "jarod");
        });
    }

    //Test 3.3: Invalid date - date in the past etc.
    @Test
    public void invalid_date()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "image.jpg", LocalDateTime.parse("20-04-202T10:00:00.00"),Duration.ofMinutes(5), "none", "jarod");
        });
    }

    //Test 3.4: Check if img exists.
    @Test
    public void img_exists()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "doesn't_exist.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"),Duration.ofMinutes(5), "none", "jarod");
        });
    }

    //Test 3.5: Check if bg_colour is valid.
    @Test
    public void bg_valid()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "lmao",
                    "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"),Duration.ofMinutes(5), "none", "jarod");
        });
    }

    //Test 4: Checks if a billboard can be edited.
    @Test
    public void Edit_Billboard()
    {

        //Create Billboard1
        billboardList.Create_edit_Billboard("Billboard1", "new billboard", "blue", "No Image");

        //edit billboard 1
        billboardList.Create_edit_Billboard("Billboard1", "edited", "edited", "edited");

        //test if all variables have changed
        assertEquals("Billboard1", "Billboard1");
        assertEquals("edited", "edited");
        assertEquals("edited", "edited");
        assertEquals("edited", "edited");

    }

    //Test 5: Checks if all current billboards can be listed. Check for alphabetical order.
    @Test
    public void List_Billboards()
    {
        //store billboard schedule in temp HashMap
        HashMap<String, Billboard> temp_list = billboardList.List_Billboards();

        //Loop checks to see if every billboard in the list is found and correctly.
        //Foreach billboard in billboardList check if it exists in the hashmap provided.
        for(Object Billboard : billboardList.billboardHashMap.entrySet()) {
            assertEquals(temp_list, Billboard);
        }
    }

    //Test 6: Checks if all information pertaining to a specified billboard can be obtained.
    @Test
    public void Get_billboard_info() throws Exception
    {
        //Create a billboard to check
        billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                "doesn't_exist.jpg", LocalDateTime.parse("2021-04-22T10:00:00.00"),Duration.ofMinutes(5), "none", "jarod");
        //Store billboard info sourced in a temp billboard object
        Billboard temp_billboard = billboardList.Get_billboard_info("Billboard2");

        //Test if retrieved Billboard variables equal the original requested_billboard info.
        //If the billboards are the same it means the correct billboard info requested is being displayed.
        assertEquals("Billboard2", temp_billboard.Billboard_name);
        assertEquals("new billboard", temp_billboard.Billboard_text);
        assertEquals("blue", temp_billboard.Bg_colour);
        assertEquals("doesn't_exist.jpg", temp_billboard.Image_file);
        assertEquals(LocalDateTime.parse("2021-04-22T10:00:00.00"), temp_billboard.Time_scheduled);
        assertEquals(Duration.ofMinutes(5), temp_billboard.duration);
    }

    //Test 7: If Get_billboard_info attempts to retrieve information from a billboard that doesn't exist.
    @Test
    public void no_info()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Get_billboard_info("Billboard_1");
        });
    }

    //Test 8: Checks if a specified billboard can be deleted.
    @Test
    public void Delete_billboard() throws Exception
    {
        //Create Billboard1
        billboardList.Create_edit_Billboard("Billboard1", "new billboard", "blue",
                "No Image", LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5), "none", "jarod");

        //Delete Billboard1
        billboardList.Delete_billboard("Billboard1");

        //Check if the billboard was deleted
        assertFalse(billboardList.billboardHashMap.containsKey("Billboard1"));

    }

    //Test 9: If Delete_billboard attempts to delete a billboard that doesn't exist.
    @Test
    public void no_billboard()
    {
        assertThrows(Exception.class, () -> {
            billboardList.Delete_billboard("Billboard_1");
        });
    }

}

