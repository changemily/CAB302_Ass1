import java.sql.Connection;
import java.util.HashMap;

public class UserList {
    static HashMap<String, User> userHashMap;

    public UserList(){
        userHashMap = new HashMap<>();
    }

    public void retrieveUsersFromDB(Connection connection){

    }

    public void addUserToList(User newUser){
        userHashMap.put(newUser.Username, newUser);
    }

    public void addUserToDB(User newUser){

    }

    public void deleteUser(User oldUser){

    }

    public void deleteUserToDB(User oldUser){

    }

    public User getUserInformation(String userName) throws Exception {
        return new User("a", "b");
    }

    public void modifyUser(User oldUser, User newUser){

    }

    public void modifyUserToDB(User oldUser, User newUser){

    }

}

