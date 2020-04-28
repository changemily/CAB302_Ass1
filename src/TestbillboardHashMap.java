/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestbillboardHashMap<E> {

    ArrayList<E> billboardValues;
    static billboardHashMap billboardList;


    //Test 1: Creating an object for testing.
    @BeforeEach
    @Test public void setupBillboardHashmap() {
        billboardList = new billboardHashMap();
    }


    //Test 2: Checks if a billboard can be created and added to HashMap.
    @Test
    public void add_Billboard() throws Exception
    {

        //Billboard with no scheduled viewing
        billboardList.Create_edit_Billboard("Billboard1", "new billboard", "blue", "No Image");

        assertEquals(true, billboardList.containsKey("Billboard1"));

        //Billboard with scheduled viewing and image
        billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"), 5);

        assertEquals(true, billboardList.containsKey("Billboard2"));

    }

    //Test 3.1: Check for negative duration.
    @Test
    public void neg_duration() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"), -5);
        });
    }

    //Test 3.2: Check for 0 duration.
    @Test
    public void no_duration() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"), 0);
        });
    }

    //Test 3.3: Invalid date - date in the past etc.
    @Test
    public void invalid_date() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "image.jpg", LocalDateTime.parse("20-04-202T10:00:00.00"), 5);
        });
    }

    //Test 3.4: Check if img exists.
    @Test
    public void img_exists() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                    "doesn't_exist.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"), 5);
        });
    }

    //Test 3.5: Check if bg_colour is valid.
    @Test
    public void bg_valid() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardList.Create_edit_Billboard("Billboard2", "new billboard", "lmao",
                    "image.jpg", LocalDateTime.parse("20-04-2020T10:00:00.00"), 5);
        });
    }

    //Test 4: Checks if a billboard can be edited.
    @Test
    public void Edit_Billboard() throws Exception
    {

        //Create Billboard1
        billboardList.Create_edit_Billboard("Billboard1", "new billboard", "blue", "No Image");

        //edit billboard 1
        billboardList.Create_edit_Billboard("Billboard1", "edited", "edited", "edited");

        //test if all variables have changed
        assertEquals("Billboard1", "");
        assertEquals("edited", "");
        assertEquals("edited", "edited");
        assertEquals("edited", "edited");

    }

    //Test 5: Checks if all current billboards can be listed. Check for alphabetical order.
    @Test
    public void List_Billboards(HashMap<String, Billboard> hashMap) throws Exception
    {
        //store billboard schedule in temp HashMap
        HashMap<String, Billboard> temp_list = billboardList.List_Billboards();

        //Loop checks to see if every billboard in the list is found and correctly.
        //Foreach billboard in billboardList check if it exists in the hashmap provided.
        for(Object Billboard : billboardList.entrySet()) {
            assertEquals(hashMap, Billboard);
        }
    }

    //Test 6: Checks if all information pertaining to a specified billboard can be obtained.
    @Test
    public void Get_billboard_info(String billboard_name) throws Exception
    {
        //Store billboard info sourced in a temp billboard object
        Billboard temp_billboard = billboardList.Get_billboard_info("Billboard_2");
        //Store the expected billboard in a temp billboard object
        Billboard requested_billboard = billboardList.Get_billboard_info(billboard_name);

        //Test if retrieved Billboard variables equal the original requested_billboard info.
        //If the billboards are the same it means the correct billboard info requested is being displayed.
        assertEquals(requested_billboard.Billboard_name, temp_billboard.Billboard_name);
        assertEquals(requested_billboard.Image_file, temp_billboard.Image_file);
        assertEquals(requested_billboard.Bg_colour, temp_billboard.Bg_colour);
        assertEquals(requested_billboard.Billboard_text, temp_billboard.Billboard_text);
        assertEquals(requested_billboard.duration, temp_billboard.duration);
        assertEquals(requested_billboard.Time_scheduled, temp_billboard.Time_scheduled);

    }

    //Test 7: If Get_billboard_info attempts to retrieve information from a billboard that doesn't exist.
    @Test
    public void no_info() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardHashMap.Get_billboard_info("Billboard_1");
        });
    }

    //Test 8: Checks if a specified billboard can be deleted.
    @Test
    public void Delete_billboard() throws Exception
    {
        billboardList.Delete_billboard("Billboard_1");

        assertEquals(false, billboardList.containsKey("Billboard_1"));

    }

    //Test 9: If Delete_billboard attempts to delete a billboard that doesn't exist.
    @Test
    public void no_billboard() throws Exception
    {
        assertThrows(Exception.class, () -> {
            billboardHashMap.Delete_billboard("Billboard_1");
        });
    }

}
*/
