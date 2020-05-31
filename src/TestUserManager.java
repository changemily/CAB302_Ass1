import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.*;

/**
 /* This class contains methods that test the functionality of billboardManager class methods
 /*  @author — Harry Estreich
 /*  @version - Final
  */
public class TestUserManager {
    /**
     * Test 1.1 - Create a user manager without a target, check no exception thrown
     * @throws  Exception throws exception if invalid permission
     */
    @Test
    public void createUserManager() throws Exception {
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser);
    }

    /**
     * Test 1.2 - Create a user manager with a target, check no exception thrown
     * @throws  Exception throws exception if invalid permission
     */
    @Test
    public void createUserManagerTarget() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, otherUser);
    }

    /**
     * Test 2.1 - Create a new user, with Edit User permissions for current user
     * @throws  Exception throws exception if invalid permission
     */
    @Test
    public void createUserAdmin() throws Exception{
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser);
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        admin.addUser(otherUser, UserList); // add to list
        assertTrue(UserList.contains(otherUser)); // check list contains
    }

    /**
     * Test 2.2 - Create a new user, without Edit User permission for current user
     * @throws  Exception throws exception if invalid permission
     */
    @Test
    public void createUserBase() throws Exception{
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234", "Salt",  "Edit All Billboards");
        UserManager admin = new UserManager(adminUser);
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        assertThrows(Exception.class, () -> admin.addUser(otherUser, UserList)); // assert that user can't add user
    }

    /**
     * Test 2.3 - Show tha list_usres creates a string[] of usernames after adding a user
     * @throws  Exception throws exception if invalid permission
     */
    @Test
    public void listUser() throws Exception{
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234", "Salt",  "Edit All Billboards", "Edit Users");
        UserManager admin = new UserManager(adminUser);
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        admin.addUser(otherUser, UserList);
        String[] Usernames = {"Other"}; // expected user list
        HashSet<String> UsernameList = new HashSet<>(Arrays.asList(Usernames));
        assertEquals(admin.listUsers(UserList), UsernameList); // assert that list contains usernames
    }

    /**
     * Test 2.4 Show that list_user doesn't work without Edit Users permission
     * @throws  Exception throws exception if invalid permission
     */
    @Test
    public void listUserBase() throws Exception{
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser);
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        assertThrows(Exception.class, () -> admin.addUser(otherUser, UserList)); // assert that list user fails
    }

    /**
     * Test 3.1 - Remove a permission from the same user
     * @throws  Exception throws an exception if invalid permission
     */
    @Test
    public void removePermission() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        String[] Permissions = {"Edit Users"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        try{
            admin.setUserPermissions(Permissions_List);
        } catch (Exception e) {
            // null
        }
        assertEquals(adminUser.Permissions, Permissions_List); // assert that new permissions are correct
    }

    /**
     * Test 3.2 - Remove a permission from different user
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void removePermissionDifferent() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, otherUser);
        String[] Permissions = {};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        admin.setUserPermissions(Permissions_List);
        assertEquals(otherUser.Permissions, Permissions_List); // assert that new permissions are correct
    }

    /**
     * Test 4.1 - Add a permission to own user with Edit Users
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void addAPermission() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        String[] Permissions = {"Edit Users", "Edit All Billboards", "Schedule Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        admin.setUserPermissions(Permissions_List);
        assertEquals(adminUser.Permissions, Permissions_List); // assert that new permissions are correct
    }

    /**
     * Test 4.2 Add a permission without to own user without Edit Users
     * @throws  Exception throws an exception if invalid permissions, and when changing permissions without Edit Users
     */
    @Test
    public void addAPermissions() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        String[] Permissions = {"Edit Users", "Edit All Billboards", "Schedule Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        assertThrows(Exception.class, () -> admin.setUserPermissions(Permissions_List)); // assert that error is thrown
    }

    /**
     * Test 4.2 Add a permission to a different user with Edit Users
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void addAPermissionDifferent() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, otherUser);
        String[] Permissions = {"Edit Users", "Edit All Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        admin.setUserPermissions(Permissions_List);
        assertEquals(adminUser.Permissions, Permissions_List); // assert that new permissions are correct
    }

    /**
     * Test 5.1 Remove edit user from self
     * @throws  Exception throws an exception if invalid permission and when removing edit user from self
     */
    @Test
    public void removeSelfUser() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        String[] Permissions = {"Edit All Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        assertThrows(Exception.class, () -> admin.setUserPermissions(Permissions_List)); // assert that error is thrown
    }

    /**
     * Test 6.1 Set own password with Edit Users, pass
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void editOwnPasswordAdmin() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        String password = "4321";
        admin.setUserPassword(password);
        assertEquals(adminUser.Password, password); // assert that new password is correct
    }

    /**
     * Test 6.2 Set own password without Edit Users, pass
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void editOwnPasswordWithoutAdmin() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        String password = "4321";
        admin.setUserPassword(password);
        assertEquals(adminUser.Password, password); // assert that new password is correct
    }

    /**
     * Test 6.3 Set other user password with Edit Users, pass
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void editOtherPasswordAdmin() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, otherUser);
        String password = "4321";
        admin.setUserPassword(password);
        assertEquals(otherUser.Password, password); // assert that new pass is correct
    }

    /**
     * Test 6.4 Set other user password without Edit Users, fail
     * @throws  Exception throws an exception if invalid permissions and when set other use password without Edit Users
     */
    @Test
    public void editOtherPasswordWithoutAdmin() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(otherUser, adminUser);
        String password = "4321";
        assertThrows(Exception.class, () -> admin.setUserPassword(password)); // assert that error is thrown
    }

    /**
     * Test 7.1 Delete own user, fail
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void deleteOwnUser() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt","Edit Users", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, adminUser);
        assertFalse(admin.deleteUser()); // assert that user can't delete
    }

    /**
     * Delete other user with Edit Users, pass
     * @throws  Exception throws an exception if invalid permissions
     */
    @Test
    public void deleteOtherUserAdmin() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, otherUser);
        assertTrue(admin.deleteUser()); // assert that user can delete
    }

    /**
     * Delete other user without Edit Users, fail
     * @throws  Exception throws an exception if invalid permission
     */
    @Test
    public void deleteOtherUserWithoutAdmin() throws Exception{
        User adminUser = new User("Admin", "1234", "Salt", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Salt", "Edit All Billboards");
        UserManager admin = new UserManager(adminUser, otherUser);
        assertFalse(admin.deleteUser()); // assert that user can't delete
    }
}

