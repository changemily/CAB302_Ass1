import java.util.Arrays;
import java.util.HashSet;

/**
 * User class
 * This class contains methods that create new users and modify existing users
 * @author - Nickhil Nischal
 * @version - under development
 *
 * TO DO: 1) Database interface with the User Class
 */
public class User implements java.io.Serializable {
    // Variables used for each User
    String username;
    String password;
    String salt;
    HashSet<String> permissions = new HashSet<>();

    // Creates an array with valid User Permissions
    final String[] PERMISSIONS_CHECK_LIST_1 = {"Create Billboards", "Edit All Billboards", "Schedule Billboards", "Edit Users"};

    // Populates a HashSet with permissions from PermissionsCheckList1 array
    HashSet<String> permissionsCheckList2 = new HashSet<>(Arrays.asList(PERMISSIONS_CHECK_LIST_1));

    /**
     * Method used to check if the permission input by the user is a valid input, it is referencing the
     *  PermissionsCheckList2 HashSet
     * @param inputPermission The input given by the user
     * @return Returns a boolean: true if permission is valid and exists in the PermissionsCheckList2 HashSet, and false
     *  if it is invalid and does not exist in the PermissionsCheckList2 HashSet
     */
    private boolean PermissionChecker(String inputPermission) {
        // Checks if the user has a permission, return true if they do, else false if they do not
        return permissionsCheckList2.contains(inputPermission);
    }

    /**
     * Constructor used to create a new user. Each user is assigned a username, password, and one to four permissions
     * (using variable arguments).
     * @param username The User's username
     * @param password The User's password
     * @param permissions The User's assigned permission/s
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    User(String username, String password, String salt, String ... permissions) throws Exception {
        // For loop that iterates over each permission that is entered, to check if each permission is valid
        for (String permission: permissions) {
            // Checks if the entered permission is a valid permission, else user is not created
            if (!PermissionChecker(permission)) {
                throw new Exception("Invalid Permission Type, user has not been created.");
            }
        }

        // Assigns the inputted Username, Password, and Permission/s to the user
        this.username = username;
        this.password = password;
        this.salt = salt;
        for (String permission2: permissions) {
            this.permissions.add(permission2);
        }
    }

    /**
     * Method used to delete a specified user
     */
    public void DeleteUser() {
        String usernameTemp = this.username;
        // Sets specified user's Username, Password, and Permissions to null
        this.username = null;
        this.password = null;
        this.salt = null;
        this.permissions = null;
        System.out.println("The user " + usernameTemp + " has been successfully deleted.");
    }

    /**
     * Method used to change a specified user's username
     * @param newUsername The new username which will replace the old username
     */
    public void EditUsername(String newUsername) {
        String usernameTemp = this.username;
        // Replaces old username with newly inputted username
        this.username = newUsername;
        System.out.println("Username successfully changed from " + usernameTemp + " to " + username + ".");
    }

    /**
     * Method used to change a specified user's password
     * @param newPassword The new password which will replace the old password
     */
    public void EditPassword(String newPassword) {
        String passwordTemp = this.password;
        // Replaces old password with newly inputted password
        this.password = newPassword;
        System.out.println("Password successfully changed from " + passwordTemp + " to " + password + ".");
    }

    /**
     * Method used to remove a specified user's permission
     * @param permission The permission that is to be removed from the specified user's permissions
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public void RemovePermissions(String permission) throws Exception {
        // Checks if the entered permission is a valid permission, else permission is not removed from user
        if (PermissionChecker(permission)) {
            // Checks if permission exists in user's HashSet
            if (this.permissions.contains(permission)) {
                this.permissions.remove(permission);
                System.out.println("Permission " + permission + " successfully removed.");
            }
        }
        else {
            throw new Exception("Invalid Permission Type, permission has not been removed.");
        }
    }

    /**
     * Method used to add a specified user's permission
     * @param permission The permission that is to be added to the specified user's permissions
     * @throws Exception Throws exception when an invalid permission type is entered
     */
    public void AddPermissions(String permission) throws Exception {
        // Checks if the entered permission is a valid permission, else permission is not added to user
        if (PermissionChecker(permission)) {
            // Checks if permission already exists in user's HashSet
            if (!this.permissions.contains(permission)) {
                this.permissions.add(permission);
                System.out.println("Permission " + permission + " successfully added.");
            }
        }
        else {
            throw new Exception("Invalid Permission Type, permission has not been added.");
        }
    }
}
