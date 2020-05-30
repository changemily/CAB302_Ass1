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
        User newUser = new User("admin", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser);
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin"), newUser);
    }

    @Test
    public void addMultipleUsers() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        User newUser2 = new User("admin2", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser);
        UserList.addUserToList(userList.listUsers(), newUser2);
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin"), newUser);
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin2"), newUser2);
    }

    @Test
    public void addUserSameName() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        User newUser2 = new User("admin", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser);
        assertThrows(Exception.class, () -> UserList.addUserToList(userList.listUsers(), newUser2));
    }

    @Test
    public void removeUser() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        UserList.addUserToList(userList.listUsers(), newUser);
        UserList.deleteUser(userList.listUsers(), newUser);
        assertThrows(Exception.class, () -> UserList.getUserInformation(userList.listUsers(), "admin"));
    }

    @Test
    public void removeUserNonExist() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        assertThrows(Exception.class, () -> UserList.deleteUser(userList.listUsers(), newUser));
    }

    @Test
    public void modifyUser() throws Exception{
        UserList userList = new UserList();
        User newUser = new User("admin", "password", "salt");
        User newUser2 = new User("admin", "password1", "salt");
        UserList.addUserToList(userList.listUsers(), newUser);
        UserList.modifyUser(userList.listUsers(), newUser, newUser2);
        assertEquals(UserList.getUserInformation(userList.listUsers(), "admin"), newUser2);
    }
}
