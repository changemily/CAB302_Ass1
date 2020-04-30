/*
import org.junit.jupiter.api.Test;

*/
/**
 * This class contains methods that test the functionality of billboardManager class methods
 *  * @author — Harry Estreich
 *  * @version - extended skeleton
 *//*


public class TestUserManager {

    // Test 1.1 Remove a permission from same user
    @Test
    public void remove_permission(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.remove_user_permissions(adminUser, "Edit Billboards");
        // pass
    }

    // Test 1.2 Remove a permission from different user
    @Test
    public void remove_permission_different(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.remove_user_permissions(otherUser, "Edit Billboards");
        // pass
    }

    // Test 2.1 Remove permission that doesn't exist from same user
    @Test
    public void remove_permission_non_exist(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.remove_user_permissions(adminUser, "Schedule Billboards");
        // fail
    }

    // Test 2.2 Remove permission that doesn't exist from different user
    @Test
    public void remove_permission_non_exist_different(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.remove_user_permissions(otherUser, "Schedule Billboards");
        // fail
    }

    // Test 3.1 Remove permission from a user that doesn't exist
    @Test
    public void remove_permission_non_exist_user(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.remove_user_permissions(otherUser, "Schedule Billboards");
        // fail
    }

    // Test 4.1 Add permission to own user with admin
    @Test
    public void add_a_permission(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.add_user_permissions(adminUser, "Schedule Billboards");
        // pass
    }

    // Test 4.2 Add a permission to own user without admin
    @Test
    public void add_a_permissions(){
        User adminUser = new User("Admin", "1234", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.add_user_permissions(adminUser, "Schedule Billboards");
        // fail
    }

    // Test 4.3 Add permission to another user with admin
    @Test
    public void add_a_permission_different(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.add_user_permissions(otherUser, "Schedule Billboards");
        // pass
    }

    // Test 4.4 Add permission to another user without admin

    @Test
    public void add_existing_permission(){
        User adminUser = new User("Admin", "1234","Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.add_user_permissions(otherUser, "Schedule Billboards");
        // fail
    }

    // Test 4.5 Add permission to own user that already exists
    @Test
    public void add_a_permission(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.add_user_permissions(adminUser, "Edit Billboards");
        // fail
    }

    // Test 5.1 Remove edit user for self user -> fails
    @Test
    public void remove_self_user(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.remove_user_permissions(adminUser, "Edit Users");
        // fail
    }

    // Test 6.1 Set own password, with admin, pass
    @Test
    public void edit_own_password_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.set_user_password(adminUser, "4321");
        // pass
    }

    // Test 6.2 Set own password, without admin, pass
    @Test
    public void edit_own_password_without_admin(){
        User adminUser = new User("Admin", "1234", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.set_user_password(adminUser, "4321");
        // pass
    }

    // Test 6.3 Set other password, with admin, pass
    @Test
    public void edit_other_password_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.set_user_password(otherUser, "8765");
        // pass
    }

    // Test 6.4 Set other password, without admin, fail
    @Test
    public void edit_other_password_without_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(otherUser, adminUser);
        admin.set_user_password(adminUser, "4321");
        // fail
    }

    // Test 7.1 Delete own user, fail
    @Test
    public void delete_own_user(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        userManager admin = new userManager(adminUser, adminUser);
        admin.delete_user(adminUser);
        // fail
    }

    // Test 7.2 Delete other user, with admin, pass
    @Test
    public void delete_other_user_admin(){
        User adminUser = new User("Admin", "1234", "Edit Users", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.delete_user(otherUser);
        // pass
    }

    // Delete other user, without admin, fail
    @Test
    public void delete_other_user_without_admin(){
        User adminUser = new User("Admin", "1234", "Edit Billboards");
        User otherUser = new User("Other", "5678", "Edit Billboards");
        userManager admin = new userManager(adminUser, otherUser);
        admin.delete_user(otherUser);
        // fail
    }
}

*/
