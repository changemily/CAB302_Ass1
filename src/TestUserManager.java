import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

//*
 /* This class contains methods that test the functionality of billboardManager class methods
 /*  * @author — Harry Estreich
 /*  * @version - Draft, All Current Test Have Passed
  */



public class TestUserManager {
    // Test 1.1 Create a user manager without a target
    @Test
    public void create_user_manager(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser);
    }

    // Test 1.2 Create a user manager with a target
    @Test
    public void create_user_manager_target(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(adminUser, otherUser);
    }

    // Test 2.1 Create a new user with Edit Users permission
    @Test
    public void create_user_admin(){
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser);
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        admin.add_user(otherUser, UserList);
        assertTrue(UserList.contains(otherUser));
    }

    // Test 2.1 Create a new user without Edit Users permission
    @Test
    public void create_user_base(){
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234",  "Edit All Billboards");
        userManager admin = new userManager(adminUser);
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        admin.add_user(otherUser, UserList);
        assertFalse(UserList.contains(otherUser));
    }

    // Test 2.1 Show a that list_user creates a list of usernames that have been added to UserList
    @Test
    public void list_user(){
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234",  "Edit All Billboards", "Edit Users");
        userManager admin = new userManager(adminUser);
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        admin.add_user(otherUser, UserList);
        String Usernames[] = {"Other"};
        HashSet<String> UsernameList = new HashSet<>(Arrays.asList(Usernames));
        assertEquals(admin.list_users(UserList), UsernameList);
    }

    // Test 2.1 Show a that list_user doesn't work without edit users
    @Test
    public void list_user_base(){
        HashSet<User> UserList = new HashSet<>();
        User adminUser = new User("Admin", "1234",  "Edit All Billboards");
        userManager admin = new userManager(adminUser);
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        admin.add_user(otherUser, UserList);
        String Error[] = {"Error"};
        HashSet<String> UsernameList = new HashSet<>(Arrays.asList(Error));
        assertEquals(admin.list_users(UserList), UsernameList);
    }

    // Test 3.1 Remove a permission from same user
    @Test
    public void remove_permission(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        String Permissions[] = {"Edit Users"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        try{
            admin.set_user_permissions(Permissions_List);
        } catch (Exception e) {
            // null
        }
        assertEquals(adminUser.Permissions, Permissions_List);
    }

    // Test 3.2 Remove a permission from different user
    @Test
    public void remove_permission_different(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        String Permissions[] = {};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        try{
            admin.set_user_permissions(Permissions_List);
        } catch (Exception e) {
            // null
        }
        assertEquals(otherUser.Permissions, Permissions_List);
    }

    // Test 4.1 Add permission to own user with admin
    @Test
    public void add_a_permission(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        String Permissions[] = {"Edit Users", "Edit All Billboards", "Schedule Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        try{
            admin.set_user_permissions(Permissions_List);
        } catch (Exception e) {
            // null
        }
        assertEquals(adminUser.Permissions, Permissions_List);
    }

    // Test 4.2 Add a permission to own user without admin
    @Test
    public void add_a_permissions(){
        User adminUser = new User("Admin", "1234", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        String Permissions[] = {"Edit Users", "Edit All Billboards", "Schedule Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        assertThrows(Exception.class, () -> {
            admin.set_user_permissions(Permissions_List);
        });
    }

    // Test 4.3 Add permission to another user with admin
    @Test
    public void add_a_permission_different(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        String Permissions[] = {"Edit Users", "Edit All Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        try{
            admin.set_user_permissions(Permissions_List);
        } catch (Exception e) {
            // null
        }
        assertEquals(adminUser.Permissions, Permissions_List);
    }

    // Test 5.1 Remove edit user for self user -> fails
    @Test
    public void remove_self_user(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        String Permissions[] = {"Edit All Billboards"};
        HashSet<String> Permissions_List = new HashSet<>(Arrays.asList(Permissions));
        assertThrows(Exception.class, () -> {
            admin.set_user_permissions(Permissions_List);
        });
    }

    // Test 6.1 Set own password, with admin, pass
    @Test
    public void edit_own_password_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        String password = "4321";
        try{
            admin.set_user_password(password);
        }
        catch (Exception e)
        {
            // null
        }
        assertEquals(adminUser.Password, password);
    }

    // Test 6.2 Set own password, without admin, pass
    @Test
    public void edit_own_password_without_admin(){
        User adminUser = new User("Admin", "1234", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        String password = "4321";
        try{
            admin.set_user_password(password);
        }
        catch (Exception e)
        {
            // null
        }
        assertEquals(adminUser.Password, password);
    }

    // Test 6.3 Set other password, with admin, pass
    @Test
    public void edit_other_password_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        String password = "4321";
        try{
            admin.set_user_password(password);
        }
        catch (Exception e)
        {
            // null
        }
        assertEquals(otherUser.Password, password);
    }

    // Test 6.4 Set other password, without admin, fail
    @Test
    public void edit_other_password_without_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(otherUser, adminUser);
        String password = "4321";
        assertThrows(Exception.class, () -> {
            admin.set_user_password(password);
        });
    }

    // Test 7.1 Delete own user, fail
    @Test
    public void delete_own_user(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        assertThrows(Exception.class, () -> {
            admin.delete_user();
        });
    }

    // Test 7.2 Delete other user, with admin, pass
    @Test
    public void delete_other_user_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        try {
            admin.delete_user();
        } catch (Exception e)
        {
            // null
        }
    }

    // Test 8.1 Delete other user, without admin, fail
    @Test
    public void delete_other_user_without_admin(){
        User adminUser = new User("Admin", "1234", "Edit All Billboards");
        User otherUser = new User("Other", "5678", "Edit All Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        assertThrows(Exception.class, () -> {
            admin.delete_user();
        });
    }
}

