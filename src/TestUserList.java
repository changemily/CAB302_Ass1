import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains tests for testing the User List class, it tests all those change the UserList[], not the database,
 * have two versions, one to change the list, one to change the database
 */
public class TestUserList {

    @Test
    public void createUserList(){
        UserList userList = new UserList();
    }

    @Test
    public void addUser() throws Exception {
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        userList.addUserToList(userList.listUsers(), newUser);
        assertEquals(userList.getUserInformation(userList.listUsers(), "admin"), newUser);
    }

    @Test
    public void addMultipleUsers() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        User newUser2 = new User("admin2", "password");
        userList.addUserToList(userList.listUsers(), newUser);
        userList.addUserToList(userList.listUsers(), newUser2);
        assertEquals(userList.getUserInformation(userList.listUsers(), "admin"), newUser);
        assertEquals(userList.getUserInformation(userList.listUsers(), "admin2"), newUser2);
    }

    @Test
    public void addUserSameName() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        User newUser2 = new User("admin", "password");
        userList.addUserToList(userList.listUsers(), newUser);
        assertThrows(Exception.class, () -> userList.addUserToList(userList.listUsers(), newUser2));
    }

    @Test
    public void removeUser() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        userList.addUserToList(userList.listUsers(), newUser);
        userList.deleteUser(userList.listUsers(), newUser);
        assertThrows(Exception.class, () -> userList.getUserInformation(userList.listUsers(), "admin"));
    }

    @Test
    public void removeUserNonExist() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        assertThrows(Exception.class, () -> userList.deleteUser(userList.listUsers(), newUser));
    }

    @Test
    public void modifyUser() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        User newUser2 = new User("admin", "password1");
        userList.addUserToList(userList.listUsers(), newUser);
        userList.modifyUser(userList.listUsers(), newUser, newUser2);
        assertEquals(userList.getUserInformation(userList.listUsers(), "admin"), newUser2);
    }
}
