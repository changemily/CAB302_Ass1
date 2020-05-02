import java.util.HashSet;

/**
 * User Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Harry Estreich
 * @version - skeleton
 */

public class userManager
{
    /**
     * Constructor used to create a userManager relation. When attempted to use UserManager methods that don't require it
     * a target user like list_users, create_user, log_out, it is not necessary to have a target user in the constructor.
     * @param current The Current User
     */

    userManager(User current){

    }

    /**
     * Overloaded Constructor used to create a directed userManager relation. Each userManager is assigned a current User, and a target user,
     * where the current User is managing the target user, the current user can also be the target user if modifying itself.
     * @param current The Current User
     * @param target The Target User
     */

    userManager(User current, User target){

    }

    /**
     * Method for creating users
     * @param newUser (user)
     * @param location (HashSet<User>)
     */
    public void add_user(User newUser, HashSet<User> location){

    }
    /**
     * Method for setting user permissions
     * @param permissions (hashset)
     */
    public void set_user_permissions(HashSet<String> permissions){

    }

    /**
     * Method for setting user password
     * @param password (string)
     */
    public void set_user_password(String password){

    }

    /**
     * Method for getting user password
     */
    public String get_user_password(){
        return "0";
    }

    /**
     * Method for deleting user
     */
    public void delete_user(){

    }

    /**
     * Method for logging out
     */
    public void log_out(){

    }
}