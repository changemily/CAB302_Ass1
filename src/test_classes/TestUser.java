package test_classes;

import org.junit.jupiter.api.Test;
import user_classes.User;

import static org.junit.jupiter.api.Assertions.*;

/**
 * user_classes.User test class
 * This class contains methods which test the methods within the user_classes.User class
 * @author - Nickhil Nischal
 * @version - Complete
 */
class TestUser {
    // Test 1.1: Create a user with one permission
    @Test
    public void testUser1() throws Exception {
        User john1 = new User("john1", "password1", "1234","Create Billboards");
    }

    // Test 1.2: Create a user with one permission - invalid permission type
    @Test
    public void testUser1Invalid() {
        assertThrows(Exception.class, () -> {
            User john2 = new User("john2", "password1", "1234","Make Billboards");
        });
    }

    // Test 2.1 Create a user with two permissions
    @Test
    public void testUser2() throws Exception {
        User john3 = new User("john3", "password1", "1234","Create Billboards", "Edit All Billboards");
    }

    // Test 2.2 Create a user with two permissions - invalid permission type
    @Test
    public void testUser2Invalid() throws Exception {
        assertThrows(Exception.class, () -> {
            User john4 = new User("john4", "password1", "1234","Create Billboards", "Edit Billboards");
        });
    }

    // Test 3.1 Create a user with three permissions
    @Test
    public void testUser3() throws Exception {
        User john5 = new User("john5", "password1", "1234","Create Billboards", "Edit All Billboards", "Schedule Billboards");
    }

    // Test 3.2 Create a user with three permissions - invalid permission type
    @Test
    public void testUser3Invalid() throws Exception {
        assertThrows(Exception.class, () -> {
            User john6 = new User("john6", "password1", "1234","Create Billboards", "Edit All Billboards", "Schedule All Billboards");
        });
    }

    // Test 4.1 Create a user with four permissions
    @Test
    public void testUser4() throws Exception {
        User john7 = new User("john7", "password1","1234", "Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users");
    }

    // Test 4.2 Create a user with four permissions - invalid permission type
    @Test
    public void testUser4Invalid() throws Exception {
        assertThrows(Exception.class, () -> {
            User john8 = new User("john8", "password1", "1234","Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit All Users");
        });
    }

    // Test 5.1 Delete a user
    @Test
    public void testDeleteUser() throws Exception {
        User john9 = new User("john9", "password1", "1234","Create Billboards");
        john9.deleteUser();
    }

    // Test 6.1 Edit a username
    @Test
    public void testEditUsername() throws Exception {
        User john10 = new User("john10", "password1", "1234","Create Billboards");
        john10.editUsername("john11");
    }

    // Test 7.1 Edit a password
    @Test
    public void testEditPassword() throws Exception {
        User john12 = new User("john12", "password1", "1234","Create Billboards");
        john12.editPassword("password2");
    }

    // Test 8.1 Remove a permission
    @Test
    public void testRemovePermission() throws Exception {
        User john13 = new User("john13", "password1", "1234","Create Billboards", "Edit All Billboards");
        john13.removePermissions("Edit All Billboards");
    }

    // Test 8.2 Remove a permission - invalid permission
    @Test
    public void testRemovePermissionInvalid1() throws Exception {
        assertThrows(Exception.class, () -> {
            User john14 = new User("john14", "password1","1234", "Create Billboards", "Edit All Billboards");
            john14.removePermissions("Edit Any Billboards");
        });
    }

    // Test 9.1 Add a permission
    @Test
    public void testAddPermission() throws Exception {
        User john15 = new User("john15", "password1", "1234","Create Billboards");
        john15.addPermissions("Edit All Billboards");
    }

    // Test 9.2 Add a permission - invalid permission
    @Test
    public void testAddPermissionInvalid1() throws Exception {
        assertThrows(Exception.class, () -> {
            User john16 = new User("john16", "password1", "1234","Create Billboards");
            User john17 = new User("john16", "password1", "1234","Create Billboards");
            john16.addPermissions("Edit Any Billboards");
        });
    }
}
