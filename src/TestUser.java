import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * User test class
 * This class contains methods which test the methods within the User class
 * @author - Nickhil Nischal
 * @version - under development
 */
class TestUser {
    //Test 1.1: Creating a user with one permission
    @Test
    public void testUser1() {
        User john1 = new User("john1", "password1", "Create Billboards");
    }

    //Test 1.2: Creating a user with one permission - invalid permission type
    @Test
    public void testUser1Invalid() {
        User john2 = new User("john2", "password1", "Make Billboards");
    }

    //Test 2.1 Creating a user with two permissions
    @Test
    public void testUser2() {
        User john3 = new User("john3", "password1", "Create Billboards", "Edit All Billboards");
    }

    //Test 2.2 Creating a user with two permissions - invalid permission type
    @Test
    public void testUser2Invalid() {
        User john4 = new User("john4", "password1", "Create Billboards", "Edit Billboards");
    }

    //Test 3.1 Creating a user with three permissions
    @Test
    public void testUser3() {
        User john5 = new User("john5", "password1", "Create Billboards", "Edit All Billboards", "Schedule Billboards");
    }

    //Test 3.2 Creating a user with three permissions - invalid permission type
    @Test
    public void testUser3Invalid() {
        User john6 = new User("john6", "password1", "Create Billboards", "Edit All Billboards", "Schedule All Billboards");
    }

    //Test 4.1 Creating a user with four permissions
    @Test
    public void testUser4() {
        User john7 = new User("john7", "password1", "Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users");
    }

    //Test 4.2 Creating a user with four permissions - invalid permission type
    @Test
    public void testUser4Invalid() {
        User john8 = new User("john8", "password1", "Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit All Users");
    }

    //Test 5.1 Deleting a user
    @Test
    public void testDeleteUser() {
        User john9 = new User("john9", "password1", "Create Billboards");
        john9.DeleteUser();
    }

    //Test 6.1 Editing a username
    @Test
    public void testEditUsername() {
        User john9 = new User("john9", "password1", "Create Billboards");
        john9.EditUsername("john10");
    }

    //Test 7.1 Editing a password
    @Test
    public void testEditPassword() {
        User john9 = new User("john9", "password1", "Create Billboards");
        john9.EditPassword("password2");
    }

    //Test 8.1 Removing a permission
    @Test
    public void testRemovePermission() {
        User john9 = new User("john9", "password1", "Create Billboards", "Edit All Billboards");
        john9.RemovePermissions("Edit All Billboards");
    }

    //Test 8.2 Removing a permission - invalid permission
    @Test
    public void testRemovePermissionInvalid1() {
        //Incomplete test
    }

    //Test 9.1 Adding a permission
    @Test
    public void testAddPermission() {
        User john9 = new User("john9", "password1", "Create Billboards");
        john9.AddPermissions("Edit All Billboards");
    }

    //Test 9.2 Adding a permission - invalid permission
    @Test
    public void testAddPermissionInvalid1() {
        //Incomplete test
    }
}
