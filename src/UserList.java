import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

public class UserList extends HashMap implements java.io.Serializable {
    public static HashMap<String, User> userHashMap;

    public UserList(){
        userHashMap = new HashMap<>();
    }

    public void retrieveUsersFromDB(Connection connection) throws Exception {
        final String SELECT = "SELECT * FROM Users ORDER BY username desc";

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(SELECT);

        while (rs.next())
        {
            String username = rs.getString(1);
            String password = rs.getString(2);
            String salt = rs.getString(3);
            int createBillboard = Integer.parseInt(rs.getString(4));
            int editBillboards = Integer.parseInt(rs.getString(5));
            int scheduleBillboards = Integer.parseInt(rs.getString(6));
            int editUsers = Integer.parseInt(rs.getString(7));
            HashSet<String> permissions = new HashSet<>();

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
            User newUser = new User(username, password, salt);
            newUser.Permissions = permissions;
            userHashMap.put(username, newUser);
        }
        //close ResultSet
        rs.close();
        //close statement
        st.close();
    }

    public HashMap<String, User> listUsers(){return userHashMap;}

    public static void clearUsersFromDB(HashMap<String, User> userHashMap, Connection connection) throws SQLException {
        //create statement to connect to db
        Statement st = connection.createStatement();

        //for all entries in billboardHashMap
        for (String username : userHashMap.keySet())
        {
            //remove each entry from DB using billboard_name
            st.execute("DELETE FROM Users WHERE username=\""+username+"\";");
        }
    }

    public static void sendUsersToDB(HashMap<String, User> userHashMap, Connection connection) throws SQLException {
        //create statement
        Statement st = connection.createStatement();

        //for every billboard name in billboardHashMap
        for (User user : userHashMap.values() ) {

            //Pass the values of each billboard to the SQL statement.
            String username = user.Username;
            String password = user.Password;
            String salt = user.Salt;
            int createBillboard;
            int editBillboards;
            int scheduleBillboards;
            int editUsers;

            if(user.Permissions.contains("Create Billboards")){
                createBillboard = 1;
            }
            else{
                createBillboard = 0;
            }
            if(user.Permissions.contains("Edit All Billboards")){
                editBillboards = 1;
            }
            else{
                editBillboards = 0;
            }
            if(user.Permissions.contains("Schedule Billboards")){
                scheduleBillboards = 1;
            }
            else{
                scheduleBillboards = 0;
            }
            if(user.Permissions.contains("Edit Users")){
                editUsers = 1;
            }
            else{
                editUsers = 0;
            }

            st.executeQuery("INSERT INTO Users (username, password, salt, createBillboard, editBillboards, scheduleBillboards, editUsers) " +
                    "VALUES(\""+username+"\",\""+password+"\", \""+salt+"\", \""+createBillboard+"\",\""+editBillboards+"\",\""+scheduleBillboards+"\",\""+editUsers+"\");");
        }
        //close statement
        st.close();
    }

    public static void addUserToList(HashMap<String, User> userHashMap,User newUser) throws Exception {
        for(String username : userHashMap.keySet()){
            if(username.equals(newUser.Username)) {
                throw new Exception("Username already exists, pick a new one.");
            }
        }
        userHashMap.put(newUser.Username, newUser);
    }

    public static void deleteUser(HashMap<String, User> userHashMap,User oldUser) throws Exception {
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


    public static User getUserInformation(HashMap<String, User> userHashMap, String userName) throws Exception {
        for(String username : userHashMap.keySet()){
            if(username.equals(userName)) {
                return userHashMap.get(userName);
            }
        }
        throw new Exception("User with this username doesn't exist");
    }

    public static void modifyUser(HashMap<String, User> userHashMap, User oldUser, User newUser) throws Exception {
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
}

