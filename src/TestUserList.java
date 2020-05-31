import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains tests for testing the User List class, it tests all those change the UserList[], not the database,
 * have two versions, one to change the list, one to change the database
 * @author - Harry Estreich
 * @version - Final
 */
public class TestUserList {

    /**
     * Test 1 - Create a userList without errors
     */
    @Test
    public void createUserList() {
        UserList userList = new UserList();
    }

    /**
     * Add user to userList and check user is there
     * @throws  Exception if user doesn't exist
     */
    @Test
    public void addUser() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser); // add user
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin"), newUser); // check user now exists
    }

    /**
     * Add multiple users to userList
     * @throws  Exception if user doesn't exist
     */
    @Test
    public void addMultipleUsers() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        User newUser2 = new User("admin2", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser); // add multiple users
        UserList.addUserToList(userList.listUsers(), newUser2);
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin"), newUser); // check both users exist
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin2"), newUser2);
    }

    /**
     * Add user with the same name and check exception is thrown
     */
    @Test
    public void addUserSameName() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        User newUser2 = new User("admin", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser); // add first user to list
        assertThrows(Exception.class, () -> UserList.addUserToList(userList.listUsers(), newUser2)); // add same username, exception thrown
    }

    /**
     * Remove user and check exception is thrown when looking for user
     * @throws  Exception if user doesn't exist
     */
    @Test
    public void removeUser() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser); // add user to list
        UserList.deleteUser(userList.listUsers(), newUser); // delete same user
        assertThrows(Exception.class, () -> UserList.getUserInformation(userList.listUsers(), "admin")); // check user doesn't exist
    }

    /**
     * Remove user that doesn't exist and check exception is thrown
     * @throws  Exception if user doesn't exist
     */
    @Test
    public void removeUserNonExist() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        assertThrows(Exception.class, () -> UserList.deleteUser(userList.listUsers(), newUser)); // check user doesn't exist
    }
}
