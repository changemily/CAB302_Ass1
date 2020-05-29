import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Random;

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

    /**
     * Hashes byte array of password into hexadecimal code
     * @param password password in the form of array of bytes
     * @return return hashed password
     */
    private static String hash(byte[] password)
    {
        StringBuffer sb = new StringBuffer();
        //for each byte of the password
        for (byte b : password)
        {
            sb.append(String.format("%02x",b &0xFF));
        }
        return sb.toString();
    }

    /**
     * Method for hashing passwords
     * @param password The password entered by the user
     * @return A string containing the hash product of the hashed password and salt
     */
    public String hashPassword(String username, String password) throws NoSuchAlgorithmException, SQLException {
        //turn password into bytes
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] passwordBytes = messageDigest.digest(password.getBytes());

        //hash password
        String hashedPassword = hash(passwordBytes);

        System.out.println("Hashed and salted password : " + hashedPassword);

        //create DB connection
        Connection connection = null;
        connection = DBconnection.getInstance();

        String user = hashPasswordAndSalt(username, hashedPassword, connection);
        return user;
    }


    /**
     * Method for hashing a salt and a password
     * @param hashedPassword The password the user enter after being hashed by hashPassword
     * @param connection Uses a connection to store the relevant information
     * @return
     */
    public String hashPasswordAndSalt(String username, String hashedPassword, Connection connection) throws NoSuchAlgorithmException, SQLException {
        //Setup ready for hashing
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        //Create a salt
        Random rnd = new Random();
        byte[] saltBytes = new byte[32];
        rnd.nextBytes(saltBytes);
        String saltString = saltBytes.toString();

        //Add a salt to the user inputted hashed password
        String inputtedPasswordSalted = (messageDigest.digest((hashedPassword + saltString).getBytes())).toString();
        String userPass = inputtedPasswordSalted;

        //Store the user information in the database(username, hasedsaltedpassword, salt)
        final String SELECT = "INSERT INTO users(username, password, salt) VALUES (\""+username+"\",\""+userPass+"\",\""+saltString+"\");";

        //create statement
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(SELECT);

        //close ResultSet
        rs.close();
        //close statement
        st.close();

        return userPass;
    }


}
