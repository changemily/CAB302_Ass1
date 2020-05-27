import javax.swing.plaf.metal.MetalIconFactory;
import java.sql.Connection;
import java.util.HashMap;

public class UserList {
    static HashMap<String, User> userHashMap;

    public UserList(){
        userHashMap = new HashMap<>();
    }

    public void retrieveUsersFromDB(Connection connection){

    }

    public void addUserToList(User newUser) throws Exception {
        for(String username : userHashMap.keySet()){
            if(username.equals(newUser.Username)) {
                throw new Exception("Username already exists, pick a new one.");
            }
        }
        userHashMap.put(newUser.Username, newUser);
    }

    public void addUserToDB(User newUser){

    }

    public void deleteUser(User oldUser) throws Exception {
        boolean userFound = false;
        for(String username : userHashMap.keySet()){
            if(username.equals(oldUser.Username)) {
                userFound = true;
                break;
            }
        }
        if(userFound){
            userHashMap.remove(oldUser.Username);
        }
        else{
            throw new Exception("User with this username doesn't exist");
        }


    }

    public void deleteUserToDB(User oldUser){

    }

    public User getUserInformation(String userName) throws Exception {
        for(String username : userHashMap.keySet()){
            if(username.equals(userName)) {
                return userHashMap.get(userName);
            }
        }
        throw new Exception("User with this username doesn't exist");
    }

    public void modifyUser(User oldUser, User newUser) throws Exception {
        boolean userFound = false;
        for(String username : userHashMap.keySet()){
            if(username.equals(oldUser.Username)){
                userHashMap.replace(oldUser.Username, newUser);
                userFound = true;
            }
        }
        if(!userFound) {
            throw new Exception("Original user doesn't exist");
        }
    }

    public void modifyUserToDB(User oldUser, User newUser){

    }

}

