import java.util.HashSet;

/**
 * User Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Harry Estreich
 * @version - Draft
 * notes - Current some of the methods are built around a HashSet User array however, once the database is implemented
 * they should be fixed to suit the database
 */

public class userManager
{
    // Main User Variables
    public User current;
    public User target;

    /**
     * Constructor used to create a userManager relation. When attempted to use UserManager methods that don't require it
     * a target user like list_users, create_user, log_out, it is not necessary to have a target user in the constructor.
     * @param current The Current User
     */

    userManager(User current){
        this.current = current;
    }

    /**
     * Overloaded Constructor used to create a directed userManager relation. Each userManager is assigned a current User, and a target user,
     * where the current User is managing the target user, the current user can also be the target user if modifying itself.
     * @param current The Current User
     * @param target The Target User
     */

    userManager(User current, User target){
        this.current = current;
        this.target = target;

    }

    /**
     * Method for listing users
     * @param location (HashSet<User>)
     */
    public HashSet<String> list_users(HashSet<User> location) {
        if (current.Permissions.contains("Edit Users")) {
            HashSet<String> usernames = new HashSet<>();

            for (User username : location) {
                usernames.add(username.Username);
            }
            return usernames;
        }
        else {
            HashSet<String> error = new HashSet<>();
            error.add("Error");
            return error;
        }
    }

    /**
     * Method for creating users
     * @param newUser (user)
     * @param location (HashSet<User>)
     */
    public void add_user(User newUser, HashSet<User> location){
        if(current.Permissions.contains("Edit Users")){
            location.add(newUser);
        }
        else{
            // null
        }
    }
    /**
     * Method for setting user permissions
     * @param permissions (hashset)
     */
    public void set_user_permissions(HashSet<String> permissions) throws Exception {
        if(current.Permissions.contains("Edit Users")){
            if(current.equals(target)){
                if(permissions.contains("Edit Users"))
                {
                    target.Permissions = permissions;
                }
                else{
                    throw new Exception("User can't remove Edit Users from themselves");
                }
            }
            else{
                target.Permissions = permissions;
            }
        }
        else{
            throw new Exception("User needs Edit Users to edit permissions");
        }
    }

    /**
     * Method for setting user password
     * @param password (string)
     */
    public void set_user_password(String password) throws Exception{
        if(!current.equals(target)){
            if(current.Permissions.contains("Edit Users")){
                target.Password = password;
            }
            else{
                throw new Exception("User needs edit users to set someone elses password");
            }
        }
        else{
            target.Password = password;
        }
    }

    /**
     * Method for deleting user
     */
    public boolean delete_user(){
        if(!current.equals(target)){
            if(current.Permissions.contains("Edit Users")){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}