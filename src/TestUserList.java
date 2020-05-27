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
        userList.addUserToList(newUser);
        assertEquals(userList.getUserInformation("admin"), newUser);
    }

    @Test
    public void addMultipleUsers() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        User newUser2 = new User("admin2", "password");
        userList.addUserToList(newUser);
        userList.addUserToList(newUser2);
        assertEquals(userList.getUserInformation("admin"), newUser);
        assertEquals(userList.getUserInformation("admin2"), newUser2);
    }

    @Test
    public void addUserSameName() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        User newUser2 = new User("admin", "password");
        userList.addUserToList(newUser);
        assertThrows(Exception.class, () -> userList.addUserToList(newUser2));
    }

    @Test
    public void removeUser() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        userList.addUserToList(newUser);
        userList.deleteUser(newUser);
        assertThrows(Exception.class, () -> userList.getUserInformation("admin"));
    }

    @Test
    public void removeUserNonExist() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        userList.deleteUser(newUser);
        assertThrows(Exception.class, () -> userList.deleteUser(newUser));
    }

    @Test
    public void modifyUser() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password");
        User newUser2 = new User("admin2", "password");
        userList.addUserToList(newUser);
        userList.modifyUser(newUser, newUser2);
        assertThrows(Exception.class, () -> userList.getUserInformation("admin"));
        assertEquals(userList.getUserInformation("admin2"), newUser2);
    }
}
