import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestbillboardHashMap<E> {

    ArrayList<E> billboardValues;
    static HashMap<String, ArrayList> billboardList;

    //Test 1: Checks if a billboard can be created and added to HashMap.
    @Test
    public void add_Billboard() {

        //Billboard with no scheduled viewing
        billboardManager.Create_edit_Billboard("Billboard1", "new billboard", "blue", "No Image");

        assertEquals(true, billboardList.containsKey("Billboard1"));

        //Billboard with scheduled viewing and image
        billboardManager.Create_edit_Billboard("Billboard2", "new billboard", "blue",
                "image.jpg", LocalDate.parse("20-04-2020"), Duration.parse("5"));

        assertEquals(true, billboardList.containsKey("Billboard2"));

    }

    //Test 2: Checks if a billboard can be edited.
    @Test
    public void Edit_Billboard() {

        //Create Billboard1
        billboardManager.Create_edit_Billboard("Billboard1", "new billboard", "blue", "No Image");

        //edit billboard 1
        billboardManager.Create_edit_Billboard("Billboard1", "edited", "edited", "edited");

        //test if all variables have changed
        assertEquals("Billboard1", "");
        assertEquals("edited", "");
        assertEquals("edited", "edited");
        assertEquals("edited", "edited");

    }

    //Test 3: Checks if all current billboards can be listed.
    @Test
    public void List_Billboards() {
        //store billboard schedule in temp HashMap
        HashMap<String, Billboard> temp_list= new HashMap<String, Billboard>();

        temp_list = billboardManager.List_Billboards();

        //compare temp HashMap to billboardList HashMap to see if they match
        // for every entry of temp_list
        for (HashMap.Entry<String, Billboard> temp_listEntry : temp_list.entrySet()) {

            // for every entry of billboardList
            for (HashMap.Entry<String, Billboard> original_entry : billboardList.entrySet()) {
                //check if all billboards are listed
                assertEquals(original_entry.getKey(),temp_listEntry.getKey());
            }
        }
    }

    //Test 4: Checks if all information pertaining to a specified billboard can be obtained.
    @Test
    public void Get_billboard_info() {
        //store billboard info sourced in a temp billboard object
        Billboard temp_billboard = billboardManager.Get_billboard_info("Billboard_2");

        //Test if retrieved Billboard variables equal the original Billboard 2 info
        assertEquals(Billboard_2.Billboard_name, temp_billboard.Billboard_name);
        assertEquals(Billboard_2.Image_file, temp_billboard.Image_file);
        assertEquals(Billboard_2.Bg_colour, temp_billboard.Bg_colour);
        assertEquals(Billboard_2.Billboard_text, temp_billboard.Billboard_text);
        assertEquals(Billboard_2.duration, temp_billboard.duration);
        assertEquals(Billboard_2.Time_scheduled, temp_billboard.Time_scheduled);

    }

    //Test 5: Checks if a specified billboard can be deleted.
    @Test
    public void Delete_billboard()
    {
        billboardManager.Delete_billboard("Billboard_1");

        assertEquals(false, billboardList.containsKey("Billboard_1"));

    }

}
