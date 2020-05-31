import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Random;

/**
 * User Manager class
 * Class contains methods for creating and controlling billboards.
 * @author - Harry Estreich
 * @version - Final
 */
public class UserManager
{
    // Main User Variables
    public final User current;
    public User target;


    /**
     * Constructor used to create a userManager relation. When attempted to use UserManager methods that don't require it
     * a target user like list_users, create_user, log_out, it is not necessary to have a target user in the constructor.
     * @param current The Current User
     */

    UserManager(User current){
        this.current = current;
    }

    /**
     * Overloaded Constructor used to create a directed userManager relation. Each userManager is assigned a current User, and a target user,
     * where the current User is managing the target user, the current user can also be the target user if modifying itself.
     * @param current The Current User
     * @param target The Target User
     */

    UserManager(User current, User target){
        this.current = current;
        this.target = target;

    }

    /**
     * Method for listing the usernames of a HashSet of Users
     * @param   userHashSet hashset of Users
     * @return  hashset of usernames
     * @throws  Exception throws exception if current user doesn't have Edit User permission
     */
    public HashSet<String> listUsers(HashSet<User> userHashSet) throws Exception {
        if (current.permissions.contains("Edit Users")) { // check for Edit Users
            HashSet<String> usernames = new HashSet<>();

            for (User username : userHashSet) { // add every username to hashset
                usernames.add(username.username);
            }
            return usernames;
        }
        else {
            throw new Exception("User can't list user with Edit Users permission");
        }
    }

    /**
     * Method for creating users and adding it to a hashset of other users
     * @param   newUser new user
     * @param   userHashSet hashset of users
     * @throws  Exception throws exception if current user doesn't have Edit User permission
     */
    public void addUser(User newUser, HashSet<User> userHashSet) throws Exception {
        if(current.permissions.contains("Edit Users")){ // check for Edit Users
            userHashSet.add(newUser); // add user to hashset
        }
        else{
            throw new Exception("User can't add user with Edit Users permission");
        }
    }
    /**
     * Method for setting user permissions for the target user
     * @param   permissions hashset of permissions that is being set
     * @throws  Exception throws exception if current user tries to remove edit user from themselves or user doesn't have edit user permission
     */
    public void setUserPermissions(HashSet<String> permissions) throws Exception {
        if(current.permissions.contains("Edit Users")){ // check for Edit Users
            if(current.equals(target)){
                if(permissions.contains("Edit Users")) // if self change, check Edit Users hasn't been removed
                {
                    target.permissions = permissions;
                }
                else{
                    throw new Exception("User can't remove Edit Users from themselves");
                }
            }
            else{
                target.permissions = permissions; // if not self change, no check
            }
        }
        else{
            throw new Exception("User needs Edit Users to edit permissions");
        }
    }

    /**
     * Method for setting user password for the target user
     * @param   password string of password
     * @throws  Exception throws exception if user tries to edit someone else's password without Edit Users permission
     */
    public void setUserPassword(String password) throws Exception{
        if(!current.equals(target)){ // check for other user
            if(current.permissions.contains("Edit Users")){ // check for Edit Users
                target.password = password;
            }
            else{
                throw new Exception("User needs edit users to set someone else's password");
            }
        }
        else{
            target.password = password; // change own password
        }
    }

    /**
     * Method for checking if user has permissions to delete user
     * @return  boolean that returns true if valid permissions
     */
    public boolean deleteUser(){
        if(!current.equals(target)){ // check different user
            return current.permissions.contains("Edit Users"); // return true if current user has edit users
        }
        else{
            return false; // can't delete yourself
        }
    }

    /**
     * Hashes byte array of password into hexadecimal code
     * @param   password password in the form of array of bytes
     * @return  return hashed password
     */
    private static String hash(byte[] password)
    {
        StringBuilder sb = new StringBuilder();
        //for each byte of the password
        for (byte b : password)
        {
            sb.append(String.format("%02x",b &0xFF));
        }
        return sb.toString();
    }

    /**
     * Method for hashing passwords
     * @param   password The password entered by the user
     * @return  A string containing the hash product of the hashed password and salt
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException{
        //turn password into bytes
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = messageDigest.digest(password.getBytes());

        //hash password
        String hashedPassword = hash(passwordBytes);

        //create DB connection
        Connection connection = DBconnection.getInstance();

        return hashedPassword;
    }

    /**
     * Method for hashing a salt and a password
     * @return  A string containing a salt
     */
    public static String createASalt()  {
        //Create a salt
        Random rnd = new Random();
        byte[] saltBytes = new byte[32];
        rnd.nextBytes(saltBytes);
        return hash(saltBytes);
    }


    /**
     * Method for hashing a salt and a password
     * @param   hashedPassword The password the user enter after being hashed by hashPassword
     * @return  A string array containing the user hashed password, and the salt string
     */
    public static String[] hashPasswordAndSalt(String hashedPassword, String saltString, MessageDigest messageDigest) {
        //Add a salt to the user inputted hashed password
        String userPass = hash(messageDigest.digest((hashedPassword + saltString).getBytes()));
        return new String[]{userPass, saltString};
    }




}
