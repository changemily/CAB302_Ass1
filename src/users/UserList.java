package users;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class contains method for return a hashmap of users with their usernames, as well as many static that network_classes.ControlPanelClient
 * uses to manipulate a list of users
 * @author - Harry Estreich
 * @version - Final
 */
public class UserList extends HashMap<String, User> implements java.io.Serializable {
    public static HashMap<String, User> userHashMap;

    /**
     * Set userHashMap
     */
    public UserList(){
        userHashMap = new HashMap<>();
    }

    /**
     * Retrieves a hashmap of users from the database and sends it to userHashMap
     * @param   connection A connection for accessing the database
     * @throws  Exception throws an SQL Exception and an exception if permissions don't exist
     */
    public void retrieveUsersFromDB(Connection connection) throws Exception {
        final String SELECT = "SELECT * FROM Users ORDER BY username desc";

        // Create and execute statement
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(SELECT);

        while (rs.next())
        {
            // Get details for each user
            String username = rs.getString(1);
            String password = rs.getString(2);
            String salt = rs.getString(3);
            int createBillboard = Integer.parseInt(rs.getString(4));
            int editBillboards = Integer.parseInt(rs.getString(5));
            int scheduleBillboards = Integer.parseInt(rs.getString(6));
            int editUsers = Integer.parseInt(rs.getString(7));
            HashSet<String> permissions = new HashSet<>();

            // Add permissions based of int values
            if(createBillboard == 1){
                permissions.add("Create Billboards");
            }
            if(editBillboards == 1){
                permissions.add("Edit All Billboards");
            }
            if(scheduleBillboards == 1){
                permissions.add("Schedule Billboards");
            }
            if(editUsers == 1){
                permissions.add("Edit Users");
            }

            // Create new user and set permissions, and add to userHashMap
            User newUser = new User(username, password, salt);
            newUser.permissions = permissions;
            userHashMap.put(username, newUser);
        }
        //close ResultSet
        rs.close();
        //close statement
        st.close();
    }

    /**
     * Public method for getting the userHashMap
     * @return  HashMap of users, with the usernames as key
     */
    public HashMap<String, User> listUsers(){return userHashMap;}

    /**
     * Removes all users from database, used for adding and remove users
     * @param   userHashMap HashMap used to remove every user in HashMap from database
     * @param   connection A connection to the database
     * @throws  SQLException throws an SQL exception
     */
    public static void clearUsersFromDB(HashMap<String, User> userHashMap, Connection connection) throws SQLException {
        //create statement to connect to db
        Statement st = connection.createStatement();

        //for all entries in userHashMap
        for (String username : userHashMap.keySet())
        {
            //remove each entry from DB using username
            st.execute("DELETE FROM Users WHERE username=\""+username+"\";");
        }
    }

    /**
     * Sends all users from the HashMap to the database, used for adding and removing users
     * @param   userHashMap HashMap used ot add every user in HashMap from database
     * @param   connection A connection to the database
     * @throws  SQLException throws an SQL exception
     */
    public static void sendUsersToDB(HashMap<String, User> userHashMap, Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for every user name in userHashMap
        for (User user : userHashMap.values() ) {

            //Pass the values of each user to the SQL statement.
            String username = user.username;
            String password = user.password;
            String salt = user.salt;
            int createBillboard;
            int editBillboards;
            int scheduleBillboards;
            int editUsers;

            // Set values based of permissions
            if(user.permissions.contains("Create Billboards")){
                createBillboard = 1;
            }
            else{
                createBillboard = 0;
            }
            if(user.permissions.contains("Edit All Billboards")){
                editBillboards = 1;
            }
            else{
                editBillboards = 0;
            }
            if(user.permissions.contains("Schedule Billboards")){
                scheduleBillboards = 1;
            }
            else{
                scheduleBillboards = 0;
            }
            if(user.permissions.contains("Edit Users")){
                editUsers = 1;
            }
            else{
                editUsers = 0;
            }

            // Execute statement
            st.executeQuery("INSERT INTO Users (username, password, salt, createBillboard, editBillboards, scheduleBillboards, editUsers) " +
                    "VALUES(\""+username+"\",\""+password+"\", \""+salt+"\", \""+createBillboard+"\",\""+editBillboards+"\",\""+scheduleBillboards+"\",\""+editUsers+"\");");
        }
        //close statement
        st.close();
    }

    /**
     * Adds a new user to the HashMap
     * @param   userHashMap HashMap of all users
     * @param   newUser User being added to HashMap
     * @throws  Exception throws an exception if user already exists
     */
    public static void addUserToList(HashMap<String, User> userHashMap,User newUser) throws Exception {
        // Check user doesn't exist
        for(String username : userHashMap.keySet()){
            if(username.equals(newUser.username)) {
                userHashMap.replace(newUser.username, newUser);
            }
        }
        // Add user
        userHashMap.put(newUser.username, newUser);
    }

    /**
     * Removes a user from the HashMap
     * @param   userHashMap HashMap of all users
     * @param   oldUser User being remove from HashMap
     * @throws  Exception throws an exception if user doesn't exist
     */
    public static void deleteUserFromList(HashMap<String, User> userHashMap, User oldUser) throws Exception {
        boolean userFound = false;
        // Check user exists
        for(String username : userHashMap.keySet()){
            if(username.equals(oldUser.username)) {
                userFound = true;
                break;
            }
        }
        // If user exists remove, otherwise throw exception
        if(userFound){
            userHashMap.remove(oldUser.username);
        }
        else{
            throw new Exception("User with this username doesn't exist");
        }


    }

    /**
     * Retrieves a User from the userList given a username
     * @param   userHashMap HashMap of all users
     * @param   userName Username of user being retrieved
     * @return  Full User for username given
     * @throws  Exception throws an exception if user doesn't exist
     */
    public static User getUserInformation(HashMap<String, User> userHashMap, String userName) throws Exception {
        // Find user
        for(String username : userHashMap.keySet()){
            if(username.equals(userName)) {
                return userHashMap.get(userName);
            }
        }
        // If can't find throw exception
        throw new Exception("User with this username doesn't exist");
    }
}

