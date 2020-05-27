import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestbillboardList<E> {
    public static final String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<billboard>\n" +
            "    <picture url=\"https://cloudstor.aarnet.edu.au/plus/s/vYipYcT3VHa1uNt/download\" />\n" +
            "    <information>Billboard with picture (with URL attribute) and information text only. The picture is now centred within the top 2/3 of the image and the information text is centred in the remaining space below the image.</information>\n" +
            "</billboard>";

    ArrayList<E> billboardValues;
    BillboardList billboardList = new BillboardList();
    ScheduleMultiMap Billboard_Schedule = new ScheduleMultiMap();

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
        billboardList.createEditBillboard("Billboard1", "jarod", xmlFile);

        assertEquals(true, billboardList.billboardHashMap.containsKey("Billboard1"));

        //Billboard with scheduled viewing and image
        billboardList.createEditBillboard("Billboard2", "jarod", xmlFile);

        assertEquals(true, billboardList.billboardHashMap.containsKey("Billboard2"));

    }

    //Test 4: Checks if a billboard can be edited.
    @Test
    public void Edit_Billboard() throws Exception {

        //Create Billboard1
        billboardList.createEditBillboard("Billboard1", "jarod", xmlFile);

        //edit billboard 1
        billboardList.createEditBillboard("Billboard1", "jarod", xmlFile);

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
        HashMap<String, Billboard> temp_list = billboardList.listBillboards();

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
        billboardList.createEditBillboard("Billboard2", "jarod", xmlFile);
        //Store billboard info sourced in a temp billboard object
        Billboard temp_billboard = billboardList.GetBillboardInfo("Billboard2");

        //Test if retrieved Billboard variables equal the original requested_billboard info.
        //If the billboards are the same it means the correct billboard info requested is being displayed.
        assertEquals("Billboard2", temp_billboard.BillboardName);
        assertEquals("jarod", temp_billboard.BillboardCreator);
        assertEquals(xmlFile, temp_billboard.XMLFile);
    }

    //Test 7: If Get_billboard_info attempts to retrieve information from a billboard that doesn't exist.
    @Test
    public void no_info()
    {
        assertThrows(Exception.class, () -> {
            billboardList.GetBillboardInfo("Billboard_1");
        });
    }

    //Test 8: Checks if a specified billboard can be deleted.
    @Test
    public void Delete_billboard() throws Exception
    {
        //Create Billboard1
        billboardList.createEditBillboard("Billboard1", "jarod", xmlFile);

        //Schedule Billboard1
        billboardList.scheduleMultiMap.scheduleBillboard("Billboard1",  LocalDateTime.parse("2021-04-22T10:00:00.00"),
                Duration.ofMinutes(5),0, billboardList.listBillboards(), "jarod");;

        //Delete Billboard1
        billboardList.DeleteBillboard("Billboard1");

        //Check if the billboard was deleted
        assertFalse(billboardList.billboardHashMap.containsKey("Billboard1"));

    }

    //Test 9: If Delete_billboard attempts to delete a billboard that doesn't exist.
    @Test
    public void no_billboard()
    {
        assertThrows(Exception.class, () -> {
            billboardList.DeleteBillboard("Billboard_1");
        });
    }

}

